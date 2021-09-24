package jp.co.rakuten.oneapp.shared.local

import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

fun DiscoveryViewModel.Factory.getiOSInstance(): DiscoveryViewModel {
    val sqlDriver = NativeSqliteDriver(DiscoveryDatabase.Schema, "DiscoveryDatabase.db")
    val repository = LocalRepository(sqlDriver)
    return DiscoveryViewModel(repository)
}