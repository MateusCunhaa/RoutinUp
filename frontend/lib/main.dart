import 'package:flutter/material.dart';
import 'package:routinup_app/theme/app_theme.dart';
import 'screens/Login/login_screen.dart';


void main() {
  runApp(const RoutinUpApp());
}


class RoutinUpApp extends StatelessWidget {
  const RoutinUpApp({super.key});


  @override
  Widget build(BuildContext context) {

    return MaterialApp(

      debugShowCheckedModeBanner: false,

      title: "RoutinUP",

      theme: AppTheme.darkTheme,

      home: const LoginScreen(),

    );
  }
}