package com.example.helloworld

import android.content.ClipData
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Surface
import com.example.helloworld.ui.theme.ComposeTutorialTheme
import androidx.compose.foundation.border
import androidx.compose.material3.MaterialTheme
import android.content.res.Configuration
import android.graphics.drawable.Icon
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants
import java.math.BigInteger
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * Actividad principal de la aplicación.
 * Controla el ciclo de vida y la UI principal.
 */
class MainActivity : ComponentActivity() {
    /**
     * Método que se llama cuando la actividad es creada.
     * Aquí se configura el contenido de la UI con Jetpack Compose.
     *
     * @param savedInstanceState Estado guardado de la actividad.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            //Muestra el ejemplo de Scaffold con navegación
            ScaffoldExample()
        }
    }
}

/**
 * Vista previa del ScaffoldExample para modo claro.
 */
@Preview(name = "Light Mode")
@Composable
fun ScaffoldExamplePreview() {
    ScaffoldExample()
}

/**
 * Composable que define un Scaffold con barra superior, inferior, y FAB con navegación entre pantallas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldExample() {
    val context = LocalContext.current
    val launcher = ElegirYCompartirImagen(context)
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Top app bar")
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                BottomNavBar(navController)
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                launcher.launch("image/*")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // definición de rutas de pantallas
            NavHost(navController = navController, startDestination = "home") {
                composable("home") { HomeScreen(navController) }
                composable("info") { InfoScreen(navController) }
                composable("gallery") { GalleryScreen(navController) }
                composable("settings") { SettingsScreen(navController) }
            }
        }
    }
}

/**
 * Composable que lanza el selector de imágenes y permite compartirlas.
 *
 * @param context Contexto para iniciar Intents.
 * @return Un launcher para seleccionar contenido de tipo imagen.
 */
@Composable
fun ElegirYCompartirImagen(context: Context): ManagedActivityResultLauncher<String, Uri?> {
    val seleccionImagenLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_STREAM, it)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(
                Intent.createChooser(intent, "Compartir imagen con...")
            )
        }
    }
    return seleccionImagenLauncher
}

/**
 * Barra de navegación inferior con cuatro opciones: Home, Info, Gallery, Settings.
 *
 * @param navController Controlador de navegación para cambiar pantallas.
 */
@Composable
fun BottomNavBar(navController: NavHostController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            selected = navController.currentBackStackEntry?.destination?.route == "home",
            onClick = {
                navController.navigate("home") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Info, contentDescription = "Info") },
            selected = navController.currentBackStackEntry?.destination?.route == "info",
            onClick = {
                navController.navigate("info") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Star, contentDescription = "Gallery") },
            selected = navController.currentBackStackEntry?.destination?.route == "gallery",
            onClick = {
                navController.navigate("gallery") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            selected = navController.currentBackStackEntry?.destination?.route == "settings",
            onClick = {
                navController.navigate("settings") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}

/**
 * Pantalla principal de la app con tarjeta de mensaje y botones.
 *
 * @param navController Controlador de navegación.
 */
@Composable
fun HomeScreen(navController: NavController) {
    var presses by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        PreviewMessageCard(presses)
        setButtons(context)
    }
}

/**
 * Pantalla de información.
 *
 * @param navController Controlador de navegación.
 */
@Composable
fun InfoScreen(navController: NavController) {
    // Contenido de la pantalla Info
}

/**
 * Pantalla de galería.
 *
 * @param navController Controlador de navegación.
 */
@Composable
fun GalleryScreen(navController: NavController) {
    // Contenido de la pantalla Gallery
}

/**
 * Pantalla de configuración.
 *
 * @param navController Controlador de navegación.
 */
@Composable
fun SettingsScreen(navController: NavController) {
    // Contenido de la pantalla Settings
}

/**
 * Vista previa de MessageCard con tema aplicado.
 *
 * @param toquesBoton Número de toques para mostrar en el mensaje.
 */
@Composable
fun PreviewMessageCard(toquesBoton: Int) {
    ComposeTutorialTheme {
        Surface {
            MessageCard(
                msg = Message("Lexi", "Nº toques de botón: ", toquesBoton)
            )
        }
    }
}

/**
 * Data class que representa un mensaje con autor, cuerpo y número de toques.
 *
 * @property author Autor del mensaje.
 * @property body Contenido del mensaje.
 * @property toquesBoton Número de toques relacionados al mensaje.
 */
data class Message(val author: String, val body: String, val toquesBoton: Int)

/**
 * Composable que muestra un mensaje con imagen y texto formateado.
 *
 * @param msg Objeto Message que contiene la información a mostrar.
 */
@Composable
fun MessageCard(msg: Message) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(R.drawable.descarga),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = msg.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = msg.body + msg.toquesBoton,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/**
 * Composable que crea una fila con botones para enviar email, compartir, abrir navegador y navegar.
 *
 * @param context Contexto para iniciar Intents.
 */
@Composable
fun setButtons(context: Context) {

    Row(
        Modifier
            .fillMaxWidth()
            .border(2.dp, Color.Blue, RoundedCornerShape(8.dp)),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),

        ) {

        Button(onClick = {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:alumno@ejemplo.com")
                putExtra(Intent.EXTRA_SUBJECT, "Saludos desde Compose")
                putExtra(Intent.EXTRA_TEXT, "Hola, este es un mensaje de prueba")
            }
            context.startActivity(intent)
        }) {
            Icon(Icons.Default.MailOutline, contentDescription = "Email")
        }

        Button(onClick = {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Mira esta app de Compose genial!")
            }
            context.startActivity(Intent.createChooser(intent, "Compartir con"))
        }) {
            Icon(Icons.Default.Share, contentDescription = "Compartir")
        }

        Button(onClick = {
            val url = "https://www.google.com"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }) {
            Icon(Icons.Default.Info, contentDescription = "Webbrowser")
        }

        Button(onClick = {
            // Aquí se puede abrir otra actividad
            // val intent = Intent(context, OtraActividad::class.java)
            // context.startActivity(intent)
        }) {
            Icon(Icons.Default.Home, contentDescription = "Home")
        }
    }
}