package io.github.lazyengineer.castaway.androidApp.player

import io.github.lazyengineer.castaway.domain.entity.Episode
import io.github.lazyengineer.castawayplayer.MediaServiceClient
import io.github.lazyengineer.castawayplayer.source.MediaData

class PreparePlayerUseCase(
  private val castawayPlayer: MediaServiceClient
) {

  operator fun invoke(episodes: List<Episode>) {
    castawayPlayer.prepare(episodes.mapToMediaData())
  }

  private fun List<Episode>.mapToMediaData() = this.map {
    MediaData(
      mediaId = it.id,
      mediaUri = it.audioUrl,
      displayTitle = it.title,
      displayIconUri = it.imageUrl,
      displaySubtitle = it.subTitle.orEmpty(),
      author = it.author,
      playbackPosition = it.playbackPosition.position,
      duration = it.playbackPosition.duration,
    )
  }
}
