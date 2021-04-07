package io.github.lazyengineer.castaway.androidApp.view.style

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun ThemeNeumorphism(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
  MaterialTheme(
	content = content
  )
}