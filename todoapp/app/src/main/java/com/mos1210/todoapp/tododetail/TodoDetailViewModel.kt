package com.mos1210.todoapp.tododetail

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.support.annotation.StringRes
import com.mos1210.todoapp.R
import com.mos1210.todoapp.data.*
import com.mos1210.todoapp.util.SingleLiveEvent

class TodoDetailViewModel(app: Application, private val repository: TodoRepository) : AndroidViewModel(app) {

    val description = MutableLiveData<String>()
    val todo = MutableLiveData<Todo>()

    internal val addEvent = SingleLiveEvent<Void>()
    internal val updateEvent = SingleLiveEvent<Void>()
    internal val deleteEvent = SingleLiveEvent<Void>()
    internal val toastMessageEvent = SingleLiveEvent<Int>()

    fun loadTodo(key: String) {
        repository.getTodo(key, object : OnGetTodoCallback {
            override fun onGetTodoFinished(data: Todo?, error: String?) {
                if (error == null) {
                    data?.let {
                        todo.postValue(it)
                        description.postValue(it.description)
                    }
                }
            }
        })
    }

    private fun showMessage(@StringRes stringRes: Int) {
        toastMessageEvent.value = stringRes
    }

    fun onClickAdd() {
        val get = description.value
        if (!get.isNullOrBlank()) {
            repository.addTodo(get, object : OnAddTodoCallback {
                override fun onAddTodoFinished(key: String?, error: String?) {
                    if (error == null) {
                        addEvent.call()
                    } else {
                        showMessage((R.string.add_failed_message))
                    }
                }
            })
        } else {
            showMessage(R.string.value_is_empty)
        }
    }

    fun onClickUpdate() {
        val get = description.value
        if (!get.isNullOrBlank()) {
            val src = todo.value
            if (src?.key != null) {
                val todo = Todo(src.key, src.complete, get)
                repository.updateTodo(src.key!!, todo, object : OnUpdateTodoCallback {
                    override fun onUpdateTodo(data: Todo?, error: String?) {
                        if (error == null) {
                            updateEvent.call()
                        } else {
                            showMessage(R.string.update_failed_message)
                        }
                    }
                })
            }
        } else {
            showMessage(R.string.value_is_empty)
        }
    }

    fun onClickDelete() {
        val get = todo.value
        if (get != null) {
            repository.deleteTodo(get.key!!, object : OnDeleteTodoCallback {
                override fun onDeleteTodo(error: String?) {
                    if (error == null) {
                        deleteEvent.call()
                    } else {
                        showMessage(R.string.delete_failed_message)
                    }
                }
            })
        }
    }


}
