package com.mos1210.todoapp.todo

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableArrayList
import com.mos1210.todoapp.data.*
import com.mos1210.todoapp.util.SingleLiveEvent

class TodoViewModel(app: Application, private val repository: TodoRepository) : AndroidViewModel(app) {

    val todoList = MutableLiveData<List<Todo>>()

    val addTodoEvent = SingleLiveEvent<String>()
    val editTodoEvent = SingleLiveEvent<String>()

    fun loadTodoList() {

        repository.getTodoList(object : OnGetTodoListCallback {
            override fun onGetTodoListFinished(data: List<Todo>?, error: String?) {
                if (error == null) {
                    data?.run {
                        todoList.postValue(this)
                    }
                }
            }
        })
    }

    fun completeTodo(key: String, todo: Todo, complete: Boolean) {

        todo.complete = complete
        repository.updateTodo(key, todo, object : OnUpdateTodoCallback {
            override fun onUpdateTodo(data: Todo?, error: String?) {
                if (error == null) {
                    loadTodoList()
                }
            }
        })
    }

}