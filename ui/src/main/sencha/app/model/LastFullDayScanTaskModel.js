Ext.define('jewelry.model.LastFullDayScanTaskModel', {
    extend: 'Ext.data.Model',
    requires: [
        'jewelry.proxy.LastFullDayScanTaskProxy'
    ],

    proxy: Ext.create('jewelry.proxy.LastFullDayScanTaskProxy'),

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