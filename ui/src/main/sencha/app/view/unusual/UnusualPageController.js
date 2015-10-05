Ext.define('jewelry.view.unusual.UnusualPageController', {
    extend: 'Ext.app.ViewController',

    alias: 'controller.unusualPage',

    init: function() {
        var view = this.getView();
        view.switchToList();
    }
});