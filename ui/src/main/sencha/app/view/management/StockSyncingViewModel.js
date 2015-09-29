Ext.define('jewelry.view.management.StockSyncingViewModel', {
    extend: 'Ext.app.ViewModel',

    alias: 'viewmodel.stockSynching',

    data: {
        stockCountShanghai: 0,
        stockCountShenzhen: 0,
        lastSyncTime: '',
        isSyncing: false,
        syncingMsg: ''
    }
});