package activity.dashboard.common.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DashboardDarkColorScheme =
    darkColorScheme(
        background = Color(0xFF0F100F),
        surface = Color(0xFF1A1B1A),
        onBackground = Color.White,
        surfaceVariant = Color(0xFF1F1F1F),
        onSurface = Color.White,
        primary = Color(0xFF3AE34F),
        onPrimary = Color.Black,
        secondary = Color(0xFF2C2C2C),
        onSecondary = Color.White,
    )

private val DashboardTypography =
    Typography(
        headlineSmall =
            TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
            ),
        titleMedium =
            TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
            ),
        bodyLarge =
            TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
            ),
        bodyMedium =
            TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFFCCCCCC),
            ),
        labelLarge =
            TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFEEEEEE),
            ),
    )

private val DashboardShapes =
    Shapes(
        small = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
        medium = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
        large = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
    )

@Composable
fun ActivityDashboardTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DashboardDarkColorScheme,
        typography = DashboardTypography,
        shapes = DashboardShapes,
        content = content,
    )
}
