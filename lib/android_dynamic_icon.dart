import 'android_dynamic_icon_platform_interface.dart';

class AndroidDynamicIcon {
  static Future<void> initialize({required Map<String, dynamic> data}) async {
    await AndroidDynamicIconPlatform.instance
        .initialize(data: data);
  }

  Future<void> changeIcon({required List<String> classNames}) async {
    await AndroidDynamicIconPlatform.instance
        .changeIcon(classNames: classNames);
  }
}
