package io.github.lazyengineer.castaway.shared

import io.github.lazyengineer.castaway.shared.native.Platform

class Greeting {

  fun greeting(): String {
	return "Hello, ${Platform().platform}!"
  }
}
