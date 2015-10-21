Ext.define('jewelry.proxy.StockAnalyseTaskProxy', {
    extend: 'Ext.data.proxy.Rest',
    alias: 'proxy.stockAnalyseTaskProxy',

    url: 'http://localhost:8080/jewelry-service/api/analyse_tasks',
    format: 'json',
    reader: {
        type: 'json'
    },
    writer: {
        type: 'json',
        writeAllFields: true
    }
});