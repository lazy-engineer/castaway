package io.github.lazyengineer.castaway.shared.common

import co.touchlab.stately.ensureNeverFrozen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

abstract class UseCase<out Type, in Params> where Type : Any {

	init {
		ensureNeverFrozen()
	}

    abstract suspend fun run(params: Params): Result<Type>

    suspend operator fun invoke(
		params: Params,
		onSuccess: (Type) -> Unit,
		onError: (Exception) -> Unit
	) {
        when (val result = run(params)) {
			is Result.Success -> {
				onSuccess(result.data)
			}
			is Result.Error -> {
				onError(result.exception)
			}
        }
    }
}

abstract class FlowableUseCase<out Type, in Params> where Type : Any {

	init {
		ensureNeverFrozen()
	}

    abstract fun run(params: Params): Flow<Result<Type>>

    operator fun invoke(
		params: Params,
		onEach: (Type) -> Unit,
		onError: (Throwable) -> Unit,
		onComplete: () -> Unit
	) = run(params)
        .onEach {
            when (val result = it) {
				is Result.Success -> {
					onEach(result.data)
				}
				is Result.Error -> {
					onError(result.exception)
				}
            }
        }
        .catch { onError(it) }
        .onCompletion { onComplete() }
}