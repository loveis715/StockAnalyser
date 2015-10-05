Ext.define('jewelry.view.unusual.UnusualPage', {
    extend: 'Ext.panel.Panel',

    xtype: 'jewelry.unusualPage',

    layout: 'border',
    padding: '9 9 9 9',
    bodyStyle: 'background-color: transparent',

    items: [{
        xtype: 'panel',
        region: 'north',
        margin: '3 3 3 3',
        items: [{
            xtype: 'datefield',
            width: 500,
            labelWidth: 80,
            fieldLabel: jewelry.Messages.labels.selectDate,
            value: new Date()
        }]
    }, {
        xtype: 'grid',
        width: '100%',
        region: 'center',
        margin: '3 3 3 3',
        frame: true,
        columns: [{
            text: jewelry.Messages.headers.stockName,
            dataIndex: 'stockName',
            flex: 1
        }, {
            text: jewelry.Messages.headers.stockCode,
            dataIndex: 'stockCode',
            flex: 1
        }, {
            text: jewelry.Messages.headers.unusualCases,
            dataIndex: 'usualCases',
            flex: 4
        }]
    }]
});