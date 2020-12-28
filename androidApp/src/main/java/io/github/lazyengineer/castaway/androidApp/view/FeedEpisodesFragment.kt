package io.github.lazyengineer.castaway.androidApp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.lazyengineer.castaway.androidApp.databinding.FragmentFeedEpisodesBinding
import io.github.lazyengineer.castaway.androidApp.view.ChannelItemAdapter.OnItemClickListener
import io.github.lazyengineer.castaway.androidApp.view.ChannelItemAdapter.ViewHolder
import io.github.lazyengineer.castaway.androidApp.viewmodel.MainViewModel
import io.github.lazyengineer.castaway.shared.entity.Episode
import io.github.lazyengineer.castaway.shared.entity.FeedData

class FeedEpisodesFragment : Fragment(), OnItemClickListener {

	private val viewModel: MainViewModel by activityViewModels()
	private val binding: FragmentFeedEpisodesBinding by lazy { FragmentFeedEpisodesBinding.inflate(layoutInflater) }

	private lateinit var episodesAdapter: ChannelItemAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		initAdapter()

		binding.fetchFeedBtn.setOnClickListener {
			viewModel.fetchFeed()
		}

		viewModel.feed.observe(viewLifecycleOwner, { feed ->
			hideButton()
			showFeed(feed)
		})

		viewModel.updatedEpisodes.observe(viewLifecycleOwner, { episodes ->
			episodes.forEach { episode ->
				updateProgressBar(episode)
			}
		})

		return binding.root
	}

	private fun initAdapter() {
		binding.feedList.layoutManager = LinearLayoutManager(activity)
		episodesAdapter = ChannelItemAdapter(this)
		binding.feedList.adapter = episodesAdapter
	}

	private fun hideButton() {
		binding.fetchFeedBtn.visibility = View.GONE
	}

	private fun showFeed(feed: FeedData) {
		binding.feedList.visibility = View.VISIBLE
		showList(feed.episodes)
	}

	private fun showList(episodes: List<Episode>) {
		episodesAdapter.setData(episodes)
	}

	private fun updateProgressBar(episode: Episode) {
		episode.playbackPosition.percentage?.let { percentage ->
			val index = episode.index()
			if (index != -1) {
				val viewHolder = binding.feedList.findViewHolderForAdapterPosition(index)
				viewHolder?.let { (it as ViewHolder).progressBar.progress = percentage.toInt() }
			}
		}
	}

	private fun Episode.index(): Int {
		return episodesAdapter.items.indexOfFirst {
			this.id == it.id
		}
	}

	override fun onItemClick(item: Episode) {
		viewModel.episodeClicked(item)
	}

	override fun onPlayClick(item: Episode) {
		viewModel.mediaItemClicked(item.id)
	}

	companion object {

		@JvmStatic
		fun newInstance() = FeedEpisodesFragment()
	}
}