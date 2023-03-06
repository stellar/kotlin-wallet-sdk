module.exports = {
    resolver: {
        extraNodeModules: {
            https: require.resolve('https-browserify'),
            http: require.resolve('https-browserify'),
            buffer: require.resolve('buffer/'),
        },
    }
};
