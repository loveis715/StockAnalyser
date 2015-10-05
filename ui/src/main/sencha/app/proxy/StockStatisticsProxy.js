Ext.define('jewelry.proxy.StockStatisticsProxy', {
    extend: 'Ext.data.proxy.Rest',
    alias: 'proxy.stockStatisticsProxy',

    url: 'http://localhost:8080/jewelry-service/api/stocks/statistics',
    appendId: false,
    format: 'json',
    reader: {
        type: 'json'
    },
    writer: {
        type: 'json',
        writeAllFields: true
    }
});