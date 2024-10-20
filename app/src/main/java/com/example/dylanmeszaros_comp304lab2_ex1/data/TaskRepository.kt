package com.example.dylanmeszaros_comp304lab2_ex1.data

interface TaskRepository {
    fun addTask(newTask: Task): Task
    fun editTask(task: Task): Task
    fun getTasks(): List<Task>
}