package io.github.lazyengineer.castaway.androidApp.util

import app.cash.turbine.testIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlin.coroutines.EmptyCoroutineContext

class StateFlowTurbineViewRobot<T>(
  private val scope: CoroutineScope = CoroutineScope(EmptyCoroutineContext),
  private val scheduler: TestCoroutineScheduler,
  private val stateFlow: StateFlow<T>,
) {

  val listOfStates = mutableListOf<T>()

  private val turbine = stateFlow.testIn(scope)

  suspend fun collect() {
    scheduler.advanceUntilIdle()
    listOfStates.add(turbine.awaitItem())
  }

  fun collectMostRecentItem() {
    scheduler.advanceUntilIdle()
    listOfStates.add(turbine.expectMostRecentItem())
  }
}
