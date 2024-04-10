package io.github.lazyengineer.castaway.domain.common

import io.github.lazyengineer.castaway.domain.entity.common.DataResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

sealed class UseCase<out Type, in Params>(private val suspender: suspend () -> DataResult<Type>) where Type : Any {

  fun subscribe(
    scope: CoroutineScope,
    onSuccess: (Type) -> Unit,
    onError: (String) -> Unit
  ) = scope.launch {
    when (val result = suspender()) {
      is DataResult.Success -> {
        onSuccess(result.data)
      }

      is DataResult.Error -> {
        onError((result.exception.message ?: "subscribe error"))
      }
    }
  }
}

class UseCaseWrapper<out Type : Any, in Params>(suspender: suspend () -> DataResult<Type>) : UseCase<Type, Params>(suspender)

sealed class FlowableUseCase<out Type, in Params>(private val flow: () -> Flow<DataResult<Type>>) where Type : Any {

  fun subscribe(
    scope: CoroutineScope,
    onEach: (Type) -> Unit,
    onError: (String) -> Unit,
    onComplete: () -> Unit
  ) = flow()
    .onEach {
      when (val result = it) {
        is DataResult.Success -> {
          onEach(result.data)
        }

        is DataResult.Error -> {
          onError((result.exception.message ?: "subscribe flow error"))
        }
      }
    }
    .catch { onError((it.message ?: "catch error")) }
    .onCompletion { onComplete() }
    .launchIn(scope)
}

class FlowableUseCaseWrapper<out Type : Any, in Params>(flow: () -> Flow<DataResult<Type>>) : FlowableUseCase<Type, Params>(flow)
