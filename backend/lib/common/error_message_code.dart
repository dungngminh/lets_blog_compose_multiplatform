class ErrorMessageCode {
  ErrorMessageCode._();

  // Base
  static const serverError = 'server-error';
  static const bodyEmpty = 'body-empty';
  static const bodyInvalid = 'body-invalid';

  // Login
  static const invalidEmailOrPassword = 'invalid-email-or-password';

  // Register
  static const emailRegisterd = 'email-registerd';

  // Blog
  static const blogNotFound = 'blog-not-found';

  // Upload
  static const noImageUpload = 'no-image-upload';
  static const uploadFailed = 'upload-failed';

  // Favorite
  static const cannotActionOwnBlog = 'cannot-ation-own-blog';
  static const alreadyFavorited = 'already-favorited';

  // Blog by user id
  static const youAreNotCreator = 'you-are-not-creator';

  // User
  static const userNotFound = 'user-not-found';
}
