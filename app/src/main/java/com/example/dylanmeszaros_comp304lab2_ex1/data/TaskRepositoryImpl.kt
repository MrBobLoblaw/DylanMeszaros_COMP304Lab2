package com.example.dylanmeszaros_comp304lab2_ex1.data

var tasks = mutableListOf(
    Task(1, "First Task", "Temp Desc", "October 12th 2025", false),
    Task(2, "Clean The Dishes", "Temp Desc 2", "October 17th 2024", true)
)

class TaskRepositoryImpl: TaskRepository {
    override fun addTask(newTask: Task): Task {
        tasks.add(newTask);
        return newTask;
    }
    override fun getTasks(): List<Task> {
        return tasks;
    }
}