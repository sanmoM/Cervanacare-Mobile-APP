const { getDefaultConfig } = require('expo/metro-config');

const config = getDefaultConfig(__dirname);

// Enable package exports resolution for React Native subpath exports
config.resolver = {
  ...config.resolver,
  unstable_enablePackageExports: true,
  resolveRequest: (context, moduleName, platform) => {
    // Fallback handler for react-native internal subpath resolution
    if (moduleName === 'react-native/unstable-internals-do-not-use') {
      try {
        return context.resolveRequest(context, 'react-native/Libraries/Image/AssetSourceResolver', platform);
      } catch (err) {
        // Fallback to default resolution if custom path fails
      }
    }
    return context.resolveRequest(context, moduleName, platform);
  },
};

module.exports = config;
