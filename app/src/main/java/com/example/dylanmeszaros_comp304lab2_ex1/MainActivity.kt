package com.example.dylanmeszaros_comp304lab2_ex1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.dylanmeszaros_comp304lab2_ex1.ui.theme.CoreTheme
import androidx.lifecycle.ViewModel
import com.example.dylanmeszaros_comp304lab2_ex1.data.Task
import com.example.dylanmeszaros_comp304lab2_ex1.data.TaskRepository
import com.example.dylanmeszaros_comp304lab2_ex1.data.TaskRepositoryImpl
import com.example.dylanmeszaros_comp304lab2_ex1.di.appModules
import com.example.dylanmeszaros_comp304lab2_ex1.viewmodel.TaskViewModel
import com.example.dylanmeszaros_comp304lab2_ex1.views.TaskList
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.startKoin

var onStartup = false;

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (onStartup == false){
            onStartup = true;
            startKoin {
                modules(appModules)
            }
        }
        enableEdgeToEdge()
        setContent {
            CoreTheme {
                HomeActivity_Main(onEditTask = {
                    startActivity(Intent(this@MainActivity, EditActivity::class.java));
                    finish();
                }, onCreateTask = {
                    startActivity(Intent(this@MainActivity, CreateActivity::class.java));
                    finish();
                });
            }
        }
    }
}

class CreateActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreateActivity_Main(onCreatedTask = {
                startActivity(Intent(this@CreateActivity, MainActivity::class.java));
                finish();
            });
        }
    }
}

class EditActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EditActivity_Main(onEditedTask = {
                startActivity(Intent(this@EditActivity, MainActivity::class.java));
                finish();
            });
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeActivity_Main(onEditTask: () -> Unit, onCreateTask: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Tasks")
                },
                colors =  TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateTask) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Create Task");
            }
        },
        content =  { paddingValues ->
            TaskList(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                onEditTask
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateActivity_Main(onCreatedTask: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Create a new Task")
                },
                colors =  TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        content =  { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                var name by remember { mutableStateOf("") };
                var description by remember { mutableStateOf("") };
                var dueDate by remember { mutableStateOf("") };
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                );
                Spacer(modifier = Modifier.height(8.dp));
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 10
                );
                Spacer(modifier = Modifier.height(16.dp));
                TextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text("Due Date") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1
                );
                Spacer(modifier = Modifier.height(16.dp));
                Button(onClick = {
                    onCreatedTask();
                    //Add Task to Repositiory

                }) {
                    Text("Add Task");
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditActivity_Main(onEditedTask: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Update The Task")
                },
                colors =  TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        content =  { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                var name by remember { mutableStateOf("") };
                var description by remember { mutableStateOf("") };
                var dueDate by remember { mutableStateOf("") };
                var status by remember { mutableStateOf("") };
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                );
                Spacer(modifier = Modifier.height(8.dp));
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 10
                );
                Spacer(modifier = Modifier.height(16.dp));
                TextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text("Due Date") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1
                );
                Spacer(modifier = Modifier.height(16.dp));
                TextField(
                    value = status,
                    onValueChange = { dueDate = it },
                    label = { Text("Status") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1
                );
                Spacer(modifier = Modifier.height(16.dp));
                Button(onClick = {
                    onEditedTask();
                    //Edit Task in Repository

                }) {
                    Text("Update Task");
                }
            }
        }
    )
}































