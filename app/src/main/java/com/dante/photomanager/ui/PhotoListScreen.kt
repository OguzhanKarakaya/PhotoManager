package com.dante.photomanager.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.dante.photomanager.model.PhotoModel

@Composable
fun PhotoListScreen(
    photos: List<PhotoModel>,
    onCapturePhoto: (String) -> Unit,
    onPhotoClick: (String) -> Unit
) {
    Scaffold(

        floatingActionButton = {
            CapturePhotoButton(onCapture = onCapturePhoto)
        }
    ) { padding ->
        PhotoList(photos = photos, onPhotoClick = onPhotoClick, modifier = Modifier.padding(padding))
    }
}

@Composable
fun PhotoList(photos: List<PhotoModel>, onPhotoClick: (String) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(photos) { photo ->
            PhotoItem(photo = photo, onPhotoClick = onPhotoClick)
        }
    }
}

@Composable
fun PhotoItem(photo: PhotoModel, onPhotoClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .clickable { photo.uri?.let { onPhotoClick(it) } }
    ) {
        Image(
            painter = rememberAsyncImagePainter(photo.uri),
            contentDescription = "Photo",
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = photo.photoName ?: "", style = MaterialTheme.typography.bodyLarge)
            Text(text = photo.createdDate ?: "", style = MaterialTheme.typography.bodySmall)
        }
    }
}