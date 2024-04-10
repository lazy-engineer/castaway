package io.github.lazyengineer.castawayplayer.extention

import android.content.ContentResolver
import android.net.Uri
import java.io.File

fun File.asAlbumArtContentUri(): Uri {
  return Uri.Builder()
    .scheme(ContentResolver.SCHEME_CONTENT)
    .authority(AUTHORITY)
    .appendPath(this.path)
    .build()
}

private const val AUTHORITY = "io.github.lazyengineer.castawayplayer.provider"
