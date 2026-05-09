package com.example.projectobcane.ui.elements

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.projectobcane.R
import com.example.projectobcane.ui.theme.chipCornerRadius
import com.example.projectobcane.ui.theme.chipPaddingHorizontal
import com.example.projectobcane.ui.theme.chipPaddingVertical

import com.example.projectobcane.ui.theme.Purple


@Composable
fun StatusChip(status: String) {
    val (bg, textColor) = when (status) {
        "NEW" -> Color(0xFFEDE7FF) to Purple
        "IN_PROGRESS" -> Color(0xFFFFF3E0) to Color(0xFFE65100)
        "SOLVED" -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
        else -> Color(0xFFF5F5F5) to Color(0xFF757575)
    }
    val label = when (status) {
        "NEW" -> stringResource(R.string.status_new)
        "IN_PROGRESS" -> stringResource(R.string.status_in_progress)
        "SOLVED" -> stringResource(R.string.status_solved)
        else -> stringResource(R.string.status_hidden)
    }
    Surface(shape = RoundedCornerShape(chipCornerRadius), color = bg) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = chipPaddingHorizontal, vertical = chipPaddingVertical),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}
