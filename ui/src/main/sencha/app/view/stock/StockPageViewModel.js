Ext.define('jewelry.view.stock.StockPageViewModel', {
    extend: 'Ext.app.ViewModel',

    alias: 'viewmodel.stockPage',

    data: {
        stockName: '',
        open: '',
        high: '',
        low: '',
        prevClose: '',
        tags: '',
        stock: null
    }
});