Ext.define('jewelry.view.stock.StockPageController', {
    extend: 'Ext.app.ViewController',
    requires: [
        'jewelry.proxy.ScanTaskRequestProxy',
        'jewelry.model.ScanTaskRequestModel',
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
                            stockCode = record.get('code');
                        me.analysisStock(stockCode);
                        me.retrieveTradings(stockCode);
                    }
                }
            });
            proxy.read(operation);
        }
    },

    analysisStock: function(stockCode) {
        var me = this,
            proxy = new jewelry.proxy.ScanTaskRequestProxy(),
            request = Ext.create('jewelry.model.ScanTaskRequestModel', {
                scanType: jewelry.Constants.scanTypes.SINGLE_STOCK,
                scanTaskId: -1,
                stockCodes: [stockCode]
            }),
            operation = proxy.createOperation('create', {
                records: [request],
                scope: me,
                callback: function(records, operation, success) {
                    var record = records[0];
                    me.populateStockAnalysisState(record.get('scanTaskId'));
                }
            });
        proxy.read(operation);
    },

    populateStockAnalysisState: function(taskId) {
        var me = this;
        jewelry.model.ScanTaskModel.load(taskId, {
            scope: me,
            success: function(record, operation) {
                var taskState = record.get('taskState');
                if (taskState == 'SCHEDULED' || taskState == 'IN_PROGRESS') {
                    var runner = new Ext.util.TaskRunner();
                    runner.start({
                        run: me.populateStockAnalysisState,
                        scope: me,
                        args: [taskId],
                        interval: '1000',
                        repeat: false
                    });
                } else if (taskState == 'SUCCESS') {
                    var results = record.get('results'),
                        viewModel = me.getViewModel();
                    viewModel.set('tags', results[0].tags);
                }
            }
        });
    },
    
    retrieveTradings: function(stockCode) {
        var me = this,
            proxy = new jewelry.proxy.StockTradingsProxy({
                url: 'http://localhost:8080/jewelry-service/api/stocks/' + stockCode + '/tradings'
            }),
            operation = proxy.createOperation('read', {
            callback: function(records, operation, success) {
                var record = records[0];
                if (success) {
                    jewelry.view.stock.StockTradingCache.tradings = record.data;

                    var view = me.getView(),
                        prevClose = me.getPrevClose(record.get('tradingInfos'), record.get('minuteDatas')),
                        minuteDataChart = view.lookupReference('minuteDataChart');
                    minuteDataChart.renderMinutePrice(record.get('minuteDatas'), prevClose);
                    me.renderStockInfo();
                }
            }
        });
        proxy.read(operation);
    },

    renderStockInfo: function() {
        var data = jewelry.view.stock.StockTradingCache.tradings;
        if (data.minuteDatas != null && data.minuteDatas.length > 0) {
            var high = data.minuteDatas[0].price,
                low = data.minuteDatas[0].price,
                viewModel = this.getViewModel();
            viewModel.set('open', data.minuteDatas[0].price);
            Ext.Array.each(data.minuteDatas, function(minuteData) {
                if (minuteData.price > high) {
                    high = minuteData.price;
                }
                if (minuteData.price < low) {
                    low = minuteData.price;
                }
            });
            viewModel.set('high', high);
            viewModel.set('low', low);
            if (data.tradingInfos != null) {
                if (data.tradingInfos.length > 1) {
                    viewModel.set('prevClose', data.tradingInfos[data.tradingInfos.length - 2].close);
                } else if (data.tradingInfos.length > 0) {
                    viewModel.set('prevClose', data.tradingInfos[data.tradingInfos.length - 1].open);
                }
            }
        }

        if (data.stock != null) {
            viewModel.set('stockName', data.stock.name + '(' + data.stock.code + ')');
        } else {
            viewModel.set('stockName', '');
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