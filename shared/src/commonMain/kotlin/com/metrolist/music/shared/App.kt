package com.metrolist.music.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFC084FC), // Vibrant Purple
    secondary = Color(0xFFE879F9), // Magenta
    background = Color(0xFF0F172A), // Deep dark blue/gray
    surface = Color(0xFF1E293B),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)

@Composable
fun App() {
    MaterialTheme(
        colorScheme = DarkColorScheme
    ) {
        Row(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
        ) {
            // Sidebar
            Column(
                modifier = Modifier
                    .width(250.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                    .padding(24.dp)
            ) {
                Text(
                    text = "Metrolist",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
                
                Text("Home", color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(vertical = 12.dp))
                Text("Explore", color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(vertical = 12.dp))
                Text("Library", color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(vertical = 12.dp))
            }
            
            // Main Content Area
            Column(
                modifier = Modifier.fillMaxSize().weight(1f).padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Now Playing",
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(32.dp))
                
                // Placeholder Album Art
                Box(
                    modifier = Modifier
                        .size(300.dp)
                        .background(MaterialTheme.colorScheme.surface, shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Album Art", color = MaterialTheme.colorScheme.onSurface)
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = "Song Title",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Artist Name",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
