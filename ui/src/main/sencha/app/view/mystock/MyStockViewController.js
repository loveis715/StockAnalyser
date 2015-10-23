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

    onAddNote: function() {
    },

    onEditNote: function() {
    },

    onDeleteNote: function() {
    }
});