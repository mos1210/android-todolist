package com.mos1210.todoapp.util

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.mos1210.todoapp.data.TodoRepository
import com.mos1210.todoapp.tododetail.TodoDetailViewModel
import com.mos1210.todoapp.todo.TodoViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val application: Application) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            TodoViewModel::class.java -> {
                TodoViewModel(application, TodoRepository.instance)
            }
            TodoDetailViewModel::class.java -> {
                TodoDetailViewModel(application, TodoRepository.instance)
            }
            else -> throw IllegalArgumentException()
        } as T
    }

}