Ext.define('jewelry.view.stock.StockPage', {
    extend: 'Ext.panel.Panel',
    requires: [
        'jewelry.view.stock.StockPageController',
        'jewelry.view.stock.StockPageViewModel',
        'jewelry.view.charts.MinuteDataChart'
    ],

    xtype: 'jewelry.stockPage',
    controller: 'stockPage',
    viewModel: 'stockPage',

    layout: 'border',
    padding: '9 9 9 9',
    minHeight: 800,
    minWidth: 1200,

    tbar: [{
        xtype: 'textfield',
        reference: 'stockNameInput',
        width: 300,
        fieldLabel: jewelry.Messages.labels.stockAnalysis,
        emptyText: jewelry.Messages.messages.inputStockName
    }, {
        xypte: 'button',
        text: jewelry.Messages.labels.search,
        handler: 'onSearch'
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
    }, {
        xtype: 'panel',
        region: 'east',
        width: 488,
        height: '100%'
    }, {
        xtype: 'panel',
        region: 'center',
        height: '100%',
        layout: 'vbox',
        items: [{
            xtype: 'label',
            height: 50,
            style: 'font-size: 30px',
            // label is just a label element in html, which does not take the height style
            // into layout calculation. So it does not participate in layout calculation
            // properly, and will always be 12px height. Use this padding to adjust layout
            padding: '10 0 0 0',
            bind: {
                text: '{stockName}'
            }
        }, {
            xtype: 'panel',
            layout: {
                type: 'table',
                columns: 2
            },
            items: [{
                xtype: 'displayfield',
                fieldLabel: jewelry.Messages.labels.prevClose,
                labelWidth: 60,
                margin: '0 50 0 0',
                bind: {
                    value: '{prevClose}'
                }
            }, {
                xtype: 'displayfield',
                fieldLabel: jewelry.Messages.labels.open,
                labelWidth: 60,
                margin: '0 50 0 0',
                bind: {
                    value: '{open}'
                }
            }, {
                xtype: 'displayfield',
                fieldLabel: jewelry.Messages.labels.high,
                labelWidth: 60,
                margin: '0 50 0 0',
                bind: {
                    value: '{high}'
                }
            }, {
                xtype: 'displayfield',
                fieldLabel: jewelry.Messages.labels.low,
                labelWidth: 60,
                margin: '0 50 0 0',
                bind: {
                    value: '{low}'
                }
            }]
        }]
    }]
});