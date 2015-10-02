Ext.define('jewelry.proxy.StockSyncingTaskProxy', {
    extend: 'Ext.data.proxy.Rest',
    alias: 'proxy.stockSyncingTaskProxy',

    url: 'http://localhost:8080/jewelry-service/api/stock_syncing_tasks',
    format: 'json',
    reader: {
        type: 'json',
        rootProperty: 'stockSyncingTask'
    },
    writer: {
        type: 'json',
        writeAllFields: true
    }
});