import 'dart:io';

import 'package:dart_frog/dart_frog.dart';
import 'package:json_annotation/json_annotation.dart';
import 'package:very_good_blog_app_backend/common/constants.dart';

part 'base_response_data.g.dart';

@JsonSerializable(
  explicitToJson: true,
  includeIfNull: false,
)
class BaseResponseData {
  const BaseResponseData({
    required this.success,
    this.result,
    this.errorCode,
    this.message = kSuccessResponseMessage,
  });

  factory BaseResponseData.fromJson(Map<String, dynamic> json) =>
      _$BaseResponseDataFromJson(json);

  factory BaseResponseData.data(dynamic result) {
    return BaseResponseData(
      success: true,
      result: result,
    );
  }

  factory BaseResponseData.empty([String? message]) {
    return BaseResponseData(
      success: true,
      message: message ?? kSuccessResponseMessage,
    );
  }

  factory BaseResponseData.failed({String? errorCode, String? message}) {
    return BaseResponseData(
      success: false,
      errorCode: errorCode,
      message: message ?? kFailedResponseMessage,
    );
  }

  final bool success;
  final String? errorCode;
  final String message;
  final dynamic result;

  Map<String, dynamic> toJson() => _$BaseResponseDataToJson(this);

  BaseResponseData copyWith({
    bool? success,
    String? errorCode,
    String? message,
    dynamic result,
  }) {
    return BaseResponseData(
      success: success ?? this.success,
      errorCode: errorCode ?? this.errorCode,
      message: message ?? this.message,
      result: result ?? this.result,
    );
  }
}

class OkResponse extends Response {
  OkResponse([dynamic data])
      : super.json(
          statusCode: HttpStatus.ok,
          body: data == null
              ? BaseResponseData.empty().toJson()
              : BaseResponseData.data(data).toJson(),
        );
}

class CreatedResponse extends Response {
  CreatedResponse([String? message])
      : super.json(
          statusCode: HttpStatus.created,
          body: BaseResponseData.empty(message).toJson(),
        );
}

class NotFoundResponse extends Response {
  NotFoundResponse([String? errorCode, String? message])
      : super.json(
          statusCode: HttpStatus.notFound,
          body: BaseResponseData.failed(errorCode: errorCode, message: message)
              .toJson(),
        );
}

class ConflictResponse extends Response {
  ConflictResponse([String? errorCode, String? message])
      : super.json(
          statusCode: HttpStatus.conflict,
          body: BaseResponseData.failed(errorCode: errorCode, message: message)
              .toJson(),
        );
}

class UnauthorizedResponse extends Response {
  UnauthorizedResponse([String? errorCode, String? message])
      : super.json(
          statusCode: HttpStatus.unauthorized,
          body: BaseResponseData.failed(errorCode: errorCode, message: message)
              .toJson(),
        );
}

class BadRequestResponse extends Response {
  BadRequestResponse([String? errorCode, String? message])
      : super.json(
          statusCode: HttpStatus.badRequest,
          body: BaseResponseData.failed(errorCode: errorCode, message: message)
              .toJson(),
        );
}

class ForbiddenResponse extends Response {
  ForbiddenResponse([String? errorCode, String? message])
      : super.json(
          statusCode: HttpStatus.forbidden,
          body: BaseResponseData.failed(errorCode: errorCode, message: message)
              .toJson(),
        );
}

class InternalServerErrorResponse extends Response {
  InternalServerErrorResponse([String? errorCode, String? message])
      : super.json(
          statusCode: HttpStatus.internalServerError,
          body: BaseResponseData.failed(errorCode: errorCode, message: message)
              .toJson(),
        );
}

class MethodNotAllowedResponse extends Response {
  MethodNotAllowedResponse()
      : super.json(
          statusCode: HttpStatus.methodNotAllowed,
        );
}
