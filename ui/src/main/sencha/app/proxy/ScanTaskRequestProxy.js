Ext.define('jewelry.proxy.ScanTaskRequestProxy', {
    extend: 'Ext.data.proxy.Rest',
    requires: [
        'jewelry.model.ScanTaskRequestModel'
    ],
    alias: 'proxy.scanTaskRequestProxy',

    model: 'jewelry.model.ScanTaskRequestModel',
    url: 'http://localhost:8080/jewelry-service/api/scan_tasks',
    format: 'json',
    reader: {
        type: 'json'
    },
    writer: {
        type: 'json',
        writeAllFields: true
    }
});