Ext.define('jewelry.proxy.TestScanTaskProxy', {
    extend: 'Ext.data.proxy.Rest',
    alias: 'proxy.testScanTaskProxy',

    url: 'http://localhost:8080/jewelry-service/api/test_scan_tasks',
    format: 'json',
    reader: {
        type: 'json'
    },
    writer: {
        type: 'json',
        writeAllFields: true
    }
});