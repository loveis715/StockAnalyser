Ext.define('jewelry.view.unusual.UnusualView', {
    extend: 'Ext.panel.Panel',
    requires: [
        'jewelry.proxy.ScanTaskProxy',
        'jewelry.model.ScanResultModel',
        'jewelry.view.unusual.UnusualViewViewModel',
        'jewelry.view.unusual.UnusualViewController'
    ],

    xtype: 'jewelry.unusualView',
    controller: 'unusualView',

    layout: 'border',
    bodyStyle: 'background-color: transparent',

    viewModel: 'unusualView',

    config: {
        recordId: null
    },

    initComponent: function() {
        var me = this;

        this.items = [{
            xtype: 'panel',
            region: 'north',

            width: '100%',
            margin: '3 3 3 3',
            layout: {
                type: 'table',
                columns: 3
            },

            items: [{
                xtype: 'displayfield',
                width: '100%',
                fieldLabel: jewelry.Messages.headers.startTime,
                bind: {
                    value: '{startTime}'
                }
            }, {
                xtype: 'displayfield',
                width: '100%',
                fieldLabel: jewelry.Messages.headers.endTime,
                bind: {
                    value: '{endTime}'
                }
            }, {
                xtype: 'displayfield',
                width: '100%',
                fieldLabel: jewelry.Messages.headers.scanType,
                bind: {
                    value: '{scanType}'
                }
            }, {
                xtype: 'displayfield',
                width: '100%',
                fieldLabel: jewelry.Messages.headers.taskStatus,
                bind: {
                    value: '{taskState}'
                }
            }, {
                xtype: 'displayfield',
                width: '100%',
                fieldLabel: jewelry.Messages.headers.unusualCount,
                bind: {
                    value: '{unusualCount}'
                }
            }]
        }, {
            xtype: 'grid',
            region: 'center',
            margin: '3 3 3 3',
            frame: true,
            reference: 'resultGrid',

            store: Ext.create('Ext.data.Store', {
                model: 'jewelry.model.ScanResultModel',
                data: []
            }),

            columns: [{
                text: jewelry.Messages.headers.stockName,
                dataIndex: 'stockName',
                flex: 1
            }, {
                text: jewelry.Messages.headers.stockCode,
                dataIndex: 'stockCode',
                flex: 1
            }, {
                text: jewelry.Messages.headers.stockCategory,
                dataIndex: 'stockCategory',
                flex: 1
            }, {
                text: jewelry.Messages.headers.unusualCases,
                dataIndex: 'unusualCases',
                flex: 6
            }]
        }];

        this.bbar = ['->', {
            xtype: 'button',
            text: jewelry.Messages.labels.ok,
            handler: function() {
                me.fireEvent('viewComplete');
            }
        }];

        this.callParent(arguments);
    }
});