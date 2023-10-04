package io.github.lazyengineer.castaway.androidApp.util

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import io.github.lazyengineer.castaway.domain.common.StateReducerFlow
import kotlinx.coroutines.test.runTest

fun <S, E> StateReducerFlow<S, E>.testState(test: suspend (ReceiveTurbine<S>) -> Unit) {
  runTest {
	this@testState.test {
	  test(this)
	}
  }
}
