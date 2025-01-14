import 'package:dart_frog/dart_frog.dart';
import 'package:mocktail/mocktail.dart';
import 'package:test/test.dart';
import 'package:very_good_blog_app_backend/common/constants.dart';
import 'package:very_good_blog_app_backend/dtos/response/base_response_data.dart';

import '../../../../routes/api/auth/login/index.dart' as route;

class _MockRequestContext extends Mock implements RequestContext {}

void main() {
  group('POST /api/auth/login', () {
    test('responds with a 200 and returns Ok Response().', () async {
      final context = _MockRequestContext();
      when(context.request.body).thenAnswer((_) async => '{}');
      final response = await route.onRequest(context);
      expect(response, isA<BadRequestResponse>());
      expect(
        response.json(),
        completion(
          equals(
            {'success': false, 'message': kFailedResponseMessage},
          ),
        ),
      );
    });
  });
}
