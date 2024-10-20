package com.example.dylanmeszaros_comp304lab2_ex1.viewmodel

import androidx.lifecycle.ViewModel
import com.example.dylanmeszaros_comp304lab2_ex1.data.Task
import com.example.dylanmeszaros_comp304lab2_ex1.data.TaskRepository
import com.example.dylanmeszaros_comp304lab2_ex1.data.tasks

class TaskViewModel(
    private val tasksRepository: TaskRepository
): ViewModel() {

    fun getTasks() = tasksRepository.getTasks()
    fun addTask(newTask: Task) = tasksRepository.addTask(newTask)
    fun editTask(task: Task) = tasksRepository.editTask(task)
}