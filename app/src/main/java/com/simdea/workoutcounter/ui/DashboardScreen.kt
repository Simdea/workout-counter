package com.simdea.workoutcounter.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simdea.workoutcounter.R
import com.simdea.workoutcounter.domain.model.WorkoutStats
import com.simdea.workoutcounter.ui.theme.WorkoutCounterTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: WorkoutViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Workout Counter", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.logWorkout() },
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("Log Workout") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is WorkoutUiState.Loading -> {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
                is WorkoutUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                is WorkoutUiState.Success -> {
                    DashboardContent(stats = state.stats)
                }
            }
        }
    }
}

@Composable
private fun DashboardContent(stats: WorkoutStats) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatCard(
            title = "Total Workouts",
            value = stats.totalDone.toString(),
            icon = Icons.Rounded.CheckCircle,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )

        StatCard(
            title = "Workouts Required",
            value = stats.totalRequired.toString(),
            icon = Icons.Rounded.History,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )

        val compensationColor = if (stats.toCompensate > 0) {
            MaterialTheme.colorScheme.errorContainer
        } else {
            MaterialTheme.colorScheme.tertiaryContainer
        }
        val onCompensationColor = if (stats.toCompensate > 0) {
            MaterialTheme.colorScheme.onErrorContainer
        } else {
            MaterialTheme.colorScheme.onTertiaryContainer
        }

        StatCard(
            title = "To Compensate",
            value = stats.toCompensate.toString(),
            icon = if (stats.toCompensate > 0) Icons.Rounded.Warning else Icons.Rounded.Info,
            containerColor = compensationColor,
            contentColor = onCompensationColor
        )
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    containerColor: androidx.compose.ui.graphics.Color,
    contentColor: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = contentColor.copy(alpha = 0.8f)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardPreview() {
    WorkoutCounterTheme {
        DashboardContent(
            stats = WorkoutStats(
                totalDone = 12,
                totalRequired = 15,
                toCompensate = 3
            )
        )
    }
}
