package io.github.lazyengineer.castaway.androidApp.view.nowplaying

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import io.github.lazyengineer.castaway.androidApp.player.FastForwardPlaybackUseCase
import io.github.lazyengineer.castaway.androidApp.player.PlayPauseUseCase
import io.github.lazyengineer.castaway.androidApp.player.PlaybackSpeedUseCase
import io.github.lazyengineer.castaway.androidApp.player.PlayerStateUseCase
import io.github.lazyengineer.castaway.androidApp.player.PreparePlayerUseCase
import io.github.lazyengineer.castaway.androidApp.player.RewindPlaybackUseCase
import io.github.lazyengineer.castaway.androidApp.player.SeekToUseCase
import io.github.lazyengineer.castaway.androidApp.view.mock.FakeMediaServiceClient
import io.github.lazyengineer.castaway.androidApp.view.mock.MockData
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEpisode.Companion.toEpisode
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEpisode.Companion.toNowPlayingEpisode
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.EditPlaybackSpeed
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.EditingPlayback
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.EpisodeLoaded
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.FastForward
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.ObservePlayer
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.PlayPause
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.Rewind
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.SeekTo
import io.github.lazyengineer.castaway.data.database.LocalFeedDataSource
import io.github.lazyengineer.castaway.data.repository.FeedRepository
import io.github.lazyengineer.castaway.data.webservice.RemoteFeedDataSource
import io.github.lazyengineer.castaway.domain.common.StateReducerFlow
import io.github.lazyengineer.castaway.domain.repository.FeedDataSource
import io.github.lazyengineer.castaway.domain.usecase.GetStoredEpisodesUseCase
import io.github.lazyengineer.castaway.domain.usecase.SaveEpisodeUseCase
import io.github.lazyengineer.castawayplayer.MediaServiceClient
import io.github.lazyengineer.castawayplayer.config.MediaServiceConfig
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.shouldBe
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

