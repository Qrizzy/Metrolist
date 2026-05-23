package com.metrolist.music.ui.screens.wrapped

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
fun WrappedScreen(
    navController: NavController,
    viewModel: WrappedViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.prepare(
            fromTimeStamp = WrappedViewModel.HALF_YEAR_START,
            toTimeStamp = WrappedViewModel.HALF_YEAR_END,
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
            Text("Loading your half-year wrapped...")
        } else if (state.isDataReady) {
            Text("Your Half-Year in Music")
            Text("${state.totalMinutes} minutes listened")
            Text("${state.uniqueSongCount} unique songs")
            Text("${state.uniqueArtistCount} unique artists")
            Text("${state.topSongs.size} songs in top list")

            Button(onClick = { navController.popBackStack() }) {
                Text("Back")
            }
        }
    }
}
