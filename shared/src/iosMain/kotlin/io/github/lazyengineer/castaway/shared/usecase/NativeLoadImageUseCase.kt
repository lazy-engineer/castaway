package io.github.lazyengineer.castaway.shared.usecase

import co.touchlab.stately.ensureNeverFrozen
import io.github.lazyengineer.castaway.shared.Image
import io.github.lazyengineer.castaway.shared.MainScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class NativeLoadImageUseCase : KoinComponent {

  private val loadImageUseCase: LoadImageUseCase by inject()
  private val coroutineScope = MainScope(Dispatchers.Main)

  init {
	ensureNeverFrozen()
  }

  fun run(
	url: String,
	onSuccess: (Image) -> Unit,
	onError: (String) -> Unit,
  ) {
	coroutineScope.launch {
	  loadImageUseCase(
		url,
		onSuccess = { onSuccess(it) },
		onError = { onError(it.message ?: "Error load Image") })
	}
  }

  fun onDestroy() {
	coroutineScope.onDestroy()
  }
}