import Foundation
import discoveryrepo

extension MainScreenType {
	func toDiscoveryDynamicTab() -> DiscoveryDynamicTab {
		return DiscoveryDynamicTab(title: title, feedType: queryTag, showOnDiscoveryTab: true, showOnSeeMoreScreen: hasSeeMore)
	}
}

extension Component {
	func toDiscoverySectionModel() -> DiscoverySectionModel {
		if let compo = self as? Carousel {
			return DiscoverySectionModel(forItemType: DiscoverySectionType.carousel, title: compo.title)
		} else if let compo = self as? ContentFeed {
			return DiscoverySectionModel(forItemType: DiscoverySectionType.contentFeed, title: compo.title)
		} else if let compo = self as? RecommendedServices {
			return DiscoverySectionModel(forItemType: DiscoverySectionType.recommendedServices, title: compo.title)
		} else if let compo = self as? RakutenServices {
			return DiscoverySectionModel(forItemType: DiscoverySectionType.rakutenServices, title: compo.title)
		} else if let compo = self as? BigCards {
			return DiscoverySectionModel(forItemType: DiscoverySectionType.campaigns, title: "")
		} else if let compo = self as? Album {
			return DiscoverySectionModel(forItemType: DiscoverySectionType.music, title: compo.title)
		} else if let compo = self as? Partners {
			return DiscoverySectionModel(forItemType: DiscoverySectionType.partners, title: compo.title)
		} else if let compo = self as? MiniApps {
			return DiscoverySectionModel(forItemType: DiscoverySectionType.miniApps, title: compo.title)
		} else if let compo = self as? Games {
			return DiscoverySectionModel(forItemType: DiscoverySectionType.gamesMiniApps, title: compo.title)
		} else if let compo = self as? FavoriteServices {
			return DiscoverySectionModel(forItemType: DiscoverySectionType.favoriteServices, title: compo.title)
		} else if let compo = self as? ServicesHistory {
			return DiscoverySectionModel(forItemType: DiscoverySectionType.servicesHistory, title: compo.title)
		} else if let compo = self as? TimeLimitedBanner {
			return DiscoverySectionModel(forItemType: DiscoverySectionType.bannerGroup, title: compo.title)
		}
	}
}

func getFavoriteItem(with favoriteService: DiscoveryFavoriteServicesCellModel) -> Item {
	
	let realmFavoriteService = favoriteService.toRealmModel()
	
	let link = Links(
		webUrl: realmFavoriteService.webURL,
		appLinkAndroid: realmFavoriteService.androidAppURL,
		appLinkIos: realmFavoriteService.appURL,
		type: realmFavoriteService.itemType,
		link: favoriteService.link.link
	)
	
	let trackingInfo = ItemTrackingInfo(
		trackingTag: realmFavoriteService.rtg,
		recommendationItemId: realmFavoriteService.rItemID,
		rpl: realmFavoriteService.rpl,
		ratName: realmFavoriteService.ratName
	)
	
	let service = Item.ServiceDetails(
		id: favoriteService.id,
		itemType: favoriteService.itemType,
		title: favoriteService.title,
		subtitle: favoriteService.subtitle,
		imageUrl: realmFavoriteService.imageURL ?? "",
		historyImage: favoriteService.historyImage,
		link: link,
		tags: nil,
		trackingInfo: trackingInfo,
		isSaved: false
	)
	
	if(favoriteService.itemType == "ticket") {
		return Item.CarouselItem(
			id: favoriteService.id,
			itemType: favoriteService.itemType,
			title: favoriteService.title,
			imageUrl: favoriteService.imageUrl ?? "",
			link: link,
			trackingInfo: trackingInfo,
			isSaved: true,
			service: service
		)
	} else if (favoriteService.itemType == "track") {
		return Item.AlbumItem(
			id: favoriteService.id,
			itemType: favoriteService.itemType,
			title: favoriteService.title,
			subtitle: favoriteService.subtitle ?? "",
			imageUrl: favoriteService.imageUrl ?? "",
			link: link,
			trackingInfo: trackingInfo
		)
	} else if (favoriteService.itemType == "mini_app") {
		let miniAppType = discoveryrepo.MiniAppType.miniApp
		return Item.MiniApp(
			id: favoriteService.id,
			itemType: favoriteService.itemType,
			title: favoriteService.title,
			imageUrl: favoriteService.imageUrl ?? "",
			versionId: "",
			versionTag: "",
			isSaved: true,
			miniAppType: miniAppType,
			trackingInfo: trackingInfo
		)
	} else if (favoriteService.itemType == "big_card_item") {
		return Item.CardItem(
			id: favoriteService.id,
			itemType: favoriteService.itemType,
			title: favoriteService.title,
			subtitle: favoriteService.subtitle ?? "",
			description: realmFavoriteService.description,
			imageUrl: realmFavoriteService.imageURL ?? "",
			link: link,
			trackingInfo: trackingInfo,
			isSaved: true,
			service: service
		)
	} else if (favoriteService.itemType == "content_feed_item") {
		return Item.ContentFeedItem(
			id: favoriteService.id,
			itemType: favoriteService.itemType,
			title: favoriteService.title,
			subtitle: favoriteService.subtitle ?? "",
			imageUrl: realmFavoriteService.imageURL ?? "",
			link: link,
			trackingInfo: trackingInfo,
			isSaved: true,
			service: service
		)
	} else if (favoriteService.itemType == "rebates_partner") {
		return Item.PartnerItem(
			id: favoriteService.id,
			itemType: favoriteService.itemType,
			title: favoriteService.title,
			subtitle: favoriteService.subtitle ?? "",
			imageUrl: realmFavoriteService.imageURL ?? "",
			historyImage: favoriteService.historyImage,
			link: link,
			trackingInfo: trackingInfo,
			isSaved: true
		)
	} else {
		return Item.BannerItem(
			id: favoriteService.id,
			title: favoriteService.title,
			description: realmFavoriteService.description,
			imageUrl: realmFavoriteService.imageURL ?? "",
			link: link,
			itemType: favoriteService.itemType,
			trackingInfo: trackingInfo
		)
	}
}

