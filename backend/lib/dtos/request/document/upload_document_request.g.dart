// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'upload_document_request.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

UploadDocumentRequest _$UploadDocumentRequestFromJson(
        Map<String, dynamic> json) =>
    $checkedCreate(
      'UploadDocumentRequest',
      json,
      ($checkedConvert) {
        final val = UploadDocumentRequest(
          folderName: $checkedConvert('folder_name', (v) => v as String),
          fileName: $checkedConvert('file_name', (v) => v as String),
          base64Source: $checkedConvert('base64_source', (v) => v as String),
        );
        return val;
      },
      fieldKeyMap: const {
        'folderName': 'folder_name',
        'fileName': 'file_name',
        'base64Source': 'base64_source'
      },
    );

Map<String, dynamic> _$UploadDocumentRequestToJson(
        UploadDocumentRequest instance) =>
    <String, dynamic>{
      'folder_name': instance.folderName,
      'file_name': instance.fileName,
      'base64_source': instance.base64Source,
    };
