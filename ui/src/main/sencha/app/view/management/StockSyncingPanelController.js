Ext.define('jewelry.view.management.StockSyncingPanelController', {
    extend: 'Ext.app.ViewController',
    requires: [
        'jewelry.model.StockStatisticsModel',
        'jewelry.proxy.StockSyncingTaskProxy',
        'jewelry.model.StockSyncingTaskModel',
        'jewelry.proxy.LastStockSyncingTaskProxy',
        'jewelry.model.LastStockSyncingTaskModel'
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

        jewelry.model.LastStockSyncingTaskModel.load(0, {
            scope: me,
            success: function(record, operation) {
                var taskState = record.get('taskState');
                if (taskState == 'SCHEDULED' || taskState == 'IN_PROGRESS') {
                    var viewModel = this.getView().getViewModel();
                    viewModel.set('isSyncing', true);

                    var runner = new Ext.util.TaskRunner();
                    runner.start({
                        run: me.populateSyncingTaskState,
                        scope: me,
                        args: [record.get('id')],
                        interval: '1000',
                        repeat: false
                    });
                }
            }
        });
    },

    populateStockStatistics: function(record) {
        var viewModel = this.getView().getViewModel();
        viewModel.set('stockCountShanghai', record.get('stockCountSH'));
        viewModel.set('stockCountShenzhen', record.get('stockCountSZ'));

        var milliseconds = record.get('lastSyncTime');
        if (milliseconds > 0) {
            var date = new Date(milliseconds);
            viewModel.set('lastSyncTime', Ext.util.Format.date(date, 'm/d/Y h:iA'));
        }
    },

    startSyncStocks: function() {
        var me = this,
            model = Ext.create('jewelry.model.StockSyncingTaskModel');
        model.set('id', -1);
        model.save({
            scope: me,
            success: function(record, operation) {
                me.populateSyncingTaskState(record.get('id'));
            }
        });
    },

    populateSyncingTaskState: function(taskId) {
        var me = this;
        jewelry.model.StockSyncingTaskModel.load(taskId, {
            scope: me,
            success: function(record, operation) {
                me.fillTaskInfoToViewModel(record);

                var taskState = record.get('taskState');
                if (taskState == 'SCHEDULED' || taskState == 'IN_PROGRESS') {
                    var runner = new Ext.util.TaskRunner();
                    runner.start({
                        run: me.populateSyncingTaskState,
                        scope: me,
                        args: [taskId],
                        interval: '1000',
                        repeat: false
                    });
                }
            }
        });
    },

    fillTaskInfoToViewModel: function(record) {
        var taskState = record.get('taskState'),
            viewModel = this.getView().getViewModel(),
            isSyncing = (taskState == 'SCHEDULED' || taskState == 'IN_PROGRESS');
        viewModel.set('isSyncing', isSyncing);
        if (isSyncing) {
            var listingTaskForShanghai = record.get('listingTaskForShanghai'),
                listingTaskForShenzhen = record.get('listingTaskForShenzhen');
            if (listingTaskForShanghai.taskState == 'SCHEDULED'
                || listingTaskForShanghai.taskState == 'IN_PROGRESS') {
                viewModel.set('syncingMsg', jewelry.Messages.messages.sycingTaskForShanghai);
            } else if (listingTaskForShenzhen.taskState == 'SCHEDULED'
                || listingTaskForShenzhen.taskState == 'IN_PROGRESS') {
                viewModel.set('syncingMsg', jewelry.Messages.messages.sycingTaskForShenzhen);
            }
        } else {
            var me = this;
            viewModel.set('syncingMsg', '');
            jewelry.model.StockStatisticsModel.load(1, {
                scope: me,
                success: function(record, operation) {
                    me.populateStockStatistics(record);
                }
            });
        }
    }
});