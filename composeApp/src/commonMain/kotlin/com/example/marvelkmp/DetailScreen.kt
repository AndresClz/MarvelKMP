package com.example.marvelkmp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.marvelkmp.domain.Character

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
