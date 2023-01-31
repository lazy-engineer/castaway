package io.github.lazyengineer.castaway.androidApp.acceptancetest

import io.github.lazyengineer.castaway.androidApp.player.FastForwardPlaybackUseCase
import io.github.lazyengineer.castaway.androidApp.player.PlayPauseUseCase
import io.github.lazyengineer.castaway.androidApp.player.PlaybackSpeedUseCase
import io.github.lazyengineer.castaway.androidApp.player.PlayerStateUseCase
import io.github.lazyengineer.castaway.androidApp.player.PreparePlayerUseCase
import io.github.lazyengineer.castaway.androidApp.player.RewindPlaybackUseCase
import io.github.lazyengineer.castaway.androidApp.player.SeekToUseCase
import io.github.lazyengineer.castaway.androidApp.util.Scenario
import io.github.lazyengineer.castaway.androidApp.util.StateFlowTurbineViewRobot
import io.github.lazyengineer.castaway.androidApp.view.mock.FakeMediaServiceClient
import io.github.lazyengineer.castaway.androidApp.view.mock.MockData.episode
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEpisode.Companion.toEpisode
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEpisode.Companion.toNowPlayingEpisode
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.EditPlaybackPosition
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.EditPlaybackSpeed
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.EpisodeLoaded
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.FastForward
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.ObservePlayer
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.PlayPause
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.Rewind
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.SeekTo
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingState
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingViewModel
import io.github.lazyengineer.castaway.data.database.LocalFeedDataSource
import io.github.lazyengineer.castaway.data.repository.FeedRepository
import io.github.lazyengineer.castaway.data.webservice.RemoteFeedDataSource
import io.github.lazyengineer.castaway.domain.common.StateReducerFlow
import io.github.lazyengineer.castaway.domain.entity.common.DataResult
import io.github.lazyengineer.castaway.domain.repository.FeedDataSource
import io.github.lazyengineer.castaway.domain.usecase.GetStoredEpisodesUseCase
import io.github.lazyengineer.castaway.domain.usecase.SaveEpisodeUseCase
import io.github.lazyengineer.castawayplayer.MediaServiceClient
import io.github.lazyengineer.castawayplayer.config.MediaServiceConfig
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.BehaviorSpec
import kotlinx.coroutines.test.StandardTestDispatcher
import org.amshove.kluent.`should be equal to`
import org.mockito.kotlin.any
import org.mockito.kotlin.clearInvocations
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalKotest::class)
class PlaybackPlayerAcceptanceTest : BehaviorSpec({

  val mediaServiceConfig = MediaServiceConfig(fastForwardInterval = 3_000, rewindInterval = 3_000)
  var fakeMediaServiceClient = FakeMediaServiceClient(mediaServiceConfig)

  val mockRemoteDataSource = mock<RemoteFeedDataSource>()
  val mockLocalDataSource = mock<LocalFeedDataSource>()
  val feedDataSource = FeedRepository(remoteDataSource = mockRemoteDataSource, localDataSource = mockLocalDataSource)

  lateinit var viewModel: NowPlayingViewModel
  lateinit var state: StateReducerFlow<NowPlayingState, NowPlayingEvent>

  afterTest {
	clearInvocations(
	  mockRemoteDataSource,
	  mockLocalDataSource,
	)
	fakeMediaServiceClient = FakeMediaServiceClient(mediaServiceConfig)
  }

  Given("the app starts") {
	Scenario("Observing player status for the first time") {
	  createViewModel(feedDataSource, fakeMediaServiceClient).also {
		viewModel = it
		state = it.nowPlayingState
	  }

	  val robot = StateFlowTurbineViewRobot(
		scheduler = StandardTestDispatcher().scheduler,
		stateFlow = state,
	  )
	  Given("the mini playback player is opened by the user") {
		state.handleEvent(ObservePlayer)

		When("the player is in its initial state") {
		  robot.collect()

		  Then("the user should see a loading indication") {
			robot.listOfStates.last() `should be equal to` NowPlayingState.Initial.copy(loading = true)
		  }
		}
	  }
	}

	Scenario("Playing a new episode") {
	  createViewModel(feedDataSource, fakeMediaServiceClient).also {
		viewModel = it
		state = it.nowPlayingState
	  }

	  val robot = StateFlowTurbineViewRobot(
		scheduler = StandardTestDispatcher().scheduler,
		stateFlow = state,
	  )

	  Given("the mini playback player is open") {
		val episode = episode().toNowPlayingEpisode()
		PreparePlayerUseCase(fakeMediaServiceClient).invoke(listOf(episode.toEpisode()))
		state.handleEvent(ObservePlayer)
		state.handleEvent(EpisodeLoaded(episode.toEpisode()))

		When("a new episode is chosen to play") {
		  state.handleEvent(PlayPause(episode.id))
		  robot.collectMostRecentItem()

		  Then("the mini playback player should display details of the new episode") {
			robot.listOfStates.last().episode `should be equal to` episode.copy(
			  playbackPosition = episode.playbackPosition.copy(
				position = episode.playbackPosition.duration
			  )
			)
		  }
		  Then("the episode should start playing") {
			robot.listOfStates.last().playing `should be equal to` true
		  }
		}
	  }
	}

	Scenario("Toggling playback between play and pause") {
	  createViewModel(feedDataSource, fakeMediaServiceClient).also {
		viewModel = it
		state = it.nowPlayingState
	  }

	  val robot = StateFlowTurbineViewRobot(
		scheduler = StandardTestDispatcher().scheduler,
		stateFlow = state,
	  )

	  Given("an episode is playing on the mini playback player") {
		val episode = episode()
		PreparePlayerUseCase(fakeMediaServiceClient).invoke(listOf(episode))
		state.handleEvent(ObservePlayer)
		state.handleEvent(EpisodeLoaded(episode))
		state.handleEvent(PlayPause(episode.id))

		When("the user hits the play/pause button") {
		  state.handleEvent(PlayPause(episode.id))
		  robot.collectMostRecentItem()

		  Then("the episode playback should toggle between play and pause states") {
			robot.listOfStates.last().playing `should be equal to` false
		  }
		}
	  }
	}

	Scenario("Changing the playback speed") {
	  createViewModel(feedDataSource, fakeMediaServiceClient).also {
		viewModel = it
		state = it.nowPlayingState
	  }

	  val robot = StateFlowTurbineViewRobot(
		scheduler = StandardTestDispatcher().scheduler,
		stateFlow = state,
	  )
	  Given("the mini playback player is actively playing an episode") {
		val episode = episode()
		PreparePlayerUseCase(fakeMediaServiceClient).invoke(listOf(episode))
		state.handleEvent(ObservePlayer)
		state.handleEvent(EpisodeLoaded(episode))
		state.handleEvent(PlayPause(episode.id))

		When("the user changes the playback speed to ‘<speed>‘") {
		  state.handleEvent(EditPlaybackSpeed(2f))
		  robot.collectMostRecentItem()

		  Then("the playback speed of the episode should match '<speed>'") {
			robot.listOfStates.last().playbackSpeed `should be equal to` 2f
		  }
		}
	  }
	}

	Scenario("Modifying playback position") {
	  createViewModel(feedDataSource, fakeMediaServiceClient).also {
		viewModel = it
		state = it.nowPlayingState
	  }

	  val robot = StateFlowTurbineViewRobot(
		scheduler = StandardTestDispatcher().scheduler,
		stateFlow = state,
	  )

	  Given("an episode is paused on the mini playback player") {
		val episode = episode()
		val expectedPosition = episode.playbackPosition.duration - 1
		whenever(mockLocalDataSource.saveEpisode(any())).thenReturn(DataResult.Success(episode))
		PreparePlayerUseCase(fakeMediaServiceClient).invoke(listOf(episode))
		state.handleEvent(ObservePlayer)
		robot.collect()
		state.handleEvent(EpisodeLoaded(episode))
		robot.collect()

		When("the user adjusts the playback position") {
		  state.handleEvent(EditPlaybackPosition(expectedPosition))
		  robot.collect()

		  Then("the episode should be able continue playing from the new position") {
			robot.listOfStates.last().episode?.playbackPosition?.position `should be equal to` expectedPosition
		  }
		}
	  }
	}

	Scenario("Seeking to a specific position") {
	  createViewModel(feedDataSource, fakeMediaServiceClient).also {
		viewModel = it
		state = it.nowPlayingState
	  }

	  val robot = StateFlowTurbineViewRobot(
		scheduler = StandardTestDispatcher().scheduler,
		stateFlow = state,
	  )

	  Given("an episode is playing on the mini playback player") {
		val episode = episode()
		val expectedPosition = episode.playbackPosition.duration - 1
		PreparePlayerUseCase(fakeMediaServiceClient).invoke(listOf(episode))
		state.handleEvent(ObservePlayer)
		state.handleEvent(EpisodeLoaded(episode))

		When("the user seeks to a specific position") {
		  state.handleEvent(SeekTo(expectedPosition))
		  robot.collectMostRecentItem()

		  Then("the playback should jump to the chosen position") {
			robot.listOfStates.last().episode?.playbackPosition?.position `should be equal to` expectedPosition
		  }
		}
	  }
	}

	Scenario("Using fast-forward functionality") {
	  createViewModel(feedDataSource, fakeMediaServiceClient).also {
		viewModel = it
		state = it.nowPlayingState
	  }

	  val robot = StateFlowTurbineViewRobot(
		scheduler = StandardTestDispatcher().scheduler,
		stateFlow = state,
	  )

	  Given("an episode is paused on the mini playback player") {
		val episode = episode()
		PreparePlayerUseCase(fakeMediaServiceClient).invoke(listOf(episode))
		state.handleEvent(ObservePlayer)
		state.handleEvent(EpisodeLoaded(episode))

		When("the user hits the fast-forward button") {
		  state.handleEvent(FastForward)
		  robot.collectMostRecentItem()

		  Then("the playback should advance by the predefined interval") {
			robot.listOfStates.last().episode?.playbackPosition?.position `should be equal to` mediaServiceConfig.fastForwardInterval
		  }
		}
	  }
	}

	Scenario("Using rewind functionality") {
	  createViewModel(feedDataSource, fakeMediaServiceClient).also {
		viewModel = it
		state = it.nowPlayingState
	  }

	  val robot = StateFlowTurbineViewRobot(
		scheduler = StandardTestDispatcher().scheduler,
		stateFlow = state,
	  )

	  Given("an episode is paused on the mini playback player") {
		val episode = episode()
		PreparePlayerUseCase(fakeMediaServiceClient).invoke(listOf(episode))
		state.handleEvent(ObservePlayer)
		state.handleEvent(EpisodeLoaded(episode))

		When("the playback position is greater than the rewind interval") {
		  state.handleEvent(SeekTo(mediaServiceConfig.rewindInterval + 1))

		  And("the user hits the rewind button") {
			state.handleEvent(Rewind)
			robot.collectMostRecentItem()

			Then("the playback should go back by the predefined interval") {
			  robot.listOfStates.last().episode?.playbackPosition?.position `should be equal to` 1
			}
		  }
		}
	  }
	}

	Scenario("Handling errors during episode storage") {
	  createViewModel(feedDataSource, fakeMediaServiceClient).also {
		viewModel = it
		state = it.nowPlayingState
	  }

	  val robot = StateFlowTurbineViewRobot(
		scheduler = StandardTestDispatcher().scheduler,
		stateFlow = state,
	  )

	  Given("the mini playback player has an episode loaded") {
		When("there's an error storing the episode's playback details") {
		  xThen("the user should be notified of the storage error") {

		  }
		}
	  }
	}

	Scenario("Successful storage of episode details") {
	  createViewModel(feedDataSource, fakeMediaServiceClient).also {
		viewModel = it
		state = it.nowPlayingState
	  }

	  val robot = StateFlowTurbineViewRobot(
		scheduler = StandardTestDispatcher().scheduler,
		stateFlow = state,
	  )

	  Given("the mini playback player has an episode loaded") {
		When("the episode's playback details are stored successfully") {
		  xThen("the user's episode playback progress should be saved") {

		  }
		}
	  }
	}
  }
})

private fun createViewModel(
  feedDataSource: FeedDataSource,
  fakeMediaServiceClient: MediaServiceClient
) = NowPlayingViewModel(
  getStoredEpisodesUseCase = GetStoredEpisodesUseCase(feedDataSource),
  playerStateUseCase = PlayerStateUseCase(fakeMediaServiceClient),
  playPauseUseCase = PlayPauseUseCase(fakeMediaServiceClient),
  saveEpisodeUseCase = SaveEpisodeUseCase(feedDataSource),
  fastForwardPlaybackUseCase = FastForwardPlaybackUseCase(fakeMediaServiceClient),
  seekToUseCase = SeekToUseCase(fakeMediaServiceClient),
  rewindPlaybackUseCase = RewindPlaybackUseCase(fakeMediaServiceClient),
  playbackSpeedUseCase = PlaybackSpeedUseCase(fakeMediaServiceClient),
)
