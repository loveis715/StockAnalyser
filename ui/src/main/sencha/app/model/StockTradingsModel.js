Ext.define('jewelry.model.StockTradingsModel', {
    extend: 'Ext.data.Model',
    requires: [
        'jewelry.proxy.StockTradingsProxy'
    ],

    proxy: Ext.create('jewelry.proxy.StockTradingsProxy'),

    fields: [{
        name: 'stock',
        type: 'auto'
    }, {
        name: 'tradingInfos'
    }, {
        name: 'minuteDatas'
    }]
});