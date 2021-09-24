package jp.co.rakuten.oneapp.shared.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

const val KEY_COMPONENT_TYPE = "type"

@Serializable
data class ApiFeedType(
    val title: String,
    val feedType: String,
    val showOnDiscoveryTab: Boolean,
    val showOnSeeMoreScreen: Boolean
)

@Serializable
data class ApiFeed(val feed: List<ApiComponent>)

@Serializable
sealed class ApiComponent

@Serializable
sealed class ApiItem {
    abstract val id: String

    @Serializable
    @SerialName("content_feed_item")
    data class ApiContentFeedItem(
        override val id: String,
        val title: String,
        val subtitle: String,
        val imageUrl: String,
        val link: ApiLinks,
        val trackingInfo: ApiItemTrackingInfo
    ) : ApiItem()

    @Serializable
    @SerialName("rakuten_service")
    data class ApiRakutenServiceInfo(
        override val id: String,
        val title: String,
        val subtitle: String? = null, // Only used in Recommended services to retrieve subtitle for Partner items
        val imageUrl: String,
        val historyImage: String,
        val link: ApiLinks,
        val tags: String?,
        val trackingInfo: ApiItemTrackingInfo
    ) : ApiItem()

    @Serializable
    @SerialName("big_card_item")
    data class ApiCard(
        override val id: String,
        val title: String,
        val subtitle: String,
        val description: String,
        val imageUrl: String,
        val link: ApiLinks,
        val trackingInfo: ApiItemTrackingInfo
    ) : ApiItem()

    @Serializable
    @SerialName("track")
    data class ApiAlbumItem(
        override val id: String,
        val title: String,
        val subtitle: String,
        val imageUrl: String,
        val link: ApiLinks,
        val trackingInfo: ApiItemTrackingInfo
    ) : ApiItem()

    @Serializable
    @SerialName("mini_app")
    data class ApiMiniApp(
        override val id: String,
        val title: String,
        val imageUrl: String,
        val versionId: String?,
        val versionTag: String?,
        val trackingInfo: ApiItemTrackingInfo
    ) : ApiItem()

    @Serializable
    @SerialName("game_mini_app")
    data class ApiGameMiniApp(
        override val id: String,
        val title: String,
        val imageUrl: String,
        val versionId: String?,
        val versionTag: String?,
        val trackingInfo: ApiItemTrackingInfo
    ) : ApiItem()

    @Serializable
    @SerialName("ticket")
    data class ApiCarouselItem(
        override val id: String,
        val title: String,
        val imageUrl: String,
        val link: ApiLinks,
        val trackingInfo: ApiItemTrackingInfo
    ) : ApiItem()

    @Serializable
    @SerialName("rebates_partner")
    data class ApiPartner(
        override val id: String,
        val title: String,
        val subtitle: String,
        val imageUrl: String,
        val historyImage: String,
        val link: ApiLinks,
        val trackingInfo: ApiItemTrackingInfo
    ) : ApiItem()

    @Serializable
    @SerialName("banner")
    data class ApiBannerItem(
        override val id: String,
        val title: String,
        val description: String,
        val imageUrl: String,
        val link: ApiLinks,
        val trackingInfo: ApiItemTrackingInfo
    ) : ApiItem()
}

object ApiUnknownComponent : ApiComponent()

@Serializable
@SerialName("favorites")
data class ApiFavoriteServices(
    val title: String
) : ApiComponent()

@Serializable
@SerialName("service_history")
data class ApiServicesHistory(
    val title: String
) : ApiComponent()

@Serializable
@SerialName("content_feed")
data class ApiContentFeed(
    val title: String,
    val service: ApiServiceDetail,
    val items: List<ApiItem.ApiContentFeedItem>,
    val trackingInfo: ApiTrackingInfo
) : ApiComponent()

@Serializable
@SerialName("recommended_services")
data class ApiRecommendedServices(
    val title: String,
    val serviceCategories: List<ApiRecommendedServiceCategory>
) : ApiComponent()

@Serializable
data class ApiRecommendedServiceCategory(
    val title: String,
    val services: List<ApiItem.ApiRakutenServiceInfo>,
    val trackingInfo: ApiTrackingInfo
)

@Serializable
@SerialName("rakuten_services")
data class ApiRakutenServices(
    val title: String,
    val seeMoreTag: String,
    val link: ApiLinks,
    val services: List<ApiItem.ApiRakutenServiceInfo>,
    val trackingInfo: ApiTrackingInfo
) : ApiComponent()

@Serializable
@SerialName("big_card")
data class ApiBigCard(
    val service: ApiServiceDetail,
    val cards: List<ApiItem.ApiCard>,
    val trackingInfo: ApiTrackingInfo
) : ApiComponent()

@Serializable
@SerialName("album")
data class ApiAlbum(
    val title: String,
    val service: ApiServiceDetail,
    val items: List<ApiItem.ApiAlbumItem>,
    val seeMoreLink: ApiSeeMoreLink,
    val trackingInfo: ApiTrackingInfo
) : ApiComponent()

@Serializable
@SerialName("carousel")
data class ApiCarousel(
    val title: String,
    val service: ApiServiceDetail,
    val items: List<ApiItem.ApiCarouselItem>,
    val trackingInfo: ApiTrackingInfo
) : ApiComponent()

@Serializable
data class ApiSeeMoreLink(
    val text: String,
    val url: String
)

@Serializable
@SerialName("mini_apps")
data class ApiMiniApps(
    val title: String,
    val seeMoreTag: String,
    val miniApps: List<ApiItem.ApiMiniApp>,
    val trackingInfo: ApiTrackingInfo
) : ApiComponent()

@Serializable
@SerialName("games_mini_apps")
data class ApiGamesMiniApps(
    val title: String,
    val seeMoreTag: String,
    val miniApps: List<ApiItem.ApiGameMiniApp>,
    val trackingInfo: ApiTrackingInfo
) : ApiComponent()

@Serializable
@SerialName("partners")
data class ApiPartners(
    val title: String,
    val subtitle: String,
    val service: ApiPartnerServiceDetail,
    val seeMoreTag: String,
    val serviceHistoryImage: String,
    val link: ApiLinks,
    val partners: List<ApiItem.ApiPartner>,
    val trackingInfo: ApiTrackingInfo
) : ApiComponent()

@Serializable
data class ApiLinks(
    val webUrl: String,
    val appLinkAndroid: String?,
    val appLinkIos: String?,
    val type: String? = null,
    val link: String? = null
)

@Serializable
data class ApiServiceDetail(
    val id: String,
    val title: String,
    val imageUrl: String,
    val historyImage: String,
    val link: ApiLinks
)

@Serializable
data class ApiPartnerServiceDetail(
    val id: String,
    val title: String,
    val imageUrl: String,
    val historyImage: String,
    val link: ApiLinks,
    val trackingInfo: ApiItemTrackingInfo
)

@Serializable
data class ApiTrackingInfo(
    val rtg: String,
    val ritemid: List<String>,
    val rpl: String,
    val ratName: List<String>
)

@Serializable
data class ApiItemTrackingInfo(
    val rtg: String,
    val ritemid: String,
    val rpl: String,
    val ratName: String
)