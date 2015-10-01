Ext.define('jewelry.model.StockStatisticsModel', {
    extend: 'Ext.data.Model',
    requires: [
        'jewelry.proxy.StockStatisticsProxy'
    ],

    proxy: Ext.create('jewelry.proxy.StockStatisticsProxy'),

    fields: [{
        name: 'stockCountSH',
        type: 'number'
    }, {
        name: 'stockCountSZ',
        type: 'number'
    }, {
        name: 'lastSyncTime',
        type: 'number'
    }]
});