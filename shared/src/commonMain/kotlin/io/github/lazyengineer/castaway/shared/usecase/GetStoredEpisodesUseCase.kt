package io.github.lazyengineer.castaway.shared.usecase

import io.github.lazyengineer.castaway.shared.common.Result
import io.github.lazyengineer.castaway.shared.common.UseCase
import io.github.lazyengineer.castaway.shared.entity.Episode
import io.github.lazyengineer.castaway.shared.repository.FeedDataSource

class GetStoredEpisodesUseCase constructor(
    private val feedRepository: FeedDataSource
) : UseCase<List<Episode>, List<String>>() {

    override suspend fun run(episodeIds: List<String>): Result<List<Episode>> {
        return feedRepository.loadEpisodes(episodeIds)
    }
}