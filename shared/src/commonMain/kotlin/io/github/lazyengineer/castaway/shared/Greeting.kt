package io.github.lazyengineer.castaway.shared

class Greeting {

  fun greeting(): String {
	return "Hello, ${Platform().platform}!"
  }
}
