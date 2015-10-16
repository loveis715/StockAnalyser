Ext.define('jewelry.model.ScanTaskRequestModel', {
    extend: 'Ext.data.Model',

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