package com.mos1210.todoapp.todo

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mos1210.todoapp.R
import com.mos1210.todoapp.tododetail.TodoDetailActivity
import com.mos1210.todoapp.util.ViewModelFactory

class TodoActivity : AppCompatActivity() {

    private lateinit var viewModel: TodoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, TodoFragment.newInstance())
                .commitNow()
        }

        viewModel =
                ViewModelProviders.of(this@TodoActivity, ViewModelFactory(application)).get(TodoViewModel::class.java)

        subscribeEvent()
    }

    private fun subscribeEvent() {

        viewModel.run {
            editTodoEvent.observe(this@TodoActivity, Observer { key ->
                onStartTodoDetailActivity(key)
            })

            addTodoEvent.observe(this@TodoActivity, Observer {
                onStartTodoDetailActivity()
            })
        }
    }

    private fun onStartTodoDetailActivity(key: String? = null) {
        val intent = Intent(this, TodoDetailActivity::class.java)
        intent.putExtra(TodoDetailActivity.TODO_KEY, key)
        startActivity(intent)
    }
}


