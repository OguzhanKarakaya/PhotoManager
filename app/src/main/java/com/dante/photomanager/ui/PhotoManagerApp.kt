package com.dante.photomanager.ui

import android.annotation.SuppressLint
import android.app.Application
import android.net.Uri
import androidx.compose.material3.Scaffold
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PhotoManagerApp() {
    val viewModel: PhotoViewModel = viewModel(factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
        LocalContext.current.applicationContext as Application
    ))
    val navController = rememberNavController()
    val photos by viewModel.filteredPhotoList.collectAsState()

    Scaffold(
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
                    onPhotoClick = { uri -> navController.navigate("fullScreenPhoto/${Uri.encode(uri)}") }
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

