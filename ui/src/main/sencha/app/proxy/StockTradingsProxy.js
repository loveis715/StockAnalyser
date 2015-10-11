Ext.define('jewelry.proxy.StockTradingsProxy', {
    extend: 'Ext.data.proxy.Rest',
    alias: 'proxy.stockTradingsProxy',

    url: 'http://localhost:8080/jewelry-service/api/stocks/tradings',
    format: 'json',
    reader: {
        type: 'json'
    },
    writer: {
        type: 'json',
        writeAllFields: true
    }
});