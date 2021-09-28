package jp.co.rakuten.oneapp.shared.local

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver

fun DiscoveryViewModel.Factory.getInstance(context : Context) : DiscoveryViewModel {
    val sqlDriver = AndroidSqliteDriver(DiscoveryDatabase.Schema, context, "Local.db")
    val repository = Repository(sqlDriver)
    return DiscoveryViewModel(repository)
}