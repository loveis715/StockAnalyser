Ext.define('jewelry.view.management.StockSyncingViewModel', {
    extend: 'Ext.app.ViewModel',

    alias: 'viewmodel.stockSynching',

    data: {
        stockCategory: '',
        stockCount: 0,
        lastSyncTime: '',
        isSyncing: false,
        syncingMsg: ''
    },

    formulas: {
        labelText: function (get) {
            var category = get('stockCategory');
            if (category == jewelry.Constants.stockCategory.SHANGHAI) {
                return jewelry.Messages.labels.shanghaiStockName;
            } else if (category == jewelry.Constants.stockCategory.SHENZHEN) {
                return jewelry.Messages.labels.shenzhenStockName;
            }
        }
    }
});