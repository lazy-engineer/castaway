package io.github.lazyengineer.castaway.domain.common

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

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
