// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'user_of_get_blog_response.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

UserOfGetBlogResponse _$UserOfGetBlogResponseFromJson(
        Map<String, dynamic> json) =>
    $checkedCreate(
      'UserOfGetBlogResponse',
      json,
      ($checkedConvert) {
        final val = UserOfGetBlogResponse(
          id: $checkedConvert('id', (v) => v as String),
          fullName: $checkedConvert('full_name', (v) => v as String),
          email: $checkedConvert('email', (v) => v as String),
          following: $checkedConvert('following', (v) => (v as num).toInt()),
          follower: $checkedConvert('follower', (v) => (v as num).toInt()),
          avatarUrl: $checkedConvert('avatar_url', (v) => v as String?),
        );
        return val;
      },
      fieldKeyMap: const {'fullName': 'full_name', 'avatarUrl': 'avatar_url'},
    );

Map<String, dynamic> _$UserOfGetBlogResponseToJson(
        UserOfGetBlogResponse instance) =>
    <String, dynamic>{
      'id': instance.id,
      'full_name': instance.fullName,
      'email': instance.email,
      'avatar_url': instance.avatarUrl,
      'following': instance.following,
      'follower': instance.follower,
    };
