package jp.co.rakuten.oneapp.shared.entity

import kotlinx.serialization.Serializable

@Serializable
sealed class Wrapper() {
    abstract val item: Item

    @Serializable
    data class FavoriteWrapper(
        override val item: Item,
        val timestamp: String
    ) : Wrapper()

    @Serializable
    data class BookmarkWrapper(
        override val item: Item,
        val service: Item.ServiceDetails,
        val timestamp: String
    ) : Wrapper()

    @Serializable
    data class HistoryWrapper(
        override val item: Item,
        val timestamp: String
    ) : Wrapper()
}


fun Wrapper.FavoriteWrapper.toApiFavoriteWrapper() = DiscoveryApiWrapper.ApiFavoriteWrapper(
    item = item.toApiItem(),
    timestamp = timestamp
)

fun Wrapper.BookmarkWrapper.toApiBookmarkWrapper() = DiscoveryApiWrapper.ApiBookmarkWrapper(
    item = item.toApiItem(),
    service = service.toApiServiceDetail(),
    timestamp = timestamp
)

fun Wrapper.HistoryWrapper.toApiHistoryWrapper() = DiscoveryApiWrapper.ApiHistoryWrapper(
    item = item.toApiItem(),
    timestamp = timestamp
)

// DTO
@Serializable
sealed class DiscoveryApiWrapper {
    abstract val item: ApiItem

    @Serializable
    data class ApiFavoriteWrapper(
        override val item: ApiItem,
        val timestamp: String
    ) : DiscoveryApiWrapper()

    data class ApiBookmarkWrapper(
        override val item: ApiItem,
        val service: ApiItem.ApiRakutenServiceInfo,
        val timestamp: String
    ) : DiscoveryApiWrapper()

    data class ApiHistoryWrapper(
        override val item: ApiItem,
        val timestamp: String
    ) : DiscoveryApiWrapper()
}

// DTO to ViewModel
fun DiscoveryApiWrapper.ApiFavoriteWrapper.toFavoriteWrapper() = Wrapper.FavoriteWrapper(
    item = item.toItem(),
    timestamp = timestamp
)

fun DiscoveryApiWrapper.ApiBookmarkWrapper.toBookmarkWrapper() = Wrapper.BookmarkWrapper(
    item = item.toItem(service.toServiceDetails()),
    service = service.toServiceDetails(),
    timestamp = timestamp
)

fun DiscoveryApiWrapper.ApiHistoryWrapper.toHistoryWrapper() = Wrapper.HistoryWrapper(
    item = item.toItem(),
    timestamp = timestamp
)
