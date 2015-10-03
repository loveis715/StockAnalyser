Ext.define('jewelry.model.FullDayScanTaskModel', {
    extend: 'Ext.data.Model',
    requires: [
        'jewelry.proxy.FullDayScanTaskProxy'
    ],

    proxy: Ext.create('jewelry.proxy.FullDayScanTaskProxy'),

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