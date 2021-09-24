package jp.co.rakuten.oneapp.shared.entity

import jp.co.rakuten.oneapp.shared.entity.Item.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.serialDescriptor


// DTO to ViewModel
fun ApiComponent.toComponent(): Component? = when (this) {
    is ApiFavoriteServices -> this.toFavoriteServices()
    is ApiServicesHistory -> this.toServicesHistory()
    is ApiMiniApps -> this.toMiniAppCollection()
    is ApiGamesMiniApps -> this.toMiniAppCollection()
    is ApiContentFeed -> this.toContentFeed()
    is ApiRecommendedServices -> this.toRecommendedServices()
    is ApiRakutenServices -> this.toRakutenServices()
    is ApiBigCard -> this.toBigCards()
    is ApiPartners -> this.toPartners()
    is ApiAlbum -> this.toAlbum()
    is ApiCarousel -> this.toCarousel()
    is ApiUnknownComponent -> null
}

fun ApiItem.toItem(): Item = when (this) {
    is ApiItem.ApiRakutenServiceInfo -> this.toServiceDetails()
    is ApiItem.ApiPartner -> this.toPartnerItem()
    is ApiItem.ApiAlbumItem -> this.toAlbumItem()
    is ApiItem.ApiMiniApp -> this.toMiniApp()
    is ApiItem.ApiBannerItem -> this.toBannerItem()
    else -> throw IllegalArgumentException("${this::class} cannot be converted to object.")
}

fun ApiItem.toItem(service: Item.ServiceDetails): Item = when (this) {
    is ApiItem.ApiContentFeedItem -> this.toContentFeedItem(service)
    is ApiItem.ApiCard -> this.toCardItem(service)
    is ApiItem.ApiCarouselItem -> this.toCarouselItem(service)
    else -> throw IllegalArgumentException("${this::class} cannot be converted to object.")
}

fun ApiFeedType.toMainScreenType() = MainScreenType(
    title = title,
    queryTag = feedType,
    ratTag = feedType,
    hasSeeMore = showOnSeeMoreScreen
)

fun ApiFavoriteServices.toFavoriteServices() = FavoriteServices(
    title = title,
    favoriteServices = listOf()
)

fun ApiServicesHistory.toServicesHistory() = ServicesHistory(
    title = title,
    services = listOf()
)

fun ApiMiniApps.toMiniAppCollection() = MiniApps(
    title = title,
    seeMoreTag = seeMoreTag,
    miniApps = miniApps.map { it.toMiniApp() },
    trackingInfo = trackingInfo.toTrackingInfo()
)

fun ApiGamesMiniApps.toMiniAppCollection() = MiniApps(
    title = title,
    seeMoreTag = seeMoreTag,
    miniApps = miniApps.map { it.toGameMiniApp() },
    trackingInfo = trackingInfo.toTrackingInfo()
)

@ExperimentalSerializationApi
fun ApiItem.ApiMiniApp.toMiniApp() = MiniApp(
    id = id,
    itemType = serialDescriptor<ApiItem.ApiMiniApp>().serialName,
    title = title,
    imageUrl = imageUrl,
    versionId = versionId ?: "",
    versionTag = versionTag ?: "",
    miniAppType = MiniAppType.MINI_APP,
    trackingInfo = trackingInfo.toTrackingInfo()
)

@ExperimentalSerializationApi
fun ApiItem.ApiGameMiniApp.toGameMiniApp() = MiniApp(
    id = id,
    itemType = serialDescriptor<ApiItem.ApiMiniApp>().serialName,
    title = title,
    imageUrl = imageUrl,
    versionId = versionId ?: "",
    versionTag = versionTag ?: "",
    miniAppType = MiniAppType.GAME,
    trackingInfo = trackingInfo.toTrackingInfo()
)

@ExperimentalSerializationApi
fun ApiContentFeed.toContentFeed(): ContentFeed {
    val completeService = service.toServiceDetails(
        null,
        serialDescriptor<ApiItem.ApiContentFeedItem>().serialName,
        trackingInfo
    )

    return ContentFeed(
        title = title,
        service = completeService,
        feedItems = items.map { it.toContentFeedItem(completeService) },
        trackingInfo = trackingInfo.toTrackingInfo()
    )
}

@ExperimentalSerializationApi
fun ApiItem.ApiContentFeedItem.toContentFeedItem(serviceDetails: ServiceDetails) = ContentFeedItem(
    id = id,
    itemType = serialDescriptor<ApiItem.ApiContentFeedItem>().serialName,
    title = title,
    imageUrl = imageUrl,
    subtitle = subtitle,
    service = serviceDetails,
    link = link.toLinks(),
    trackingInfo = trackingInfo.toTrackingInfo()
)

