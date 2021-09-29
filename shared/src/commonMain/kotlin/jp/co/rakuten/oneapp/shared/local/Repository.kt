package jp.co.rakuten.oneapp.shared.local

import com.squareup.sqldelight.db.SqlDriver
import jp.co.rakuten.oneapp.shared.entity.BookmarkDetails
import jp.co.rakuten.oneapp.shared.entity.*
import jp.co.rakuten.oneapp.shared.entity.Item.*
import jp.co.rakuten.oneapp.shared.remote.DiscoveryAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule

class Repository(private val sqlDriver: SqlDriver,
                 private val useDefaultDispatcher: Boolean = true) {

    private val db by lazy { DiscoveryDatabase(sqlDriver) }
    private val api by lazy { DiscoveryAPI() }

    suspend fun <T> withRepoContext (block: suspend () -> T) : T {
        return if (useDefaultDispatcher) {
            withContext(Dispatchers.Default) {
                block()
            }
        } else {
            block()
        }
    }

    private fun Item.toHistoryWrapper() = Wrapper.HistoryWrapper(this, getCurrentDateTime())
    private fun Item.toFavoriteWrapper() = Wrapper.FavoriteWrapper(this, getCurrentDateTime())
    private fun Item.toBookmarkWrapper() = when (this) {
        is ContentFeedItem -> service
        is CardItem -> service
        is CarouselItem -> service
        else -> throw IllegalArgumentException("Item of type ${this::class} cannot be casted to Bookmark.")
    }.let {
        Wrapper.BookmarkWrapper(
            item = this,
            service = it,
            timestamp = getCurrentDateTime()
        )
    }

    private fun getCurrentDateTime(): String {
        return Clock.System.now().epochSeconds.toString()
    }

    @Throws(Exception::class) suspend fun fetchData(accessToken: String, feedType: String): List<Component> {
        return api.fetchTabData(
            accessToken,
            feedType
        )
            .feed
            .mapNotNull {
                it.toComponent()
            }
    }

    @Throws(Exception::class) suspend fun fetchFeedTypes(accessToken: String): List<MainScreenType> {
        return api.fetchFeedTypes(
            accessToken
        )
            .filter {
                it.showOnDiscoveryTab
            }
            .map {
                it.toMainScreenType()
            }
    }

    @Throws(Exception::class)
    suspend fun fetchRakutenServices(accessToken: String,
                                     screenType: String,
                                     version: String): List<Item.ServiceDetails> {
        return api.fetchService(
            accessToken = accessToken,
            collectionId = "rakutenServiceList",
            version = version,
            tabType = screenType
        ).let {
            it as ApiRakutenServices
            it.toRakutenServices().services
        }
    }

    @Throws(Exception::class)
    suspend fun fetchMiniApps(accessToken: String,
                              screenType: String,
                              version: String): List<Item.MiniApp> {
        return api.fetchService(
            accessToken = accessToken,
            collectionId = "miniAppList",
            version = version,
            tabType = screenType
        ).let {
            it as ApiMiniApps
            it.toMiniAppCollection().miniApps
        }
    }

    @Throws(Exception::class)
    suspend fun fetchMiniApps(accessToken: String,
                              screenType: String,
                              partnerType: String,
                              version: String): List<Item.PartnerItem> {
        return api.fetchService(
            accessToken = accessToken,
            collectionId = partnerType,
            version = version,
            tabType = screenType
        ).let {
            it as ApiPartners
            it.toPartners().partners
        }
    }

    fun saveFavorite(item: Item): Unit =
        item.run {
            val result = this.toFavoriteWrapper()
            val json = jsonConfiguration.encodeToString(result)
            print(json)

            db.discoveryDatabaseQueries.insertFavorite(
                item.id,
                item.itemType,
                json,
                getCurrentDateTime()
            )
        }

    fun removeFavorite(serviceId: String): Unit =
        db.discoveryDatabaseQueries.deleteFavorite(serviceId)

    fun getFavorites(): List<FavoriteServiceDetails> {
        return db.discoveryDatabaseQueries.selectAllFavorites(::toFavoriteWrapper).executeAsList()
            .mapNotNull {
                it.item.run {
                    when (this) {
                        is ServiceDetails -> this.toFavoriteServiceDetails(it.timestamp)
                        is PartnerItem -> this.toFavoriteServiceDetails(it.timestamp)
                        is MiniApp -> this.toFavoriteServiceDetails(it.timestamp)
                        else -> null
                    }
                }
            }
    }

    fun saveBookmark(item: Item): Unit =
        item.run {
            val result = this.toBookmarkWrapper()
            val json = jsonConfiguration.encodeToString(result)

            db.discoveryDatabaseQueries.insertBookmark(
                item.id,
                item.itemType,
                json,
                getCurrentDateTime()
            )
        }

    fun removeBookmark(serviceId: String): Unit =
        db.discoveryDatabaseQueries.deleteBookmark(serviceId)

    fun deleteBookmarks(): Unit = db.discoveryDatabaseQueries.deleteBookmarks()

    fun deleteFavorites(): Unit = db.discoveryDatabaseQueries.deleteFavorites()

    fun getBookmarks(): List<BookmarkDetails> =
        db.discoveryDatabaseQueries.selectAllBookmarks(::toBookmarkWrapper).executeAsList()
            .mapNotNull {
                it.item.run {
                    when (this) {
                        is ContentFeedItem -> this.toBookmarkDetails(it.timestamp)
                        is CardItem -> this.toBookmarkDetails(it.timestamp)
                        is CarouselItem -> this.toBookmarkDetails(it.timestamp)
                        else -> null
                    }
                }
            }

    fun saveServiceHistory(item: Item) =
        item.run {
            val result = this.toHistoryWrapper().toApiHistoryWrapper()
            val json = jsonConfiguration.encodeToString(result)

            db.discoveryDatabaseQueries.insertHistory(
                item.id,
                item.itemType,
                json,
                getCurrentDateTime()
            )
        }

    fun getServicesHistory(): List<ServiceHistoryDetails> =
        db.discoveryDatabaseQueries.selectAllHistory(::toHistoryWrapper).executeAsList()
            .mapNotNull {
                it.item.run {
                    when (this) {
                        is ServiceDetails -> this.toServiceHistoryDetails(it.timestamp)
                        is PartnerItem -> this.toServiceHistoryDetails(it.timestamp)
                        else -> null
                    }
                }
            }
}

