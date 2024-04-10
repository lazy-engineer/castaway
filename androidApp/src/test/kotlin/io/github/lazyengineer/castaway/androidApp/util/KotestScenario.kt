package io.github.lazyengineer.castaway.androidApp.util

import io.kotest.common.ExperimentalKotest
import io.kotest.core.names.TestName
import io.kotest.core.spec.style.scopes.BehaviorSpecGivenContainerScope
import io.kotest.core.spec.style.scopes.ContainerScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@Suppress("TestFunctionName")
@ExperimentalKotest
@OptIn(ExperimentalCoroutinesApi::class)
suspend fun ContainerScope.Scenario(
  name: String,
  dispatcher: TestDispatcher = StandardTestDispatcher(),
  test: suspend BehaviorSpecGivenContainerScope.() -> Unit
) =
  registerContainer(
    TestName("Scenario: ", name, true),
    disabled = false,
    null,
  ) {
    Dispatchers.setMain(dispatcher)
    BehaviorSpecGivenContainerScope(this).test()
    Dispatchers.resetMain()
  }
