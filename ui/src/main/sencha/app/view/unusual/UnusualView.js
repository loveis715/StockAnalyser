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
                text: jewelry.Messages.headers.score,
                dataIndex: 'score',
                flex: 1
            }, {
                text: jewelry.Messages.headers.unusualCases,
                dataIndex: 'unusualCases',
                flex: 6,
                renderer: function(tagString) {
                    return me.getTagHtml(tagString);
                }
            }]
        }];

        this.bbar = ['->', {
            xtype: 'button',
            text: jewelry.Messages.labels.showAsText,
            handler: function() {
                me.showAsText();
            }
        }, {
            xtype: 'button',
            text: jewelry.Messages.labels.ok,
            handler: function() {
                me.fireEvent('viewComplete');
            }
        }];

        this.callParent(arguments);
    },

    showAsText: function() {
        var text = this.getTextRepresentation(),
            dialog = Ext.create('Ext.window.Window', {
                title: jewelry.Messages.labels.showAsText,
                width: 800,
                height: 500,
                modal: true,
                layout: 'border',
                bbar: ['->', {
                    xtype: 'button',
                    text: jewelry.Messages.labels.ok,
                    handler: function() {
                        dialog.hide();
                    }
                }],
                items: [{
                    xtype: 'textarea',
                    value: text,
                    editable: false,
                    hideLabel: true,
                    region: 'center'
                }]
            });
        dialog.show();
    },

    getTextRepresentation: function() {
        var text = '',
            viewModel = this.getViewModel(),
            startTime = viewModel.get('startTime'),
            endTime = viewModel.get('endTime'),
            scanType = viewModel.get('scanType'),
            taskState = viewModel.get('taskState'),
            unusualCount = viewModel.get('unusualCount'),
            results = viewModel.get('results');
        text += jewelry.Messages.formatters.startTime + startTime + jewelry.Messages.formatters.separator;
        text += jewelry.Messages.formatters.endTime + endTime + jewelry.Messages.formatters.separator;
        text += jewelry.Messages.formatters.scanType + jewelry.Messages.scanTypes.FULL_SCAN + jewelry.Messages.formatters.return;
        text += jewelry.Messages.formatters.taskState + jewelry.Messages.taskStates[taskState] + jewelry.Messages.formatters.separator;
        text += jewelry.Messages.formatters.unusualCount + unusualCount + jewelry.Messages.formatters.return;

        Ext.Array.each(results, function(result) {
            var stockName = result.stockName;
            text += stockName + jewelry.Messages.formatters.separator;

            var stockCode = (result.stockCategory == 'SHANGHAI' ? 'sh' : 'sz') + result.stockCode;
            text += stockCode + jewelry.Messages.formatters.separator;

            var score = result.score;
            text += score + jewelry.Messages.formatters.separator;

            var tagString = '',
                tags = result.unusualCases.split(';');
            Ext.Array.each(tags, function(tag) {
                var segments = tag.split(':'),
                    tagName = jewelry.Messages.tagNames[segments[0]],
                    text = tagName + ':' + segments[2];
                if (tagString.length > 0) {
                    tagString += ';';
                }
                tagString += text;
            });
            text += tagString + jewelry.Messages.formatters.return;
        });
        return text;
    },
    
    getTagHtml: function(tagString) {
        var tags = tagString.split(';'),
            html = '<ul class="tag-cell" style="margin: -2px 0 -2px 0; padding: 0 3px 0 3px; cursor: text;">';
        Ext.Array.each(tags, function(tag) {
            var color = '',
                segments = tag.split(':'),
                tagName = jewelry.Messages.tagNames[segments[0]],
                text = tagName + ':' + segments[2];
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