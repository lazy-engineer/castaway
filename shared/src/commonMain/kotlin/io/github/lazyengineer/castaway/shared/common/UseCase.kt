package io.github.lazyengineer.castaway.shared.common

import co.touchlab.stately.freeze
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

sealed class UseCase<out Type, in Params>(private val suspender: suspend () -> Result<Type>) where Type : Any {

  fun subscribe(
	scope: CoroutineScope,
	onSuccess: (Type) -> Unit,
	onError: (String) -> Unit
  ) = scope.launch {
	when (val result = suspender()) {
	  is Result.Success -> {
		onSuccess(result.data.freeze())
	  }
	  is Result.Error -> {
		onError((result.exception.message ?: "subscribe error").freeze())
	  }
	}
  }.freeze()
}

class UseCaseWrapper<out Type : Any, in Params>(suspender: suspend () -> Result<Type>) : UseCase<Type, Params>(suspender)

sealed class FlowableUseCase<out Type, in Params>(private val flow: () -> Flow<Result<Type>>) where Type : Any {

  init {
	freeze()
  }

  fun subscribe(
	scope: CoroutineScope,
	onEach: (Type) -> Unit,
	onError: (String) -> Unit,
	onComplete: () -> Unit
  ) = flow()
	.onEach {
	  when (val result = it.freeze()) {
		is Result.Success -> {
		  onEach(result.data)
		}
		is Result.Error -> {
		  onError((result.exception.message ?: "subscribe flow error").freeze())
		}
	  }
	}
	.catch { onError((it.message ?: "catch error").freeze()) }
	.onCompletion { onComplete() }
	.launchIn(scope)
	.freeze()
}

class FlowableUseCaseWrapper<out Type : Any, in Params>(flow: () -> Flow<Result<Type>>) : FlowableUseCase<Type, Params>(flow)