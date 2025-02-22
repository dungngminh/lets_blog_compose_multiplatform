import 'package:dart_frog/dart_frog.dart';
import 'package:stormberry/stormberry.dart';
import 'package:very_good_blog_app_backend/common/error_message_code.dart';
import 'package:very_good_blog_app_backend/dtos/response/base_response_data.dart';
import 'package:very_good_blog_app_backend/dtos/response/blogs/get_blog_response.dart';
import 'package:very_good_blog_app_backend/models/blog.dart';

/// @Allow(GET)
Future<Response> onRequest(RequestContext context, String id) {
  return switch (context.request.method) {
    HttpMethod.get => _onUsersByIdBlogsGet(context, id),
    _ => Future.value(MethodNotAllowedResponse()),
  };
}

Future<Response> _onUsersByIdBlogsGet(RequestContext context, String id) {
  return context
      .read<Database>()
      .blogs
      .queryBlogs(
        QueryParams(
          where: 'creator_id=@id and is_deleted=false',
          orderBy: 'created_at DESC',
          values: {'id': id},
        ),
      )
      .then((views) => views.map(GetBlogResponse.fromView))
      .then<Response>((res) => OkResponse(res.map((e) => e.toJson()).toList()))
      .onError(
        (e, _) => InternalServerErrorResponse(ErrorMessageCode.serverError),
      );
}
