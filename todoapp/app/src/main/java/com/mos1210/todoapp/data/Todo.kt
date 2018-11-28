package com.mos1210.todoapp.data

import android.os.SystemClock

class Todo (
    @Transient var key: String? = null,
    var complete: Boolean = false,
    var description: String,
    var timestamp: Long = System.currentTimeMillis()
)
