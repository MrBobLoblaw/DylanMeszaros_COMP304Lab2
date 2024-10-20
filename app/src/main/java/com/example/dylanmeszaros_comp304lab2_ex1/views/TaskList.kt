package com.example.dylanmeszaros_comp304lab2_ex1.views

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Typeface
import androidx.compose.ui.unit.dp
import com.example.dylanmeszaros_comp304lab2_ex1.data.Task
import com.example.dylanmeszaros_comp304lab2_ex1.viewmodel.TaskViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TaskList(modifier: Modifier, onEditTask: (Task) -> Unit) {
    val taskViewModel: TaskViewModel = koinViewModel();

    LazyColumn(
        modifier = modifier
    ) {
        items(taskViewModel.getTasks()) { task ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                //Text(text = "Name: ${task.name}")
                //Text(text = "Species: ${task.status}")
                Display_TaskCard(task, onEditClick = { onEditTask(task) });
            }
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun Display_TaskCard(task: Task, onEditClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = task.name, style = MaterialTheme.typography.headlineLarge);
                IconButton(onClick = onEditClick) {
                    Log.d("onEditClick", task.id.toString())
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Edit");
                }
            }
            Spacer(modifier = Modifier.height(4.dp));
            Text(text = task.description.take(50) + "...", style = MaterialTheme.typography.bodyMedium);
            Spacer(modifier = Modifier.height(4.dp));

            BoxWithConstraints {
                var maxWidth = maxWidth;
                var maxHeight = maxHeight;

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),

                    colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                ){
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        var progressState = task.progress / task.progressThreshold;
                        if (progressState <= 0 || progressState.isNaN()) { progressState = 1f }; // Without this it will be greyed out
                        Card(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(maxWidth * progressState),
                            colors = CardDefaults.cardColors(containerColor = Color.Green)
                        ){
                            Text(text = "");
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(1.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Completed: " + (task.progress >= task.progressThreshold),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White
                            );
                        }
                    }
                }
            }
        }
    }
}