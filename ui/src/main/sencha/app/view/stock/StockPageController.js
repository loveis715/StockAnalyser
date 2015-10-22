Ext.define('jewelry.view.stock.StockPageController', {
    extend: 'Ext.app.ViewController',
    requires: [
        'jewelry.proxy.ScanTaskRequestProxy',
        'jewelry.model.ScanTaskRequestModel',
        'jewelry.proxy.StockProxy',
        'jewelry.proxy.StockTradingsProxy',
        'jewelry.proxy.StockAnalyseTaskProxy',
        'jewelry.model.StockAnalyseTaskModel',
        'jewelry.model.StockAnalyseTaskRequestModel',
        'jewelry.proxy.StockAnalyseTaskRequestProxy',
        'jewelry.store.MyStockStore',
        'jewelry.view.stock.StockTradingCache'
    ],

    alias: 'controller.stockPage',

    init: function() {
        if (jewelry.view.stock.StockTradingCache.tradings == null) {
            this.retrieveTradings('sh000001');
        }
        if (jewelry.view.stock.StockTradingCache.myStocks == null) {
            var myStockStore = Ext.create('jewelry.store.MyStockStore');
            myStockStore.load();
            jewelry.view.stock.StockTradingCache.myStocks = myStockStore;
        }
    },

    onSearch: function() {
        var me = this,
            view = this.getView(),
            viewModel = this.getViewModel(),
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
                        viewModel.set('stock', record.data);
                        me.analysisStock(stockCode);
                        me.retrieveTradings(stockCode);
                        me.updateMyStockButton(stockCode);
                    }
                }
            });
            proxy.read(operation);
        }
    },

    analysisStock: function(stockCode) {
        var me = this,
            proxy = new jewelry.proxy.StockAnalyseTaskRequestProxy(),
            request = Ext.create('jewelry.model.StockAnalyseTaskRequestModel', {
                analyseTaskId: -1,
                stockCode: stockCode
            }),
            operation = proxy.createOperation('create', {
                records: [request],
                scope: me,
                callback: function(records, operation, success) {
                    var record = records[0];
                    me.populateStockAnalysisState(record.get('analyseTaskId'));
                }
            });
        proxy.read(operation);
    },

    populateStockAnalysisState: function(taskId) {
        var me = this;
        jewelry.model.StockAnalyseTaskModel.load(taskId, {
            scope: me,
            success: function(record, operation) {
                var taskState = record.get('taskState');
                if (taskState == 'SCHEDULED' || taskState == 'IN_PROGRESS') {
                    var task = new Ext.util.DelayedTask(function() {
                        me.populateStockAnalysisState(taskId);
                    });
                    task.delay(1000);
                } else if (taskState == 'SUCCESS') {
                    var tags = record.get('resultTags'),
                        viewModel = me.getViewModel();
                    viewModel.set('tags', tags);
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
    },

    updateMyStockButton: function(stockCode) {
        var exists = false;
        jewelry.view.stock.StockTradingCache.myStocks.each(function(record) {
            if (record.get('code') == stockCode) {
                exists = true;
                return false;
            }
        });

        var view = this.getView(),
            addButton = view.lookupReference('addButton'),
            removeButton = view.lookupReference('removeButton');
        if (exists) {
            addButton.hide();
            removeButton.show();
        } else {
            removeButton.hide();
            addButton.show();
        }
    },

    onAddToMyStock: function() {
        var viewModel = this.getViewModel(),
            myStockStore = jewelry.view.stock.StockTradingCache.myStocks,
            myStock = Ext.create('jewelry.model.MyStockModel', {
                id: -1,
                addTime: new Date(),
                stock: viewModel.get('stock')
            });
        myStockStore.add(myStock);
        myStockStore.sync();
        this.updateMyStockButton(viewModel.get('stock').code);
    },

    onRemoveFromMyStock: function() {
        var viewModel = this.getViewModel(),
            myStockStore = jewelry.view.stock.StockTradingCache.myStocks,
            stock = viewModel.get('stock'),
            index = myStockStore.findBy(function(record) {
                if (record.get('stock').code == stock.code) {
                    return true;
                } else {
                    return false;
                }
            });
        if (index != -1) {
            myStockStore.removeAt(index);
            myStockStore.sync();
            this.updateMyStockButton(stock.code);
        }
    }
});