package io.github.lazyengineer.castaway.shared

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.native.concurrent.freeze

/**
 * https://github.com/touchlab/SwiftCoroutines/blob/master/shared/src/iosMain/kotlin/co/touchlab/swiftcoroutines/SwiftCoroutines.kt
 */
sealed class SuspendWrapperParent<T>(private val suspender: suspend () -> T) {

  init {
	freeze()
  }

  fun subscribe(
	scope: CoroutineScope,
	onSuccess: (item: T) -> Unit,
	onThrow: (error: Throwable) -> Unit
  ) = scope.launch {
	try {
	  onSuccess(suspender().freeze())
	} catch (error: Throwable) {
	  onThrow(error.freeze())
	}
  }.freeze()
}

class SuspendWrapper<T : Any>(suspender: suspend () -> T) : SuspendWrapperParent<T>(suspender)
class NullableSuspendWrapper<T>(suspender: suspend () -> T) : SuspendWrapperParent<T>(suspender)

sealed class FlowWrapperParent<T>(private val flow: Flow<T>) {

  init {
	freeze()
  }

  fun subscribe(
	scope: CoroutineScope,
	onEach: (item: T) -> Unit,
	onComplete: () -> Unit,
	onThrow: (error: Throwable) -> Unit
  ) = flow
	.onEach { onEach(it.freeze()) }
	.catch { onThrow(it.freeze()) } // catch{} before onCompletion{} or else completion hits first and ends stream
	.onCompletion { onComplete() }
	.launchIn(scope)
	.freeze()
}

class FlowWrapper<T : Any>(flow: Flow<T>) : FlowWrapperParent<T>(flow)
class NullableFlowWrapper<T>(flow: Flow<T>) : FlowWrapperParent<T>(flow)
