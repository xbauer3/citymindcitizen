package com.example.projectobcane.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.graphics.Color

private val MARKDOWN_LINK_REGEX = Regex("""\[([^\]]+)]\((https?://[^)]+)\)""")

fun parseMarkdownLinks(
    text: String,
    linkColor: Color
): AnnotatedString = buildAnnotatedString {
    var cursor = 0
    for (match in MARKDOWN_LINK_REGEX.findAll(text)) {
        // Append plain text before this match
        append(text.substring(cursor, match.range.first))

        val label = match.groupValues[1]
        val url   = match.groupValues[2]

        pushLink(
            LinkAnnotation.Url(
                url = url,
                styles = TextLinkStyles(
                    style = SpanStyle(
                        color = linkColor,
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Medium
                    )
                )
            )
        )
        append(label)
        pop()

        cursor = match.range.last + 1
    }
    // Append any trailing plain text
    append(text.substring(cursor))
}