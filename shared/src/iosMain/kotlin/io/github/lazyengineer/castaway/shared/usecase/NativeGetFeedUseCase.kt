package io.github.lazyengineer.castaway.shared.usecase

import co.touchlab.stately.ensureNeverFrozen
import io.github.lazyengineer.castaway.shared.MainScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class NativeGetFeedUseCase : KoinComponent {
    private val getFeedUseCase: GetFeedUseCase by inject()
    private val coroutineScope = MainScope(Dispatchers.Main)

    init {
        ensureNeverFrozen()
    }

    fun run(
        url: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
    ) {
        coroutineScope.launch {
            getFeedUseCase(
                url,
                onSuccess = { onSuccess(it) },
                onError = { onError(it.message ?: "Error fetch") })
        }
    }

    fun onDestroy() {
        coroutineScope.onDestroy()
    }
}
