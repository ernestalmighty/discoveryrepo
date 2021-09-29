package jp.co.rakuten.oneapp.shared.entity

import jp.co.rakuten.oneapp.shared.entity.Item.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class MainScreenType(val title: String, val queryTag: String, val ratTag: String, val hasSeeMore: Boolean)

sealed class Component(val componentType: ComponentType)
enum class ComponentType {
    FAVORITE_SERVICES,
    BANNER,
    SERVICES_HISTORY,
    MINI_APPS,
    CONTENT_FEED,
    RECOMMENDED_SERVICES,
    RAKUTEN_SERVICES,
    BIG_CARDS,
    ALBUM,
    CAROUSEL,
    PARTNERS,
    GAMES
}

enum class MiniAppType {
    MINI_APP,
    GAME
}

interface HasTrackingInfo {
    val trackingInfo: TrackingInfo
}

@Serializable
sealed class Item: HasItemTrackingInfo {

    @SerialName("itemId")
    abstract val id: String
    @SerialName("itemtype")
    abstract val itemType: String

    // Feed Items
    @Serializable
    @SerialName("mini_app")
    data class MiniApp(
        override val id: String,
        override val itemType: String,
        val title: String,
        val imageUrl: String,
        val versionId: String,
        val versionTag: String,
        val isSaved: Boolean = false,
        val miniAppType: MiniAppType,
        override val trackingInfo: ItemTrackingInfo
    ) : Item()

    @Serializable
    @SerialName("content_feed_item")
    data class ContentFeedItem(
        override val id: String,
        override val itemType: String,
        val title: String,
        val subtitle: String,
        val imageUrl: String,
        val link: Links,
        override val trackingInfo: ItemTrackingInfo,
        val isSaved: Boolean = false,
        val service: ServiceDetails
    ) : Item()

    @Serializable
    data class ServiceDetails(
        override val id: String,
        override val itemType: String,
        val title: String,
        val subtitle: String? = null, // Only used in Recommended services to retrieve subtitle for Partner items
        val imageUrl: String,
        override val historyImage: String,
        val link: Links,
        val tags: List<String>?,
        override val trackingInfo: ItemTrackingInfo,
        val isSaved: Boolean = false
    ) : Item(), HasHistory

    @Serializable
    @SerialName("big_card_item")
    data class CardItem(
        override val id: String,
        override val itemType: String,
        val title: String,
        val subtitle: String,
        val description: String,
        val imageUrl: String,
        val link: Links,
        override val trackingInfo: ItemTrackingInfo,
        val isSaved: Boolean = false,
        val service: ServiceDetails
    ) : Item()

    @Serializable
    @SerialName("track")
    data class AlbumItem(
        override val id: String,
        override val itemType: String,
        val title: String,
        val subtitle: String,
        val imageUrl: String,
        val link: Links,
        override val trackingInfo: ItemTrackingInfo
    ) : Item()

    @Serializable
    @SerialName("ticket")
    data class CarouselItem(
        override val id: String,
        override val itemType: String,
        val title: String,
        val imageUrl: String,
        val link: Links,
        override val trackingInfo: ItemTrackingInfo,
        val isSaved: Boolean = false,
        val service: ServiceDetails
    ) : Item()

    @Serializable
    @SerialName("rebates_partner")
    data class PartnerItem(
        override val id: String,
        override val itemType: String,
        val title: String,
        val subtitle: String,
        val imageUrl: String,
        override val historyImage: String,
        val link: Links,
        override val trackingInfo: ItemTrackingInfo,
        val isSaved: Boolean = false
    ) : Item(), HasHistory

    @Serializable
    @SerialName("banner")
    data class BannerItem(
        override val id: String,
        val title: String,
        val description: String,
        val imageUrl: String = "",
        val link: Links,
        override val itemType: String,
        override val trackingInfo: ItemTrackingInfo,
    ) : Item()
}

interface HasHistory {
    val historyImage: String
}

interface HasItemTrackingInfo {
    val trackingInfo: ItemTrackingInfo
}

