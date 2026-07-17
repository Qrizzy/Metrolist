package com.metrolist.music.desktop

import com.metrolist.innertube.YouTube
import com.metrolist.innertube.models.YouTubeClient
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val songId = "dEqXyC2FvI8" // "Aku Skandal" by Bunkface (example ID, or use any known ID, wait I don't know the exact ID, let's search it first)
    // First, let's search for "Aku Skandal" to get a real ID.
    val searchResult = YouTube.searchSummary("Aku Skandal").getOrNull()
    val song = searchResult?.summaries?.flatMap { it.items }?.filterIsInstance<com.metrolist.innertube.models.SongItem>()?.firstOrNull()
    
    if (song == null) {
        println("Could not find song.")
        return@runBlocking
    }
    println("Found song: ${song.title} (${song.id})")
    
    val clients = listOf(
        YouTubeClient.VISIONOS,
        YouTubeClient.WEB,
        YouTubeClient.WEB_REMIX,
        YouTubeClient.IOS,
        YouTubeClient.ANDROID_VR_1_65_10
    )
    
    for (client in clients) {
        println("Trying client: ${client.clientName}")
        val response = YouTube.player(song.id, client = client).getOrNull()
        println("Playability status: ${response?.playabilityStatus?.status}")
        println("Playability reason: ${response?.playabilityStatus?.reason}")
        val hasStreamingData = response?.streamingData != null
        println("Has streamingData: $hasStreamingData")
        if (hasStreamingData) {
            val formats = response?.streamingData?.formats ?: emptyList()
            val adaptiveFormats = response?.streamingData?.adaptiveFormats ?: emptyList()
            println("Formats: ${formats.size}, Adaptive Formats: ${adaptiveFormats.size}")
            
            val validAudio = adaptiveFormats.firstOrNull { it.isAudio && (it.url != null || it.signatureCipher != null) }
                ?: formats.firstOrNull { it.isAudio && (it.url != null || it.signatureCipher != null) }
            println("Found valid audio format: ${validAudio != null}")
            if (validAudio != null) {
                println("URL is null: ${validAudio.url == null}")
                println("Cipher is null: ${validAudio.signatureCipher == null}")
            }
        }
        println("-------------------------")
    }
}
