Ext.define('jewelry.model.StockTradingsModel', {
    extend: 'Ext.data.Model',

    fields: [{
        name: 'stock',
        type: 'auto'
    }, {
        name: 'tradingInfos'
    }, {
        name: 'minuteDatas'
    }]
});