@OptIn(ExperimentalCoroutinesApi::class)
class NowPlayingViewModelTest : BehaviorSpec({

  val mediaServiceConfig = MediaServiceConfig(fastForwardInterval = 3_000, rewindInterval = 3_000)
  val fakeMediaServiceClient = FakeMediaServiceClient(mediaServiceConfig)

  val mockRemoteDataSource = mock<RemoteFeedDataSource>()
  val mockLocalDataSource = mock<LocalFeedDataSource>()
  val feedDataSource = FeedRepository(remoteDataSource = mockRemoteDataSource, localDataSource = mockLocalDataSource)

  lateinit var viewModel: NowPlayingViewModel
  lateinit var state: StateReducerFlow<NowPlayingState, NowPlayingEvent>

  Given("now playing viewModel is initialized") {

	Dispatchers.setMain(StandardTestDispatcher())

	createViewModel(feedDataSource, fakeMediaServiceClient).also {
	  viewModel = it
	  state = it.nowPlayingState
	}

	Then("state should be the initial state") {
	  state.value shouldBe NowPlayingState.Initial
	}

	When("the player state is observed") {
	  state.handleEvent(ObservePlayer)

	  Then("the loading state should be true") {
		state.value.loading shouldBe true
	  }
	}

	Dispatchers.resetMain()
  }

  Given("initialized viewModel and episode with id") {
	Dispatchers.setMain(StandardTestDispatcher())

	val episode = MockData.episode().toNowPlayingEpisode()
	val episodeId = episode.id

	createViewModel(feedDataSource, fakeMediaServiceClient).also {
	  viewModel = it
	  state = it.nowPlayingState
	}

	state.testState {
	  When("player is prepared and start observing") {
		PreparePlayerUseCase(fakeMediaServiceClient).invoke(listOf(episode.toEpisode()))
		state.handleEvent(ObservePlayer)

		Then("first state should be the initial state") {
		  it.awaitItem() shouldBe NowPlayingState.Initial
		}
	  }

	  When("episode is loaded") {

		state.handleEvent(EpisodeLoaded(episode.toEpisode()))

		Then("uiState should have loaded episode") {
		  it.awaitItem().episode `should be equal to` episode
		}

		And("PlayPause event is triggered") {
		  state.handleEvent(PlayPause(episodeId))

		  Then("uiState should enter the buffering state") {
			it.awaitItem().buffering `should be equal to` true
		  }

		  Then("uiState should leave the buffering state after buffering is ready") {
			it.awaitItem().buffering `should be equal to` false
		  }

		  Then("uiState should enter the playing state after episode is updated") {
			it.awaitItem() `should be equal to` NowPlayingState(
			  loading = false,
			  playing = true,
			  buffering = false,
			  episode = episode
			)
		  }

		  And("PlayPause event is triggered the second time") {
			state.handleEvent(PlayPause(episodeId))

			Then("uiState should enter the buffering state") {
			  it.awaitItem().buffering `should be equal to` true
			}

			Then("uiState should leave the buffering state after buffering is ready") {
			  it.awaitItem().buffering `should be equal to` false
			}

			Then("uiState should enter the pause state after episode is updated") {
			  it.awaitItem() `should be equal to` NowPlayingState(
				loading = false,
				playing = false,
				buffering = false,
				episode = episode
			  )
			}
		  }
		}

		And("PlayPause event is triggered for 5 seconds") {
		  val expectedPlaybackPosition = 5_000L
		  state.handleEvent(PlayPause(episodeId))
		  delay(expectedPlaybackPosition + 1)

		  Then("uiState should updated with playback position after 5 seconds") {
			it.expectMostRecentItem() `should be equal to` NowPlayingState(
			  loading = false,
			  playing = true,
			  buffering = false,
			  episode = episode.copy(playbackPosition = episode.playbackPosition.copy(position = expectedPlaybackPosition))
			)
		  }

		  And("PlayPause event is triggered the second time") {
			state.handleEvent(PlayPause(episode.id))
			delay(1)

			Then("uiState should enter the pause state after episode is updated with playback progress at point of pausing") {
			  it.expectMostRecentItem() `should be equal to` NowPlayingState(
				loading = false,
				playing = false,
				buffering = false,
				episode = episode.copy(playbackPosition = episode.playbackPosition.copy(position = expectedPlaybackPosition))
			  )
			}

			Then("verify episode saved every second staying in playing state") {
			  assertSoftly {
				verify(mockLocalDataSource).saveEpisode(episode.copy(playbackPosition = episode.playbackPosition.copy(position = 1_000)).toEpisode())
				verify(mockLocalDataSource).saveEpisode(episode.copy(playbackPosition = episode.playbackPosition.copy(position = 2_000)).toEpisode())
				verify(mockLocalDataSource).saveEpisode(episode.copy(playbackPosition = episode.playbackPosition.copy(position = 3_000)).toEpisode())
				verify(mockLocalDataSource).saveEpisode(episode.copy(playbackPosition = episode.playbackPosition.copy(position = 4_000)).toEpisode())
				verify(mockLocalDataSource).saveEpisode(episode.copy(playbackPosition = episode.playbackPosition.copy(position = 5_000)).toEpisode())
				verifyNoMoreInteractions(mockLocalDataSource)
			  }
			}
		  }
		}

		And("SeekTo event is triggered") {
		  val expectedPosition = 42_000L
		  state.handleEvent(SeekTo(expectedPosition))

		  Then("uiState should enter the buffering state") {
			it.awaitItem().buffering `should be equal to` true
		  }

		  Then("uiState be updated with playback position sought to after buffering is ready") {
			it.awaitItem() `should be equal to` NowPlayingState(
			  loading = false,
			  playing = false,
			  buffering = false,
			  episode = episode.copy(playbackPosition = episode.playbackPosition.copy(position = expectedPosition))
			)
		  }

		  And("SeekTo event is triggered with playback position 0") {
			state.handleEvent(SeekTo(0))

			Then("uiState should enter the buffering state") {
			  it.awaitItem().buffering `should be equal to` true
			}

			Then("uiState be updated with playback position of 0") {
			  it.awaitItem() `should be equal to` NowPlayingState(
				loading = false,
				playing = false,
				buffering = false,
				episode = episode.copy(playbackPosition = episode.playbackPosition.copy(position = 0))
			  )
			}
		  }
		}

		And("FastForward event is triggered") {
		  state.handleEvent(FastForward)

		  Then("uiState should enter the buffering state") {
			it.awaitItem().buffering `should be equal to` true
		  }

		  Then("uiState be updated with playback position fast forwarded to after buffering is ready") {
			it.awaitItem() `should be equal to` NowPlayingState(
			  loading = false,
			  playing = false,
			  buffering = false,
			  episode = episode.copy(
				playbackPosition = episode.playbackPosition.copy(
				  position = mediaServiceConfig.fastForwardInterval
				)
			  )
			)
		  }

		  And("FastForward event is triggered second time with playback position is near to the end of the episode") {
			val expectedPositionAtTheEnd = episode.playbackPosition.duration
			state.handleEvent(SeekTo(expectedPositionAtTheEnd - 1))
			state.handleEvent(FastForward)

			Then("uiState should enter the buffering state") {
			  it.awaitItem().buffering `should be equal to` true
			}

			Then("uiState be updated with playback safePosition fast forwarded to the end of the episode and not more") {
			  it.awaitItem().episode?.playbackPosition?.safePosition `should be equal to` expectedPositionAtTheEnd
			}
		  }
		}

		And("Rewind event is triggered") {
		  val expectedPosition = 1L
		  state.handleEvent(SeekTo(mediaServiceConfig.rewindInterval + expectedPosition))

		  state.handleEvent(Rewind)

		  Then("uiState should enter the buffering state") {
			it.awaitItem().buffering `should be equal to` true
		  }

		  Then("uiState be updated with playback position rewound to after buffering is ready") {
			it.awaitItem() `should be equal to` NowPlayingState(
			  loading = false,
			  playing = false,
			  buffering = false,
			  episode = episode.copy(
				playbackPosition = episode.playbackPosition.copy(
				  position = expectedPosition
				)
			  )
			)
		  }

		  And("Rewind event is triggered second time") {
			state.handleEvent(Rewind)

			Then("uiState should enter the buffering state") {
			  it.awaitItem().buffering `should be equal to` true
			}

			Then("uiState be updated with playback safePosition rewound that is not negative") {
			  it.awaitItem().episode?.playbackPosition?.safePosition `should be equal to` 0
			}
		  }
		}

		And("EditPlaybackSpeed event is triggered") {
		  val expectedPlaybackSpeed = 2f
		  state.handleEvent(EditPlaybackSpeed(expectedPlaybackSpeed))

		  Then("uiState should update with playback speed") {
			it.awaitItem() `should be equal to` NowPlayingState(
			  loading = false,
			  playbackSpeed = expectedPlaybackSpeed,
			  episode = episode
			)
		  }
		}

		And("EditingPlayback event is triggered with true") {
		  state.handleEvent(EditingPlayback(true))

		  Then("uiState should update with editing state true") {
			it.awaitItem().editing `should be equal to` true
		  }

		  And("PlayPause event is triggered that triggers player state changes") {
			state.handleEvent(PlayPause(episodeId))

			Then("uiState should enter the buffering state") {
			  it.awaitItem().buffering `should be equal to` true
			}

			Then("uiState should enter the playing state") {
			  it.awaitItem().playing `should be equal to` true
			}

			Then("no more uiState updates should be emitted") {
			  it.expectNoEvents()
			}

			And("EditingPlayback event is triggered again with false") {
			  state.handleEvent(EditingPlayback(false))

			  Then("uiState should update with editing state false") {
				it.awaitItem().editing `should be equal to` false
			  }

			  Then("uiState updates should be emitted again") {
				it.awaitItem().playing `should be equal to` true
			  }
			}
		  }
		}
	  }
	}

	Dispatchers.resetMain()
  }
})

private fun createViewModel(feedDataSource: FeedDataSource, fakeMediaServiceClient: MediaServiceClient) = NowPlayingViewModel(
  getStoredEpisodesUseCase = GetStoredEpisodesUseCase(feedDataSource),
  playerStateUseCase = PlayerStateUseCase(fakeMediaServiceClient),
  playPauseUseCase = PlayPauseUseCase(fakeMediaServiceClient),
  saveEpisodeUseCase = SaveEpisodeUseCase(feedDataSource),
  fastForwardPlaybackUseCase = FastForwardPlaybackUseCase(fakeMediaServiceClient),
  seekToUseCase = SeekToUseCase(fakeMediaServiceClient),
  rewindPlaybackUseCase = RewindPlaybackUseCase(fakeMediaServiceClient),
  playbackSpeedUseCase = PlaybackSpeedUseCase(fakeMediaServiceClient),
)

private fun StateReducerFlow<NowPlayingState, NowPlayingEvent>.testState(test: suspend (ReceiveTurbine<NowPlayingState>) -> Unit) {
  runTest {
	this@testState.test {
	  test(this)
	}
  }
}
