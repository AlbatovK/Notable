package com.albatros.notable.model.analyzer

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class ImageAnalyzer(private val listener: AnalyzerListener) : ImageAnalysis.Analyzer {

    interface AnalyzerListener {
        fun onSuccess(text: Text)
        fun onFailure(e: Exception)
        fun onComplete(task: Task<Text>)
    }

    @androidx.camera.core.ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.image?.let { img ->
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            val image = InputImage.fromMediaImage(img, imageProxy.imageInfo.rotationDegrees)
            recognizer.process(image)
                .addOnSuccessListener { listener.onSuccess(it) }
                .addOnFailureListener { listener.onFailure(it) }
                .addOnCompleteListener { listener.onComplete(it) }
        }
    }
}
