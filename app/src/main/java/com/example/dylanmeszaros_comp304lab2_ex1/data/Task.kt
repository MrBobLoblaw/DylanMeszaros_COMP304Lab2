package com.example.dylanmeszaros_comp304lab2_ex1.data

data class Task (
    val id: Int,
    val name: String,
    val description: String,
    val dueDate: String,
    val progress: Float,
    val progressThreshold: Float
)