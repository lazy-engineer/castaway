package io.github.lazyengineer.castaway.shared.usecase

import io.github.lazyengineer.castaway.shared.common.Result
import io.github.lazyengineer.castaway.shared.common.UseCase
import io.github.lazyengineer.castaway.shared.entity.Episode
import io.github.lazyengineer.castaway.shared.repository.FeedDataSource

class SaveEpisodeUseCase constructor(
	private val feedRepository: FeedDataSource
) : UseCase<Episode, Episode>() {

	override suspend fun run(episode: Episode): Result<Episode> {
		return feedRepository.saveEpisode(episode)
	}
}
