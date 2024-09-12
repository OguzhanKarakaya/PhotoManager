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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.dante.photomanager.model.PhotoModel


/**
 * A composable function that displays the main screen for the photo manager app.
 *
 * @param photos A list of PhotoModel objects representing the photos to be displayed.
 * @param onCapturePhoto A callback function to handle the captured photo's URI.
 * @param onPhotoClick A callback function to handle the click event on a photo.
 * @param viewModel The ViewModel that manages the state and logic for the photo list screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoListScreen(
    photos: List<PhotoModel>,
    onCapturePhoto: (String) -> Unit,
    onPhotoClick: (String) -> Unit,
    viewModel: PhotoViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Photo Manager") },
                actions = {
                    SearchBarWithDropdown(
                        query = viewModel.searchQuery.collectAsState().value,
                        onQueryChanged = { query -> viewModel.updateSearchQuery(query) },
                        onSortSelected = { sortOption -> viewModel.sortPhotos(sortOption) }
                    )
                }
            )
        },
        floatingActionButton = {
            CapturePhotoButton(onCapture = onCapturePhoto)
        }
    ) { padding ->
        PhotoList(photos = photos, onPhotoClick = onPhotoClick, modifier = Modifier.padding(padding))
    }
}

/**
 * A composable function that displays a list of photos.
 *
 * @param photos A list of PhotoModel objects representing the photos to be displayed.
 * @param onPhotoClick A callback function to handle the click event on a photo.
 * @param modifier A Modifier for styling the LazyColumn.
 */
@Composable
fun PhotoList(photos: List<PhotoModel>, onPhotoClick: (String) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(photos) { photo ->
            PhotoItem(photo = photo, onPhotoClick = onPhotoClick)
        }
    }
}

/**
 * A composable function that displays a single photo item.
 *
 * @param photo A PhotoModel object representing the photo to be displayed.
 * @param onPhotoClick A callback function to handle the click event on the photo.
 */
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