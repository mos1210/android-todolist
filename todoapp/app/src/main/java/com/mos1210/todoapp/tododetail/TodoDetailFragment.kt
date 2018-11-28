package com.mos1210.todoapp.tododetail

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mos1210.todoapp.R
import com.mos1210.todoapp.databinding.TodoDetailFragmentBinding
import com.mos1210.todoapp.util.ViewModelFactory

class TodoDetailFragment : Fragment() {

    companion object {
        private const val TODO_KEY = "TODO_KEY"

        fun newInstance(key: String?): TodoDetailFragment {
            val fragment = TodoDetailFragment()
            val bundle = Bundle().apply {
                this.putString(TODO_KEY, key)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var viewDataBinding: TodoDetailFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val root = inflater.inflate(R.layout.todo_detail_fragment, container, false)
        viewDataBinding = TodoDetailFragmentBinding.bind(root).apply {
            setLifecycleOwner(this@TodoDetailFragment)
            activity?.let {
                viewModel =
                        ViewModelProviders.of(it, ViewModelFactory(it.application))
                            .get(TodoDetailViewModel::class.java)
            }
        }
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.getString(TODO_KEY)?.run {
            viewDataBinding.viewModel?.loadTodo(this)
        }
    }

}