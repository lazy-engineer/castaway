package io.github.lazyengineer.castaway.shared.usecase

import io.github.lazyengineer.castaway.shared.Image
import io.github.lazyengineer.castaway.shared.common.MainScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class NativeLoadImageUseCase(
  private val loadImageUseCase: LoadImageUseCase
) {

  val coroutineScope = MainScope(Dispatchers.Main)

  fun subscribe(
	url: String,
	scope: CoroutineScope,
	onSuccess: (Image) -> Unit,
	onError: (String) -> Unit,
  ) = loadImageUseCase(url).subscribe(scope, onSuccess, onError)

  fun onDestroy() {
	coroutineScope.onDestroy()
  }
}
