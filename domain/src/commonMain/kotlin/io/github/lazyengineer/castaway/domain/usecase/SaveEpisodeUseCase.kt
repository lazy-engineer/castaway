package io.github.lazyengineer.castaway.domain.usecase

import io.github.lazyengineer.castaway.domain.entity.Episode
import io.github.lazyengineer.castaway.domain.entity.common.DataResult
import io.github.lazyengineer.castaway.domain.repository.FeedDataSource

class SaveEpisodeUseCase(
  private val feedRepository: FeedDataSource
) {

  suspend operator fun invoke(episode: Episode): DataResult<Episode> {
    return feedRepository.saveEpisode(episode)
  }
}
