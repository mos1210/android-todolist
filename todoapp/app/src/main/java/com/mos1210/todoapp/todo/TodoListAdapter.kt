package com.mos1210.todoapp.todo

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mos1210.todoapp.R
import com.mos1210.todoapp.data.Todo
import com.mos1210.todoapp.databinding.TodoListItemBinding

class TodoListAdapter(private val viewModel: TodoViewModel) :
    RecyclerView.Adapter<TodoListAdapter.MyViewHolder>() {

    private var mInfoList: List<Todo>? = null

    class MyViewHolder(val binding: TodoListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: TodoListItemBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.todo_list_item, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        mInfoList?.run {
            holder.binding.todoListItem = this[position]
            holder.binding.itemLayout.setOnClickListener {
                viewModel.editTodoEvent.value = this[position].key
            }
        }
    }

    override fun getItemCount(): Int {
        return mInfoList?.size ?: 0
    }

    fun setItemList(list: List<Todo>) {

        if (this.mInfoList == null) {
            this.mInfoList = list
            notifyDataSetChanged()

        } else {
            val result: DiffUtil.DiffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return mInfoList?.get(oldItemPosition) == list[newItemPosition]
                }

                override fun getOldListSize(): Int {
                    return mInfoList?.size ?: 0
                }

                override fun getNewListSize(): Int {
                    return list.size
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val a = mInfoList?.get(oldItemPosition)
                    val b = list[newItemPosition]

                    return a == b
                }

            })
            this.mInfoList = list
            result.dispatchUpdatesTo(this)
        }
    }
}