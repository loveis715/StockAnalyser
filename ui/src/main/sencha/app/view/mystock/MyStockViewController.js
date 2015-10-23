Ext.define('jewelry.view.mystock.MyStockViewController', {
    extend: 'Ext.app.ViewController',
    requires: [
        'jewelry.model.MyStockModel'
    ],

    alias: 'controller.myStockView',

    init: function() {
        var me = this,
            view = this.getView(),
            recordId = view.getConfig('recordId');
        jewelry.model.MyStockModel.load(recordId, {
            scope: me,
            success: function(record, operation) {
                view.editRecord(record);
            }
        });
    },

    onSave: function() {
        var view = this.getView(),
            record = view.getEditedRecord();
        record.save({
            callback: function() {
                view.fireEvent('editComplete');
            }
        });
    },

    onAddNote: function() {
        var view = this.getView();
        view.showNoteEditor();
    },

    onEditNote: function() {
        var view = this.getView(),
            noteList = view.lookupReference('noteList'),
            selection = noteList.getSelection();
        if (selection.length > 0) {
            view.showNoteEditor(selection[0]);
        }
    },

    onDeleteNote: function() {
        var view = this.getView(),
            noteList = view.lookupReference('noteList'),
            store = noteList.getStore(),
            selection = noteList.getSelection();
        if (selection.length > 0) {
            store.remove(selection[0]);
        }
    }
});