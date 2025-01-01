import 'package:json_annotation/json_annotation.dart';

part 'upload_document_request.g.dart';

@JsonSerializable()
class UploadDocumentRequest {
  UploadDocumentRequest({
    required this.folderName,
    required this.fileName,
    required this.base64Source,
  });

  factory UploadDocumentRequest.fromJson(Map<String, dynamic> data) =>
      _$UploadDocumentRequestFromJson(data);

  final String folderName;
  final String fileName;
  final String base64Source;

  Map<String, dynamic> toJson() => _$UploadDocumentRequestToJson(this);
}
