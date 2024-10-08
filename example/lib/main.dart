import 'package:flutter/material.dart';
import 'package:android_dynamic_icon/android_dynamic_icon.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final _androidDynamicIconPlugin = AndroidDynamicIcon();

  @override
  void initState() {
    AndroidDynamicIcon.initialize(data: {
      "classNames": ['MainActivity', 'ExampleIconActivity'],
      "packageName": "com.example.android_dynamic_icon_example"
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: GestureDetector(
          child: Center(
            child: ElevatedButton(
              onPressed: () async {
                await _androidDynamicIconPlugin
                    .changeIcon(classNames: ['ExampleActivity', '']);
                // To change icon back to default one.
                // await _androidDynamicIconPlugin
                //     .changeIcon(classNames: ['MainActivity', '']);
              },
              child: const Text('Change Icon'),
            ),
          ),
        ),
      ),
    );
  }
}
