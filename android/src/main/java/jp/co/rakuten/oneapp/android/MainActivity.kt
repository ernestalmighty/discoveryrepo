package jp.co.rakuten.oneapp.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jp.co.rakuten.oneapp.shared.entity.*
import jp.co.rakuten.oneapp.shared.local.DiscoveryViewModel
import jp.co.rakuten.oneapp.shared.local.getInstance
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val CAROUSEL_SERVICE_DETAILS = Item.ServiceDetails(
            id = "carousel_service_id",
            itemType = "carousel_item_type",
            title = "carousel_service_title",
            imageUrl = "carousel_service_icon_url",
            historyImage = "carousel_service_history_image",
            link = Links(
                "carousel_service_web_url",
                "carousel_service_app_url_android",
                "carousel_service_app_url_ios",
                "service_link_type",
                "service_link"
            ),
            tags = null,
            trackingInfo = ItemTrackingInfo(
                trackingTag = "carousel_tracking_tag",
                recommendationItemId = "",
                rpl = "carousel_rpl",
                ratName = "carousel_service_name"
            )
        )
        val CAROUSEL_ITEM = Item.CarouselItem(
            id = "carousel_item_id",
            itemType = "carousel_item_type",
            title = "carousel_item_title",
            imageUrl = "carousel_item_image_url",
            link = Links(
                "carousel_item_web_url",
                "carousel_item_app_url_android",
                "carousel_item_app_url_ios",
                "service_link_type",
                "service_link"
            ),
            service = CAROUSEL_SERVICE_DETAILS,
            trackingInfo = ItemTrackingInfo(
                trackingTag = "carousel_item_tracking_tag",
                recommendationItemId = "carousel_item_ritemid",
                rpl = "carousel_item_rpl",
                ratName = "carousel_item_service_name"
            )
        )
        val CAROUSEL = Carousel(
            title = "carousel_title",
            service = CAROUSEL_SERVICE_DETAILS,
            items = listOf(CAROUSEL_ITEM),
            trackingInfo = TrackingInfo(
                trackingTag = "carousel_tracking_tag",
                recommendationItemId = listOf("carousel_ritemid"),
                rpl = "carousel_rpl",
                ratName = listOf("carousel_service_name")
            )
        )
        val CAROUSEL_SERVICE_DETAILS_FAVORITE = FavoriteServiceDetails(
            title = "carousel_service_title",
            imageUrl = "carousel_service_icon_url",
            historyImage = "carousel_service_history_image",
            link = Links(
                "carousel_service_web_url",
                "carousel_service_app_url_android",
                "carousel_service_app_url_ios",
                "service_link_type",
                "service_link"
            ),
            item = CAROUSEL_SERVICE_DETAILS,
            timestamp = "2020-02-02 20:20:20"
        )
        val CAROUSEL_ITEM_BOOKMARK = BookmarkDetails(
            title = "carousel_item_title",
            imageUrl = "carousel_item_image_url",
            serviceName = "carousel_service_title",
            serviceImageUrl = "carousel_service_icon_url",
            link = Links("carousel_item_web_url", "carousel_item_app_url_android", "carousel_item_app_url_ios", "service_link_type", "service_link"),
            item = CAROUSEL_ITEM,
            timestamp = "2020-02-02 20:20:20"
        )

        val disco = DiscoveryViewModel.Factory.getInstance(this)
        MainScope().launch {

            // Bookmarks
            disco.repository.deleteBookmarks()
            val bookmarkItem = disco.repository.saveBookmark(CAROUSEL_ITEM)
            val getBookmarksItem = disco.repository.getBookmarks()

            // Favorites
            disco.repository.deleteFavorites()
            val favoriteItem = disco.repository.saveFavorite(CAROUSEL_ITEM)
            val getFavoritesItem = disco.repository.getFavorites()

            // one-app/disc/api/v2/discovery
            val fetchDataResult = disco.repository.fetchData(
                accessToken = "OAuth2 IgABNGYWkHjm0S_ODC8GNKHC-Hw5m5Gm3UjT_H6z1KnwgVULuq7",
                feedType = "top"
            )

            // one-app/disc/api/v2/feedtype?client_support_dynamic_feed_type=true
            val fetchFeedTypesResult = disco.repository.fetchFeedTypes(
                accessToken = "OAuth2 IgABNGYWkHjm0S_ODC8GNKHC-Hw5m5Gm3UjT_H6z1KnwgVULuq7"
            )

            // one-app/disc/api/v2/services/rakutenServiceList
            val fetchRakutenServicesResult = disco.repository.fetchRakutenServices(
                accessToken = "OAuth2 IgABNGYWkHjm0S_ODC8GNKHC-Hw5m5Gm3UjT_H6z1KnwgVULuq7",
                screenType = "top",
                version = "1.0"
            )

            // one-app/disc/api/v2/services/miniAppList
            val fetchMiniApps1Result = disco.repository.fetchMiniApps(
                accessToken = "OAuth2 IgABNGYWkHjm0S_ODC8GNKHC-Hw5m5Gm3UjT_H6z1KnwgVULuq7",
                screenType = "top",
                version = "1.0"
            )

            // one-app/disc/api/v2/services/partnerType
            val fetchMiniApps2Result = disco.repository.fetchMiniApps(
                accessToken = "OAuth2 IgABNGYWkHjm0S_ODC8GNKHC-Hw5m5Gm3UjT_H6z1KnwgVULuq7",
                screenType = "top",
                partnerType = "partner",
                version = "1.0"
            )
        }
    }
}
