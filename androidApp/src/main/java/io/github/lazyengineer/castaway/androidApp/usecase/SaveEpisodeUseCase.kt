package io.github.lazyengineer.castaway.androidApp.usecase

import io.github.lazyengineer.castaway.shared.Result
import io.github.lazyengineer.castaway.androidApp.common.UseCase
import io.github.lazyengineer.castaway.androidApp.entity.Episode
import io.github.lazyengineer.castaway.androidApp.repository.FeedDataSource

class SaveEpisodeUseCase constructor(
	private val feedRepository: FeedDataSource
) : UseCase<Episode, Episode>() {

	override suspend fun run(episode: Episode): Result<Episode> {
		return feedRepository.saveEpisode(episode)
	}
}
