package com.albatros.notable.ui.fragments.detail

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.albatros.notable.R
import com.albatros.notable.databinding.DialogLayoutBinding
import com.albatros.notable.databinding.FragmentDetailBinding
import com.albatros.notable.model.data.SubTask
import com.albatros.notable.ui.activities.MainActivity
import com.albatros.notable.ui.adapters.task.TaskAdapter
import com.albatros.notable.ui.adapters.task.TaskAdapterListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class NoteDetailFragment : Fragment(), MainActivity.IOnBackPressed {

    private lateinit var binding: FragmentDetailBinding
    private val arguments by navArgs<NoteDetailFragmentArgs>()
    private val viewModel: DetailViewModel by viewModel { parametersOf(arguments.arg) }

    override fun onBackPressed(): Boolean {
        val direction = NoteDetailFragmentDirections.actionNoteDetailFragmentToNoteListFragment()
        findNavController().navigate(direction)
        return true
    }

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
        R.id.share -> {
            val intent = Intent(Intent.ACTION_SEND)
            val shareBody = arguments.arg.title + "\n\n" + arguments.arg.data
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(intent, ""))
            true
        }
        android.R.id.home -> {
            val direction = NoteDetailFragmentDirections.actionNoteDetailFragmentToNoteListFragment()
            findNavController().navigate(direction)
            true
        }
        R.id.action_change -> {
            val direction = NoteDetailFragmentDirections
                .actionNoteDetailFragmentToUpdaterFragment(arguments.arg)
            findNavController().navigate(direction)
            true
        } else -> false
    }

    private val listener = object: TaskAdapterListener {
        override fun onTaskStateSet(task: SubTask, view: ImageView) {
            val src = if (task.finished) R.drawable.ic_green_check else R.drawable.ic_red_cross
            view.setImageResource(src)
        }

        override fun onTaskFinished(task: SubTask, view: ImageView) {
            view.setImageResource(R.drawable.ic_green_check)
            viewModel.finishTask(task)
        }
    }

    private val onTasksLoadedObserver = Observer<List<SubTask>> {
        with(binding) {
            list.adapter = TaskAdapter(it, listener)
            list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            ItemTouchHelper(touchCallback).attachToRecyclerView(list)
            val decorator = DividerItemDecoration(binding.root.context, LinearLayoutManager.VERTICAL)
            list.addItemDecoration(decorator)
            lifecycleScope.launch {
                if (container.visibility == View.INVISIBLE && it.isNotEmpty()) {
                    delay(500)
                    container.startAnimation(AnimationUtils.loadAnimation(context, R.anim.alpha_animation))
                    container.visibility = View.VISIBLE
                }
                if (it.isEmpty() && container.visibility == View.VISIBLE) {
                    container.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out_animation))
                    container.visibility =  View.INVISIBLE
                }
                if (addCard.visibility == View.INVISIBLE) {
                    delay(1000)
                    addCard.startAnimation(AnimationUtils.loadAnimation(context, R.anim.alpha_animation))
                    addCard.visibility = View.VISIBLE
                }
            }
        }
    }

    private val onNoteFinishedObserver = Observer<Boolean> {
        if (it) { animateNoteFinished() }
    }

    private val onPercentCountLoadedObserver = Observer<Double> {
        with(binding) {
            percentCount.text = context?.getString(R.string.percent, it.toInt())
        }
    }

    private val onCountChangedObserver = Observer<Pair<Int, Int>> {
        with(binding) {
            doneHint.text = context?.getString(R.string.done_hint, it.first, it.second)
        }
    }

    private val touchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(rcv: RecyclerView, vh: RecyclerView.ViewHolder, trg: RecyclerView.ViewHolder): Boolean = true
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, dir: Int) {
            if (dir == ItemTouchHelper.LEFT || dir == ItemTouchHelper.RIGHT) {
                val item = (viewHolder as TaskAdapter.TaskViewHolder).task
                item?.let {
                    viewModel.deleteTask(it)
                }
            }
        }
    }

    private fun animateNoteFinished() {
        with(binding) {
            val note = arguments.arg
            val color = context?.resources?.getColor(R.color.neon_green, context?.theme)
            val old = cardView.cardBackgroundColor.defaultColor
            color?.let { note.color = it }
            viewModel.finishNote(note)
            ValueAnimator.ofObject(ArgbEvaluator(), old, color).apply {
                duration = 400
                addUpdateListener { anim -> color?.let { cardView.setCardBackgroundColor(anim.animatedValue as Int) } }
            }.start()
        }
    }

    private fun showOnTaskCreateDialog() {
        val dialogBinding = DialogLayoutBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(binding.root.context).apply {
            setView(dialogBinding.root)
            setNegativeButton("Отмена") { it, _ -> it.cancel() }
            setPositiveButton("Добавить") { it, _ ->
                val title = dialogBinding.titleEditText.text.toString()
                val task = SubTask(title.ifBlank { "Безымянная задача" }, false, arguments.arg.id)
                viewModel.insertSubTask(task)
                it.cancel()
            }
        }.create()
        dialog.window?.let {
            it.setBackgroundDrawable(ResourcesCompat.getDrawable(resources, R.drawable.dialog_shape, activity?.theme))
            it.attributes.verticalMargin = -0.08F
        }
        dialog.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        postponeEnterTransition()
        ViewCompat.setTransitionName(binding.cardView, arguments.arg.id.toString())
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
        startPostponedEnterTransition()

        viewModel.initViewModel(arguments.arg.id)
        viewModel.loadSubTasks()

        viewModel.percentCount.observe(viewLifecycleOwner, onPercentCountLoadedObserver)
        viewModel.tasks.observe(viewLifecycleOwner, onTasksLoadedObserver)
        viewModel.finished.observe(viewLifecycleOwner, onNoteFinishedObserver)
        viewModel.doneCount.observe(viewLifecycleOwner, onCountChangedObserver)

        with(binding) {
            container.visibility = View.INVISIBLE
            addCard.visibility = View.INVISIBLE
            description.text = arguments.arg.data
            title.text = arguments.arg.title
            cardView.setCardBackgroundColor(arguments.arg.color)

            doneImg.setOnClickListener { animateNoteFinished() }
            addCard.setOnClickListener { showOnTaskCreateDialog() }
        }
    }
}