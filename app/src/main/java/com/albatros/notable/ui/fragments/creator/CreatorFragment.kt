package com.albatros.notable.ui.fragments.creator

import android.app.Activity
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.albatros.notable.R
import com.albatros.notable.databinding.FragmentCreatorBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatorFragment : Fragment() {

    private lateinit var binding: FragmentCreatorBinding
    private val viewModel: CreatorViewModel by viewModel()
    private val args by navArgs<CreatorFragmentArgs>()

    private val onValidStateChangedObserver = Observer<Boolean> {
        with(binding) {
            if (it) {
                titleEditText.clearFocus()
                descriptionEditText.clearFocus()
                hideKeyboard()
                val direction = CreatorFragmentDirections.actionCreatorFragmentToNoteListFragment()
                findNavController().navigate(direction)
            } else {
                val isTitle = titleEditText.text.isNullOrEmpty()
                val missingTextView = if (isTitle) titleEditText else descriptionEditText
                missingTextView.requestFocusFromTouch()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            titleEditText.setText(args.title)
            descriptionEditText.setText(args.data)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_creator, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_create -> {
            sendNoteData()
            true
        }
        R.id.action_to_camera -> {
            val direction = CreatorFragmentDirections.actionCreatorFragmentToCameraFragment()
            findNavController().navigate(direction)
            true
        }
        android.R.id.home -> {
            val direction = CreatorFragmentDirections.actionCreatorFragmentToNoteListFragment()
            findNavController().navigate(direction)
            true
        } else -> false
    }

    private fun sendNoteData() {
        with(binding) {
            val title = titleEditText.text.toString()
            val description = descriptionEditText.text.toString()
            viewModel.processNote(title, description)
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
        setHasOptionsMenu(true)
        binding = FragmentCreatorBinding.inflate(inflater, container, false)
        postponeEnterTransition()
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
        viewModel.inputValid.observe(viewLifecycleOwner, onValidStateChangedObserver)
        startPostponedEnterTransition()
        return binding.root
    }
}