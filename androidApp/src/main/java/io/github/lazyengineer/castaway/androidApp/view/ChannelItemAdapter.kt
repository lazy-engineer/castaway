package io.github.lazyengineer.castaway.androidApp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import io.github.lazyengineer.castaway.androidApp.R.drawable
import io.github.lazyengineer.castaway.androidApp.databinding.FeedItemBinding
import io.github.lazyengineer.castaway.androidApp.view.ChannelItemAdapter.ViewHolder
import io.github.lazyengineer.castaway.shared.entity.Episode

class ChannelItemAdapter(private val clickListener: OnItemClickListener) :
  RecyclerView.Adapter<ViewHolder>() {

  interface OnItemClickListener {

	fun onItemClick(item: Episode)
	fun onPlayClick(item: Episode)
  }

  private var itemList: MutableList<Episode> = mutableListOf()
  val items: List<Episode>
	get() = itemList

  fun setData(episodes: List<Episode>) {
	itemList = episodes.toMutableList()
	notifyDataSetChanged()
  }

  fun updateDataSeparately(episodes: List<Episode>) {
	episodes.forEach { episode ->
	  val index = itemList.indexOfFirst {
		episode.id == it.id
	  }
	  if (index != -1) {
		itemList[index] = episode
		notifyItemChanged(index)
	  }
	}
  }

  override fun getItemCount(): Int = itemList.size

  override fun onBindViewHolder(
	holder: ViewHolder,
	position: Int
  ) = holder.bind(itemList[position])

  override fun onCreateViewHolder(
	parent: ViewGroup,
	viewType: Int
  ): ViewHolder {
	val inflater = LayoutInflater.from(parent.context)
	val binding = FeedItemBinding.inflate(inflater, parent, false)
	return ViewHolder(binding)
  }

  inner class ViewHolder(private val binding: FeedItemBinding) : RecyclerView.ViewHolder(binding.root) {

	val progressBar: ProgressBar = binding.progressBar

	fun bind(item: Episode) {
	  binding.itemTitle.text = item.title
	  binding.playItem.setImageResource(playbackResourceId(item.isPlaying))

	  progressBar.progress = ((item.playbackPosition.position / item.playbackPosition.duration) * 100).toInt()

	  binding.playItem.setOnClickListener {
		clickListener.onPlayClick(item)
	  }

	  binding.feedItemCard.setOnClickListener {
		clickListener.onItemClick(item)
	  }
	}

	private fun playbackResourceId(playing: Boolean): Int {
	  return when {
		playing -> drawable.ic_pause
		else -> drawable.ic_play_arrow
	  }
	}
  }
}
