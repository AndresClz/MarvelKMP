package com.example.marvelkmp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.example.marvelkmp.domain.Character
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import marvelkmp.composeapp.generated.resources.Res
import marvelkmp.composeapp.generated.resources.image_not_found
import org.jetbrains.compose.resources.painterResource

@Composable
fun DetailScreen(
    character: Character,
    onBack: () -> Unit,
    paddingValues: PaddingValues,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp),
    ) {
        Text(
            text = "${character.name}",
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(16.dp))
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
        Button(onClick = onBack) {
            Text("Volver")
        }
    }
}
