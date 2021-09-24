package jp.co.rakuten.oneapp.shared

import jp.co.rakuten.oneapp.shared.local.DiscoveryViewModel
import jp.co.rakuten.oneapp.shared.local.getWebInstance
import kotlinx.browser.document
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.w3c.dom.events.Event

@JsExport
fun start() {
    MainScope().launch {
        val shared = DiscoveryViewModel.Factory
            .getWebInstance()
            .localRepository
            .fetchFeedTypes("OAuth2 IgABNGYWkHjm0S_ODC8GNKHC-Hw5m5Gm3UjT_H6z1KnwgVULuq7")

        console.log(shared.first().title)
    }
}