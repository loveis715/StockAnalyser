Ext.define('jewelry.model.LastScanTaskModel', {
    extend: 'Ext.data.Model',
    requires: [
        'jewelry.proxy.LastScanTaskProxy'
    ],

    proxy: Ext.create('jewelry.proxy.LastScanTaskProxy'),

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
        name: 'scanningStockName',
        type: 'string'
    }]
});