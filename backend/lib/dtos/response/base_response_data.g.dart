// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'base_response_data.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

BaseResponseData _$BaseResponseDataFromJson(Map<String, dynamic> json) =>
    $checkedCreate(
      'BaseResponseData',
      json,
      ($checkedConvert) {
        final val = BaseResponseData(
          success: $checkedConvert('success', (v) => v as bool),
          result: $checkedConvert('result', (v) => v),
          errorCode: $checkedConvert('error_code', (v) => v as String?),
          message: $checkedConvert(
              'message', (v) => v as String? ?? kSuccessResponseMessage),
        );
        return val;
      },
      fieldKeyMap: const {'errorCode': 'error_code'},
    );

Map<String, dynamic> _$BaseResponseDataToJson(BaseResponseData instance) {
  final val = <String, dynamic>{
    'success': instance.success,
  };

  void writeNotNull(String key, dynamic value) {
    if (value != null) {
      val[key] = value;
    }
  }

  writeNotNull('error_code', instance.errorCode);
  val['message'] = instance.message;
  writeNotNull('result', instance.result);
  return val;
}
