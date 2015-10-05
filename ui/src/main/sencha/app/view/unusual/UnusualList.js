Ext.define('jewelry.view.unusual.UnusualList', {
    extend: 'Ext.grid.Panel',
    requires: [
        'jewelry.proxy.FullDayScanTaskProxy',
        'jewelry.store.FullDayScanTaskStore'
    ],

    xtype: 'jewelry.unusualList',

    frame: true,
    autoLoad: true,

    // TODO: Deal with this warning
    store: Ext.create('jewelry.store.FullDayScanTaskStore'),

    columns: [{
        text: jewelry.Messages.headers.startTime,
        dataIndex: 'formattedStartTime',
        flex: 1
    }, {
        text: jewelry.Messages.headers.endTime,
        dataIndex: 'formattedEndTime',
        flex: 1
    }, {
        text: jewelry.Messages.headers.taskStatus,
        dataIndex: 'taskState',
        flex: 1
    }, {
        text: jewelry.Messages.headers.unusualCount,
        dataIndex: 'unusualCount',
        flex: 1
    }]
});