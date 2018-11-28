package com.mos1210.todoapp.todo

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import com.mos1210.todoapp.data.Todo

object TodoListBinding {

    @BindingAdapter("items")
    @JvmStatic
    fun setItems(view: RecyclerView, items: List<Todo>?) {
        items?.run {
            (view.adapter as TodoListAdapter).setItemList(items)
        }
    }

}