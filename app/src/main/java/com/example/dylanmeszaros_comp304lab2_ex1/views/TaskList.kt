package com.example.dylanmeszaros_comp304lab2_ex1.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dylanmeszaros_comp304lab2_ex1.data.Task
import com.example.dylanmeszaros_comp304lab2_ex1.viewmodel.TaskViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TaskList(modifier: Modifier, onEditClick: () -> Unit) {
    val tasksViewModel: TaskViewModel = koinViewModel()
    LazyColumn(
        modifier = modifier
    ) {
        items(tasksViewModel.getTasks()) { task ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                //Text(text = "Name: ${task.name}")
                //Text(text = "Species: ${task.status}")
                Display_TaskCard(task, onEditClick);
            }
        }
    }
}

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
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Edit");
                }
            }
            Spacer(modifier = Modifier.height(4.dp));
            Text(text = task.description.take(50) + "...", style = MaterialTheme.typography.bodyMedium); // Truncate content
            Spacer(modifier = Modifier.height(4.dp));
            Text(text = "Completed: " + task.status.toString(), style = MaterialTheme.typography.bodyMedium); // Truncate content
        }
    }
}