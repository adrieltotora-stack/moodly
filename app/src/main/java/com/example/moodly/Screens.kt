package com.example.moodly

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Estos son críticos para que "by remember" y "collectAsState" funcionen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
@Composable
fun HomeScreen(vm: MoodViewModel, onReg: () -> Unit) {
    val list by vm.entries.collectAsState()
    Column(Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Moodly", style = MaterialTheme.typography.displayMedium)
        Card(Modifier.fillMaxWidth().padding(vertical = 20.dp)) {
            Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Ánimo más frecuente de la semana")
                Text(vm.getMostFrequent(list), fontSize = 80.sp)
            }
        }
        Button(onClick = onReg) { Text("Registrar hoy") }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onSave: (Int, String, List<String>) -> Unit) {
    var mood by remember { mutableStateOf(3) }
    var note by remember { mutableStateOf("") }
    val tags = listOf("Estrés", "Sueño", "Social", "Académico")
    val selectedTags = remember { mutableStateListOf<String>() }

    Column(Modifier.padding(16.dp)) {
        Text("¿Cómo te sientes?", style = MaterialTheme.typography.titleLarge)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            (1..5).forEach { i ->
                IconButton(onClick = { mood = i }) {
                    Text(if(i==1)"😢" else if(i==2)"😕" else if(i==3)"😐" else if(i==4)"🙂" else "😄",
                        fontSize = if(mood == i) 40.sp else 24.sp)
                }
            }
        }
        OutlinedTextField(note, {note = it}, label = {Text("Nota (opcional)")}, modifier = Modifier.fillMaxWidth())
        Text("Etiquetas", Modifier.padding(top = 10.dp))
        Row {
            tags.forEach { tag ->
                FilterChip(selected = selectedTags.contains(tag), label = {Text(tag)},
                    onClick = { if(selectedTags.contains(tag)) selectedTags.remove(tag) else selectedTags.add(tag) })
            }
        }
        Button(onClick = { onSave(mood, note, selectedTags.toList()) }, Modifier.fillMaxWidth()) { Text("Guardar") }
    }
}

@Composable
fun HistoryScreen(vm: MoodViewModel) {
    val list by vm.entries.collectAsState()
    LazyColumn {
        items(list) { item ->
            ListItem(headlineContent = { Text(item.note) },
                supportingContent = { Text(item.tags) },
                leadingContent = { Text(vm.mapToEmoji(item.moodLevel), fontSize = 30.sp) })
            HorizontalDivider()
        }
    }
}

@Composable
fun ResourcesScreen() {
    val tips = listOf("Respira hondo", "Toma agua", "Busca ayuda si la necesitas: 0800-1234", "Todo estará bien.")
    Column(Modifier.padding(16.dp)) {
        Text("Recursos de Apoyo", style = MaterialTheme.typography.headlineSmall)
        tips.forEach { Text("• $it", Modifier.padding(8.dp)) }
    }
}