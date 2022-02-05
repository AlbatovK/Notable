package com.albatros.notable.ui.fragments.updater

import android.app.Activity
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.albatros.notable.R
import com.albatros.notable.databinding.FragmentUpdaterBinding
import com.albatros.notable.model.data.Note
import org.koin.androidx.viewmodel.ext.android.viewModel

class UpdaterFragment : Fragment() {

    private lateinit var binding: FragmentUpdaterBinding
    private val viewModel: UpdaterViewModel by viewModel()
    private val arguments by navArgs<UpdaterFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.inputValid.observe(viewLifecycleOwner, onValidStateChangedObserver)
        postponeEnterTransition()
        with(arguments.arg) {
            binding.titleEditText.setText(title, TextView.BufferType.EDITABLE)
            binding.descriptionEditText.setText(data, TextView.BufferType.EDITABLE)
        }
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
        startPostponedEnterTransition()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_updater, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.action_save -> {
            sendNoteData()
            true
        } else -> false
    }

    private fun sendNoteData() {
        with(binding) {
            val title = titleEditText.text.toString()
            val description = descriptionEditText.text.toString()
            viewModel.processNote(arguments.arg, title, description)
        }
    }

    private val onValidStateChangedObserver = Observer<Note?> {
        with(binding) {
            if (it != null) {
                titleEditText.clearFocus()
                descriptionEditText.clearFocus()
                hideKeyboard()
                val direction = UpdaterFragmentDirections.actionUpdaterFragmentToNoteDetailFragment(it)
                findNavController().navigate(direction)
            } else {
                val isTitle = titleEditText.text.isNullOrEmpty()
                val missingTextView = if (isTitle) titleEditText else descriptionEditText
                missingTextView.requestFocusFromTouch()
            }
        }
    }

    private fun hideKeyboard() {
        val manager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdaterBinding.inflate(inflater, container, false)
        return binding.root
    }
}