package com.example.marvelkmp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.marvelkmp.data.local.CacheCharactersRepository
import com.example.marvelkmp.data.local.DatabaseDriverFactory
import com.example.marvelkmp.data.local.createDatabase
import com.example.marvelkmp.data.network.HttpClientFactory
import com.example.marvelkmp.data.repositories.KtorCharactersRepository
import com.example.marvelkmp.domain.Character
import com.example.marvelkmp.domain.CharactersService
import com.example.marvelkmp.domain.ScreenState
import com.example.marvelkmp.ui.CharactersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(driverFactory: DatabaseDriverFactory) {
    val viewModel =
        viewModel {
            CharactersViewModel(
                CharactersService(
                    CacheCharactersRepository(
                        KtorCharactersRepository(
                            HttpClientFactory.create(),
                        ),
                        createDatabase(driverFactory),
                    ),
                ),
            )
        }
    val state by viewModel.state.collectAsStateWithLifecycle()

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Marvel") })
            },
        ) { paddingValues ->
            when (val current = state) {
                is ScreenState.Loading -> {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is ScreenState.ShowCharacters -> {
                    LazyColumn(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                    ) {
                        items(current.characters) { character ->
                            CharacterItem(
                                character = character,
                                onClick = { viewModel.selectCharacter(character) }
                            )
                        }
                    }
                }

                is ScreenState.ShowDetail -> {
                    DetailScreen(
                        character = current.character,
                        onBack = { viewModel.back() },
                        paddingValues = paddingValues
                    )
                }
            }
        }
    }
}

@Composable
private fun CharacterItem(
    character: Character,
    onClick: () -> Unit
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(72.dp)
                    .background(Color.LightGray),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = character.name,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (character.description.isBlank()) {
                Text(
                    text = "Sin descripción disponible",
                    style =
                        MaterialTheme.typography.bodySmall.copy(
                            fontStyle = FontStyle.Italic,
                            color = Color.Gray,
                        ),
                )
            } else {
                Text(
                    text = character.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                )
            }
        }
    }
}

@Composable
fun DetailScreen(character: Character,
                 onBack: () -> Unit,
                 paddingValues: PaddingValues) {
    Column(
        modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp)) {
        androidx.compose.material3.Text(
            "Detalle de: ${character.name}",
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        androidx.compose.material3.Text(text = character.description)
        Spacer(modifier = Modifier.height(24.dp))
        androidx.compose.material3.Button(onClick = onBack) {
            androidx.compose.material3.Text("Volver")
        }
    }
}