// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'upload_document_response.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

UploadDocumentResponse _$UploadDocumentResponseFromJson(
        Map<String, dynamic> json) =>
    $checkedCreate(
      'UploadDocumentResponse',
      json,
      ($checkedConvert) {
        final val = UploadDocumentResponse(
          url: $checkedConvert('url', (v) => v as String),
        );
        return val;
      },
    );

Map<String, dynamic> _$UploadDocumentResponseToJson(
        UploadDocumentResponse instance) =>
    <String, dynamic>{
      'url': instance.url,
    };
