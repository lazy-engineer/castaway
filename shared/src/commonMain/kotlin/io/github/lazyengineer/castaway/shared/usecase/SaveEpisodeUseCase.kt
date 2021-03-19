package io.github.lazyengineer.castaway.shared.usecase

import io.github.lazyengineer.castaway.shared.common.UseCaseWrapper
import io.github.lazyengineer.castaway.shared.entity.Episode
import io.github.lazyengineer.castaway.shared.repository.FeedDataSource

class SaveEpisodeUseCase constructor(
  private val feedRepository: FeedDataSource
) {

  operator fun invoke(episode: Episode) = UseCaseWrapper<Episode, Episode> {
	feedRepository.saveEpisode(episode)
  }
}
