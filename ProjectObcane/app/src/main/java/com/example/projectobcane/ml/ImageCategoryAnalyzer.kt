package com.example.projectobcane.ml

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class ImageCategoryAnalyzer {

    fun analyze(
        context: Context,
        imageUri: Uri,
        onResult: (String) -> Unit
    ) {
        val image = InputImage.fromFilePath(context, imageUri)

        val labeler = ImageLabeling.getClient(
            ImageLabelerOptions.DEFAULT_OPTIONS
        )

        labeler.process(image)
            .addOnSuccessListener { labels ->
                val bestLabel = labels
                    .filter { it.confidence > 0.6f }
                    .maxByOrNull { it.confidence }

                onResult(bestLabel?.text ?: "Other")
            }
            .addOnFailureListener {
                onResult("Other")
            }
    }
}
