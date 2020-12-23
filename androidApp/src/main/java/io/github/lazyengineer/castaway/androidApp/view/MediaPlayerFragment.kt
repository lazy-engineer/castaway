package io.github.lazyengineer.castaway.androidApp.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import io.github.lazyengineer.castaway.androidApp.ArgumentsKt
import io.github.lazyengineer.castaway.androidApp.R.drawable
import io.github.lazyengineer.castaway.androidApp.databinding.FragmentMediaPlayerBinding
import io.github.lazyengineer.castaway.androidApp.entity.Episode
import io.github.lazyengineer.castaway.androidApp.entity.PlaybackPosition
import io.github.lazyengineer.castaway.androidApp.viewmodel.MainViewModel
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.MINUTES

class MediaPlayerFragment : Fragment() {

	private val viewModel: MainViewModel by activityViewModels()

	private val binding: FragmentMediaPlayerBinding by lazy { FragmentMediaPlayerBinding.inflate(layoutInflater) }
	private val episode: Episode by lazy { ArgumentsKt.fromBundle(arguments) }
	private var seekBarUpdateEnabled: Boolean = true

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		val seekBarHandler = Handler(Looper.getMainLooper())
		observeEpisodeUpdates(seekBarHandler)

		binding.episodeSeekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
			override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
				playbackPositionTxt(progress.toLong())
			}

			override fun onStartTrackingTouch(seekBar: SeekBar?) {
				seekBarUpdateEnabled = false
			}

			override fun onStopTrackingTouch(seekBar: SeekBar?) {
				seekBarUpdateEnabled = true
				seekBar?.progress?.let {
					viewModel.seekTo(it.toLong())
				}
			}
		})

		observeCurrentEpisode()
		observeFeed()
		loadEpisodeImage(episode.imageUrl)
		initPlayButton()
		initPlaybackSpeed()
		initMediaControls()
		return binding.root
	}

	private fun observeFeed() {
		viewModel.feed.observe(viewLifecycleOwner, { feed ->
			val nowPlayingEpisode = viewModel.currentEpisode.value
			if (nowPlayingEpisode != null) {
				val feedEpisode = feed.episodes.first {
					nowPlayingEpisode.id == it.id
				}

				binding.playButton.setImageResource(playbackResourceId(feedEpisode.isPlaying))
			} else {
				binding.playButton.setImageResource(playbackResourceId(episode.isPlaying))
			}
		})
	}

	private fun observeCurrentEpisode() {
		viewModel.currentEpisode.observe(viewLifecycleOwner, { episode ->
			binding.playButton.setImageResource(playbackResourceId(episode.isPlaying))
			loadEpisodeImage(episode.imageUrl)
		})
	}

	private fun observeEpisodeUpdates(seekBarHandler: Handler) {
		viewModel.updatedEpisodes.observe(viewLifecycleOwner, { episodes ->
			if (seekBarUpdateEnabled) {
				val currentEpisode = episodes.first()
				playbackPositionTxt(currentEpisode.playbackPosition.position)
				seekBarPlaybackPosition(seekBarHandler, currentEpisode.playbackPosition)
			}
		})
	}

	private fun seekBarPlaybackPosition(
		seekBarHandler: Handler,
		playbackPosition: PlaybackPosition,
	) {
		seekBarHandler.post {
			binding.episodeSeekbar.max = playbackPosition.duration!!.toInt()
			binding.episodeSeekbar.progress = playbackPosition.position.toInt()
		}
	}

	private fun playbackPositionTxt(positionMilliSeconds: Long) {
		binding.episodePositionTxt.text = String.format(
			"%02d:%02d",
			MILLISECONDS.toMinutes(positionMilliSeconds),
			MILLISECONDS.toSeconds(positionMilliSeconds) -
					MINUTES.toSeconds(MILLISECONDS.toMinutes(positionMilliSeconds))
		)
	}

	private fun initPlayButton() {
		if (episode.isPlaying) {
			binding.playButton.setImageResource(drawable.ic_pause_circle_filled)
		}
	}

	private fun loadEpisodeImage(imageUrl: String?) {
		binding.itemImage.scaleType = ImageView.ScaleType.FIT_CENTER
		binding.itemImage.load(imageUrl) {
			placeholder(drawable.ic_crop_original)
			error(drawable.ic_crop_original)
		}
	}

	private fun initPlaybackSpeed() {
		val playbackSpeed1 = "1x"
		val playbackSpeed2 = "1.5x"
		val playbackSpeed3 = "2x"

		binding.episodePlaybackSpeed.text = playbackSpeed1

		binding.episodePlaybackSpeed.setOnClickListener {
			when (binding.episodePlaybackSpeed.text) {
				playbackSpeed1 -> {
					binding.episodePlaybackSpeed.text = playbackSpeed2
					viewModel.playbackSpeed(1.5f)
				}
				playbackSpeed2 -> {
					binding.episodePlaybackSpeed.text = playbackSpeed3
					viewModel.playbackSpeed(2f)
				}
				playbackSpeed3 -> {
					binding.episodePlaybackSpeed.text = playbackSpeed1
					viewModel.playbackSpeed(1f)
				}
			}
		}
	}

	private fun initMediaControls() {
		binding.playButton.setOnClickListener {
			val nowPlayingEpisode = viewModel.currentEpisode.value
			if (nowPlayingEpisode != null) {
				viewModel.mediaItemClicked(nowPlayingEpisode)
			} else {
				viewModel.mediaItemClicked(episode)
			}
		}

		binding.forwardButton.setOnClickListener {
			viewModel.forwardCurrentItem()
		}

		binding.replayButton.setOnClickListener {
			viewModel.replayCurrentItem()
		}

		binding.previousButton.setOnClickListener {
			viewModel.skipToPrevious()
		}

		binding.nextButton.setOnClickListener {
			viewModel.skipToNext()
		}
	}

	private fun playbackResourceId(playing: Boolean): Int {
		return when {
			playing -> drawable.ic_pause_circle_filled
			else -> drawable.ic_play_circle_filled
		}
	}

	companion object {

		@JvmStatic
		fun newInstance(episode: Episode) =
			MediaPlayerFragment().apply {
				arguments = episode.toBundle()
			}
	}
}
