Ext.define('jewelry.proxy.StockAnalyseTaskRequestProxy', {
    extend: 'Ext.data.proxy.Rest',
    requires: [
        'jewelry.model.StockAnalyseTaskRequestModel'
    ],
    alias: 'proxy.stockAnalyseTaskRequestProxy',

    model: 'jewelry.model.StockAnalyseTaskRequestModel',
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