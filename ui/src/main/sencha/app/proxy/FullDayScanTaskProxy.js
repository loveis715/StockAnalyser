Ext.define('jewelry.proxy.FullDayScanTaskProxy', {
    extend: 'Ext.data.proxy.Rest',
    alias: 'proxy.fullDayScanTaskProxy',

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