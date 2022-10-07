package io.github.lazyengineer.castaway.shared.di

import io.github.lazyengineer.castaway.data.di.coreModule
import io.github.lazyengineer.castaway.data.di.platformModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
  startKoin {
	appDeclaration()
	modules(platformModule(), coreModule())
  }

// called by iOS etc
fun initKoin() = initKoin {}
