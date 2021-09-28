package jp.co.rakuten.oneapp.shared.local

import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

fun DiscoveryViewModel.Factory.getInstance(): DiscoveryViewModel {
    val sqlDriver = NativeSqliteDriver(DiscoveryDatabase.Schema, "DiscoveryDatabase.db")
    val repository = Repository(sqlDriver)
    return DiscoveryViewModel(repository)
}