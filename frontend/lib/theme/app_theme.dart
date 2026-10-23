import 'package:flutter/material.dart';

class AppTheme {
//Cores Padrões
  static const Color background = Color(0xFF000000);

  static const Color gold = Color(0xFFD4AF37);

  static const Color goldLight = Color(0xFFFFD700);

  static const Color textWhite = Color(0xFFFFFFFF);

  static const Color textGray = Color(0xFFB0B0B0);


  static ThemeData darkTheme = ThemeData(

    brightness: Brightness.dark,

    scaffoldBackgroundColor: background,


    colorScheme: const ColorScheme.dark(

      primary: gold,

      secondary: goldLight,

      surface: background,

    ),


    inputDecorationTheme: InputDecorationTheme(

      filled: true,

      fillColor: Colors.black,

      labelStyle: const TextStyle(
        color: textGray,
      ),


      enabledBorder: OutlineInputBorder(

        borderRadius: BorderRadius.all(
          Radius.circular(15),
        ),

        borderSide: BorderSide(
          color: gold,
        ),

      ),


      focusedBorder: OutlineInputBorder(

        borderRadius: BorderRadius.all(
          Radius.circular(15),
        ),

        borderSide: BorderSide(
          color: goldLight,
          width: 2,
        ),

      ),

    ),


    elevatedButtonTheme: ElevatedButtonThemeData(

      style: ElevatedButton.styleFrom(

        backgroundColor: gold,

        foregroundColor: Colors.black,

        padding: const EdgeInsets.symmetric(
          vertical: 15,
        ),

        shape: RoundedRectangleBorder(

          borderRadius:
          BorderRadius.circular(15),

        ),

      ),

    ),

  );

}