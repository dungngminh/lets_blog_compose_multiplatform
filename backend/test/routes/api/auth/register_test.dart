import 'package:dart_frog/dart_frog.dart';
import 'package:mocktail/mocktail.dart';
import 'package:stormberry/stormberry.dart';
import 'package:test/test.dart';
import 'package:very_good_blog_app_backend/dtos/response/base_response_data.dart';
import 'package:very_good_blog_app_backend/models/user.dart';

import '../../../../routes/api/auth/register/index.dart' as route;

class MockDatabase extends Mock implements Database {}

class MockRequestContext extends Mock implements RequestContext {}

class MockRequest extends Mock implements Request {}

class MockUserRepository extends Mock implements UserRepository {}

void main() {
  group('POST /api/auth/register', () {
    late MockDatabase db;
    late MockRequestContext context;
    late MockRequest request;
    late MockUserRepository userRepository;
    final mockUser = UserView(
      id: '1',
      email: '',
      fullName: '',
      password: '',
      follower: 1,
      following: 1,
    );

    setUp(() {
      db = MockDatabase();
      context = MockRequestContext();
      request = MockRequest();
      userRepository = MockUserRepository();
    });

    test('returns BadRequestResponse when body is empty', () async {

      final response = await route.onRequest(context);

      expect(response, isA<BadRequestResponse>());
      
    });

    test('returns ConflictResponse when email is already registered', () async {
      const body =
          '{"email": "test@example.com", "password": "password", "fullName": "Test User"}';
      when(() => request.body()).thenAnswer((_) async => body);
      when(() => userRepository.queryUsers(any()))
          .thenAnswer((_) async => [mockUser]);

      final response = await route.onRequest(context);

      expect(response, isA<ConflictResponse>());
    });

    // test('returns CreatedResponse when registration is successful', () async {
    //   const body =
    //       '{"email": "test@example.com", "password": "password", "fullName": "Test User"}';
    //   when(request.body()).thenAnswer((_) async => body);
    //   when(userRepository.queryUsers(any)).thenAnswer((_) async => []);
    //   when(userRepository.insertOne(any)).thenAnswer((_) async => User());

    //   final response = await _onRegisterPostRequest(context);

    //   expect(response, isA<CreatedResponse>());
    // });

    // test('returns InternalServerErrorResponse on database error', () async {
    //   const body =
    //       '{"email": "test@example.com", "password": "password", "fullName": "Test User"}';
    //   when(request.body()).thenAnswer((_) async => body);
    //   when(userRepository.queryUsers(any)).thenAnswer((_) async => []);
    //   when(userRepository.insertOne(any)).thenThrow(Exception());

    //   final response = await _onRegisterPostRequest(context);

    //   expect(response, isA<InternalServerErrorResponse>());
    //   expect(
    //     (response as InternalServerErrorResponse).message,
    //     ErrorMessageCode.serverError,
    //   );
    // });
  });
}
