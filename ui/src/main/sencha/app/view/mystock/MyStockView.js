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

    listeners: {
        onSave: 'onSave'
    },

    initComponent: function() {
        var me = this;

        this.bbar = ['->', {
            xtype: 'button',
            text: jewelry.Messages.labels.ok,
            handler: function() {
                me.fireEvent('onSave');
            }
        }, {
            xtype: 'button',
            text: jewelry.Messages.labels.cancel,
            handler: function() {
                me.fireEvent('editComplete');
            }
        }];

        this.items = [{
            xtype: 'label',
            region: 'north',
            reference: 'stockNameLabel',
            height: 50,
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
                rowBodyTpl: new Ext.XTemplate('<p>{content}</p>')
            }],
            store: Ext.create('Ext.data.Store', {
                model: 'jewelry.model.StockNoteModel',
                data: []
            })
        }];

        this.callParent(arguments);
    },

    editRecord: function(record) {
        this.editingRecord = record;

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
    },

    getEditedRecord: function() {
        var record = this.editingRecord,
            noteList = this.lookupReference('noteList'),
            noteStore = noteList.getStore(),
            stockNotes = [];
        noteStore.each(function(note) {
            stockNotes.push(note.data);
        });
        record.set('stockNotes', stockNotes);
        return record;
    },

    showNoteEditor: function(record) {
        var title = record != null ? record.title : '',
            content = record != null ? record.content : '',
            noteList = this.lookupReference('noteList'),
            noteStore = noteList.getStore(),
            dialog = Ext.create('Ext.window.Window', {
                referenceHolder: true,
                title: record != null ? jewelry.Messages.labels.addNote : jewelry.Messages.labels.editNote,
                width: 800,
                height: 500,
                padding: '9 9 9 9',
                modal: true,
                layout: 'border',
                bodyStyle: 'background-color: transparent',
                bbar: ['->', {
                    xtype: 'button',
                    text: jewelry.Messages.labels.ok,
                    handler: function() {
                        var titleField = dialog.lookupReference('titleField'),
                            contentField = dialog.lookupReference('contentField');
                        if (record != null) {
                            record.title = titleField.getValue();
                            record.content = contentField.getValue();
                        } else {
                            var record = noteStore.add({
                                title: titleField.getValue(),
                                content: contentField.getValue(),
                                noteCategory: 'POSITIVE',
                                addTime: new Date()
                            })[0];
                            record.set('id', -1);
                        }
                        dialog.hide();
                    }
                }, {
                    xtype: 'button',
                    text: jewelry.Messages.labels.cancel,
                    handler: function() {
                        dialog.hide();
                    }
                }],
                items: [{
                    xtype: 'textfield',
                    reference: 'titleField',
                    margin: '3 3 3 3',
                    value: title,
                    hideLabel: true,
                    region: 'north'
                }, {
                    xtype: 'textarea',
                    reference: 'contentField',
                    margin: '3 3 3 3',
                    value: content,
                    hideLabel: true,
                    region: 'center'
                }]
            });
        dialog.show();
    }
});