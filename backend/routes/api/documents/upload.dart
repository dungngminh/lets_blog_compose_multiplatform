import 'dart:typed_data';

import 'package:cloudinary/cloudinary.dart' hide Response;
import 'package:dart_frog/dart_frog.dart';
import 'package:very_good_blog_app_backend/common/error_message_code.dart';
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
  try {
    final request = await context.request.formData();
    final folderName = request.fields['folderName'];
    final uploadedFile = request.files['image'];

    if (uploadedFile == null) {
      return BadRequestResponse(ErrorMessageCode.noImageUpload);
    }

    final imageByteData = await uploadedFile.readAsBytes();

    return cloudinary
        .upload(
      file: uploadedFile.name,
      fileBytes: Uint8List.fromList(imageByteData),
      folder: folderName,
      resourceType: CloudinaryResourceType.image,
    )
        .then<Response>((cloudinaryResponse) {
      final url = cloudinaryResponse.secureUrl;
      if (url != null) {
        return OkResponse(UploadDocumentResponse(url: url));
      } else {
        return InternalServerErrorResponse(ErrorMessageCode.serverError);
      }
    });
  } catch (e, _) {
    return InternalServerErrorResponse(ErrorMessageCode.serverError);
  }
}
