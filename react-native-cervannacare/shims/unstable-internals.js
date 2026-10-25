/**
 * Shim for react-native/unstable-internals-do-not-use
 * Resolves compatibility between React Native 0.76+ package exports and expo-asset
 */
let AssetSourceResolver;

try {
  AssetSourceResolver = require('react-native/Libraries/Image/AssetSourceResolver');
  if (AssetSourceResolver && AssetSourceResolver.default) {
    AssetSourceResolver = AssetSourceResolver.default;
  }
} catch (e) {
  AssetSourceResolver = class AssetSourceResolver {
    constructor(serverUrl, jsbundleUrl, asset) {
      this.serverUrl = serverUrl;
      this.jsbundleUrl = jsbundleUrl;
      this.asset = asset;
    }
    defaultAsset() {
      return this.asset;
    }
    assetServerURL() {
      return this.asset;
    }
    scaledAssetURL(scale) {
      return this.asset;
    }
  };
}

module.exports = {
  AssetSourceResolver: AssetSourceResolver,
};
