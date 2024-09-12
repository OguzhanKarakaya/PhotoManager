package com.dante.photomanager.ui

import android.annotation.SuppressLint
import android.app.Application
import android.net.Uri
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dante.photomanager.model.PhotoModel


/**
 * A composable function that displays the main application screen for the Photo Manager app.
 * It sets up the navigation and the top-level UI components.
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PhotoManagerApp() {
    val viewModel: PhotoViewModel = viewModel(factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
        LocalContext.current.applicationContext as Application
    ))
    val navController = rememberNavController()
    val photos by viewModel.filteredPhotoList.collectAsState()

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
            CapturePhotoButton(onCapture = { uri -> viewModel.addPhoto(PhotoModel(uri = uri, photoName = "Photo ${photos.size + 1}", createdDate = viewModel.getCurrentDateAndTime())) })
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = "photoList"
        ) {
            composable("photoList") {
                PhotoListScreen(
                    photos = photos,
                    onCapturePhoto = { uri -> viewModel.addPhoto(PhotoModel(uri = uri, photoName = "Photo ${photos.size + 1}", createdDate = viewModel.getCurrentDateAndTime())) },
                    onPhotoClick = { uri -> navController.navigate("fullScreenPhoto/${Uri.encode(uri)}") },
                    viewModel = viewModel
                )
            }
            composable(
                route = "fullScreenPhoto/{uri}",
                arguments = listOf(navArgument("uri") { type = NavType.StringType })
            ) { backStackEntry ->
                val uri = backStackEntry.arguments?.getString("uri")
                uri?.let {
                    FullScreenPhotoScreen(photoUri = it)
                }
            }
        }
    }
}

