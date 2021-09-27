# discoveryrepo
KMP POC


just run `./gradlew build`

when build is successful, it will generate builds for the ff platforms:

android -> shared/build/outputs/aar/shared-debug.aar


ios -> shared/build/fat-framework/debug/discovery.framework


web -> shared/build/distributions/shared.js


Network callls

iOS: 

`let repo = DiscoveryViewModel.Factory().getiOSInstance()`

`1. one-app/disc/api/v2/feedtype?client_support_dynamic_feed_type=true`

`repo.localRepository.fetchFeedTypes(accessToken: "[token]") { MainScreenType, Error in`
   `// code here`
`}`

`2. one-app/disc/api/v2/discovery`

`repo.localRepository.fetchTabData(accessToken: "[token]", feedType: "[feedType]") { Component, Error in`
  `// code here`
`}`

`3. one-app/disc/api/v2/discovery`

`repo.localRepository.fetchService(accessToken: "[token]", collectionId: "[collectionId]", version: "[version]", tabType: "[tabType]") { ServiceDetails, Error in`
  `// code here`
`}`
