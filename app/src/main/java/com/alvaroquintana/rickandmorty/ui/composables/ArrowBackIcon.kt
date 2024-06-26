package com.alvaroquintana.rickandmorty.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.alvaroquintana.rickandmorty.R

@Composable
fun ArrowBackIcon(onUpClick: () -> Unit) {
	val context = LocalContext.current
	IconButton(onClick = onUpClick) {
		Icon(
			imageVector = Icons.AutoMirrored.Filled.ArrowBack,
			contentDescription = context.getString(R.string.icon)
		)
	}
}
