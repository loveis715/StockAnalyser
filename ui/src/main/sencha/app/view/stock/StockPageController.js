Ext.define('jewelry.view.stock.StockPageController', {
    extend: 'Ext.app.ViewController',
    requires: [
        'jewelry.model.StockTradingsModel',
        'jewelry.view.stock.StockTradingCache'
    ],

    alias: 'controller.stockPage',

    init: function() {
        if (jewelry.view.stock.StockTradingCache.tradings == null) {
            jewelry.model.StockTradingsModel.load('sh000001', {
                scope: this,
                success: function(record, operation) {
                    if (jewelry.view.stock.StockTradingCache.tradings == null) {
                        jewelry.view.stock.StockTradingCache.tradings = record.data;

                        var tradingInfos = record.get('tradingInfos'),
                            prevClose, tradingDate;
                        if (tradingInfos.length >= 2) {
                            prevClose = tradingInfos[tradingInfos.length - 2].close;
                        } else if (tradingInfos.length == 1) {
                            prevClose = tradingInfos[tradingInfos.length - 1].open;
                        } else {
                            return;
                        }

                        var view = this.getView(),
                            minuteDataChart = view.lookupReference('minuteDataChart');
                        minuteDataChart.renderMinutePrice(record.get('minuteDatas'), prevClose);
                    }
                }
            });
        }
    }
});