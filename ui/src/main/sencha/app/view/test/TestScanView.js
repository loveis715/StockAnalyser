Ext.define('jewelry.view.test.TestScanView', {
    extend: 'Ext.panel.Panel',
    requires: [
        'jewelry.proxy.TestScanTaskProxy',
        'jewelry.model.TestScanResultModel',
        'jewelry.view.test.TestScanViewViewModel',
        'jewelry.view.test.TestScanViewController'
    ],

    xtype: 'jewelry.testScanView',
    controller: 'testScanView',

    layout: 'border',
    bodyStyle: 'background-color: transparent',

    viewModel: 'testScanView',

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
                model: 'jewelry.model.TestScanResultModel',
                data: [],
                sorters: [{
                    property: 'stockCode',
                    direction: 'DESC'
                }, {
                    property: 'day',
                    direction: 'DESC'
                }],
            }),

            columns: [{
                text: jewelry.Messages.headers.stockName,
                dataIndex: 'stockName',
                flex: 1,
                renderer: function(value, metadata, record) {
                    var url = 'http://finance.sina.com.cn/realstock/company/' + record.get('stockCode') + '/nc.shtml';
                    return '<a href="' + url + '" target="_blank">' + value + '</a>';
                }
            }, {
                text: jewelry.Messages.headers.stockCode,
                dataIndex: 'stockCode',
                flex: 1,
                renderer: function(value) {
                    var url = 'http://finance.sina.com.cn/realstock/company/' + value + '/nc.shtml';
                    return '<a href="' + url + '" target="_blank">' + value + '</a>';
                }
            }, {
                text: jewelry.Messages.headers.stockCategory,
                dataIndex: 'stockCategory',
                flex: 1
            }, {
                text: jewelry.Messages.headers.day,
                dataIndex: 'day',
                flex: 1
            }, {
                text: jewelry.Messages.headers.priceChange,
                dataIndex: 'priceChange',
                flex: 1
            }, {
                text: jewelry.Messages.headers.tag,
                dataIndex: 'tag',
                flex: 2,
                renderer: function(tagString) {
                    return me.getTagHtml(tagString);
                }
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
    },

    getTagHtml: function(tagString) {
        var tags = tagString.split(';'),
            html = '<ul class="tag-cell" style="margin: -2px 0 -2px 0; padding: 0 3px 0 3px; cursor: text;">';
        Ext.Array.each(tags, function(tag) {
            var color = '',
                segments = tag.split(':'),
                tagName = jewelry.Messages.tagNames[segments[0]],
                text = tagName;
            if (segments[1] == '-1') {
                color = 'lightgreen';
            } else if (segments[1] == '1') {
                color = 'pink';
            } else {
                color = 'beige';
            }
            html = html + '<li class="tag-item" style="border-radius: 3px; border: 1px solid #dcdcdc; margin: 0 4px 0 0; padding: 0 5px 0 5px; display: inline-block; background-color: ' + color + ';">' + text + '</li>';
        });
        return html + '</ul>';
    }
});