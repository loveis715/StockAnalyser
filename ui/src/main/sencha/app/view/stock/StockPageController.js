Ext.define('jewelry.view.stock.StockPageController', {
    extend: 'Ext.app.ViewController',
    requires: [
        'jewelry.proxy.StockProxy',
        'jewelry.proxy.StockTradingsProxy',
        'jewelry.view.stock.StockTradingCache'
    ],

    alias: 'controller.stockPage',

    init: function() {
        if (jewelry.view.stock.StockTradingCache.tradings == null) {
            this.retrieveTradings('sh000001');
        }
    },

    onSearch: function() {
        var me = this,
            view = this.getView(),
            stockNameInput = view.lookupReference('stockNameInput'),
            stockName = stockNameInput.getValue();
        if (stockName.length > 0) {
            var proxy = new jewelry.proxy.StockProxy({
                url: 'http://localhost:8080/jewelry-service/api/stocks/alias/' + stockName + '.json'
            });
            var operation = proxy.createOperation('read', {
                callback: function(records, operation, success) {
                    if (records != null && records.length > 0) {
                        var record = records[0],
                            viewModel = me.getViewModel(),
                            stockCode = record.get('stockCategory') == jewelry.Constants.stockCategory.SHANGHAI ? 'sh' + record.get('code') : 'sz' + record.get('code');
                        me.retrieveTradings(stockCode);
                    }
                }
            });
            proxy.read(operation);
        }
    },
    
    retrieveTradings: function(stockCode) {
        var me = this,
            proxy = new jewelry.proxy.StockTradingsProxy({
                url: 'http://localhost:8080/jewelry-service/api/stocks/' + stockCode + '/tradings'
            }),
            operation = proxy.createOperation('read', {
            callback: function(records, operation, success) {
                var record = records[0];
                if (success && jewelry.view.stock.StockTradingCache.tradings == null) {
                    jewelry.view.stock.StockTradingCache.tradings = record.data;

                    var view = me.getView(),
                        prevClose = me.getPrevClose(record.get('tradingInfos'), record.get('minuteDatas')),
                        minuteDataChart = view.lookupReference('minuteDataChart');
                    minuteDataChart.renderMinutePrice(record.get('minuteDatas'), prevClose);
                }
            }
        });
        proxy.read(operation);
    },

    getPrevClose: function(tradingInfos, minuteDatas) {
        if (minuteDatas != null && minuteDatas.length > 0) {
            // We have trading today
            var newOpen = minuteDatas[minuteDatas.length - 1].price;
            if (tradingInfos.length >= 2) {
                if (tradingInfos[tradingInfos.length - 1].open == newOpen) {
                    // Has end today's trading
                    return tradingInfos[tradingInfos.length - 2].close;
                } else {
                    // Has not end today's trading
                    return tradingInfos[tradingInfos.length - 1].close;
                }
            } else if (tradingInfos.length == 1) {
                return tradingInfos[tradingInfos.length - 1].open;
            } else {
                return 0;
            }
        } else {
            if (tradingInfos.length >= 2) {
                return tradingInfos[tradingInfos.length - 2].close;
            } else if (tradingInfos.length == 1) {
                return tradingInfos[tradingInfos.length - 1].open;
            } else {
                return 0;
            }
        }
    }
});