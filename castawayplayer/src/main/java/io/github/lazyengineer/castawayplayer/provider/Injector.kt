package io.github.lazyengineer.castawayplayer.provider

import android.content.Context
import android.content.SharedPreferences
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.util.CoilUtils
import com.google.gson.Gson
import io.github.lazyengineer.castawayplayer.config.MediaServiceConfig
import io.github.lazyengineer.castawayplayer.source.FeedMediaSource
import io.github.lazyengineer.castawayplayer.source.LocalDataSource
import io.github.lazyengineer.castawayplayer.source.PREFERENCES_NAME
import okhttp3.OkHttpClient

class Injector private constructor(private val context: Context) {

  private lateinit var config: MediaServiceConfig
  private lateinit var sharedPreferences: SharedPreferences
  private lateinit var gson: Gson
  private lateinit var imageLoader: ImageLoader

  fun provideConfig(config: MediaServiceConfig? = null): MediaServiceConfig {
	config?.let { this.config = it }

	return if (this::config.isInitialized) {
	  this.config
	} else {
	  MediaServiceConfig()
	}
  }

  private fun provideSharedPrefs(): SharedPreferences {
	return if (this::sharedPreferences.isInitialized) {
	  sharedPreferences
	} else {
	  context.getSharedPreferences(
		PREFERENCES_NAME,
		Context.MODE_PRIVATE
	  )
	}
  }

  private fun provideGson(): Gson {
	return if (this::gson.isInitialized) {
	  gson
	} else {
	  provideConfig().gson ?: Gson()
	}
  }

  fun provideImageLoader(): ImageLoader {
	return if (this::imageLoader.isInitialized) {
	  imageLoader
	} else {
	  provideConfig().imageLoader ?: ImageLoader.Builder(context)
		.memoryCache {
		  MemoryCache.Builder(context)
			.maxSizePercent(0.25)
			.build()
		}
		.crossfade(true)
		.build()
	}
  }

  fun provideMediaSource(
	gson: Gson = provideGson(),
	sharedPreferences: SharedPreferences = provideSharedPrefs(),
  ): FeedMediaSource {
	return FeedMediaSource.getInstance(
	  LocalDataSource.getInstance(sharedPreferences, gson),
	)
  }

  companion object {

	@Volatile
	private var INSTANCE: Injector? = null

	fun getInstance(
	  context: Context,
	  config: MediaServiceConfig? = null
	): Injector {
	  return INSTANCE ?: synchronized(this) {
		INSTANCE ?: Injector(context).also {
		  INSTANCE = it
		  it.provideConfig(config)
		}
	  }
	}
  }
}
