package com.albatros.notable.ui.fragments.detail

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.albatros.notable.R
import com.albatros.notable.databinding.FragmentDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class NoteDetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: DetailViewModel by viewModel()
    private val arguments by navArgs<NoteDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.action_delete -> {
            viewModel.deleteNote(arguments.arg)
            val direction = NoteDetailFragmentDirections.actionNoteDetailFragmentToNoteListFragment()
            findNavController().navigate(direction)
            true
        }
        R.id.action_change -> {

            true
        } else -> false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        postponeEnterTransition()
        ViewCompat.setTransitionName(binding.cardView, arguments.arg.id.toString())
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
        startPostponedEnterTransition()

        with(binding) {
            description.text = arguments.arg.data
            title.text = arguments.arg.title
        }
    }
}