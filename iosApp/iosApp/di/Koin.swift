import Foundation
import shared

func initKoin() {
    let koinApplication = KoinKt.doInitKoin()
    _koin = koinApplication.koin
}

private var _koin: Koin_coreKoin? = nil
var koin: Koin_coreKoin {
    return _koin!
}
