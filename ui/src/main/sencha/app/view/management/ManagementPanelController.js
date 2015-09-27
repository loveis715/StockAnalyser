Ext.define('morpho.view.management.ManagementPanelController', {
    extend: 'Ext.app.ViewController',

    alias: 'controller.management',

    onActivate: function() {
        var view = this.getView(),
            shanghaiVM = view.lookupReference('shanghaiStock').getViewModel();
        shanghaiVM.set('stockCategory', jewelry.Constants.stockCategory.SHANGHAI);

        var shenzhenVM = view.lookupReference('shenzhenStock').getViewModel();
        shenzhenVM.set('stockCategory', jewelry.Constants.stockCategory.SHENZHEN);
    }
});