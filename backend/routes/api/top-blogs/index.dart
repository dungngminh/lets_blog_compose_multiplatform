import 'package:dart_frog/dart_frog.dart';
import 'package:dartx/dartx.dart';
import 'package:stormberry/stormberry.dart';
import 'package:very_good_blog_app_backend/common/extensions/header_extesion.dart';
import 'package:very_good_blog_app_backend/dtos/response/base_response_data.dart';
import 'package:very_good_blog_app_backend/dtos/response/blogs/get_blog_response.dart';
import 'package:very_good_blog_app_backend/models/favorite_blogs_users.dart';
import 'package:very_good_blog_app_backend/models/user.dart';
import 'package:very_good_blog_app_backend/util/jwt_handler.dart';

/// @Allow(GET)
/// @Query(limit)
/// @Query(search)
Future<Response> onRequest(RequestContext context) {
  return switch (context.request.method) {
    HttpMethod.get => _onTopBlogsGetRequest(context),
    _ => Future.value(MethodNotAllowedResponse()),
  };
}

Future<Response> _onTopBlogsGetRequest(RequestContext context) async {
  final db = context.read<Database>();
  final queryParams = context.request.uri.queryParameters;
  final limit = int.tryParse(queryParams['limit'].orEmpty()) ?? 20;

  UserView? user;
  final bearerToken = context.request.headers.bearer();
  if (bearerToken != null) {
    final jwtHandler = context.read<JwtHandler>();
    user = await jwtHandler.userFromToken(bearerToken);
  }

  var favoriteBlogIds = <String>[];
  if (user != null) {
    favoriteBlogIds = await db.favoriteBlogsUserses
        .queryFavoriteBlogsUserses(
          QueryParams(where: 'user_id=@id', values: {'id': user.id}),
        )
        .onError((error, stackTrace) => [])
        .then(
          (favoriteBlogs) => favoriteBlogs.map((e) => e.blog.id).toList(),
        );
  }
  return db.favoriteBlogsUserses
      .queryFavoriteBlogsUserses()
      .then((result) => result.groupBy((e) => e.blog.id).entries)
      .then((entries) => entries.sortedByDescending((e) => e.value.length))
      .then(
        (entries) => entries
            .mapNotNull((e) => e.value.firstOrNull?.blog)
            .take(limit)
            .map(
              (view) => GetBlogResponse.fromView(
                view,
                isFavoritedByUser:
                    user == null ? null : favoriteBlogIds.contains(view.id),
              ),
            )
            .toList(),
      )
      .then<Response>(
        (topBlogs) => OkResponse(topBlogs.map((e) => e.toJson()).toList()),
      )
      .onError((e, _) => InternalServerErrorResponse(e.toString()));
}
