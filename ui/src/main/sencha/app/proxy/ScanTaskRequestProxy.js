Ext.define('jewelry.proxy.ScanTaskRequestProxy', {
    extend: 'Ext.data.proxy.Rest',
    alias: 'proxy.scanTaskRequestProxy',

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