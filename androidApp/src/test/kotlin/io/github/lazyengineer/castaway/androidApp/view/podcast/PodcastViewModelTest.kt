package io.github.lazyengineer.castaway.androidApp.view.podcast

import io.github.lazyengineer.castaway.androidApp.player.PlayPauseUseCase
import io.github.lazyengineer.castaway.androidApp.player.PlayerStateUseCase
import io.github.lazyengineer.castaway.androidApp.player.PreparePlayerUseCase
import io.github.lazyengineer.castaway.androidApp.player.SubscribeToPlayerUseCase
import io.github.lazyengineer.castaway.androidApp.view.mock.MockData.podcastEpisode
import io.github.lazyengineer.castaway.androidApp.view.podcast.PodcastEvent.FeedEvent
import io.github.lazyengineer.castaway.androidApp.view.podcast.PodcastViewModel.Companion.TEST_URL
import io.github.lazyengineer.castaway.domain.common.StateReducerFlow
import io.github.lazyengineer.castaway.domain.usecase.GetStoredFeedUseCase
import io.github.lazyengineer.castaway.domain.usecase.StoreAndGetFeedUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

//@OptIn(ExperimentalCoroutinesApi::class)
//class PodcastViewModelTest : BehaviorSpec({
//
//  val getStoredFeedUseCase: GetStoredFeedUseCase = mockk()
//  val storeAndGetFeedUseCase: StoreAndGetFeedUseCase = mockk()
//  val subscribeToPlayerUseCase: SubscribeToPlayerUseCase = mockk()
//  val preparePlayerUseCase: PreparePlayerUseCase = mockk()
//  val playerStateUseCase: PlayerStateUseCase = mockk()
//  val playPauseUseCase: PlayPauseUseCase = mockk()
//
//  lateinit var viewModel: PodcastViewModel
//  lateinit var state: StateReducerFlow<PodcastViewState, PodcastEvent>
//
//  beforeTest {
//  }
//
//  Given("PodcastViewModel is initialized") {
//	Dispatchers.setMain(StandardTestDispatcher())
//
//	viewModel = PodcastViewModel(
//	  getStoredFeedUseCase = getStoredFeedUseCase,
//	  storeAndGetFeedUseCase = storeAndGetFeedUseCase,
//	  subscribeToPlayerUseCase = subscribeToPlayerUseCase,
//	  preparePlayerUseCase = preparePlayerUseCase,
//	  playerStateUseCase = playerStateUseCase,
//	  playPauseUseCase = playPauseUseCase
//	)
//	state = viewModel.podcastState
//
//	When("view model is created") {
//	  Then("initial podcast state should be Initial") {
//
//	  }
//	}
//
//	When("open screen for first time") {
//	  state.handleEvent(FeedEvent.Load(TEST_URL))
//	  Then("show loading state") {
//
//	  }
//	}
//
//	When("initial load feed") {
//	  val expectedState = PodcastViewState(
//		loading = false,
//		episodes = EpisodesList(items = listOf(podcastEpisode()))
//	  )
//	  state.handleEvent(FeedEvent.Load(TEST_URL))
//	  Then("show loaded feed") {
//
//	  }
//	}
//  }
//
//  Dispatchers.resetMain()
//})

