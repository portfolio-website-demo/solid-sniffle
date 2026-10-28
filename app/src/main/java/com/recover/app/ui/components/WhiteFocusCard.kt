package com.recover.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.recover.app.R

/**
 * Single v1 White Focus Card component matching root `card.png` artwork,
 * using `recover_logo_winged_black.png` and binding live ledger balance and cardholder name.
 */
@Composable
fun WhiteFocusCard(
    creditsBalance: Int,
    cardHolderName: String,
    cardNumber: String = "0116931",
    modifier: Modifier = Modifier,
    formatWithDecimals: Boolean = false
) {
    val cardShape = RoundedCornerShape(22.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.78f)
            .shadow(elevation = 10.dp, shape = cardShape, clip = false)
            .clip(cardShape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFF7F8FA),
                        Color(0xFFEEEEF1),
                        Color(0xFFE4E5E9)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(900f, 600f)
                )
            )
            .border(1.dp, Color(0xFFFFFFFF).copy(alpha = 0.85f), cardShape)
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Row: Black Winged Recover Logo + CARD NO.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.recover_logo_winged_black),
                    contentDescription = "Recover Logo",
                    modifier = Modifier.size(width = 48.dp, height = 36.dp)
                )
                Text(
                    text = "CARD NO. $cardNumber",
                    color = Color(0xFF686A6E),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.6.sp
                )
            }

            // Middle Row: Dynamic Credits Balance + Metallic EMV Chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    val formatted = if (formatWithDecimals) {
                        "$creditsBalance.00"
                    } else {
                        creditsBalance.toString()
                    }
                    Text(
                        text = formatted,
                        color = Color(0xFF0A0A0C),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp,
                        modifier = Modifier.alignByBaseline()
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "credits",
                        color = Color(0xFF141416),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.alignByBaseline()
                    )
                }

                EmvChipGraphic()
            }

            // Bottom Row: Cardholder Name
            Text(
                text = "${cardHolderName.uppercase()}’S CARD",
                color = Color(0xFF121214),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.8.sp
            )
        }
    }
}

@Composable
private fun EmvChipGraphic() {
    Box(
        modifier = Modifier
            .size(width = 44.dp, height = 34.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFFC9CBD0), Color(0xFFB5B8BE), Color(0xFFAEB1B7))
                )
            )
            .border(0.8.dp, Color(0xFF8E9197), RoundedCornerShape(6.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeColor = Color(0xFF7D8086)
            val strokeWidth = 1.dp.toPx()
            val w = size.width
            val h = size.height

            drawLine(strokeColor, Offset(w * 0.32f, 0f), Offset(w * 0.32f, h), strokeWidth)
            drawLine(strokeColor, Offset(w * 0.68f, 0f), Offset(w * 0.68f, h), strokeWidth)
            drawLine(strokeColor, Offset(0f, h * 0.38f), Offset(w * 0.32f, h * 0.38f), strokeWidth)
            drawLine(strokeColor, Offset(0f, h * 0.68f), Offset(w * 0.32f, h * 0.68f), strokeWidth)
            drawLine(strokeColor, Offset(w * 0.68f, h * 0.38f), Offset(w, h * 0.38f), strokeWidth)
            drawLine(strokeColor, Offset(w * 0.68f, h * 0.68f), Offset(w, h * 0.68f), strokeWidth)
            drawRoundRect(
                color = strokeColor,
                topLeft = Offset(w * 0.32f, h * 0.25f),
                size = androidx.compose.ui.geometry.Size(w * 0.36f, h * 0.5f),
                cornerRadius = CornerRadius(4f, 4f),
                style = Stroke(width = strokeWidth)
            )
        }
    }
}
