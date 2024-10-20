package com.example.dylanmeszaros_comp304lab2_ex1

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly

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
                HomeActivity_Main(onEditTask = { task ->
                    startActivity(Intent(this@MainActivity, EditActivity::class.java).apply {
                        putExtra("taskID", task.id);
                    });
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
            val taskViewModel: TaskViewModel = koinViewModel();
            val taskID = intent.getIntExtra("taskID", 1);

            //Log.d("EditActivity", "Start")

            EditActivity_Main(taskViewModel.getTasks()[taskID - 1], onEditedTask = {
                startActivity(Intent(this@EditActivity, MainActivity::class.java));
                finish();
            });
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeActivity_Main(onEditTask: (Task) -> Unit, onCreateTask: () -> Unit) {
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
    val taskViewModel: TaskViewModel = koinViewModel();

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
                var name by remember { mutableStateOf("New Task") };

                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                );
                Spacer(modifier = Modifier.height(8.dp));

                var description by remember { mutableStateOf("...") };
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 10
                );
                Spacer(modifier = Modifier.height(16.dp));

                var dueDate by remember { mutableStateOf("October 20th, 2024") };
                TextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text("Due Date") },
                    modifier = Modifier
                        .fillMaxWidth(),
                    maxLines = 1
                );
                Spacer(modifier = Modifier.height(16.dp));

                var progress by remember { mutableStateOf(TextFieldValue("0")) };
                var progressThreshold by remember { mutableStateOf(TextFieldValue("1")) };
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Row() {
                        BoxWithConstraints {
                            var maxWidth = maxWidth;

                            TextField(
                                value = progress,
                                onValueChange = {
                                    if (it.text.isDigitsOnly()) {
                                        progress = it;

                                        if (it.text.isEmpty()){
                                            progress = TextFieldValue("0");
                                        } else {
                                            if (it.text.toFloat() < 0) {
                                                progress = TextFieldValue("0");
                                            }
                                            if (it.text.toFloat() > progressThreshold.text.toFloat()) {
                                                progress = progressThreshold;
                                            }
                                        }
                                    }
                                },
                                label = { Text("Progress") },
                                modifier = Modifier
                                    .width(maxWidth / 2f),
                                maxLines = 1
                            );
                        }

                        BoxWithConstraints {
                            var maxWidth = maxWidth;

                            TextField(
                                value = progressThreshold,
                                onValueChange = {
                                    if (it.text.isDigitsOnly()) {
                                        progressThreshold = it;

                                        if (it.text.isEmpty()){
                                            progressThreshold = progress;
                                        } else {
                                            if (it.text.toFloat() < progress.text.toFloat()) {
                                                progressThreshold = progress;
                                            }
                                        }
                                    }
                                },
                                label = { Text("Threshold") },
                                modifier = Modifier
                                    .width(maxWidth),
                                maxLines = 1
                            );
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp));
                Button(onClick = {
                    onCreatedTask();
                    //Add Task to Repositiory

                    taskViewModel.addTask(Task(taskViewModel.getTasks().count() + 1, name, description, dueDate, progress.text.toFloat(), progressThreshold.text.toFloat()))
                }) {
                    Text("Create Task");
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditActivity_Main(task: Task, onEditedTask: () -> Unit) {
    val taskViewModel: TaskViewModel = koinViewModel();

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
                var name by remember { mutableStateOf(task.name) };

                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                );
                Spacer(modifier = Modifier.height(8.dp));

                var description by remember { mutableStateOf(task.description) };
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 10
                );
                Spacer(modifier = Modifier.height(16.dp));

                var dueDate by remember { mutableStateOf(task.dueDate) };
                TextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text("Due Date") },
                    modifier = Modifier
                        .fillMaxWidth(),
                    maxLines = 1
                );
                Spacer(modifier = Modifier.height(16.dp));

                var progress by remember { mutableStateOf(TextFieldValue(task.progress.toInt().toString())) };
                var progressThreshold by remember { mutableStateOf(TextFieldValue(task.progressThreshold.toInt().toString())) };
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Row() {
                        BoxWithConstraints {
                            var maxWidth = maxWidth;

                            TextField(
                                value = progress,
                                onValueChange = {
                                    if (it.text.isDigitsOnly()) {
                                        progress = it;

                                        if (it.text.isEmpty()){
                                            progress = TextFieldValue("0");
                                        } else {
                                            if (it.text.toFloat() < 0) {
                                                progress = TextFieldValue("0");
                                            }
                                            if (it.text.toFloat() > progressThreshold.text.toFloat()) {
                                                progress = progressThreshold;
                                            }
                                        }
                                    }
                                },
                                label = { Text("Progress") },
                                modifier = Modifier
                                    .width(maxWidth / 2f),
                                maxLines = 1
                            );
                        }

                        BoxWithConstraints {
                            var maxWidth = maxWidth;

                            TextField(
                                value = progressThreshold,
                                onValueChange = {
                                    if (it.text.isDigitsOnly()) {
                                        progressThreshold = it;

                                        if (it.text.isEmpty()){
                                            progressThreshold = progress;
                                        } else {
                                            if (it.text.toFloat() < progress.text.toFloat()) {
                                                progressThreshold = progress;
                                            }
                                        }
                                    }
                                },
                                label = { Text("Threshold") },
                                modifier = Modifier
                                    .width(maxWidth),
                                maxLines = 1
                            );
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp));
                Button(onClick = {
                    onEditedTask();
                    taskViewModel.editTask(Task(task.id - 1, name, description, dueDate, progress.text.toFloat(), progressThreshold.text.toFloat()));
                }) {
                    Text("Update Task");
                }
            }
        }
    )
}































