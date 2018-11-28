package com.mos1210.todoapp.tododetail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mos1210.todoapp.R
import com.mos1210.todoapp.util.ViewModelFactory

class TodoDetailActivity : AppCompatActivity() {

    companion object {
        const val TODO_KEY = "TODO_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo_detail_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    TodoDetailFragment.newInstance(intent.getStringExtra(TODO_KEY))
                )
                .commitNow()
        }

        val viewModel = ViewModelProviders.of(this, ViewModelFactory(application)).get(TodoDetailViewModel::class.java)
        viewModel.toastMessageEvent.observe(this, Observer {
            it?.run {
                Toast.makeText(this@TodoDetailActivity, it, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.let {
            it.addEvent.observe(this, Observer {
                Toast.makeText(this, getString(R.string.event_success, "Add"), Toast.LENGTH_SHORT)
                    .show()
                finish()
            })

            it.updateEvent.observe(this, Observer {
                Toast.makeText(this, getString(R.string.event_success, "Update"), Toast.LENGTH_SHORT)
                    .show()
                finish()
            })

            it.deleteEvent.observe(this, Observer {
                Toast.makeText(this, getString(R.string.event_success, "Delete"), Toast.LENGTH_SHORT)
                    .show()
                finish()
            })
        }
    }
}
