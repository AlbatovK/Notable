package com.albatros.notable.ui.fragments.camera

import androidx.camera.core.ImageProxy
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.albatros.notable.model.analyzer.ImageAnalyzer
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.text.Text

class CameraViewModel : ViewModel() {

    private var _analysisState: MutableLiveData<AnalysisState> = MutableLiveData<AnalysisState>()

    val analysisState: LiveData<AnalysisState> = _analysisState

    sealed class AnalysisState {
        data class Success(val data: Text) : AnalysisState()
        data class Processing(val processingImage: ImageProxy?) : AnalysisState()
        data class Error(val exception: Exception) : AnalysisState()
        data class Complete(val task: Task<Text>) : AnalysisState()
    }

    private val listener = object : ImageAnalyzer.AnalyzerListener {
        override fun onSuccess(text: Text) { _analysisState.value = AnalysisState.Success(text) }
        override fun onFailure(e: Exception) { _analysisState.value = AnalysisState.Error(e) }
        override fun onComplete(task: Task<Text>) { _analysisState.value = AnalysisState.Complete(task) }
    }

    @androidx.camera.core.ExperimentalGetImage
    fun analyzeImage(proxy: ImageProxy) {
        _analysisState.value = AnalysisState.Processing(proxy)
        val analyzer = ImageAnalyzer(listener)
        analyzer.analyze(proxy)
    }
}