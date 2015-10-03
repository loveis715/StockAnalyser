Ext.define('jewelry.proxy.LastFullDayScanTaskProxy', {
    extend: 'Ext.data.proxy.Rest',
    alias: 'proxy.lastFullDayScanTaskProxy',

    // TODO: From ExtJS document, seems proxy is a static config instead of a per instance one.
    // That is, each model is bound to a proxy. That's why we've create another proxy/model pair.
    // We need to figure out whether we have other way to deal with this case
    url: 'http://localhost:8080/jewelry-service/api/full_day_scan_tasks/last',
    appendId: false,
    format: 'json',
    reader: {
        type: 'json',
        rootProperty: 'fullDayScanTask'
    },
    writer: {
        type: 'json',
        writeAllFields: true
    }
});