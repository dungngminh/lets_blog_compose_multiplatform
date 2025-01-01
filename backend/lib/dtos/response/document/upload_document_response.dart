import 'package:json_annotation/json_annotation.dart';

part 'upload_document_response.g.dart';

@JsonSerializable()
class UploadDocumentResponse {
  UploadDocumentResponse({required this.url});

  factory UploadDocumentResponse.fromJson(Map<String, dynamic> json) =>
      _$UploadDocumentResponseFromJson(json);

  final String url;

  Map<String, dynamic> toJson() => _$UploadDocumentResponseToJson(this);
}
