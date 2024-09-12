package com.dante.photomanager.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.dante.photomanager.R
import com.dante.photomanager.SortOptions

/**
 * A composable function that displays a search bar with a dropdown menu for sorting options.
 *
 * @param query The current text in the search bar.
 * @param onQueryChanged A callback function to handle changes in the search bar text.
 * @param onSortSelected A callback function to handle the selection of a sort option.
 */
@Composable
fun SearchBarWithDropdown(
    query: TextFieldValue,
    onQueryChanged: (TextFieldValue) -> Unit,
    onSortSelected: (SortOptions) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val sortOptions = listOf(
        "Sort by Date (New to Old)" to SortOptions.DATE_DESCENDING,
        "Sort by Date (Old to New)" to SortOptions.DATE_ASCENDING,
        "Sort by Name (A to Z)" to SortOptions.NAME_ASCENDING,
        "Sort by Name (Z to A)" to SortOptions.NAME_DESCENDING
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp), // Add padding to the horizontal sides
        verticalAlignment = Alignment.CenterVertically, // Vertically center the content
        horizontalArrangement = Arrangement.SpaceBetween // Space between elements
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChanged,
            label = { Text("Search") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
            modifier = Modifier
                .weight(1f) // Take up remaining space
        )

        IconButton(
            onClick = { expanded = true },
            modifier = Modifier.align(Alignment.CenterVertically) // Align button vertically centered
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_sort_24), // Load the drawable resource
                contentDescription = "Sort Options"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            sortOptions.forEach { (text, sortOption) ->
                DropdownMenuItem(
                    text = { Text(text) },
                    onClick = {
                        onSortSelected(sortOption)
                        expanded = false
                    }
                )
            }
        }
    }
}