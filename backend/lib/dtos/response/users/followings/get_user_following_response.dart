import 'package:json_annotation/json_annotation.dart';
import 'package:very_good_blog_app_backend/models/user.dart';

part 'get_user_following_response.g.dart';

@JsonSerializable()
class GetUserFollowingResponse {
  GetUserFollowingResponse({
    required this.id,
    required this.fullName,
    required this.email,
    this.avatarUrl,
  });

  factory GetUserFollowingResponse.fromJson(Map<String, dynamic> json) =>
      _$GetUserFollowingResponseFromJson(json);

  factory GetUserFollowingResponse.fromView(UserView following) {
    return GetUserFollowingResponse(
      id: following.id,
      fullName: following.fullName,
      email: following.email,
      avatarUrl: following.avatarUrl,
    );
  }

  final String id;
  final String fullName;
  final String email;
  final String? avatarUrl;

  Map<String, dynamic> toJson() => _$GetUserFollowingResponseToJson(this);
}
