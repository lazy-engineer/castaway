package io.github.lazyengineer.castaway.domain.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

fun <STATE, EVENT> ViewModel.stateReducerFlow(
  initialState: STATE,
  reduceState: (STATE, EVENT) -> STATE,
): StateReducerFlow<STATE, EVENT> = stateReducerFlow(initialState, reduceState, viewModelScope)
