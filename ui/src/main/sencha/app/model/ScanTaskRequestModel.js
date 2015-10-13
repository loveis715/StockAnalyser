Ext.define('jewelry.model.ScanTaskRequestModel', {
    extend: 'Ext.data.Model',
    requires: [
        'jewelry.proxy.ScanTaskRequestProxy'
    ],

    proxy: Ext.create('jewelry.proxy.ScanTaskRequestProxy'),

    fields: [{
        name: 'scanType',
        type: 'string'
    }, {
        name: 'scanTaskId',
        type: 'number'
    }, {
        name: 'stockIds'
    }]
});