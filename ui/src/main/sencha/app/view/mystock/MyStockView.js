Ext.define('jewelry.view.mystock.MyStockView', {
    extend: 'Ext.panel.Panel',
    requires: [
        'jewelry.proxy.MyStockProxy',
        'jewelry.model.MyStockModel',
        'jewelry.model.StockNoteModel',
        'jewelry.view.mystock.MyStockViewController'
    ],

    xtype: 'jewelry.myStockView',
    controller: 'myStockView',

    bodyStyle: 'background-color: transparent',
    layout: 'border',

    config: {
        recordId: null
    },

    initComponent: function() {
        var me = this;

        this.bbar = ['->', {
            xtype: 'button',
            text: jewelry.Messages.labels.ok,
            handler: function() {
                me.fireEvent('viewComplete');
            }
        }, {
            xtype: 'button',
            text: jewelry.Messages.labels.cancel,
            handler: function() {
                me.fireEvent('viewComplete');
            }
        }];

        this.items = [{
            xtype: 'label',
            region: 'north',
            reference: 'stockNameLabel',
            height: 60,
            style: 'font-size: 30px',
            // label is just a label element in html, which does not take the height style
            // into layout calculation. So it does not participate in layout calculation
            // properly, and will always be 12px height. Use this padding to adjust layout
            padding: '10 0 0 0',
        }, {
            xtype: 'gridpanel',
            region: 'center',
            reference: 'noteList',
            viewConfig: {
                markDirty: false
            },
            tbar: [{
                xtype: 'button',
                text: jewelry.Messages.labels.add,
                handler: 'onAddNote'
            }, {
                xtype: 'button',
                text: jewelry.Messages.labels.edit,
                handler: 'onEditNote'
            }, {
                xtype: 'button',
                text: jewelry.Messages.labels.delete,
                handler: 'onDeleteNote'
            }],
            columns: [{
                header: jewelry.Messages.headers.addTime,
                flex: 1,
                dataIndex: 'addTime'
            }, {
                header: jewelry.Messages.headers.title,
                flex: 1,
                dataIndex: 'title'
            }, {
                header: jewelry.Messages.headers.noteCategory,
                flex: 1,
                dataIndex: 'noteCategory'
            }],
            plugins: [{
                ptype: 'rowexpander',
                rowBodyTpl : new Ext.XTemplate('<p>{content}</p>')
            }],
            store: Ext.create('Ext.data.Store', {
                model: 'jewelry.model.StockNoteModel',
                data: []
            })
        }];

        this.callParent(arguments);
    },

    editRecord: function(record) {
        var stock = record.get('stock'),
            stockNameLabel = this.lookupReference('stockNameLabel'),
            stockName = stock != null ? stock.name : '';
        stockNameLabel.setText(stockName);
        
        var stockNotes = record.get('stockNotes'),
            noteList = this.lookupReference('noteList'),
            noteStore = noteList.getStore();
        noteStore.removeAll();
        Ext.Array.forEach(stockNotes, function(record) {
            noteStore.add(record);
        });
    }
});