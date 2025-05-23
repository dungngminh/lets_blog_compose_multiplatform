import 'package:dart_frog/dart_frog.dart';
import 'package:dartx/dartx.dart';
import 'package:json_annotation/json_annotation.dart';
import 'package:stormberry/stormberry.dart';
import 'package:uuid/uuid.dart';
import 'package:very_good_blog_app_backend/common/error_message_code.dart';
import 'package:very_good_blog_app_backend/common/extensions/header_extesion.dart';
import 'package:very_good_blog_app_backend/common/extensions/json_ext.dart';
import 'package:very_good_blog_app_backend/dtos/request/blogs/create_blog_request.dart';
import 'package:very_good_blog_app_backend/dtos/response/base_response_data.dart';
import 'package:very_good_blog_app_backend/dtos/response/blogs/get_blog_response.dart';
import 'package:very_good_blog_app_backend/models/blog.dart';
import 'package:very_good_blog_app_backend/models/favorite_blogs_users.dart';
import 'package:very_good_blog_app_backend/models/user.dart';
import 'package:very_good_blog_app_backend/util/jwt_handler.dart';

/// @Allow(GET, POST)
/// @Query(limit)
/// @Query(page)
/// @Query(search)
/// @Query(descending)
Future<Response> onRequest(RequestContext context) {
  return switch (context.request.method) {
    HttpMethod.get => _onBlogsGetRequest(context),
    HttpMethod.post => _onBlogsPostRequest(context),
    _ => Future.value(MethodNotAllowedResponse()),
  };
}

Future<Response> _onBlogsGetRequest(RequestContext context) async {
  final db = context.read<Database>();
  final queryParams = context.request.uri.queryParameters;
  final limit = int.tryParse(queryParams['limit'].orEmpty()) ?? 20;
  final currentPage = int.tryParse(queryParams['page'].orEmpty()) ?? 1;
  final descending = bool.tryParse(
        queryParams['descending'].orEmpty(),
        caseSensitive: false,
      ) ??
      true;
  final search = queryParams['search'];
  final category = queryParams['category'];

  UserView? user;
  final bearerToken = context.request.headers.bearer();
  if (bearerToken != null) {
    final jwtHandler = context.read<JwtHandler>();
    user = await jwtHandler.userFromToken(bearerToken);
  }

  try {
    final results = await db.blogs.queryBlogs(
      QueryParams(
        limit: limit,
        orderBy: descending ? 'created_at DESC' : 'created_at ASC',
        offset: (currentPage - 1) * limit,
        where: category.isNullOrEmpty
            ? search.isNullOrEmpty
                ? 'is_deleted=false'
                : '(LOWER(title) LIKE @search OR LOWER(content) LIKE @search) '
                    'AND is_deleted=false'
            : search.isNullOrEmpty
                ? 'category=@category AND is_deleted=false'
                : '(LOWER(title) LIKE @search OR LOWER(content) LIKE @search) '
                    'AND category=@category AND is_deleted=false',
        values: search.isNullOrEmpty
            ? category.isNullOrEmpty
                ? null
                : {'category': category}
            : {'search': '%${search?.toLowerCase()}%'},
      ),
    );
    var favoriteBlogIds = <String>[];
    if (user != null) {
      final favoriteBlogs =
          await db.favoriteBlogsUserses.queryFavoriteBlogsUserses(
        QueryParams(
          where: 'user_id=@id',
          values: {'id': user.id},
        ),
      );
      favoriteBlogIds = favoriteBlogs
          .filter((e) => !e.blog.isDeleted)
          .map((e) => e.blog.id)
          .toList();
    }

    final blogs = results
        .map(
          (view) => GetBlogResponse.fromView(
            view,
            isFavoritedByUser:
                user == null ? null : favoriteBlogIds.contains(view.id),
          ),
        )
        .toList();

    return OkResponse(blogs.map((e) => e.toJson()).toList());
  } catch (e) {
    return InternalServerErrorResponse(ErrorMessageCode.serverError);
  }
}

Future<Response> _onBlogsPostRequest(RequestContext context) async {
  final db = context.read<Database>();
  final user = context.read<UserView>();

  final body = await context.request.body();

  if (body.isEmpty) return BadRequestResponse();
  try {
    final request = CreateBlogRequest.fromJson(body.asJson());

    await db.blogs
        .insertOne(
          BlogInsertRequest(
            id: const Uuid().v4(),
            title: request.title,
            category: request.category,
            content: request.content,
            imageUrl: request.imageUrl,
            createdAt: DateTime.now().toUtc(),
            creatorId: user.id,
            updatedAt: DateTime.now().toUtc(),
            isDeleted: false,
          ),
        )
        .whenComplete(db.close);
    return CreatedResponse('New blog is created');
  } on CheckedFromJsonException {
    return BadRequestResponse(ErrorMessageCode.bodyInvalid);
  } catch (e) {
    return InternalServerErrorResponse(ErrorMessageCode.serverError);
  }
}
