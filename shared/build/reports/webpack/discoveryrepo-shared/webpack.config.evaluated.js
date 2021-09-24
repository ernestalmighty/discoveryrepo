{
  mode: 'production',
  resolve: {
    modules: [
      '/Users/ernest.gayyed/IdeaProjects/discoveryrepo/build/js/packages/discoveryrepo-shared/kotlin-dce',
      'node_modules'
    ]
  },
  plugins: [
    ProgressPlugin {
      profile: false,
      handler: [Function: handler],
      modulesCount: 5000,
      dependenciesCount: 10000,
      showEntries: true,
      showModules: true,
      showDependencies: true,
      showActiveModules: false,
      percentBy: undefined
    },
    TeamCityErrorPlugin {}
  ],
  module: {
    rules: [
      {
        test: /\.js$/,
        use: [
          'source-map-loader'
        ],
        enforce: 'pre'
      },
      {
        test: /\.css$/,
        use: [
          {
            loader: 'style-loader',
            options: {}
          },
          {
            loader: 'css-loader',
            options: {}
          }
        ]
      }
    ]
  },
  entry: {
    main: [
      '/Users/ernest.gayyed/IdeaProjects/discoveryrepo/build/js/packages/discoveryrepo-shared/kotlin-dce/discoveryrepo-shared.js'
    ]
  },
  output: {
    path: '/Users/ernest.gayyed/IdeaProjects/discoveryrepo/shared/build/distributions',
    filename: [Function: filename],
    library: 'shared',
    libraryTarget: 'umd',
    globalObject: 'this'
  },
  devtool: 'source-map',
  ignoreWarnings: [
    /Failed to parse source map/
  ],
  stats: {
    warnings: false,
    errors: false
  }
}