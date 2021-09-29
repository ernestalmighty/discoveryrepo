# discoveryrepo

just run `./gradlew build`

when build is successful, it will generate builds for the ff platforms:

android -> shared/build/outputs/aar/shared-debug.aar


ios -> shared/build/fat-framework/debug/discovery.framework


web -> shared/build/distributions/shared.js


**How to use:**

*Android:*

after importing aar module, create an instance of the DiscoveryViewModel and use as follows:

<img width="652" alt="Screenshot 2021-09-29 at 9 07 27 AM" src="https://user-images.githubusercontent.com/20489695/135185877-8a2a3bcb-6084-4967-ae2f-1236c1d95ba6.png">
