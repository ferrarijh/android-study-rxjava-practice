package com.jonathan.practice.rxjavapractice

class MyTask(val description: String, val priority: Int){
    private var _isComplete = false
    val isComplete
        get() = _isComplete

    fun setComplete(b: Boolean){
        _isComplete = b
    }

}