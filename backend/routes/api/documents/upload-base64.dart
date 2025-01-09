import 'dart:convert';

import 'package:cloudinary/cloudinary.dart' hide Response;
import 'package:dart_frog/dart_frog.dart';
import 'package:json_annotation/json_annotation.dart';
import 'package:very_good_blog_app_backend/common/extensions/json_ext.dart';
import 'package:very_good_blog_app_backend/dtos/request/document/upload_document_request.dart';
import 'package:very_good_blog_app_backend/dtos/response/base_response_data.dart';
import 'package:very_good_blog_app_backend/dtos/response/document/upload_document_response.dart';

/// @Allow(POST)
Future<Response> onRequest(RequestContext context) {
  return switch (context.request.method) {
    HttpMethod.post => _onUploadPostRequest(context),
    _ => Future.value(MethodNotAllowedResponse())
  };
}

Future<Response> _onUploadPostRequest(RequestContext context) async {
  final cloudinary = context.read<Cloudinary>();
  final body = await context.request.body();
  if (body.isEmpty) {
    return BadRequestResponse();
  }
  try {
    final request = UploadDocumentRequest.fromJson(body.asJson());

    return cloudinary
        .upload(
      file: request.fileName,
      fileBytes: base64Decode(request.base64Source),
      folder: request.fileName,
      resourceType: CloudinaryResourceType.image,
    )
        .then<Response>((cloudinaryResponse) {
      final url = cloudinaryResponse.secureUrl;
      if (url != null) {
        return OkResponse(UploadDocumentResponse(url: url));
      } else {
        return InternalServerErrorResponse('upload-failed');
      }
    });
  } on CheckedFromJsonException catch (e) {
    return BadRequestResponse(e.message);
  } catch (e, st) {
    return InternalServerErrorResponse(st.toString());
  }
}
