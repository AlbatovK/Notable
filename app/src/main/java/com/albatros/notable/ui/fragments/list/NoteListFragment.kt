package com.albatros.notable.ui.fragments.list

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.albatros.notable.R
import com.albatros.notable.databinding.FragmentListBinding
import com.albatros.notable.domain.safeNavigate
import com.albatros.notable.model.data.Note
import com.albatros.notable.ui.adapters.note.NoteAdapter
import com.albatros.notable.ui.adapters.note.NoteAdapterListener
import org.koin.androidx.viewmodel.ext.android.viewModel


class NoteListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private val viewModel: ListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val listener = object : NoteAdapterListener {
        override fun onNoteSelected(note: Note, view: View) {
            val extras = FragmentNavigatorExtras(view to note.id.toString())
            val direction = NoteListFragmentDirections
                .actionNoteListFragmentToNoteDetailFragment(note)
            /* On uncertain occasions destination isn't accessible */
            findNavController().safeNavigate(direction, extras)
        }
    }

    private val onNotesLoadedObserver = Observer<List<Note>?> {
        with(binding) {
            list.adapter = NoteAdapter(it.toMutableList(), listener)
            list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)

        val searchView = menu.findItem(R.id.action_search).actionView as SearchView

        val listener = object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                viewModel.loadNotesList()
                return true
            }

            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }
        }

        menu.findItem(R.id.action_search).setOnActionExpandListener(listener)

        searchView.setOnCloseListener {
            viewModel.loadNotesList()
            true
        }

        val queryListener = object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.fetchByTopics(query) }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        }

        searchView.setOnQueryTextListener(queryListener)
        val manager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val info = manager.getSearchableInfo(activity?.componentName)
        searchView.setSearchableInfo(info)

        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_exit -> {
            activity?.finishAffinity()
            true
        }
        R.id.action_add -> {
            val direction = NoteListFragmentDirections.actionNoteListFragmentToCreatorFragment()
            findNavController().navigate(direction)
            true
        }
        else -> false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        postponeEnterTransition()
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
        viewModel.notes.observe(viewLifecycleOwner, onNotesLoadedObserver)
        (binding.list as? ViewGroup)?.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }
}