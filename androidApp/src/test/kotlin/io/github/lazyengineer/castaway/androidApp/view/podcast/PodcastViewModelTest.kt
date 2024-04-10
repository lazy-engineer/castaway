package io.github.lazyengineer.castaway.androidApp.view.podcast

import io.github.lazyengineer.castaway.androidApp.player.PlayPauseUseCase
import io.github.lazyengineer.castaway.androidApp.player.PlayerState
import io.github.lazyengineer.castaway.androidApp.player.PlayerStateUseCase
import io.github.lazyengineer.castaway.androidApp.player.PreparePlayerUseCase
import io.github.lazyengineer.castaway.androidApp.player.SubscribeToPlayerUseCase
import io.github.lazyengineer.castaway.androidApp.util.testState
import io.github.lazyengineer.castaway.domain.common.StateReducerFlow
import io.github.lazyengineer.castaway.domain.usecase.GetStoredFeedUseCase
import io.github.lazyengineer.castaway.domain.usecase.StoreAndGetFeedUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.amshove.kluent.`should be equal to`

@OptIn(ExperimentalCoroutinesApi::class)
class PodcastViewModelTest : BehaviorSpec({
  lateinit var viewModel: PodcastViewModel
  lateinit var state: StateReducerFlow<PodcastViewState, PodcastEvent>

  val playerStateUseCase: PlayerStateUseCase = mockk {
    every { this@mockk.invoke() } returns flow {
      emit(PlayerState.Initial)
      emit(PlayerState.Initial)
    }
  }

  val getStoredFeedUseCase: GetStoredFeedUseCase = mockk()
  val storeAndGetFeedUseCase: StoreAndGetFeedUseCase = mockk()
  val subscribeToPlayerUseCase: SubscribeToPlayerUseCase = mockk {
    coEvery { this@mockk.invoke() } returns Unit
  }
  val preparePlayerUseCase: PreparePlayerUseCase = mockk {
    every { this@mockk.invoke(any()) } returns Unit
  }

  val playPauseUseCase: PlayPauseUseCase = mockk()

  Given("PodcastViewModel is initialized") {
    Dispatchers.setMain(StandardTestDispatcher())

    viewModel = PodcastViewModel(
      getStoredFeedUseCase = getStoredFeedUseCase,
      playerStateUseCase = playerStateUseCase,
      storeAndGetFeedUseCase = storeAndGetFeedUseCase,
      subscribeToPlayerUseCase = subscribeToPlayerUseCase,
      preparePlayerUseCase = preparePlayerUseCase,
      playPauseUseCase = playPauseUseCase
    )
    state = viewModel.podcastState

    state.testState {
      When("view model is created") {
        Then("initial podcast state should be Initial") {
          state.value `should be equal to` PodcastViewState.Initial
        }

        Then("subscribe to player use case should be invoked") {
          coVerify(exactly = 1) { subscribeToPlayerUseCase() }
        }

        Then("prepare player use case should be invoked with empty list") {
          verify { preparePlayerUseCase(emptyList()) }
        }

        Then("podcast state should be loading") {
          it.awaitItem().loading `should be equal to` true
        }
      }
    }

    Dispatchers.resetMain()
  }
})
