package io.github.lazyengineer.castaway.shared.di

import io.github.lazyengineer.castaway.shared.database.FeedLocalDataSource
import io.github.lazyengineer.castaway.shared.database.createDb
import io.github.lazyengineer.castaway.shared.repository.FeedRepository
import io.github.lazyengineer.castaway.shared.usecase.GetFeedUseCase
import io.github.lazyengineer.castaway.shared.usecase.GetStoredEpisodesUseCase
import io.github.lazyengineer.castaway.shared.usecase.GetStoredFeedUseCase
import io.github.lazyengineer.castaway.shared.usecase.LoadImageUseCase
import io.github.lazyengineer.castaway.shared.usecase.SaveEpisodeUseCase
import io.github.lazyengineer.castaway.shared.usecase.SaveFeedUseCase
import io.github.lazyengineer.castaway.shared.usecase.StoredEpisodeFlowableUseCase
import io.github.lazyengineer.castaway.shared.webservice.FeedRemoteDataSource
import io.github.lazyengineer.castaway.shared.webservice.ImageLoader
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
  startKoin {
	appDeclaration()
	modules(coreModule, platformModule)
  }

// called by iOS etc
fun initKoin() = initKoin {}

private val coreModule = module {
  single { createDb() }
  single { HttpClient() }
  single { ImageLoader(get()) }
  single {
	FeedRepository(
	  imageLoader = get(),
	  remoteDataSource = FeedRemoteDataSource(get(), Dispatchers.Default),
	  localDataSource = FeedLocalDataSource(get(), Dispatchers.Default),
	)
  }

  single { GetFeedUseCase(get() as FeedRepository) }
  single { GetStoredFeedUseCase(get() as FeedRepository) }
  single { GetStoredEpisodesUseCase(get() as FeedRepository) }
  single { SaveEpisodeUseCase(get() as FeedRepository) }
  single { SaveFeedUseCase(get() as FeedRepository) }
  single { StoredEpisodeFlowableUseCase(get() as FeedRepository) }
  single { LoadImageUseCase(get()) }
}

expect val platformModule: Module