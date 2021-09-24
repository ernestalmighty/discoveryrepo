package jp.co.rakuten.oneapp.shared.remote

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import jp.co.rakuten.oneapp.shared.Platform
import jp.co.rakuten.oneapp.shared.entity.ApiComponent
import jp.co.rakuten.oneapp.shared.entity.ApiFeed
import jp.co.rakuten.oneapp.shared.entity.ApiFeedType
import kotlinx.serialization.modules.SerializersModule

internal class DiscoveryAPI {
    companion object {
        private const val BASE_ENDPOINT = "https://prerelease.gateway-api.global.rakuten.com"
        private const val TAB_DATA_ENDPOINT = "one-app/disc/api/v2/discovery"
        private const val FEED_TYPES_ENDPOINT = "one-app/disc/api/v2/feedtype?client_support_dynamic_feed_type=true"
        private const val SERVICE_ENDPOINT = "one-app/disc/api/v2/services"
    }

    private val module = SerializersModule {
//        polymorphic(ApiComponent::class, ApiCarousel::class, ApiCarousel.serializer())
//        polymorphic(ApiComponent::class, ApiFavoriteServices::class, ApiFavoriteServices.serializer())
//        polymorphic(ApiComponent::class, ApiServicesHistory::class, ApiServicesHistory.serializer())
//        polymorphic(ApiComponent::class, ApiMiniApps::class, ApiMiniApps.serializer())
//        polymorphic(ApiComponent::class, ApiContentFeed::class, ApiContentFeed.serializer())
//        polymorphic(ApiComponent::class, ApiRecommendedServices::class, ApiRecommendedServices.serializer())
//        polymorphic(ApiComponent::class, ApiRakutenServices::class, ApiRakutenServices.serializer())
//        polymorphic(ApiComponent::class, ApiBigCard::class, ApiBigCard.serializer())
//        polymorphic(ApiComponent::class, ApiAlbum::class, ApiAlbum.serializer())
//        polymorphic(ApiComponent::class, ApiCarousel::class, ApiCarousel.serializer())
//        polymorphic(ApiComponent::class, ApiPartners::class, ApiPartners.serializer())
//
//        polymorphic(ApiItem::class, ApiItem.ApiContentFeedItem::class, ApiItem.ApiContentFeedItem.serializer())
//        polymorphic(ApiItem::class, ApiItem.ApiRakutenServiceInfo::class, ApiItem.ApiRakutenServiceInfo.serializer())
//        polymorphic(ApiItem::class, ApiItem.ApiCard::class, ApiItem.ApiCard.serializer())
//        polymorphic(ApiItem::class, ApiItem.ApiAlbumItem::class, ApiItem.ApiAlbumItem.serializer())
//        polymorphic(ApiItem::class, ApiItem.ApiMiniApp::class, ApiItem.ApiMiniApp.serializer())
//        polymorphic(ApiItem::class, ApiItem.ApiBannerItem::class, ApiItem.ApiBannerItem.serializer())
//        polymorphic(ApiItem::class, ApiItem.ApiCarouselItem::class, ApiItem.ApiCarouselItem.serializer())
//        polymorphic(ApiItem::class, ApiItem.ApiPartner::class, ApiItem.ApiPartner.serializer())
    }

    private val httpClient = HttpClient {
        install(JsonFeature) {
            val json = kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
            }

            serializer = KotlinxSerializer(json)
        }
    }

    suspend fun fetchTabData(accessToken: String, feedType: String): ApiFeed {
        return httpClient.get("$BASE_ENDPOINT/$TAB_DATA_ENDPOINT") {
            headers {
                append(HttpHeaders.Authorization, accessToken)
            }
            parameter("feed_type", feedType)
            parameter("device_type", Platform().platform)
        }
    }

    suspend fun fetchFeedTypes(accessToken: String): List<ApiFeedType> {
        return httpClient.get("$BASE_ENDPOINT/$FEED_TYPES_ENDPOINT") {
            headers {
                append(HttpHeaders.Authorization, accessToken)
                parameter("device_type", Platform().platform)
            }
        }
    }

    suspend fun fetchService(accessToken: String, collectionId: String, version: String, tabType: String): ApiComponent {
        return httpClient.get("$BASE_ENDPOINT/$SERVICE_ENDPOINT/$collectionId") {
            headers {
                append(HttpHeaders.Authorization, accessToken)
            }
            parameter("client_version", version)
            parameter("feed_type", tabType)
            parameter("device_type", Platform().platform)
        }
    }
}