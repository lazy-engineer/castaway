package io.github.lazyengineer.castaway.androidApp.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import io.github.lazyengineer.castaway.androidApp.R.id
import io.github.lazyengineer.castaway.androidApp.databinding.ActivityMainBinding
import io.github.lazyengineer.castaway.androidApp.view.screen.StartScreen
import io.github.lazyengineer.castaway.androidApp.view.style.ThemeNeumorphism
import io.github.lazyengineer.castaway.androidApp.viewmodel.CastawayViewModel
import io.github.lazyengineer.castaway.shared.Greeting
import io.github.lazyengineer.castaway.shared.entity.FeedData
import org.koin.androidx.viewmodel.ext.android.viewModel

fun greet(): String {
  return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {

  private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
  private val viewModel: CastawayViewModel by viewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
	super.onCreate(savedInstanceState)
	setContent {
	  ThemeNeumorphism {
		StartScreen(viewModel)
	  }
	}

	binding.bottomNav.setOnNavigationItemSelectedListener { item ->
	  when (item.itemId) {
		id.nav_action_overview -> {
		  Toast.makeText(applicationContext, "Item 1", Toast.LENGTH_SHORT)
			.show()
		  true
		}
		id.nav_action_play_lists -> {
		  Toast.makeText(applicationContext, "Item 2", Toast.LENGTH_SHORT)
			.show()
		  true
		}

		id.nav_action_discover -> {
		  Toast.makeText(applicationContext, "Item 3", Toast.LENGTH_SHORT)
			.show()
		  true
		}

		id.nav_action_user -> {
		  Toast.makeText(applicationContext, "Item 4", Toast.LENGTH_SHORT)
			.show()
		  true
		}
		else -> false
	  }
	}

	viewModel.feed.observe(this, { feed ->
	  showFeed(feed)
	})

//	initFragment()
  }

  private fun initFragment() {
	replaceFragment(FeedEpisodesFragment.newInstance())
	viewModel.navigateToFragment.observe(this, {
	  replaceFragment(it)
	})
  }

  private fun replaceFragment(fragment: Fragment) {
	supportFragmentManager.commit {
	  replace(id.fragment_container, fragment)
	  setReorderingAllowed(true)
	  addToBackStack(fragment.tag)
	}
  }

  private fun showFeed(feed: FeedData) {
	binding.toolbar.title = feed.info.title
  }
}
