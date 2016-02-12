Ext.define('jewelry.proxy.TestScanTaskRequestProxy', {
    extend: 'Ext.data.proxy.Rest',
    requires: [
        'jewelry.model.TestScanTaskRequestModel'
    ],
    alias: 'proxy.testScanTaskRequestProxy',

    model: 'jewelry.model.TestScanTaskRequestModel',
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