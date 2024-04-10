package io.github.lazyengineer.castaway.data.di

import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.github.lazyengineer.castaway.data.database.adapter.PlaybackPositionAdapter
import io.github.lazyengineer.castaway.data.parser.FeedParserImpl
import io.github.lazyengineer.castaway.data.repository.FeedRepository
import io.github.lazyengineer.castaway.db.CastawayDatabase
import io.github.lazyengineer.castaway.domain.usecase.StoreAndGetFeedUseCase
import io.ktor.client.engine.android.Android
import iogithublazyengineercastawaydb.EpisodeEntity.Adapter
import org.koin.core.module.Module
import org.koin.dsl.module
import org.xmlpull.v1.XmlPullParserFactory

actual fun platformModule(): Module = module {

  single { Android.create() }

  single {
    val driver = AndroidSqliteDriver(CastawayDatabase.Schema, get(), "castaway.db")

    CastawayDatabase(
      driver, episodeEntityAdapter = Adapter(
        playbackPositionAdapter = PlaybackPositionAdapter
      )
    )
  }

  single {
    val factory = XmlPullParserFactory.newInstance()
    val xmlPullParser = factory.newPullParser()
    StoreAndGetFeedUseCase(get() as FeedRepository, FeedParserImpl(xmlPullParser))
  }
}
