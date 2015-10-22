Ext.define('jewelry.view.mystock.MyStockPageController', {
    extend: 'Ext.app.ViewController',

    alias: 'controller.myStockPage',

    init: function() {
        var view = this.getView();
        view.switchToList();
    },

    onViewRecord: function(recordId) {
        var view = this.getView();
        view.switchToViewer(recordId);
    },

    onViewComplete: function() {
        var view = this.getView();
        view.switchToList();
    }
});