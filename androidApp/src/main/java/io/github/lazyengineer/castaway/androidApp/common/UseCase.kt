package io.github.lazyengineer.castaway.androidApp.common

import io.github.lazyengineer.castaway.shared.Result

abstract class UseCase<out Type, in Params> where Type : Any {

	abstract suspend fun run(params: Params): Result<Type>

	suspend operator fun invoke(params: Params, onSuccess: (Type) -> Unit, onError: (Exception) -> Unit) {
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
