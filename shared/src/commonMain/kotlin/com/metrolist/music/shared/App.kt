package com.metrolist.music.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import coil3.compose.AsyncImage
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metrolist.innertube.YouTube
import com.metrolist.innertube.models.SongItem
import com.metrolist.innertube.models.YouTubeClient
import com.metrolist.innertube.models.YouTubeLocale
import com.metrolist.innertube.pages.SearchSummaryPage
import com.metrolist.music.shared.media.MediaPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.delay

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFC084FC),
    secondary = Color(0xFFE879F9),
    background = Color(0xFF0F172A),
    surface = Color(0xFF1E293B),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(mediaPlayer: MediaPlayer? = null) {
    // Ensure YouTube locale is explicitly set synchronously so the API request is valid on Desktop
    YouTube.locale = YouTubeLocale("US", "en")
    
    var selectedTab by remember { mutableStateOf("Home") }
    var currentSong by remember { mutableStateOf<SongItem?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    MaterialTheme(colorScheme = DarkColorScheme) {
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            Row(modifier = Modifier.fillMaxSize().padding(bottom = if (currentSong != null) 96.dp else 0.dp)) {
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

                    val tabs = listOf("Home", "Explore", "Library", "Settings")
                    tabs.forEach { tab ->
                        val isSelected = selectedTab == tab
                        val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent
                        val textColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(backgroundColor)
                                .clickable { selectedTab = tab }
                                .padding(vertical = 12.dp, horizontal = 16.dp)
                        ) {
                            Text(
                                text = tab, 
                                color = textColor, 
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                
                // Main Content Area
                Column(
                    modifier = Modifier.fillMaxSize().weight(1f).padding(32.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    when (selectedTab) {
                        "Home" -> HomeTab(mediaPlayer) { song, playing -> 
                            currentSong = song
                            isPlaying = playing 
                        }
                        "Explore" -> ExploreTab(mediaPlayer) { song, playing -> 
                            currentSong = song
                            isPlaying = playing 
                        }
                        "Settings" -> SettingsTab()
                        else -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    text = "$selectedTab Content",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Player UI
            if (currentSong != null) {
                var currentProgress by remember { mutableStateOf(0f) }
                var currentVolume by remember { mutableStateOf(mediaPlayer?.volume ?: 1f) }
                
                LaunchedEffect(isPlaying) {
                    while(isPlaying) {
                        val pos = mediaPlayer?.currentPositionMs ?: 0L
                        val len = mediaPlayer?.lengthMs ?: 1L
                        if (len > 0) {
                            currentProgress = pos.toFloat() / len.toFloat()
                        }
                        delay(1000)
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(96.dp)
                        .align(Alignment.BottomCenter)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Thumbnail & Info
                        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = currentSong?.thumbnail,
                                contentDescription = "Cover",
                                modifier = Modifier.size(64.dp).clip(RoundedCornerShape(8.dp))
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = currentSong?.title ?: "",
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                                Text(
                                    text = currentSong?.artists?.joinToString(", ") { it.name } ?: "",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                    maxLines = 1
                                )
                            }
                        }
                        
                        // Controls & Seekbar
                        Column(modifier = Modifier.weight(2f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                IconButton(onClick = { /* Previous stub */ }) {
                                    Icon(Icons.Default.SkipPrevious, "Previous", tint = MaterialTheme.colorScheme.onSurface)
                                }
                                IconButton(
                                    onClick = {
                                        if (isPlaying) {
                                            mediaPlayer?.pause()
                                            isPlaying = false
                                        } else {
                                            mediaPlayer?.resume()
                                            isPlaying = true
                                        }
                                    },
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Icon(
                                        if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, 
                                        "Play/Pause", 
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                                IconButton(onClick = { /* Next stub */ }) {
                                    Icon(Icons.Default.SkipNext, "Next", tint = MaterialTheme.colorScheme.onSurface)
                                }
                            }
                            
                            Slider(
                                value = currentProgress,
                                onValueChange = { 
                                    currentProgress = it
                                    val len = mediaPlayer?.lengthMs ?: 0L
                                    mediaPlayer?.seekTo((it * len).toLong())
                                },
                                modifier = Modifier.fillMaxWidth(0.8f).height(24.dp),
                                colors = SliderDefaults.colors(
                                    thumbColor = MaterialTheme.colorScheme.primary,
                                    activeTrackColor = MaterialTheme.colorScheme.primary,
                                    inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                )
                            )
                        }
                        
                        // Volume
                        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                            Text("Vol", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.width(8.dp))
                            Slider(
                                value = currentVolume,
                                onValueChange = { 
                                    currentVolume = it
                                    mediaPlayer?.setVolume(it)
                                },
                                modifier = Modifier.width(100.dp),
                                colors = SliderDefaults.colors(
                                    thumbColor = MaterialTheme.colorScheme.secondary,
                                    activeTrackColor = MaterialTheme.colorScheme.secondary,
                                    inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeTab(mediaPlayer: MediaPlayer?, onPlayStateChanged: (SongItem?, Boolean) -> Unit) {
    Text(
        text = "Trending Now",
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(modifier = Modifier.height(24.dp))
    
    val searchResult by produceState<Result<SearchSummaryPage>?>(initialValue = null) {
        value = withContext(Dispatchers.IO) { YouTube.searchSummary("trending") }
    }
    
    if (searchResult == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    } else if (searchResult?.isFailure == true) {
        val exception = searchResult?.exceptionOrNull()
        exception?.printStackTrace()
        Text("Failed to load data: ${exception?.message}", color = MaterialTheme.colorScheme.error)
    } else {
        val page = searchResult?.getOrNull()
        val songs = page?.summaries?.flatMap { it.items }?.filterIsInstance<SongItem>() ?: emptyList()
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(songs) { song ->
                SongRow(song, mediaPlayer, onPlayStateChanged)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreTab(mediaPlayer: MediaPlayer?, onPlayStateChanged: (SongItem?, Boolean) -> Unit) {
    var query by remember { mutableStateOf("") }
    var searchResult by remember { mutableStateOf<Result<SearchSummaryPage>?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("Search songs...") },
            singleLine = true
        )
        Spacer(modifier = Modifier.width(16.dp))
        Button(onClick = {
            searchResult = null
            coroutineScope.launch(Dispatchers.IO) {
                searchResult = YouTube.searchSummary(query)
            }
        }) {
            Text("Search")
        }
    }
    
    Spacer(modifier = Modifier.height(24.dp))
    
    if (searchResult == null && query.isNotEmpty()) {
        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
            Text("Type to search", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
        }
    } else if (searchResult?.isFailure == true) {
        val exception = searchResult?.exceptionOrNull()
        exception?.printStackTrace()
        Text("Failed to search: ${exception?.message}", color = MaterialTheme.colorScheme.error)
    } else if (searchResult?.isSuccess == true) {
        val page = searchResult?.getOrNull()
        val songs = page?.summaries?.flatMap { it.items }?.filterIsInstance<SongItem>() ?: emptyList()
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(songs) { song ->
                SongRow(song, mediaPlayer, onPlayStateChanged)
            }
        }
    }
}

@Composable
fun SongRow(song: SongItem, mediaPlayer: MediaPlayer?, onPlayStateChanged: (SongItem?, Boolean) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { 
                coroutineScope.launch(Dispatchers.IO) {
                    try {
                        println("Fetching stream URL for song: ${song.title}")
                        withContext(Dispatchers.Main) { onPlayStateChanged(song, false) }
                        var response = YouTube.player(song.id, client = YouTubeClient.WEB_REMIX).getOrNull()
                        var format = response?.streamingData?.adaptiveFormats?.firstOrNull { it.isAudio && (it.url != null || it.signatureCipher != null) }
                            ?: response?.streamingData?.formats?.firstOrNull { it.isAudio && (it.url != null || it.signatureCipher != null) }
                        
                        if (format == null) {
                            println("WEB_REMIX format is null. Falling back to VISIONOS...")
                            response = YouTube.player(song.id, client = YouTubeClient.VISIONOS).getOrNull()
                            format = response?.streamingData?.adaptiveFormats?.firstOrNull { it.isAudio && (it.url != null || it.signatureCipher != null) }
                                ?: response?.streamingData?.formats?.firstOrNull { it.isAudio && (it.url != null || it.signatureCipher != null) }
                        }

                        if (format == null) {
                            println("VISIONOS format is null. Falling back to IOS...")
                            response = YouTube.player(song.id, client = YouTubeClient.IOS).getOrNull()
                            format = response?.streamingData?.adaptiveFormats?.firstOrNull { it.isAudio && (it.url != null || it.signatureCipher != null) }
                                ?: response?.streamingData?.formats?.firstOrNull { it.isAudio && (it.url != null || it.signatureCipher != null) }
                        }

                        if (format == null) {
                            println("IOS format is null. Falling back to TVHTML5...")
                            response = YouTube.player(song.id, client = YouTubeClient.TVHTML5).getOrNull()
                            format = response?.streamingData?.adaptiveFormats?.firstOrNull { it.isAudio && (it.url != null || it.signatureCipher != null) }
                                ?: response?.streamingData?.formats?.firstOrNull { it.isAudio && (it.url != null || it.signatureCipher != null) }
                        }
                        
                        var url = format?.url ?: format?.let { com.metrolist.innertube.NewPipeUtils.getStreamUrl(it, song.id).getOrNull() }
                        
                        if (url == null) {
                            println("All standard clients failed or URL decryption failed. Trying NewPipeExtractor...")
                            // Ensure NewPipeUtils is initialized
                            com.metrolist.innertube.NewPipeUtils.hashCode()
                            val newPipeStreams = com.metrolist.innertube.NewPipeExtractor.newPipePlayer(song.id)
                            url = newPipeStreams.firstOrNull()?.second
                        }
                        
                        if (url != null) {
                            println("Found URL! Playing audio...")
                            mediaPlayer?.play(url)
                            withContext(Dispatchers.Main) { onPlayStateChanged(song, true) }
                        } else {
                            println("Failed to find a playable URL!")
                            withContext(Dispatchers.Main) { onPlayStateChanged(null, false) }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        println("Error loading song: ${e.message}")
                        withContext(Dispatchers.Main) { onPlayStateChanged(null, false) }
                    }
                }
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(song.title, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
            val artists = song.artists.joinToString { it.name }
            Text(artists, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), fontSize = 14.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTab() {
    var cookie by remember { mutableStateOf(YouTube.cookie ?: "") }
    
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Settings", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(24.dp))
        
        OutlinedTextField(
            value = cookie,
            onValueChange = { cookie = it },
            label = { Text("YouTube Cookie") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { YouTube.cookie = cookie }) {
            Text("Save Settings")
        }
    }
}
