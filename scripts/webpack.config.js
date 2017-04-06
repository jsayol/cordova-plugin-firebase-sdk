const pathResolve = require('path').resolve;
const {CheckerPlugin} = require('awesome-typescript-loader');

module.exports = {
  entry: [
    pathResolve(__dirname, './src/index.ts')
  ],
  devtool: 'source-map',
  externals: [
    'cordova/exec'
  ],
  module: {
    loaders: [
      {
        test: /\.ts$/,
        loader: 'awesome-typescript-loader'
      }
    ]
  },
  plugins: [
    new CheckerPlugin()
  ],
  output: {
    path: pathResolve(__dirname, '../www'),
    libraryTarget: 'commonjs2',
    library: 'Firebase',
    filename: 'index.js'
  },
  resolve: {
    extensions: ['.ts', '.js']
  }
};
