package com.dante.photomanager.ui

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dante.photomanager.SortOptions
import com.dante.photomanager.model.PhotoModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PhotoViewModel : ViewModel() {
    private val _photoList = MutableStateFlow<List<PhotoModel>>(emptyList())
    private val _searchQuery = MutableStateFlow(TextFieldValue(""))
    val searchQuery: StateFlow<TextFieldValue> = _searchQuery

    val filteredPhotoList: StateFlow<List<PhotoModel>> = _photoList
        .combine(_searchQuery) { list, query ->
            list.filter {
                it.photoName?.contains(query.text, ignoreCase = true) ?: false ||
                        it.createdDate?.contains(query.text, ignoreCase = true) ?: false
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L), //Specify how the flow will start
            initialValue = emptyList()
        )

    fun addPhoto(photo: PhotoModel) {
        val newPhoto = PhotoModel(photo.uri, photo.photoName, photo.createdDate)
        _photoList.value += newPhoto
    }

    fun updateSearchQuery(query: TextFieldValue) {
        _searchQuery.value = query
    }

    fun sortPhotos(sortOptions: SortOptions) {
        _photoList.value = when (sortOptions) {
            SortOptions.DATE_DESCENDING -> _photoList.value.sortedByDescending { it.createdDate }
            SortOptions.DATE_ASCENDING -> _photoList.value.sortedBy { it.createdDate }
            SortOptions.NAME_ASCENDING -> _photoList.value.sortedBy { it.photoName }
            SortOptions.NAME_DESCENDING -> _photoList.value.sortedByDescending { it.photoName }
        }
    }

    fun getCurrentDateAndTime(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy / HH:mm:ss")
        return currentDateTime.format(formatter)
    }

}