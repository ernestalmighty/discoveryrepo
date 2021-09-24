package jp.co.rakuten.oneapp.shared

import jp.co.rakuten.oneapp.shared.local.DiscoveryViewModel
import jp.co.rakuten.oneapp.shared.local.getWebInstance
import kotlinx.browser.document
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.w3c.dom.events.Event

/**
 * main function for JavaScript
 */
fun main(vararg args: String) {
    //nothing here, it's executed before DOM is ready
    println("main() web called") //this is written to JavaScript browser's developer console
    document.addEventListener("DOMContentLoaded", {
        println("DOMContentLoaded")
        document.getElementById("startButton")?.addEventListener("click", ::start)
    })
}

/**
 * We start this function from <button onClick="
 */
fun start(ev: Event) {
    MainScope().launch {
        val shared = DiscoveryViewModel.Factory
            .getWebInstance()
            .localRepository
            .fetchFeedTypes("OAuth2 IgABNGYWkHjm0S_ODC8GNKHC-Hw5m5Gm3UjT_H6z1KnwgVULuq7")

        console.log(shared.first().title)
    }
}