Ext.define('jewelry.view.unusual.UnusualList', {
    extend: 'Ext.grid.Panel',
    requires: [
        'jewelry.proxy.ScanTaskProxy',
        'jewelry.store.ScanTaskStore'
    ],

    xtype: 'jewelry.unusualList',

    frame: true,
    autoLoad: true,

    // TODO: Deal with this warning
    store: Ext.create('jewelry.store.ScanTaskStore'),

    initComponent: function() {
        var me = this;

        this.columns = [{
            xtype: 'actioncolumn',
            width: 26,
            items: [{
                tooltip: jewelry.Messages.messages.view,
                icon: '../../../resources/icon/view.png',
                handler: function(view, rowIndex, colIndex, item, e, record) {
                    me.fireEvent('editRecord', record.get('id'));
                }
            }]
        }, {
            text: jewelry.Messages.headers.startTime,
            dataIndex: 'formattedStartTime',
            flex: 1
        }, {
            text: jewelry.Messages.headers.endTime,
            dataIndex: 'formattedEndTime',
            flex: 1
        }, {
            text: jewelry.Messages.headers.scanType,
            dataIndex: 'scanType',
            flex: 1
        }, {
            text: jewelry.Messages.headers.taskStatus,
            dataIndex: 'taskState',
            flex: 1
        }, {
            text: jewelry.Messages.headers.unusualCount,
            dataIndex: 'unusualCount',
            flex: 1
        }];

        this.callParent(arguments);
    }
});