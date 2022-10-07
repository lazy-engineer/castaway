package io.github.lazyengineer.castaway.data.di

import io.github.lazyengineer.castaway.data.database.FeedLocalDataSource
import io.github.lazyengineer.castaway.data.repository.FeedRepository
import io.github.lazyengineer.castaway.data.webservice.FeedRemoteDataSource
import io.github.lazyengineer.castaway.domain.usecase.GetFeedUseCase
import io.github.lazyengineer.castaway.domain.usecase.GetStoredEpisodesUseCase
import io.github.lazyengineer.castaway.domain.usecase.GetStoredFeedUseCase
import io.github.lazyengineer.castaway.domain.usecase.SaveEpisodeUseCase
import io.github.lazyengineer.castaway.domain.usecase.SaveFeedUseCase
import io.github.lazyengineer.castaway.domain.usecase.StoredEpisodeFlowableUseCase
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module

fun coreModule() = module {
  single { HttpClient() }
  single {
	FeedRepository(
	  remoteDataSource = FeedRemoteDataSource(get()),
	  localDataSource = FeedLocalDataSource(get(), Dispatchers.Default),
	)
  }

  single { GetFeedUseCase(get() as FeedRepository) }
  single { GetStoredFeedUseCase(get() as FeedRepository) }
  single { GetStoredEpisodesUseCase(get() as FeedRepository) }
  single { SaveEpisodeUseCase(get() as FeedRepository) }
  single { SaveFeedUseCase(get() as FeedRepository) }
  single { StoredEpisodeFlowableUseCase(get() as FeedRepository) }
}

expect fun platformModule(): Module