//class PodcastViewModelTest {
//
//  @get:Rule
//  val dispatcherRule = MainDispatcherRule()
//
//  private val fakeMediaServiceClient = FakeMediaServiceClient()
//
//  private val mockFeedParser = mock<FeedParser> {
//	whenever(mock.parseFeed(any(), any())).thenReturn(feedData())
//  }
//
//  private val remoteFeedDataSource = mock<RemoteFeedDataSource>()
//  private val localFeedDataSource = mock<LocalFeedDataSource> {
//	runTest {
//	  whenever(mock.loadFeed(any())).thenReturn(DataResult.Success(feedData()))
//	  whenever(mock.saveEpisode(any())).thenReturn(DataResult.Success(episode()))
//	  whenever(mock.episodeFlow(any<String>())).thenReturn(flowOf(listOf(episode())))
//	}
//  }
//
//  private val dataSource = FeedRepository(
//	remoteDataSource = remoteFeedDataSource,
//	localDataSource = localFeedDataSource
//  )
//
//  private val getStoredFeedUseCase = GetStoredFeedUseCase(dataSource)
//  private val storeAndGetFeedUseCase = StoreAndGetFeedUseCase(dataSource, mockFeedParser)
//  private val saveEpisodeUseCase = SaveEpisodeUseCase(dataSource)
//
//  private val subscribeToPlayerUseCase = SubscribeToPlayerUseCase(fakeMediaServiceClient)
//  private val preparePlayerUseCase = PreparePlayerUseCase(fakeMediaServiceClient)
//  private val playerStateUseCase = PlayerStateUseCase(fakeMediaServiceClient)
//  private val playPauseUseCase = PlayPauseUseCase(fakeMediaServiceClient)
//
//  private lateinit var viewModel: PodcastViewModel
//  private lateinit var state: StateReducerFlow<PodcastViewState, PodcastEvent>
//
//  @Before
//  fun setup() {
//	viewModel = PodcastViewModel(
//	  getStoredFeedUseCase = getStoredFeedUseCase,
//	  storeAndGetFeedUseCase = storeAndGetFeedUseCase,
//	  saveEpisodeUseCase = saveEpisodeUseCase,
//	  subscribeToPlayerUseCase = subscribeToPlayerUseCase,
//	  preparePlayerUseCase = preparePlayerUseCase,
//	  playerStateUseCase = playerStateUseCase,
//	  playPauseUseCase = playPauseUseCase
//	)
//	state = viewModel.podcastState
//  }
//
//  @Test
//  fun `initial podcast state`() {
//	state.value `should be equal to` PodcastViewState.Initial
//  }
//
//  @Test
//  fun `WHEN open screen for first time THEN show loading state`() = runTest {
//	state.handleEvent(FeedEvent.Load(TEST_URL))
//
//	state.test {
//	  awaitItem().loading `should be` true
//	}
//  }
//
//  @Test
//  fun `WHEN initial load feed THEN show loaded feed`() = runTest {
//	val expectedState = PodcastViewState(
//	  loading = false,
//	  episodes = listOf(podcastEpisode())
//	)
//
//	state.test {
//	  state.handleEvent(FeedEvent.Load(TEST_URL))
//
//	  awaitItem().loading `should be` true
//	  awaitItem() `should be equal to` expectedState
//	}
//  }
//
//  @Test
//  fun `WHEN loaded feed force update THEN feed should loading`() = runTest {
//	val expectedState = PodcastViewState(
//	  loading = false,
//	  episodes = listOf(podcastEpisode())
//	)
//
//	state.test {
//	  skipUntilLoaded()
//
//	  state.handleEvent(FeedEvent.Load(TEST_URL, forceUpdate = true))
//
//	  awaitItem().loading `should be` true
//	  awaitItem() `should be equal to` expectedState
//	}
//  }
//
//  @Test
//  fun `WHEN click play episode THEN show episode buffering`() = runTest {
//	val podcastEpisode = podcastEpisode()
//	val expectedState = PodcastViewState(
//	  loading = false,
//	  episodes = listOf(podcastEpisode.copy(buffering = true))
//	)
//
//	state.test {
//	  skipInitialState()
//
//	  state.handleEvent(FeedEvent.Load(TEST_URL))
//	  skipItems(1)
//
//	  state.handleEvent(EpisodeRowEvent.PlayPause(episode().id))
//	  awaitItem() `should be equal to` expectedState
//	}
//  }
//
//  @Test
//  fun `WHEN click play episode THEN show episode buffering but other do not`() = runTest {
//	val podcastEpisode = podcastEpisode()
//	val clickedEpisodeId = episode().id
//	val otherEpisodeId = "other id"
//
//	val givenFeedData = feedData().copy(episodes = listOf(episode(), episode().copy(id = otherEpisodeId)))
//	whenever(localFeedDataSource.loadFeed(any())).thenReturn(DataResult.Success(givenFeedData))
//
//	val expectedState = PodcastViewState(
//	  loading = false,
//	  episodes = listOf(
//		podcastEpisode.copy(id = clickedEpisodeId, buffering = true),
//		podcastEpisode.copy(id = otherEpisodeId, buffering = false)
//	  )
//	)
//
//	state.test {
//	  skipInitialState()
//
//	  state.handleEvent(FeedEvent.Load(TEST_URL))
//	  skipItems(1)
//
//	  state.handleEvent(EpisodeRowEvent.PlayPause(clickedEpisodeId))
//
//	  awaitItem() `should be equal to` expectedState
//	}
//  }
//
//  @Test
//  fun `WHEN episode buffering ready THEN show episode playing`() = runTest {
//	val podcastEpisode = podcastEpisode()
//	val expectedState = PodcastViewState(
//	  loading = false,
//	  episodes = listOf(podcastEpisode.copy(buffering = false, playing = true))
//	)
//
//	state.test {
//	  skipUntilLoaded()
//
//	  state.handleEvent(EpisodeRowEvent.PlayPause(episode().id))
//	  skipItems(1)
//
//	  awaitItem() `should be equal to` expectedState
//	}
//  }
//
//  @Test
//  fun `WHEN playing episode clicked THEN show episode paused`() = runTest {
//	val podcastEpisode = podcastEpisode()
//	val expectedState = PodcastViewState(
//	  loading = false,
//	  episodes = listOf(podcastEpisode.copy(buffering = false, playing = false))
//	)
//
//	state.test {
//	  skipUntilLoaded()
//
//	  state.handleEvent(EpisodeRowEvent.PlayPause(episode().id))
//	  skipItems(2) // Skip RowPlayPause episode.id and RowPlaying episode.id
//
//	  state.handleEvent(EpisodeRowEvent.PlayPause(episode().id))
//	  skipItems(1) // Skip RowPlayPause episode.id
//
//	  awaitItem() `should be equal to` expectedState
//	}
//  }
//
//  @Test
//  fun `WHEN other episode clicked THEN playing episode pauses and other episode playing`() = runTest {
//	val podcastEpisode = podcastEpisode()
//	val playingEpisodeId = episode().id
//	val clickedOtherEpisodeId = "other id"
//
//	val givenFeedData = feedData().copy(episodes = listOf(episode(), episode().copy(id = clickedOtherEpisodeId)))
//	whenever(localFeedDataSource.loadFeed(any())).thenReturn(DataResult.Success(givenFeedData))
//
//	val expectedState = PodcastViewState(
//	  loading = false,
//	  episodes = listOf(
//		podcastEpisode.copy(id = playingEpisodeId, buffering = false, playing = false),
//		podcastEpisode.copy(id = clickedOtherEpisodeId, buffering = false, playing = true)
//	  )
//	)
//
//	state.test {
//	  skipUntilLoaded(givenFeedData)
//
//	  state.handleEvent(EpisodeRowEvent.PlayPause(playingEpisodeId))
//	  skipItems(2) // Skip RowPlayPause playingEpisodeId and RowPlaying playingEpisodeId
//
//	  state.handleEvent(EpisodeRowEvent.PlayPause(clickedOtherEpisodeId))
//	  skipItems(1) // Skip RowPlayPause clickedOtherEpisodeId
//
//	  awaitItem() `should be equal to` expectedState
//	}
//  }
//
//  @Test
//  fun `WHEN row clicked THEN show episode details`() = runTest {
//	val podcastEpisode = podcastEpisode()
//
//	state.test {
//	  skipUntilLoaded()
//
//	  state.handleEvent(EpisodeRowEvent.ShowDetails(podcastEpisode))
//
//	  awaitItem().showDetails `should be equal to` podcastEpisode
//	}
//  }
//
//  @Test
//  fun `WHEN episode playing THEN show playback progress`() = runTest {
//	val expectedPlaybackPosition = 5_000L
//	val podcastEpisode = podcastEpisode()
//	val expectedState = PodcastViewState(
//	  loading = false,
//	  episodes = listOf(
//		podcastEpisode.copy(
//		  buffering = false,
//		  playing = true,
//		  playbackPosition = expectedPlaybackPosition
//		)
//	  )
//	)
//
//	state.test {
//	  skipUntilLoaded()
//
//	  state.handleEvent(EpisodeRowEvent.PlayPause(episode().id))
//	  delay(expectedPlaybackPosition + 1)
//
//	  expectMostRecentItem() `should be equal to` expectedState
//	  cancelAndIgnoreRemainingEvents()
//	}
//  }
//
//  @Test
//  fun `WHEN episode paused THEN show playback progress at point of pausing`() = runTest {
//	val expectedPlaybackPosition = 5_000L
//	val podcastEpisode = podcastEpisode()
//	val expectedState = PodcastViewState(
//	  loading = false,
//	  episodes = listOf(
//		podcastEpisode.copy(
//		  buffering = false,
//		  playing = false,
//		  playbackPosition = expectedPlaybackPosition
//		)
//	  )
//	)
//
//	state.test {
//	  skipUntilLoaded()
//
//	  state.handleEvent(EpisodeRowEvent.PlayPause(episode().id))
//	  delay(expectedPlaybackPosition)
//	  state.handleEvent(EpisodeRowEvent.PlayPause(episode().id))
//	  delay(1)
//
//	  expectMostRecentItem() `should be equal to` expectedState
//	  cancelAndIgnoreRemainingEvents()
//	}
//  }
//
//  @Test
//  fun `WHEN episode with playback progress THEN verify episode saved every second`() = runTest {
//	val playEpisodeTillMillis = 5_000L
//	val podcastEpisode = podcastEpisode()
//
//	state.test {
//	  skipUntilLoaded()
//
//	  state.handleEvent(EpisodeRowEvent.PlayPause(episode().id))
//	  delay(playEpisodeTillMillis + 1)
//
//	  verify(localFeedDataSource).saveEpisode(podcastEpisode.copy(playbackPosition = 1_000).toEpisode())
//	  verify(localFeedDataSource).saveEpisode(podcastEpisode.copy(playbackPosition = 2_000).toEpisode())
//	  verify(localFeedDataSource).saveEpisode(podcastEpisode.copy(playbackPosition = 3_000).toEpisode())
//	  verify(localFeedDataSource).saveEpisode(podcastEpisode.copy(playbackPosition = 4_000).toEpisode())
//	  verify(localFeedDataSource).saveEpisode(podcastEpisode.copy(playbackPosition = 5_000).toEpisode())
//	  verifyNoMoreInteractions(localFeedDataSource)
//	  cancelAndIgnoreRemainingEvents()
//	}
//  }
//
//  @Test
//  fun `WHEN context 'play' clicked THEN show episode playing`() = runTest {
//	// To be defined
//  }
//
//  @Test
//  fun `WHEN context 'to playlist' THEN show choose playlist`() = runTest {
//	// To be defined
//  }
//
//  @Test
//  fun `WHEN context 'download' clicked THEN download episode`() = runTest {
//	// To be defined
//  }
//
//  @Test
//  fun `WHEN context 'archive' clicked THEN archive episode`() = runTest {
//	// To be defined
//  }
//
//  @Test
//  fun `WHEN context 'mark as' clicked THEN show mark as dialog`() = runTest {
//	// To be defined
//  }
//
//  @Test
//  fun `WHEN context 'to queue' clicked THEN add episode to queue`() = runTest {
//	// To be defined
//  }
//
//  @Test
//  fun `WHEN context 'to favorites' clicked THEN add episode to favorites`() = runTest {
//	// To be defined
//  }
//
//  @Test
//  fun `WHEN context 'share' clicked THEN show share menu`() = runTest {
//	// To be defined
//  }
//
//  private suspend fun ReceiveTurbine<PodcastViewState>.skipInitialState() {
//	skipItems(1)
//  }
//
//  private suspend fun ReceiveTurbine<PodcastViewState>.skipUntilLoaded(feedData: FeedData = feedData()) {
//	skipInitialState()
//
//	state.handleEvent(Loaded(feedData))
//	skipItems(1)
//  }
//}
