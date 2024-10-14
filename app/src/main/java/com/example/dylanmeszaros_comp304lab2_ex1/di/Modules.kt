package com.example.dylanmeszaros_comp304lab2_ex1.di

import com.example.dylanmeszaros_comp304lab2_ex1.data.TaskRepository
import com.example.dylanmeszaros_comp304lab2_ex1.data.TaskRepositoryImpl
import com.example.dylanmeszaros_comp304lab2_ex1.viewmodel.TaskViewModel
import org.koin.dsl.module

val appModules = module {
    single<TaskRepository> { TaskRepositoryImpl() }
    single { TaskViewModel(get()) }
}