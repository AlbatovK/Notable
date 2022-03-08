package com.albatros.notable.ui.fragments.camera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.core.CameraSelector.LENS_FACING_BACK
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.albatros.notable.R
import com.albatros.notable.databinding.FragmentCameraBinding
import com.albatros.notable.ui.activities.MainActivity
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CameraFragment : Fragment() {

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var binding: FragmentCameraBinding
    private lateinit var cameraSelector: CameraSelector
    private lateinit var preview: Preview
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var imageCapture: ImageCapture

    private val viewModel: CameraViewModel by viewModel()

    companion object {
        private const val permissions_code = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private fun hasPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(binding.root.context,
            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermissions() {
        val permissions = arrayOf(Manifest.permission.CAMERA)
        @Suppress("deprecation")
        requestPermissions(permissions, permissions_code)
    }

    private val onImageStateChangedObserver = Observer<CameraViewModel.AnalysisState> {
        when(it) {
            is CameraViewModel.AnalysisState.Success -> {
                cameraProvider.unbindAll()
                lifecycleScope.launch {
                    with(binding) {
                        captureBtn.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out_animation))
                        cameraHint.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out_animation))
                        captureBtn.visibility = View.INVISIBLE
                        cameraHint.visibility = View.INVISIBLE
                    }
                    delay(600)
                    val title = it.data.textBlocks.firstOrNull()?.text ?: ""
                    val direction = CameraFragmentDirections.actionCameraFragmentToCreatorFragment(title, it.data.text)
                    findNavController().navigate(direction)
                }
            }
            is CameraViewModel.AnalysisState.Error -> {
                cameraProvider.unbindAll()
                Toast.makeText(binding.root.context, it.exception.localizedMessage, Toast.LENGTH_SHORT).show()
                val direction = CameraFragmentDirections.actionCameraFragmentToCreatorFragment()
                findNavController().navigate(direction)
            }
            is CameraViewModel.AnalysisState.Processing -> {
                Toast.makeText(binding.root.context, "Processing", Toast.LENGTH_SHORT).show()
            } else -> {}
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        (activity as MainActivity).onUserInteraction()
        if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED } ) {
            setupCameraUI()
        } else {
            val direction = CameraFragmentDirections.actionCameraFragmentToCreatorFragment()
            findNavController().navigate(direction)
        }
        @Suppress("deprecation")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private val callback = object: ImageCapture.OnImageCapturedCallback() {
        @androidx.camera.core.ExperimentalGetImage
        override fun onCaptureSuccess(image: ImageProxy) {
            viewModel.analyzeImage(image)
            viewModel.analysisState.observe(viewLifecycleOwner, onImageStateChangedObserver)
        }
        override fun onError(exception: ImageCaptureException) {
            Toast.makeText(binding.root.context, "Ошибка. Поробуйте ещё раз", Toast.LENGTH_SHORT).show()
            val direction = CameraFragmentDirections.actionCameraFragmentToCreatorFragment()
            findNavController().navigate(direction)
        }
    }

    private fun bindPreview(cameraProvider : ProcessCameraProvider) {
        preview = Preview.Builder().build()
        cameraSelector = CameraSelector.Builder()
            .requireLensFacing(LENS_FACING_BACK)
            .build()
        val camera = cameraProvider.bindToLifecycle(viewLifecycleOwner, cameraSelector, preview)
        preview.setSurfaceProvider(binding.previewView.createSurfaceProvider(camera.cameraInfo))

    }

    private fun captureImage() {
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setTargetRotation(binding.root.display.rotation)
            .build()
       cameraProvider.bindToLifecycle(viewLifecycleOwner, cameraSelector, imageCapture)
    }

    private fun setupCameraUI() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(binding.root.context)
        val executor = ContextCompat.getMainExecutor(binding.root.context)
        cameraProviderFuture.addListener( {
            cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, executor)

        binding.captureBtn.setOnClickListener {
            binding.captureBtn.setOnClickListener {
                Toast.makeText(binding.root.context, "Image is already being processed", Toast.LENGTH_SHORT).show()
            }
            captureImage()
            imageCapture.takePicture(ContextCompat.getMainExecutor(context), callback)
        }
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
        startPostponedEnterTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        if (!hasPermissions()) {
            requestCameraPermissions()
            sharedElementEnterTransition = TransitionInflater.from(context)
                .inflateTransition(android.R.transition.move)
            startPostponedEnterTransition()
            return
        } else setupCameraUI()
    }
}