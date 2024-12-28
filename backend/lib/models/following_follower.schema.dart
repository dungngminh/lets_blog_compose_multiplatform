// ignore_for_file: annotate_overrides

part of 'following_follower.dart';

extension FollowingFollowerRepositories on Session {
  FollowingFollowerRepository get followingFollowers => FollowingFollowerRepository._(this);
}

abstract class FollowingFollowerRepository
    implements
        ModelRepository,
        ModelRepositoryInsert<FollowingFollowerInsertRequest>,
        ModelRepositoryUpdate<FollowingFollowerUpdateRequest> {
  factory FollowingFollowerRepository._(Session db) = _FollowingFollowerRepository;

  Future<List<FollowingFollowerView>> queryFollowingFollowers([QueryParams? params]);
}

class _FollowingFollowerRepository extends BaseRepository
    with
        RepositoryInsertMixin<FollowingFollowerInsertRequest>,
        RepositoryUpdateMixin<FollowingFollowerUpdateRequest>
    implements FollowingFollowerRepository {
  _FollowingFollowerRepository(super.db) : super(tableName: 'following_followers');

  @override
  Future<List<FollowingFollowerView>> queryFollowingFollowers([QueryParams? params]) {
    return queryMany(FollowingFollowerViewQueryable(), params);
  }

  @override
  Future<void> insert(List<FollowingFollowerInsertRequest> requests) async {
    if (requests.isEmpty) return;
    var values = QueryValues();
    await db.execute(
      Sql.named('INSERT INTO "following_followers" ( "following_id", "follower_id" )\n'
          'VALUES ${requests.map((r) => '( ${values.add(r.followingId)}:text, ${values.add(r.followerId)}:text )').join(', ')}\n'),
      parameters: values.values,
    );
  }

  @override
  Future<void> update(List<FollowingFollowerUpdateRequest> requests) async {
    if (requests.isEmpty) return;
    var values = QueryValues();
    await db.execute(
      Sql.named('UPDATE "following_followers"\n'
          'SET "following_id" = COALESCE(UPDATED."following_id", "following_followers"."following_id"), "follower_id" = COALESCE(UPDATED."follower_id", "following_followers"."follower_id")\n'
          'FROM ( VALUES ${requests.map((r) => '( ${values.add(r.followingId)}:text::text, ${values.add(r.followerId)}:text::text )').join(', ')} )\n'
          'AS UPDATED("following_id", "follower_id")\n'
          'WHERE '),
      parameters: values.values,
    );
  }
}

class FollowingFollowerInsertRequest {
  FollowingFollowerInsertRequest({
    required this.followingId,
    required this.followerId,
  });

  final String followingId;
  final String followerId;
}

class FollowingFollowerUpdateRequest {
  FollowingFollowerUpdateRequest({
    this.followingId,
    this.followerId,
  });

  final String? followingId;
  final String? followerId;
}

class FollowingFollowerViewQueryable extends ViewQueryable<FollowingFollowerView> {
  @override
  String get query => 'SELECT "following_followers".*'
      'FROM "following_followers"';

  @override
  String get tableAlias => 'following_followers';

  @override
  FollowingFollowerView decode(TypedMap map) => FollowingFollowerView(
      followingId: map.get('following_id'), followerId: map.get('follower_id'));
}

class FollowingFollowerView {
  FollowingFollowerView({
    required this.followingId,
    required this.followerId,
  });

  final String followingId;
  final String followerId;
}
