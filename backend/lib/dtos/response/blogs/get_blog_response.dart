import 'package:json_annotation/json_annotation.dart';
import 'package:very_good_blog_app_backend/dtos/response/users/blogs/user_of_get_blog_response.dart';
import 'package:very_good_blog_app_backend/models/blog.dart';
import 'package:very_good_blog_app_backend/models/blog_category.dart';

part 'get_blog_response.g.dart';

@JsonSerializable()
class GetBlogResponse {
  GetBlogResponse({
    required this.id,
    required this.title,
    required this.content,
    required this.imageUrl,
    required this.category,
    required this.createdAt,
    required this.updatedAt,
    required this.creator,
    this.isFavoritedByUser,
  });

  factory GetBlogResponse.fromJson(Map<String, dynamic> json) =>
      _$GetBlogResponseFromJson(json);

  factory GetBlogResponse.fromView(BlogView view, {bool? isFavoritedByUser}) {
    return GetBlogResponse(
      id: view.id,
      title: view.title,
      content: view.content,
      imageUrl: view.imageUrl,
      category: view.category,
      isFavoritedByUser: isFavoritedByUser,
      createdAt: view.createdAt.toUtc(),
      updatedAt: view.updatedAt.toUtc(),
      creator: UserOfGetBlogResponse.fromView(view.creator),
    );
  }

  final String id;
  final String title;
  final String content;
  final String imageUrl;
  final BlogCategory category;
  final bool? isFavoritedByUser;
  final DateTime createdAt;
  final DateTime updatedAt;
  final UserOfGetBlogResponse creator;

  Map<String, dynamic> toJson() => _$GetBlogResponseToJson(this);
}
