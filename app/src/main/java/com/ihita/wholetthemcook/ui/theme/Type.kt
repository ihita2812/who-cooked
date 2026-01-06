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

val PlayFair = FontFamily(
    Font(R.font.playfairdisplay_extrabold, FontWeight.ExtraBold)
)

val Raleway = FontFamily(
    Font(R.font.raleway_medium, FontWeight.Medium)
)

val Jack = FontFamily(
    Font(R.font.jack_regular, FontWeight.Normal)
)

val Typography = Typography(
    titleLarge = TextStyle(fontFamily = Jack, fontSize = 38.sp),
    titleMedium = TextStyle(fontFamily = PlayFair, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold),
    labelLarge = TextStyle(fontFamily = Raleway, fontSize = 16.sp, letterSpacing = 1.6.sp),
    bodyLarge = TextStyle(fontFamily = Nunito, fontSize = 18.sp),
    bodyMedium = TextStyle(fontFamily = Nunito, fontSize = 16.sp),
    labelSmall = TextStyle(fontFamily = Poppins, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
)
