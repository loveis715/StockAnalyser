Ext.define('jewelry.view.stock.StockPageViewModel', {
    extend: 'Ext.app.ViewModel',

    alias: 'viewmodel.stockPage',

    data: {
        stockName: '',
        stockCode: '',
        open: '',
        high: '',
        low: '',
        prevClose: '',
        tags: []
    }
});