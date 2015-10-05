Ext.define('jewelry.model.LastStockSyncingTaskModel', {
    extend: 'Ext.data.Model',
    requires: [
        'jewelry.proxy.LastStockSyncingTaskProxy'
    ],

    proxy: Ext.create('jewelry.proxy.LastStockSyncingTaskProxy'),

    idProperty: 'fakeId',
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