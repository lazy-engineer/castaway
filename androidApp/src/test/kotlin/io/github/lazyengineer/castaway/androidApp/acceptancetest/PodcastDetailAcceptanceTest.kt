package io.github.lazyengineer.castaway.androidApp.acceptancetest

import io.github.lazyengineer.castaway.androidApp.player.PlayPauseUseCase
import io.github.lazyengineer.castaway.androidApp.player.PlayerStateUseCase
import io.github.lazyengineer.castaway.androidApp.player.PreparePlayerUseCase
import io.github.lazyengineer.castaway.androidApp.player.SubscribeToPlayerUseCase
import io.github.lazyengineer.castaway.androidApp.util.Scenario
import io.github.lazyengineer.castaway.androidApp.util.StateFlowTurbineViewRobot
import io.github.lazyengineer.castaway.androidApp.view.mock.FakeMediaServiceClient
import io.github.lazyengineer.castaway.androidApp.view.mock.MockData.feedData
import io.github.lazyengineer.castaway.androidApp.view.mock.MockData.podcastEpisode
import io.github.lazyengineer.castaway.androidApp.view.podcast.EpisodesList
import io.github.lazyengineer.castaway.androidApp.view.podcast.PodcastEvent
import io.github.lazyengineer.castaway.androidApp.view.podcast.PodcastEvent.EpisodeRowEvent
import io.github.lazyengineer.castaway.androidApp.view.podcast.PodcastEvent.EpisodeRowEvent.PlayPause
import io.github.lazyengineer.castaway.androidApp.view.podcast.PodcastEvent.FeedEvent.Load
import io.github.lazyengineer.castaway.androidApp.view.podcast.PodcastEvent.FeedEvent.Loaded
import io.github.lazyengineer.castaway.androidApp.view.podcast.PodcastViewModel
import io.github.lazyengineer.castaway.androidApp.view.podcast.PodcastViewState
import io.github.lazyengineer.castaway.data.database.LocalFeedDataSource
import io.github.lazyengineer.castaway.data.repository.FeedRepository
import io.github.lazyengineer.castaway.data.webservice.RemoteFeedDataSource
import io.github.lazyengineer.castaway.domain.common.StateReducerFlow
import io.github.lazyengineer.castaway.domain.entity.common.DataResult
import io.github.lazyengineer.castaway.domain.parser.FeedParser
import io.github.lazyengineer.castaway.domain.repository.FeedDataSource
import io.github.lazyengineer.castaway.domain.usecase.GetStoredFeedUseCase
import io.github.lazyengineer.castaway.domain.usecase.StoreAndGetFeedUseCase
import io.github.lazyengineer.castawayplayer.MediaServiceClient
import io.github.lazyengineer.castawayplayer.config.MediaServiceConfig
import io.kotest.assertions.assertSoftly
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.BehaviorSpec
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.shouldBe
import org.mockito.kotlin.any
import org.mockito.kotlin.clearInvocations
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalKotest::class)
class PodcastDetailAcceptanceTest : BehaviorSpec({

  val mediaServiceConfig = MediaServiceConfig(fastForwardInterval = 3_000, rewindInterval = 3_000)

  var fakeMediaServiceClient = FakeMediaServiceClient(mediaServiceConfig)
  val mockFeedParser = mock<FeedParser> {
    whenever(mock.parseFeed(any(), any())).thenReturn(feedData())
  }

  val mockRemoteDataSource = mock<RemoteFeedDataSource>()
  val mockLocalDataSource = mock<LocalFeedDataSource> {
    runTest {
      whenever(mock.saveFeedData(any(), any())).thenReturn(DataResult.Success(feedData()))
    }
  }

  val feedDataSource = FeedRepository(remoteDataSource = mockRemoteDataSource, localDataSource = mockLocalDataSource)

  lateinit var viewModel: PodcastViewModel
  lateinit var state: StateReducerFlow<PodcastViewState, PodcastEvent>

  afterTest {
    clearInvocations(
      mockFeedParser,
      mockRemoteDataSource,
      mockLocalDataSource,
    )
    fakeMediaServiceClient = FakeMediaServiceClient(mediaServiceConfig)
  }

  Given("the app starts") {
    Scenario("Loading a podcast details") {
      Given("PodcastViewModel is initialized") {
        createPodcastViewModel(
          feedDataSource = feedDataSource,
          fakeMediaServiceClient = fakeMediaServiceClient,
          mockFeedParser = mockFeedParser,
        ).also {
          viewModel = it
          state = it.podcastState
        }

        val robot = StateFlowTurbineViewRobot(
          scheduler = StandardTestDispatcher().scheduler,
          stateFlow = state,
        )

        When("the user requests to view the podcast") {
          whenever(mockRemoteDataSource.fetchFeed(feedData().info.url)).thenReturn(DataResult.Success(""))
          whenever(mockLocalDataSource.loadFeed(feedData().info.url)).thenReturn(DataResult.Error(Exception("Error loading podcast")))
          state.handleEvent(Load(feedData().info.url))
          robot.collect()

          Then("the podcast details should be fetched") {
            verify(mockRemoteDataSource).fetchFeed(feedData().info.url)
          }
          Then("the view state should display the loading indicator") {
            robot.listOfStates.first().loading shouldBe true
          }
        }
        When("the podcast is fetched successfully") {
          robot.collect()

          Then("the view state should display the podcast details") {
            assertSoftly {
              robot.listOfStates[1].episodes `should be equal to` EpisodesList(listOf(podcastEpisode()))
              robot.listOfStates[1].imageUrl `should be equal to` feedData().info.imageUrl.orEmpty()
            }
          }

          Then("the loading indicator should disappear") {
            robot.listOfStates[1].loading shouldBe false
          }
        }
      }
    }

    Scenario("Loading a podcast details fails") {
      val expectedError = Exception("Error fetching podcast")

      Given("PodcastViewModel is initialized") {
        createPodcastViewModel(
          feedDataSource = feedDataSource,
          fakeMediaServiceClient = fakeMediaServiceClient,
          mockFeedParser = mockFeedParser,
        ).also {
          viewModel = it
          state = it.podcastState
        }

        val robot = StateFlowTurbineViewRobot(
          scheduler = StandardTestDispatcher().scheduler,
          stateFlow = state,
        )

        When("the user requests to view the podcast") {
          whenever(mockRemoteDataSource.fetchFeed(feedData().info.url)).thenReturn(DataResult.Error(expectedError))
          whenever(mockLocalDataSource.loadFeed(feedData().info.url)).thenReturn(DataResult.Error(Exception("Error loading podcast")))
          state.handleEvent(Load(feedData().info.url))
          robot.collect()

          Then("the view state should display the loading indicator") {
            robot.listOfStates.first().loading shouldBe true
          }

          And("there's an error fetching the podcast") {
            robot.collect()

            Then("the view state should display an error message") {
              robot.listOfStates[1].error `should be equal to` expectedError.message
            }

            Then("the loading indicator should disappear") {
              robot.listOfStates[1].loading shouldBe false
            }
          }
        }
      }
    }

    Scenario("Selecting a podcast episode from the list") {
      createPodcastViewModel(
        feedDataSource = feedDataSource,
        fakeMediaServiceClient = fakeMediaServiceClient,
        mockFeedParser = mockFeedParser,
      ).also {
        viewModel = it
        state = it.podcastState
      }

      val robot = StateFlowTurbineViewRobot(
        scheduler = StandardTestDispatcher().scheduler,
        stateFlow = state,
      )

      Given("the podcast episode list is displayed") {
        state.handleEvent(Loaded(feedData()))
        robot.collect()
        robot.collect()

        When("the user selects a episode") {
          state.handleEvent(EpisodeRowEvent.ShowDetails(podcastEpisode()))
          robot.collect()

          Then("the view state should display to the episode detail view") {
            robot.listOfStates.last().showDetails `should be equal to` podcastEpisode()
          }
        }
      }
    }

    Scenario("Refreshing the podcast details") {
      createPodcastViewModel(
        feedDataSource = feedDataSource,
        fakeMediaServiceClient = fakeMediaServiceClient,
        mockFeedParser = mockFeedParser,
      ).also {
        viewModel = it
        state = it.podcastState
      }

      val robot = StateFlowTurbineViewRobot(
        scheduler = StandardTestDispatcher().scheduler,
        stateFlow = state,
      )

      Given("the podcast details is displayed") {
        whenever(mockRemoteDataSource.fetchFeed(feedData().info.url)).thenReturn(DataResult.Success(""))
        robot.collect()

        state.handleEvent(Loaded(feedData()))
        robot.collect()

        When("the user refreshes the podcast details") {

          state.handleEvent(Load(feedData().info.url, forceUpdate = true))
          robot.collect()

          Then("the podcast details should re-fetch") {
            verify(mockRemoteDataSource).fetchFeed(feedData().info.url)
          }

          Then("the view state should display the loading indicator") {
            robot.listOfStates.last().loading shouldBe true
          }
        }
        When("the refreshed podcast details is fetched successfully") {
          robot.collect()

          Then("the view state should display the updated podcast details") {
            assertSoftly {
              robot.listOfStates.last().episodes `should be equal to` EpisodesList(listOf(podcastEpisode()))
              robot.listOfStates.last().imageUrl `should be equal to` feedData().info.imageUrl.orEmpty()
            }
          }
          Then("the loading indicator should disappear") {
            robot.listOfStates.last().loading shouldBe false
          }
        }
      }
    }

    Scenario("Playing a podcast episode") {
      createPodcastViewModel(
        feedDataSource = feedDataSource,
        fakeMediaServiceClient = fakeMediaServiceClient,
        mockFeedParser = mockFeedParser,
      ).also {
        viewModel = it
        state = it.podcastState
      }

      val robot = StateFlowTurbineViewRobot(
        scheduler = StandardTestDispatcher().scheduler,
        stateFlow = state,
      )

      Given("the podcast detail view is displayed") {
        val feedData = feedData()
        whenever(mockRemoteDataSource.fetchFeed(feedData.info.url)).thenReturn(DataResult.Success(""))
        robot.collect()

        state.handleEvent(Loaded(feedData))
        robot.collect()

        When("the user clicks on the play button for an episode") {
          state.handleEvent(PlayPause(feedData.episodes.first().id))
          robot.collect()

          Then("the episode to play should be buffering") {
            robot.listOfStates.last().episodes.items.first().buffering shouldBe true
          }

          robot.collect()

          Then("the view state should update to show the episode as 'playing'") {
            robot.listOfStates.last().episodes.items.first().playing shouldBe true
          }
        }
      }
    }

    Scenario("Pausing a podcast episode") {
      createPodcastViewModel(
        feedDataSource = feedDataSource,
        fakeMediaServiceClient = fakeMediaServiceClient,
        mockFeedParser = mockFeedParser,
      ).also {
        viewModel = it
        state = it.podcastState
      }

      val robot = StateFlowTurbineViewRobot(
        scheduler = StandardTestDispatcher().scheduler,
        stateFlow = state,
      )

      Given("a podcast episode is playing") {
        val feedData = feedData()
        whenever(mockRemoteDataSource.fetchFeed(feedData.info.url)).thenReturn(DataResult.Success(""))
        robot.collect()

        state.handleEvent(Loaded(feedData))
        robot.collect()

        state.handleEvent(PlayPause(feedData.episodes.first().id))
        robot.collectMostRecentItem()

        When("the user clicks on the pause button") {
          state.handleEvent(PlayPause(feedData.episodes.first().id))
          robot.collect()

          Then("the episode to play should be buffering") {
            robot.listOfStates.last().episodes.items.first().buffering shouldBe true
          }

          robot.collect()

          Then("the view state should update to show the episode as 'paused'") {
            robot.listOfStates.last().episodes.items.first().playing shouldBe false
          }
        }
      }
    }

    Scenario("Subscribing to a podcast") {
      Given("the podcast detail view is displayed") {
        When("the user clicks on the subscribe button") {
          xThen("the PodcastViewModel should add the podcast to the user's subscriptions") {}
          xThen("the view state should update to show the podcast as 'subscribed'") {}
        }
      }
    }

    Scenario("Unsubscribing from a podcast") {
      Given("the user is subscribed to a podcast") {
        When("the user clicks on the unsubscribe button in the podcast detail view") {
          xThen("the PodcastViewModel should remove the podcast from the user's subscriptions") {}
          xThen("the view state should update to show the podcast as 'not subscribed'") {}
        }
      }
    }

    Scenario("Searching for a episode") {
      Given("the episode list is displayed") {
        When("the user inputs a search query") {
          xThen("the PodcastViewModel should search for episodes matching the query") {}
          xThen("the view state should display the loading indicator") {}
        }
        When("the search results are fetched successfully") {
          xThen("the view state should display the search results") {}
          xThen("the loading indicator should disappear") {}
        }
      }
    }
  }
})

fun createPodcastViewModel(feedDataSource: FeedDataSource, fakeMediaServiceClient: MediaServiceClient, mockFeedParser: FeedParser) =
  PodcastViewModel(
    getStoredFeedUseCase = GetStoredFeedUseCase(feedDataSource),
    storeAndGetFeedUseCase = StoreAndGetFeedUseCase(feedDataSource, mockFeedParser),
    subscribeToPlayerUseCase = SubscribeToPlayerUseCase(fakeMediaServiceClient),
    preparePlayerUseCase = PreparePlayerUseCase(fakeMediaServiceClient),
    playerStateUseCase = PlayerStateUseCase(fakeMediaServiceClient),
    playPauseUseCase = PlayPauseUseCase(fakeMediaServiceClient),
  )
