package io.github.lazyengineer.castaway.shared.usecase

import io.github.lazyengineer.castaway.shared.common.Result
import io.github.lazyengineer.castaway.shared.common.UseCase
import io.github.lazyengineer.castaway.shared.repository.FeedRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class GetFeedUseCase : UseCase<String, String>(), KoinComponent {

	private val feedRepository: FeedRepository by inject()

	override suspend fun run(url: String): Result<String> {
		return feedRepository.fetchXml(url)
	}
}