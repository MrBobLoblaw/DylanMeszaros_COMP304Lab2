package com.example.dylanmeszaros_comp304lab2_ex1.data

var tasks = mutableListOf(
    Task(1, "First Task", "Temp Desc", "October 12th 2025", 1f, 3f),
    Task(2, "Clean The Dishes", "Temp Desc 2", "October 17th 2024", 3f, 3f)
)

class TaskRepositoryImpl: TaskRepository {
    override fun addTask(newTask: Task): Task {
        tasks.add(newTask);
        return newTask;
    }
    override fun editTask(task: Task): Task {
        tasks[task.id] = task;
        return task;
    }
    override fun getTasks(): List<Task> {
        return tasks;
    }
}