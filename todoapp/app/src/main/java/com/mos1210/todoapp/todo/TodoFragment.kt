package com.mos1210.todoapp.todo

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mos1210.todoapp.R
import com.mos1210.todoapp.databinding.TodoFragmentBinding
import com.mos1210.todoapp.util.ViewModelFactory

class TodoFragment : Fragment() {

    companion object {
        fun newInstance() = TodoFragment()
    }

    private lateinit var binding: TodoFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.todo_fragment, container, false)
        binding = TodoFragmentBinding.bind(root).apply {
            setLifecycleOwner(this@TodoFragment)
            activity?.let {
                val vm = ViewModelProviders.of(it, ViewModelFactory(it.application)).get(TodoViewModel::class.java)
                viewModel = vm
                recyclerView.adapter = TodoListAdapter(vm)
            }

            fab.setOnClickListener {
                viewModel?.addTodoEvent?.call()
            }

        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        binding.viewModel?.loadTodoList()
    }

}
