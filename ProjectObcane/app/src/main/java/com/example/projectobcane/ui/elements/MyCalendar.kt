package com.example.projectobcane.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.*
import java.time.format.DateTimeFormatter

import androidx.core.graphics.toColorInt

import com.example.projectobcane.navigation.INavigationRouter

import com.example.projectobcane.ui.theme.basicMargin
import com.example.projectobcane.ui.theme.halfMargin
import com.example.projectobcane.ui.theme.heighOfARow
import kotlin.math.ceil
import com.example.projectobcane.R







fun parseHexColorOrWhite(hex: String?): Color {
    return try {
        val cleanedHex = hex?.trim()?.removePrefix("#") ?: return Color.White
        val colorLong = cleanedHex.toLong(16)

        when (cleanedHex.length) {
            6 -> Color(colorLong or 0x00000000FF000000) // Add full alpha if missing
            8 -> Color(colorLong)
            else -> Color.White
        }
    } catch (e: Exception) {
        Color.White
    }
}
