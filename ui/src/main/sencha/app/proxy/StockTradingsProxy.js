Ext.define('jewelry.proxy.StockTradingsProxy', {
    extend: 'Ext.data.proxy.Rest',
    requires: [
        'jewelry.model.StockTradingsModel'
    ],
    alias: 'proxy.stockTradingsProxy',

    model: 'jewelry.model.StockTradingsModel',
    format: 'json',
    reader: {
        type: 'json'
    },
    writer: {
        type: 'json',
        writeAllFields: true
    }
});