const path = require('path');
const { getDefaultConfig } = require('expo/metro-config');

const config = getDefaultConfig(__dirname);

// Intercept deep internal react-native imports that changed in React Native 0.76+
config.resolver = {
  ...config.resolver,
  resolveRequest: (context, moduleName, platform) => {
    if (moduleName === 'react-native/asset-registry') {
      return {
        filePath: path.resolve(__dirname, 'shims/asset-registry.js'),
        type: 'sourceFile',
      };
    }

    if (moduleName === 'react-native/unstable-internals-do-not-use') {
      return {
        filePath: path.resolve(__dirname, 'shims/unstable-internals.js'),
        type: 'sourceFile',
      };
    }

    return context.resolveRequest(context, moduleName, platform);
  },
};

module.exports = config;
