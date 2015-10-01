Ext.define('jewelry.view.management.StockSyncingPanelController', {
    extend: 'Ext.app.ViewController',
    requires: [
        'jewelry.model.StockStatisticsModel',
        'jewelry.proxy.StockSyncingTaskProxy',
        'jewelry.model.StockSyncingTaskModel'
    ],

    alias: 'controller.stockSyncingPanel',

    init: function() {
        var me = this;
        jewelry.model.StockStatisticsModel.load(1, {
            scope: me,
            success: function(record, operation) {
                me.populateStockStatistics(record);
            }
        });
    },

    populateStockStatistics: function(record) {
        var viewModel = this.getView().getViewModel();
        viewModel.set('stockCountShanghai', record.get('stockCountSH'));
        viewModel.set('stockCountShenzhen', record.get('stockCountSZ'));
        viewModel.set('lastSyncTime', record.get('lastSyncTime'));
    },

    startSyncStocks: function() {
        var me = this,
            model = Ext.create('jewelry.model.StockSyncingTaskModel');
        model.set('id', -1);
        model.save();
    }
});