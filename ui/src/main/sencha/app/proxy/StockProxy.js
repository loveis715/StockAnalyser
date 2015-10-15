Ext.define('jewelry.proxy.StockProxy', {
    extend: 'Ext.data.proxy.Ajax',
    requires: [
        'jewelry.model.StockModel'
    ],
    alias: 'proxy.stockProxy',

    model: 'jewelry.model.StockModel',
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