package io.github.lazyengineer.castaway.data.di

import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import io.github.lazyengineer.castaway.data.database.adapter.PlaybackPositionAdapter
import io.github.lazyengineer.castaway.db.CastawayDatabase
import io.github.lazyengineer.castaway.domain.usecase.NativeGetFeedUseCase
import io.github.lazyengineer.castaway.domain.usecase.NativeGetStoredEpisodesUseCase
import io.github.lazyengineer.castaway.domain.usecase.NativeGetStoredFeedUseCase
import io.github.lazyengineer.castaway.domain.usecase.NativeSaveEpisodeUseCase
import io.github.lazyengineer.castaway.domain.usecase.NativeSaveFeedUseCase
import io.github.lazyengineer.castaway.domain.usecase.NativeStoredEpisodeFlowableUseCase
import iogithublazyengineercastawaydb.EpisodeEntity.Adapter
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.dsl.module

actual fun platformModule() = module {

  single {
	val driver = NativeSqliteDriver(CastawayDatabase.Schema, "castaway.db")
	CastawayDatabase(
	  driver, episodeEntityAdapter = Adapter(
		playbackPositionAdapter = PlaybackPositionAdapter
	  )
	)
  }

  single { NativeGetFeedUseCase(get()) }
  single { NativeGetStoredEpisodesUseCase(get()) }
  single { NativeGetStoredFeedUseCase(get()) }
  single { NativeSaveEpisodeUseCase(get()) }
  single { NativeSaveFeedUseCase(get()) }
  single { NativeStoredEpisodeFlowableUseCase(get()) }
}
