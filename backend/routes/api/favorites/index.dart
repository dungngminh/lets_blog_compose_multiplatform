import 'package:dart_frog/dart_frog.dart';
import 'package:json_annotation/json_annotation.dart';
import 'package:stormberry/stormberry.dart';
import 'package:very_good_blog_app_backend/common/error_message_code.dart';
import 'package:very_good_blog_app_backend/common/extensions/json_ext.dart';
import 'package:very_good_blog_app_backend/dtos/request/favorites/favorite_blog_request.dart';
import 'package:very_good_blog_app_backend/dtos/response/base_response_data.dart';
import 'package:very_good_blog_app_backend/dtos/response/favorites/get_user_favorite_blog_response.dart';
import 'package:very_good_blog_app_backend/models/blog.dart';
import 'package:very_good_blog_app_backend/models/favorite_blogs_users.dart';
import 'package:very_good_blog_app_backend/models/user.dart';

/// @Allow(GET, POST)
Future<Response> onRequest(RequestContext context) {
  return switch (context.request.method) {
    HttpMethod.get => _onFavoritesGetRequest(context),
    HttpMethod.post => _onFavoritesPostRequest(context),
    _ => Future.value(MethodNotAllowedResponse()),
  };
}

Future<Response> _onFavoritesGetRequest(RequestContext context) {
  final userView = context.read<UserView>();
  final db = context.read<Database>();
  return db.favoriteBlogsUserses
      .queryFavoriteBlogsUserses(
        QueryParams(where: 'user_id=@id', values: {'id': userView.id}),
      )
      .then((r) => r.where((e) => !e.blog.isDeleted).toList())
      .then((r) => r.map(GetUserFavoriteBlogResponse.fromView).toList())
      .then<Response>((res) => OkResponse(res.map((e) => e.toJson()).toList()))
      .onError(
        (e, _) => InternalServerErrorResponse(ErrorMessageCode.serverError),
      )
      .whenComplete(db.close);
}

Future<Response> _onFavoritesPostRequest(RequestContext context) async {
  final userView = context.read<UserView>();
  final db = context.read<Database>();
  final body = await context.request.body();
  if (body.isEmpty) {
    return BadRequestResponse();
  }
  try {
    final request = FavoriteBlogRequest.fromJson(body.asJson());

    final requestedBlog = await db.blogs.queryBlog(request.blogId).onError(
          (e, _) => null,
        );
    if (requestedBlog == null) {
      return BadRequestResponse(
        ErrorMessageCode.blogNotFound,
        'Not found blog with ${request.blogId}',
      );
    }
    if (requestedBlog.creator.id == userView.id) {
      return BadRequestResponse(
        ErrorMessageCode.cannotActionOwnBlog,
        'You cannot favorite your own blog',
      );
    }

    final isFavoritedOrNot = (await db.favoriteBlogsUserses
            .queryFavoriteBlogsUserses(
              QueryParams(
                where: 'blog_id=@blogId AND user_id=@userId',
                values: {'blogId': request.blogId, 'userId': userView.id},
              ),
            )
            .onError((e, _) => []))
        .firstOrNull;

    if (isFavoritedOrNot != null && request.isFavorite) {
      return BadRequestResponse(
        ErrorMessageCode.alreadyFavorited,
        'Blog already favorited',
      );
    }

    if (request.isFavorite) {
      return db.favoriteBlogsUserses
          .insertOne(
            FavoriteBlogsUsersInsertRequest(
              blogId: request.blogId,
              userId: userView.id,
            ),
          )
          .then<Response>((_) => OkResponse())
          .onError(
            (e, _) => InternalServerErrorResponse(ErrorMessageCode.serverError),
          )
          .whenComplete(db.close);
    }
    return db
        .execute(
          Sql.indexed(
            'DELETE FROM favorite_blogs_userses '
            'WHERE blog_id= ? '
            'AND user_id= ?',
            substitution: '?',
          ),
          parameters: [
            TypedValue(Type.text, request.blogId),
            TypedValue(Type.text, userView.id),
          ],
        )
        .then<Response>((_) => OkResponse())
        .onError(
          (e, _) => InternalServerErrorResponse(ErrorMessageCode.serverError),
        )
        .whenComplete(db.close);
  } on CheckedFromJsonException catch (e) {
    return BadRequestResponse(ErrorMessageCode.bodyInvalid, e.message);
  } catch (e) {
    return InternalServerErrorResponse(ErrorMessageCode.serverError);
  }
}
