Ext.define('jewelry.proxy.StockSyncingTaskProxy', {
    extend: 'Ext.data.proxy.Ajax',
    alias: 'proxy.stockSyncingTaskProxy',

    url: 'http://localhost:8080/jewelry-service/api/stock_syncing_tasks.json',
    reader: {
        type: 'json',
        rootProperty: 'stockSyncingTask'
    },
    writer: {
        type: 'json',
        writeAllFields: true
    }
});