Ext.define('jewelry.model.StockModel', {
    extend: 'Ext.data.Model',

    fields: [{
        name: 'id',
        type: 'number'
    }, {
        name: 'name',
        type: 'string'
    }, {
        name: 'code',
        type: 'string'
    }, {
        name: 'stockCategory',
        type: 'string'
    }, {
        name: 'totalVolume',
        type: 'number'
    }]
});