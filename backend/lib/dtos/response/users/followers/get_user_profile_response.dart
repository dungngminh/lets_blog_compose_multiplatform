import 'package:json_annotation/json_annotation.dart';
import 'package:very_good_blog_app_backend/models/following_follower.dart';
import 'package:very_good_blog_app_backend/models/user.dart';

part 'get_user_profile_response.g.dart';

@JsonSerializable()
class GetUserFollowerResponse {
  GetUserFollowerResponse({
    required this.id,
    required this.fullName,
    required this.email,
    this.avatarUrl,
  });

  factory GetUserFollowerResponse.fromJson(Map<String, dynamic> json) =>
      _$GetUserFollowerResponseFromJson(json);

  factory GetUserFollowerResponse.fromView(UserView follower) {
    return GetUserFollowerResponse(
      id: follower.id,
      fullName: follower.fullName,
      email: follower.email,
      avatarUrl: follower.avatarUrl,
    );
  }

  final String id;
  final String fullName;
  final String email;
  final String? avatarUrl;

  Map<String, dynamic> toJson() => _$GetUserFollowerResponseToJson(this);
}
