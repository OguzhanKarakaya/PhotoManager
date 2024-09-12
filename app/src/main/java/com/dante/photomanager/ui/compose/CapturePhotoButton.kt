package com.dante.photomanager.ui.compose

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.dante.photomanager.R
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

/**
 * A composable function that displays a floating action button for capturing photos.
 *
 * @param onCapture A callback function to handle the captured photo's URI.
 */
@Composable
fun CapturePhotoButton(onCapture: (String) -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            coroutineScope.launch {
                val uri = saveImageToStorage(context, bitmap)
                if (uri != null) {
                    onCapture(uri)
                }
            }
        }
    }

    FloatingActionButton(
        onClick = { launcher.launch() },
        shape = CircleShape,
        containerColor = Color(0xFF6200EE)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_add_a_photo_24),
            contentDescription = "Capture Photo",
            tint = Color.White
        )
    }
}

/**
 * Saves a bitmap image to the device storage.
 *
 * @param context The context used to access the content resolver.
 * @param bitmap The bitmap image to be saved.
 * @return The URI of the saved image, or null if the save operation failed.
 */
fun saveImageToStorage(context: Context, bitmap: Bitmap): String? {
    val filename = "Photo_${System.currentTimeMillis()}.jpg"
    var outputStream: OutputStream? = null
    var uri: String? = null

    val isQ = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q

    try {
        uri = if (isQ) {
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }

            val imageUri =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            outputStream = resolver.openOutputStream(imageUri!!)
            imageUri.toString()
        } else {
            val directory =
                File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "PhotoManagerApp")
            if (!directory.exists()) {
                directory.mkdirs()
            }

            val file = File(directory, filename)
            outputStream = FileOutputStream(file)
            file.absolutePath
        }
        if (outputStream != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }
    } catch (e: Exception) {
        Log.e("saveImageToStorage", "Error saving image", e)
    } finally {
        outputStream?.close()
    }

    return uri
}