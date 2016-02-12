Ext.define('jewelry.view.test.TestScanPageController', {
    extend: 'Ext.app.ViewController',

    alias: 'controller.testScanPage',

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