private val jsonConfiguration = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}


private fun ServiceDetails.toFavoriteServiceDetails(dateTime: String) = FavoriteServiceDetails(
    title = title,
    historyImage = historyImage,
    imageUrl = imageUrl,
    link = link,
    item = this,
    timestamp = dateTime
)

private fun MiniApp.toFavoriteServiceDetails(dateTime: String) = FavoriteServiceDetails(
    title = title,
    imageUrl = imageUrl,
    item = this,
    timestamp = dateTime,
    historyImage = imageUrl,
    link = Links("", "", "", "", "")
)

private fun PartnerItem.toFavoriteServiceDetails(dateTime: String) = FavoriteServiceDetails(
    title = if (title.isNotEmpty()) title else subtitle,
    imageUrl = imageUrl,
    historyImage = historyImage,
    link = link,
    item = this,
    timestamp = dateTime
)

private fun ContentFeedItem.toBookmarkDetails(dateTime: String) = BookmarkDetails(
    title = title,
    imageUrl = imageUrl,
    serviceName = service?.title ?: "",
    serviceImageUrl = service?.imageUrl ?: "",
    link = link,
    item = this,
    timestamp = dateTime
)

private fun CardItem.toBookmarkDetails(dateTime: String) = BookmarkDetails(
    title = title,
    imageUrl = imageUrl,
    serviceName = service.title,
    serviceImageUrl = service.imageUrl,
    link = link,
    item = this,
    timestamp = dateTime
)

private fun CarouselItem.toBookmarkDetails(dateTime: String) = BookmarkDetails(
    title = title,
    imageUrl = imageUrl,
    serviceName = service.title,
    serviceImageUrl = service.imageUrl,
    link = link,
    item = this,
    timestamp = dateTime
)

private fun ServiceDetails.toServiceHistoryDetails(dateTime: String) = ServiceHistoryDetails(
    title = title,
    historyImage = historyImage,
    link = link,
    item = this,
    timestamp = dateTime
)

private fun PartnerItem.toServiceHistoryDetails(dateTime: String) = ServiceHistoryDetails(
    title = title,
    historyImage = historyImage,
    link = link,
    item = this,
    timestamp = dateTime
)

private fun toFavoriteWrapper(
    id: String,
    type: String,
    content: String,
    datestamp: String
): Wrapper.FavoriteWrapper {
    val element = jsonConfiguration.parseToJsonElement(content)
    return jsonConfiguration.decodeFromJsonElement(Wrapper.FavoriteWrapper.serializer(), element)
}

private fun toBookmarkWrapper(
    id: String,
    type: String,
    content: String,
    datestamp: String
): Wrapper.BookmarkWrapper {
    val element = jsonConfiguration.parseToJsonElement(content)
    return jsonConfiguration.decodeFromJsonElement(Wrapper.BookmarkWrapper.serializer(), element)
}

private fun toHistoryWrapper(
    id: String,
    type: String,
    content: String,
    datestamp: String
): Wrapper.HistoryWrapper {
    val element = jsonConfiguration.parseToJsonElement(content)
    return jsonConfiguration.decodeFromJsonElement(Wrapper.HistoryWrapper.serializer(), element)
}