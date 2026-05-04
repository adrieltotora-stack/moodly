package com.example.moodly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.moodly.ui.theme.MoodlyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicialización de la Base de Datos Local
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "mood_db"
        ).build()

        // Inicialización del ViewModel que maneja la lógica de negocio
        val viewModel = MoodViewModel(db.moodDao())

        setContent {
            MoodlyTheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        // Barra de navegación inferior con Material Design 3
                        NavigationBar {
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                label = { Text("Inicio") },
                                selected = false,
                                onClick = { navController.navigate("home") }
                            )
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.List, contentDescription = "Historial") },
                                label = { Text("Historial") },
                                selected = false,
                                onClick = { navController.navigate("history") }
                            )
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Info, contentDescription = "Recursos") },
                                label = { Text("Recursos") },
                                selected = false,
                                onClick = { navController.navigate("resources") }
                            )
                        }
                    }
                ) { padding ->
                    // Configuración del Host de Navegación
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(padding)
                    ) {
                        // Pantalla 1: Home
                        composable("home") {
                            HomeScreen(viewModel) { navController.navigate("register") }
                        }
                        // Pantalla 2: Registro
                        composable("register") {
                            RegisterScreen { level, note, tags ->
                                viewModel.save(level, note, tags)
                                navController.popBackStack()
                            }
                        }
                        // Pantalla 3: Historial
                        composable("history") {
                            HistoryScreen(viewModel)
                        }
                        // Pantalla 4: Recursos (Estática)
                        composable("resources") {
                            ResourcesScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MoodlyTheme {
        Greeting("Android")
    }
}