fun ApiRecommendedServices.toRecommendedServices() = RecommendedServices(
    title = title,
    servicesCategories = serviceCategories.map {
        ServiceCategory(
            title = it.title,
            services = it.services.map { it.toServiceDetails() },
            trackingInfo = it.trackingInfo.toTrackingInfo()
        )
    },
    trackingInfo = serviceCategories[0].trackingInfo.toTrackingInfo()
)

fun ApiRakutenServices.toRakutenServices() = RakutenServices(
    title = title,
    link = link.toLinks(),
    services = services.map { it.toServiceDetails() },
    trackingInfo = trackingInfo.toTrackingInfo()
)

@ExperimentalSerializationApi
fun ApiItem.ApiRakutenServiceInfo.toServiceDetails() = ServiceDetails(
    id = id,
    itemType = serialDescriptor<ApiItem.ApiRakutenServiceInfo>().serialName,
    title = title,
    subtitle = subtitle,
    imageUrl = imageUrl,
    historyImage = historyImage,
    link = link.toLinks(),
    tags = tags?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() },
    trackingInfo = trackingInfo.toTrackingInfo()
)

@ExperimentalSerializationApi
fun ApiBigCard.toBigCards() = BigCards(
    cards = cards.map {
        it.toCardItem(
            service.toServiceDetails(null, serialDescriptor<ApiItem.ApiCard>().serialName, trackingInfo)
        )
    },
    trackingInfo = trackingInfo.toTrackingInfo()
)

@ExperimentalSerializationApi
fun ApiItem.ApiCard.toCardItem(service: ServiceDetails) = CardItem(
    id = id,
    itemType = serialDescriptor<ApiItem.ApiCard>().serialName,
    title = title,
    subtitle = subtitle,
    service = service,
    imageUrl = imageUrl,
    description = description,
    link = link.toLinks(),
    trackingInfo = trackingInfo.toTrackingInfo()
)

@ExperimentalSerializationApi
fun ApiAlbum.toAlbum() = Album(
    title = title,
    service = service.toServiceDetails(null, serialDescriptor<ApiItem.ApiAlbumItem>().serialName, trackingInfo),
    items = items.map { it.toAlbumItem() },
    seeMore = Links(
        webUrl = seeMoreLink.url,
        appLinkAndroid = null,
        appLinkIos = null,
        type = null,
        link = null
    ),
    trackingInfo = trackingInfo.toTrackingInfo()
)

@ExperimentalSerializationApi
fun ApiItem.ApiAlbumItem.toAlbumItem() = AlbumItem(
    id = id,
    itemType = serialDescriptor<ApiItem.ApiAlbumItem>().serialName,
    title = title,
    subtitle = subtitle,
    imageUrl = imageUrl,
    link = link.toLinks(),
    trackingInfo = trackingInfo.toTrackingInfo()
)

fun ApiCarousel.toCarousel(): Carousel {
    val completeService = service.toServiceDetails(null, "ticket", trackingInfo)
    return Carousel(
        title = title,
        service = completeService,
        items = items.map { it.toCarouselItem(completeService) },
        trackingInfo = trackingInfo.toTrackingInfo()
    )
}

@ExperimentalSerializationApi
fun ApiItem.ApiBannerItem.toBannerItem() = BannerItem(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl,
    link = link.toLinks(),
    itemType = serialDescriptor<ApiItem.ApiBannerItem>().serialName,
    trackingInfo = trackingInfo.toTrackingInfo()
)

@ExperimentalSerializationApi
fun ApiItem.ApiCarouselItem.toCarouselItem(service: ServiceDetails) = CarouselItem(
    id = id,
    itemType = serialDescriptor<ApiItem.ApiCarouselItem>().serialName,
    title = title,
    imageUrl = imageUrl,
    link = link.toLinks(),
    service = service,
    trackingInfo = trackingInfo.toTrackingInfo()
)

fun ApiPartners.toPartners() = Partners(
    title = title,
    subtitle = subtitle,
    seeMoreTag = seeMoreTag,
    serviceHistoryImage = serviceHistoryImage,
    link = link.toLinks(),
    partners = partners.map {
        it.toPartnerItem()
    },
    trackingInfo = trackingInfo.toTrackingInfo(),
    service = service.toPartnerServiceDetails(tags = null, type = "rakuten_services")
)

@ExperimentalSerializationApi
fun ApiItem.ApiPartner.toPartnerItem() = PartnerItem(
    id = id,
    itemType = serialDescriptor<ApiItem.ApiPartner>().serialName,
    title = title,
    subtitle = subtitle,
    imageUrl = imageUrl,
    link = link.toLinks(),
    historyImage = historyImage,
    trackingInfo = trackingInfo.toTrackingInfo()
)

