/**
 * Shim for react-native/asset-registry
 * Resolves compatibility between React Native 0.76+ and @expo/vector-icons / asset loaders
 */
let assetsRegistry;

try {
  assetsRegistry = require('@react-native/assets-registry/registry');
} catch (e1) {
  try {
    assetsRegistry = require('react-native/Libraries/Image/AssetRegistry');
  } catch (e2) {
    const assets = [];
    assetsRegistry = {
      registerAsset: function (asset) {
        return assets.push(asset);
      },
      getAssetByID: function (assetId) {
        return assets[assetId - 1];
      },
    };
  }
}

module.exports = assetsRegistry;
