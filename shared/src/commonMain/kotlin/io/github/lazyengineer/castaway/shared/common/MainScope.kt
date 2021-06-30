package io.github.lazyengineer.castaway.shared.common

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class MainScope(private val mainContext: CoroutineContext) : CoroutineScope {

  override val coroutineContext: CoroutineContext
	get() = mainContext + job + exceptionHandler

  private val job = SupervisorJob()
  private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
	// TODO: pass throwable to platform
  }

  fun onDestroy() {
	job.cancel()
  }
}
