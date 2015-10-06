Ext.define('jewelry.proxy.ScanTaskProxy', {
    extend: 'Ext.data.proxy.Rest',
    alias: 'proxy.scanTaskProxy',

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