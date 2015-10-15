Ext.define('jewelry.view.stock.StockPageController', {
    extend: 'Ext.app.ViewController',
    requires: [
        'jewelry.proxy.StockTradingsProxy',
        'jewelry.view.stock.StockTradingCache'
    ],

    alias: 'controller.stockPage',

    init: function() {
        var me = this;
        if (jewelry.view.stock.StockTradingCache.tradings == null) {
            var proxy = new jewelry.proxy.StockTradingsProxy({
                url: 'http://localhost:8080/jewelry-service/api/stocks/sh000001/tradings'
            });
            var operation = proxy.createOperation('read', {
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
            /*jewelry.model.StockTradingsModel.load('sh000001', {
                scope: this,
                success: function(record, operation) {
                    if (jewelry.view.stock.StockTradingCache.tradings == null) {
                        jewelry.view.stock.StockTradingCache.tradings = record.data;

                        var view = this.getView(),
                            prevClose = me.getPrevClose(record.get('tradingInfos'), record.get('minuteDatas')),
                            minuteDataChart = view.lookupReference('minuteDataChart');
                        minuteDataChart.renderMinutePrice(record.get('minuteDatas'), prevClose);
                    }
                }
            });*/
        }
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