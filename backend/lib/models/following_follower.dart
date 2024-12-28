import 'package:stormberry/stormberry.dart';

part 'following_follower.schema.dart';

@Model()
abstract class FollowingFollower {
  String get followingId;
  String get followerId;
}
