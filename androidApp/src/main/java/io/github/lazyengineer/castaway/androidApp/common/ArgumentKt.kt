package io.github.lazyengineer.castaway.androidApp

import android.os.Bundle
import android.os.Parcelable

interface ArgumentsKt : Parcelable {

	fun toBundle(key: String = KEY_ARGUMENTS): Bundle {
		val b = Bundle()
		addToBundle(b, key)
		return b
	}

	fun addToBundle(b: Bundle, key: String = KEY_ARGUMENTS) {
		b.putParcelable(key, this)
		b.putString("$key.$KEY_TYPE", this.javaClass.canonicalName)
	}

	companion object {

		const val KEY_ARGUMENTS = "Arguments"
		const val KEY_TYPE = "Type"

		fun <T : ArgumentsKt> fromBundle(b: Bundle?): T {
			return fromBundle(b, KEY_ARGUMENTS)
		}

		@Suppress("UnsafeCallOnNullableType")
		fun <T : ArgumentsKt> fromBundle(b: Bundle?, key: String): T {
			b?.classLoader = ArgumentsKt::class.java.classLoader
			return b?.getParcelable<T>(key)!!
		}
	}
}