fun ApiServiceDetail.toServiceDetails(tags: String? = null, type: String, trackingInfo: ApiTrackingInfo) = ServiceDetails(
    id = id,
    itemType = type,
    title = title,
    imageUrl = imageUrl,
    historyImage = historyImage,
    link = link.toLinks(),
    tags = tags?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() },
    // TODO assign proper tracking info to these services
    trackingInfo = ItemTrackingInfo(
        trackingTag = trackingInfo.rtg,
        recommendationItemId = "",
        rpl = trackingInfo.rpl,
        ratName = trackingInfo.ratName.first()
    )
)

fun ApiPartnerServiceDetail.toPartnerServiceDetails(tags: String? = null, type: String) = ServiceDetails(
    id = id,
    itemType = type,
    title = title,
    imageUrl = imageUrl,
    historyImage = historyImage,
    link = link.toLinks(),
    tags = tags?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() },
    trackingInfo = trackingInfo.toTrackingInfo()
)

fun ApiLinks.toLinks() = Links(
    webUrl = webUrl.trim(),
    appLinkAndroid = appLinkAndroid,
    appLinkIos = appLinkIos,
    type = type,
    link = link
)

fun ApiTrackingInfo.toTrackingInfo() = TrackingInfo(
    trackingTag = rtg,
    recommendationItemId = ritemid,
    rpl = rpl,
    ratName = ratName
)

fun ApiItemTrackingInfo.toTrackingInfo() = ItemTrackingInfo(
    trackingTag = rtg,
    recommendationItemId = ritemid,
    rpl = rpl,
    ratName = ratName
)

// ViewModel to DTO
fun Item.toApiItem(): ApiItem = when (this) {
    is ServiceDetails -> this.toApiServiceDetail()
    is ContentFeedItem -> this.toApiContentFeedItem()
    is CardItem -> this.toApiCard()
    is PartnerItem -> this.toApiPartner()
    is AlbumItem -> this.toApiAlbumItem()
    is CarouselItem -> this.toApiCarouselItem()
    is MiniApp -> this.toApiMiniApp()
    is BannerItem -> this.toApiBannerItem()
}

fun ServiceDetails.toApiServiceDetail() = ApiItem.ApiRakutenServiceInfo(
    id = id,
    title = title,
    subtitle = subtitle,
    imageUrl = imageUrl,
    historyImage = historyImage,
    link = link.toApiLinks(),
    tags = tags?.joinToString(","),
    trackingInfo = trackingInfo.toApiItemTrackingInfo()
)

fun ContentFeedItem.toApiContentFeedItem() = ApiItem.ApiContentFeedItem(
    id = id,
    title = title,
    subtitle = subtitle,
    imageUrl = imageUrl,
    link = link.toApiLinks(),
    trackingInfo = trackingInfo.toApiItemTrackingInfo()
)

fun CardItem.toApiCard() = ApiItem.ApiCard(
    id = id,
    title = title,
    subtitle = subtitle,
    description = description,
    imageUrl = imageUrl,
    link = link.toApiLinks(),
    trackingInfo = trackingInfo.toApiItemTrackingInfo()
)

fun BannerItem.toApiBannerItem() = ApiItem.ApiBannerItem(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl,
    link = link.toApiLinks(),
    trackingInfo = trackingInfo.toApiItemTrackingInfo()
)

fun PartnerItem.toApiPartner() = ApiItem.ApiPartner(
    id = id,
    title = title,
    subtitle = subtitle,
    imageUrl = imageUrl,
    historyImage = historyImage,
    link = link.toApiLinks(),
    trackingInfo = trackingInfo.toApiItemTrackingInfo()
)

fun AlbumItem.toApiAlbumItem() = ApiItem.ApiAlbumItem(
    id = id,
    title = title,
    subtitle = subtitle,
    imageUrl = imageUrl,
    link = link.toApiLinks(),
    trackingInfo = trackingInfo.toApiItemTrackingInfo()
)

fun CarouselItem.toApiCarouselItem() = ApiItem.ApiCarouselItem(
    id = id,
    title = title,
    imageUrl = imageUrl,
    link = link.toApiLinks(),
    trackingInfo = trackingInfo.toApiItemTrackingInfo()
)

fun Links.toApiLinks() = ApiLinks(
    webUrl = this.webUrl,
    appLinkAndroid = this.appLinkAndroid,
    appLinkIos = this.appLinkIos,
    type = this.type,
    link = this.link
)

fun ItemTrackingInfo.toApiItemTrackingInfo() = ApiItemTrackingInfo(
    rtg = trackingTag,
    ritemid = recommendationItemId,
    rpl = rpl,
    ratName = ratName
)

fun MiniApp.toApiMiniApp() = ApiItem.ApiMiniApp(
    id = id,
    title = title,
    imageUrl = imageUrl,
    versionId = versionId,
    versionTag = versionTag,
    trackingInfo = trackingInfo.toApiItemTrackingInfo()
)
