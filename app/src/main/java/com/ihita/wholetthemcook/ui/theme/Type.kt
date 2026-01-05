package com.ihita.wholetthemcook.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ihita.wholetthemcook.R

val Poppins = FontFamily(
    Font(R.font.poppins_semibold, FontWeight.SemiBold)
)

val Nunito = FontFamily(
    Font(R.font.nunito_regular, FontWeight.Normal),
    Font(R.font.nunito_bold, FontWeight.Bold)
)

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = Poppins,
        fontSize = 36.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Poppins,
        fontSize = 22.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Nunito,
        fontSize = 16.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Nunito,
        fontSize = 14.sp
    )
)
