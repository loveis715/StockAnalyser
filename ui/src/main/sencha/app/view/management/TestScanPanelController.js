Ext.define('jewelry.view.management.TestScanPanelController', {
    extend: 'Ext.app.ViewController',
    requires: [
        'jewelry.proxy.TestScanTaskRequestProxy',
        'jewelry.proxy.TestScanTaskProxy',
        'jewelry.model.TestScanTaskModel',
        'jewelry.proxy.LastScanTaskProxy',
        'jewelry.model.LastScanTaskModel'
    ],

    alias: 'controller.testScanPanel',

    init: function() {
        var me = this;
        jewelry.model.LastScanTaskModel.load(0, {
            scope: me,
            success: function(record, operation) {
                var taskState = record.get('taskState'),
                    viewModel = this.getView().getViewModel();
                if (taskState == 'SCHEDULED' || taskState == 'IN_PROGRESS') {
                    viewModel.set('isSyncing', true);

                    var task = new Ext.util.DelayedTask(function() {
                        me.populateTestScanTaskState(record.get('id'));
                    });
                    task.delay(1000);
                } else {
                    viewModel.set('isScaning', false);
                    viewModel.set('unusualCount', 0);
                    viewModel.set('scanMsg', '');

                    var milliseconds = record.get('endTime');
                    if (milliseconds > 0) {
                        var date = new Date(milliseconds);
                        viewModel.set('lastScanTime', Ext.util.Format.date(date, 'm/d/Y h:iA'));
                    }
                }
            }
        });
    },

    startTestScan: function() {
        var me = this,
            proxy = new jewelry.proxy.TestScanTaskRequestProxy(),
            request = Ext.create('jewelry.model.TestScanTaskRequestModel', {
                testScanTaskId: -1
            }),
            operation = proxy.createOperation('create', {
                records: [request],
                scope: me,
                callback: function(records, operation, success) {
                    var record = records[0];
                    me.populateTestScanTaskState(record.get('testScanTaskId'));
                }
            });
        proxy.read(operation);
    },

    populateTestScanTaskState: function(taskId) {
        var me = this;
        jewelry.model.TestScanTaskModel.load(taskId, {
            scope: me,
            success: function(record, operation) {
                me.fillTaskInfoToViewModel(record);

                var taskState = record.get('taskState');
                if (taskState == 'SCHEDULED' || taskState == 'IN_PROGRESS') {
                    var task = new Ext.util.DelayedTask(function() {
                        me.populateTestScanTaskState(taskId);
                    });
                    task.delay(1000);
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
            viewModel.set('unusualCount', 0);

            var scanMsg = Ext.String.format(jewelry.Messages.messages.currentProgress, record.get('percentage'), record.get('scanningStockName'));
            viewModel.set('scanMsg', scanMsg);
        } else {
            viewModel.set('scanMsg', '');

            var milliseconds = record.get('endTime');
            if (milliseconds > 0) {
                var date = new Date(milliseconds);
                viewModel.set('lastScanTime', Ext.util.Format.date(date, 'm/d/Y h:iA'));
            }
        }
    }
});