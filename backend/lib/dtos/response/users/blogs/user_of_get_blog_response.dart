import 'package:json_annotation/json_annotation.dart';
import 'package:very_good_blog_app_backend/models/user.dart';

part 'user_of_get_blog_response.g.dart';

@JsonSerializable()
class UserOfGetBlogResponse {
  UserOfGetBlogResponse({
    required this.id,
    required this.fullName,
    required this.email,
    required this.following,
    required this.follower,
    this.avatarUrl,
  });

  factory UserOfGetBlogResponse.fromJson(Map<String, dynamic> json) =>
      _$UserOfGetBlogResponseFromJson(json);

  factory UserOfGetBlogResponse.fromView(UserView view) {
    return UserOfGetBlogResponse(
      id: view.id,
      email: view.email,
      follower: view.follower,
      following: view.following,
      fullName: view.fullName,
      avatarUrl: view.avatarUrl,
    );
  }

  final String id;
  final String fullName;
  final String email;
  final String? avatarUrl;
  final int following;
  final int follower;

  Map<String, dynamic> toJson() => _$UserOfGetBlogResponseToJson(this);
}
