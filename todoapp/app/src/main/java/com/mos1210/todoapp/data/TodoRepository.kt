package com.mos1210.todoapp.data

import com.google.gson.JsonNull
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TodoRepository(private val service: FirebaseService) {

    companion object {
        const val BASE_URL: String = "https://todolist-f7571.firebaseio.com/"
        val instance: TodoRepository by lazy {
            TodoRepository(FirebaseService.instance)
        }
    }

    fun getTodoList(onDataReadyCallback: OnGetTodoListCallback) {
        service.getTodoList().enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onDataReadyCallback.onGetTodoListFinished(error = t.message)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.isSuccessful) {
                    val body: JsonObject? = response.body()
                    val tempList = mutableListOf<Todo>().apply {
                        body?.keySet()?.forEach { key ->
                            val data: JsonObject = body.getAsJsonObject(key)
                            add(
                                Todo(
                                    key,
                                    data.get("complete").asBoolean,
                                    data.get("description").asString,
                                    data.get("timestamp").asLong
                                )
                            )
                        }
                    }
                    onDataReadyCallback.onGetTodoListFinished(tempList)
                } else {
                    onDataReadyCallback.onGetTodoListFinished(error = "getTodoList failed.")
                }
            }
        })
    }

    fun getTodo(key: String, onGetTodoCallback: OnGetTodoCallback) {
        service.getTodo(key).enqueue(object : Callback<Todo> {
            override fun onFailure(call: Call<Todo>, t: Throwable) {
                onGetTodoCallback.onGetTodoFinished(error = "getTodo failed.")
            }

            override fun onResponse(call: Call<Todo>, response: Response<Todo>) {

                if (response.isSuccessful) {
                    val todo: Todo? = response.body()
                    todo?.key = key
                    onGetTodoCallback.onGetTodoFinished(todo)
                } else {
                    onGetTodoCallback.onGetTodoFinished(error = "getTodo failed.")
                }
            }

        })
    }

    fun addTodo(description: String, onDataReadyCallback: OnAddTodoCallback) {
        val todo = Todo(description = description)
        service.createTodo(todo).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onDataReadyCallback.onAddTodoFinished(error = "addTodo failed.")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.isSuccessful) {
                    val body: JsonObject? = response.body()
                    val key = body?.get("name")?.asString
                    onDataReadyCallback.onAddTodoFinished(key)
                } else {
                    onDataReadyCallback.onAddTodoFinished(error = "addTodo failed.")
                }
            }
        })
    }

    fun updateTodo(key: String, todo: Todo, onDataReadyCallback: OnUpdateTodoCallback) {
        service.updateTodo(key, todo).enqueue(object : Callback<Todo> {
            override fun onFailure(call: Call<Todo>, t: Throwable) {
                onDataReadyCallback.onUpdateTodo(error = "updateTodo failed.")
            }

            override fun onResponse(call: Call<Todo>, response: Response<Todo>) {
                if (response.isSuccessful) {
                    onDataReadyCallback.onUpdateTodo(response.body())
                } else {
                    onDataReadyCallback.onUpdateTodo(error = "updateTodo failed.")
                }
            }

        })
    }

    fun deleteTodo(key: String, onDataReadyCallback: OnDeleteTodoCallback) {
        service.deleteTodo(key).enqueue(object : Callback<JsonNull> {
            override fun onFailure(call: Call<JsonNull>, t: Throwable) {
                onDataReadyCallback.onDeleteTodo("deleteTodo failed.")
            }

            override fun onResponse(call: Call<JsonNull>, response: Response<JsonNull>) {
                if (response.isSuccessful) {
                    onDataReadyCallback.onDeleteTodo()
                } else {
                    onDataReadyCallback.onDeleteTodo("deleteTodo failed.")
                }
            }

        })
    }

}

interface OnGetTodoListCallback {
    fun onGetTodoListFinished(data: List<Todo>? = null, error: String? = null)
}

interface OnGetTodoCallback {
    fun onGetTodoFinished(data: Todo? = null, error: String? = null)
}

interface OnAddTodoCallback {
    fun onAddTodoFinished(key: String? = null, error: String? = null)
}

interface OnUpdateTodoCallback {
    fun onUpdateTodo(data: Todo? = null, error: String? = null)
}

interface OnDeleteTodoCallback {
    fun onDeleteTodo(error: String? = null)
}