// Top level components
data class FavoriteServices(
    val title: String,
    val favoriteServices: List<FavoriteServiceDetails>
) : Component(ComponentType.FAVORITE_SERVICES)

data class ServicesHistory(
    val title: String,
    val services: List<ServiceHistoryDetails>
) : Component(ComponentType.SERVICES_HISTORY)

data class MiniApps(
    val title: String,
    val seeMoreTag: String,
    val miniApps: List<MiniApp>,
    override val trackingInfo: TrackingInfo
) : Component(ComponentType.MINI_APPS), HasTrackingInfo

data class ContentFeed(
    val title: String,
    val service: ServiceDetails,
    val feedItems: List<ContentFeedItem>,
    override val trackingInfo: TrackingInfo
) : Component(ComponentType.CONTENT_FEED), HasTrackingInfo

data class RecommendedServices(
    val title: String,
    val servicesCategories: List<ServiceCategory>,
    override val trackingInfo: TrackingInfo
) : Component(ComponentType.RECOMMENDED_SERVICES), HasTrackingInfo

data class RakutenServices(
    val title: String,
    val link: Links,
    val services: List<ServiceDetails>,
    override val trackingInfo: TrackingInfo
) : Component(ComponentType.RAKUTEN_SERVICES), HasTrackingInfo

data class BigCards(
    val cards: List<CardItem>,
    override val trackingInfo: TrackingInfo
) : Component(ComponentType.BIG_CARDS), HasTrackingInfo

data class Album(
    val title: String,
    val service: ServiceDetails,
    val items: List<AlbumItem>,
    val seeMore: Links,
    override val trackingInfo: TrackingInfo
) : Component(ComponentType.ALBUM), HasTrackingInfo

data class Carousel(
    val title: String,
    val service: ServiceDetails,
    val items: List<CarouselItem>,
    override val trackingInfo: TrackingInfo
) : Component(ComponentType.CAROUSEL), HasTrackingInfo

data class Partners(
    val title: String,
    val subtitle: String,
    val seeMoreTag: String,
    val serviceHistoryImage: String,
    val link: Links,
    val partners: List<PartnerItem>,
    override val trackingInfo: TrackingInfo,
    val service: ServiceDetails
) : Component(ComponentType.PARTNERS), HasTrackingInfo

data class ServiceCategory(
    val title: String,
    override val trackingInfo: TrackingInfo,
    val services: List<ServiceDetails>
) : HasTrackingInfo

data class TimeLimitedBanner(
    val title: String,
    val autoScrollable: Boolean,
    val banners: List<BannerItem>,
    override val trackingInfo: TrackingInfo,
) : Component(ComponentType.BANNER), HasTrackingInfo

data class Games(
    val id: String,
    val title: String,
    val seeMoreTag: String,
    val type: String,
    val miniApps: List<MiniApp>,
    override val trackingInfo: TrackingInfo
) : Component(ComponentType.GAMES), HasTrackingInfo

// Item Components
@Serializable
data class Links(
    val webUrl: String,
    val appLinkAndroid: String?,
    val appLinkIos: String?,
    val type: String?,
    val link: String?
)

data class TrackingInfo(
    val trackingTag: String,
    val recommendationItemId: List<String>,
    val rpl: String,
    val ratName: List<String>
)

@Serializable
data class ItemTrackingInfo(
    val trackingTag: String = "",
    val recommendationItemId: String = "",
    val rpl: String,
    val ratName: String
)

// Saved Items
data class FavoriteServiceDetails(
    val title: String,
    val imageUrl: String,
    val link: Links,
    override val historyImage: String,
    val item: Item,
    val timestamp: String
) : HasHistory, HasItemTrackingInfo by item

data class BookmarkDetails(
    val title: String,
    val imageUrl: String,
    val serviceName: String,
    val serviceImageUrl: String,
    val link: Links,
    val item: Item,
    val timestamp: String
) : HasItemTrackingInfo by item

data class ServiceHistoryDetails(
    val title: String,
    val link: Links,
    override val historyImage: String,
    val item: Item,
    val timestamp: String
) : HasHistory, HasItemTrackingInfo by item
