import 'package:json_annotation/json_annotation.dart';
import 'package:very_good_blog_app_backend/models/user.dart';

part 'get_user_profile_response.g.dart';

@JsonSerializable()
class GetUserProfileResponse {
  GetUserProfileResponse({
    required this.id,
    required this.fullName,
    required this.email,
    required this.following,
    required this.follower,
    required this.blogCount,
    this.avatarUrl,
  });

  factory GetUserProfileResponse.fromJson(Map<String, dynamic> json) =>
      _$GetUserProfileResponseFromJson(json);

  factory GetUserProfileResponse.fromDb({
    required UserView view,
    required int following,
    required int follower,
    required int blogCount,
  }) {
    return GetUserProfileResponse(
      id: view.id,
      fullName: view.fullName,
      email: view.email,
      following: following,
      follower: follower,
      avatarUrl: view.avatarUrl,
      blogCount: blogCount,
    );
  }

  final String id;
  final String fullName;
  final String email;
  final String? avatarUrl;
  final int following;
  final int follower;
  final int blogCount;

  Map<String, dynamic> toJson() => _$GetUserProfileResponseToJson(this);
}
