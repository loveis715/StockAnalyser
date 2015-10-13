Ext.define('jewelry.view.stock.StockPage', {
    extend: 'Ext.panel.Panel',
    requires: [
        'jewelry.view.stock.StockPageController',
        'jewelry.view.charts.MinuteDataChart'
    ],

    xtype: 'jewelry.stockPage',
    controller: 'stockPage',

    layout: 'border',
    padding: '9 9 9 9',

    tbar: [{
        xtype: 'textfield',
        width: 300,
        fieldLabel: jewelry.Messages.labels.stockAnalysis,
        emptyText: jewelry.Messages.messages.inputStockName
    }, {
        xypte: 'button',
        text: jewelry.Messages.labels.search
    }, {
        xtype: 'button',
        width: 82,
        icon: '../../../resources/icon/add.png',
        text: jewelry.Messages.labels.addToMyStock,
        border: false,
        style: {
            backgroundColor: 'white'
        }
    }, {
        xtype: 'button',
        width: 82,
        margin: '0 90 0 -90',
        icon: '../../../resources/icon/remove.png',
        hidden: true,
        border: false,
        text: jewelry.Messages.labels.removeFromMyStock,
        style: {
            backgroundColor: 'white'
        }
    }],

    items: [{
        xtype: 'panel',
        region: 'east',
        layout: 'border',
        width: 488,
        height: '100%',
        items: [{
            xtype: 'jewelry.minuteDataChart',
            reference: 'minuteDataChart',
            region: 'north'
        }]
    }]
});