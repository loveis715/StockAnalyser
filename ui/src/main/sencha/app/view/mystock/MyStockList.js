Ext.define('jewelry.view.mystock.MyStockList', {
    extend: 'Ext.grid.Panel',
    requires: [
        'jewelry.proxy.MyStockProxy',
        'jewelry.store.MyStockStore'
    ],

    xtype: 'jewelry.myStockList',

    frame: true,
    autoLoad: true,

    // TODO: Deal with this warning
    store: Ext.create('jewelry.store.MyStockStore'),

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
            text: jewelry.Messages.headers.stockName,
            dataIndex: 'stockName',
            flex: 1
        }, {
            text: jewelry.Messages.headers.stockCode,
            dataIndex: 'stockCode',
            flex: 1
        }, {
            text: jewelry.Messages.headers.addTime,
            dataIndex: 'formattedAddTime',
            flex: 1
        }, {
            text: jewelry.Messages.headers.noteCount,
            dataIndex: 'formattedEndTime',
            flex: 1
        }];

        this.callParent(arguments);
    }
});