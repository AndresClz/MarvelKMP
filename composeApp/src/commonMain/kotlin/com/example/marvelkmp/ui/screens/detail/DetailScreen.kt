package com.example.marvelkmp.ui.screens.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.marvelkmp.domain.Character
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import marvelkmp.composeapp.generated.resources.Res
import marvelkmp.composeapp.generated.resources.image_not_found
import org.jetbrains.compose.resources.painterResource

data class DetailScreen(val character: Character) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            topBar = { TopAppBar(title = { Text(character.name) }) },
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
            ) {
                KamelImage(
                    resource = { asyncPainterResource(character.thumbnailUrl) },
                    contentDescription = character.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Fit,
                    onLoading = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.LightGray),
                        )
                    },
                    onFailure = {
                        Image(
                            painter = painterResource(Res.drawable.image_not_found),
                            contentDescription = "Error loading image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                    },
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (character.description.isBlank()) {
                    Text(
                        text = "Descripción no disponible",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontStyle = FontStyle.Italic,
                            color = Color.Gray,
                        ),
                    )
                } else {
                    Text(
                        text = character.description,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = { navigator.pop() }) {
                    Text("Volver")
                }
            }
        }
    }
}
