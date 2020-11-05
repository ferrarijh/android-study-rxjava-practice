package com.jonathan.practice.rxjavapractice

class MyDataSource{
    companion object
    {
        fun createTasks(n: Int): List<MyTask>{
            val tasks = mutableListOf<MyTask>()
            for(i in 1..n)
                tasks.add(MyTask("task$i", i))
            return tasks
        }
    }
}