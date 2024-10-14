package com.example.dylanmeszaros_comp304lab2_ex1.viewmodel

import androidx.lifecycle.ViewModel
import com.example.dylanmeszaros_comp304lab2_ex1.data.TaskRepository

class TaskViewModel(
    private val tasksRepository: TaskRepository
): ViewModel() {

    fun getTasks() = tasksRepository.getTasks()
}