package jp.co.rakuten.oneapp.shared

import jp.co.rakuten.oneapp.shared.local.DiscoveryViewModel
import jp.co.rakuten.oneapp.shared.local.getInstance
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.w3c.dom.events.Event

/**
 * main function for JavaScript
 */
@JsExport
fun main(vararg args: String) {
    //nothing here, it's executed before DOM is ready
}

@JsExport
fun getEvent(event: Event) {
    getServices()
}

@JsExport
fun getServices() {
    console.log("getServices start")

    MainScope().launch {
        console.log("inside main scope")
        val shared = DiscoveryViewModel.Factory
            .getInstance().repository

        console.log("fetchFeedTypes")
        val result1 = shared.fetchFeedTypes("OAuth2 IQABNIjGtAoR_IrP7vJsp5Ui3tVWg1zCHooePMWvmGTzGdgEF2G")
        result1.forEach {
            console.log(it.title)
        }

        console.log("fetchData")
        val result2 = shared.fetchData("OAuth2 IQABNIjGtAoR_IrP7vJsp5Ui3tVWg1zCHooePMWvmGTzGdgEF2G", "top")
        result2.forEach {
            console.log(it.componentType.name)
        }

        console.log("fetchData")
        val result3 = shared.fetchMiniApps("OAuth2 IQABNIjGtAoR_IrP7vJsp5Ui3tVWg1zCHooePMWvmGTzGdgEF2G", "top", "1.0")
        result3.forEach {
            console.log(it.title)
        }
    }

    console.log("getServices end")
}