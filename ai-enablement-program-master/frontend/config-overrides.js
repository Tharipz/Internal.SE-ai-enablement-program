function addLessSupport(config) {
  const oneOfRule = config.module.rules.find((rule) => Array.isArray(rule.oneOf));
  if (!oneOfRule) {
    return config;
  }

  const cssRuleIndex = oneOfRule.oneOf.findIndex((rule) => String(rule.test) === String(/\.css$/));
  if (cssRuleIndex < 0) {
    return config;
  }

  const cssRule = oneOfRule.oneOf[cssRuleIndex];
  oneOfRule.oneOf.splice(cssRuleIndex + 1, 0, {
    test: /\.less$/,
    exclude: /\.module\.less$/,
    use: [
      ...cssRule.use,
      {
        loader: require.resolve('less-loader'),
        options: {
          lessOptions: {
            javascriptEnabled: true
          }
        }
      }
    ],
    sideEffects: true
  });

  return config;
}

module.exports = function override(config) {
  return addLessSupport(config);
};
