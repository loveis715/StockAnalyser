Ext.define('jewelry.model.StockSyncingTaskModel', {
    extend: 'Ext.data.Model',
    requires: [
        'jewelry.proxy.StockSyncingTaskProxy'
    ],

    proxy: Ext.create('jewelry.proxy.StockSyncingTaskProxy'),

    fields: [{
        name: 'taskState',
        type: 'string'
    }, {
        name: 'percentage',
        type: 'number'
    }, {
        name: 'startTime',
        type: 'number'
    }, {
        name: 'endTime',
        type: 'number'
    }, {
        name: 'listingTaskForShanghai'
    }, {
        name: 'listingTaskForShenzhen'
    }]
});