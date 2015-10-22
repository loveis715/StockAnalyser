Ext.define('jewelry.view.management.HalfDayScanPanelController', {
    extend: 'Ext.app.ViewController',
    requires: [
        'jewelry.proxy.ScanTaskRequestProxy',
        'jewelry.proxy.ScanTaskProxy',
        'jewelry.model.ScanTaskModel',
        'jewelry.proxy.LastScanTaskProxy',
        'jewelry.model.LastScanTaskModel'
    ],

    alias: 'controller.halfDayScanPanel',

    init: function() {
        var me = this;
        jewelry.model.LastScanTaskModel.load(0, {
            scope: me,
            success: function(record, operation) {
                var taskState = record.get('taskState'),
                    viewModel = this.getView().getViewModel();
                if (taskState == 'SCHEDULED' || taskState == 'IN_PROGRESS') {
                    viewModel.set('isSyncing', true);

                    var runner = new Ext.util.TaskRunner();
                    runner.start({
                        run: me.populateHalfDayScanTaskState,
                        scope: me,
                        args: [record.get('id')],
                        interval: '1000',
                        repeat: false
                    });
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

    startHalfDayScan: function() {
        var me = this,
            proxy = new jewelry.proxy.ScanTaskRequestProxy(),
            request = Ext.create('jewelry.model.ScanTaskRequestModel', {
                scanType: jewelry.Constants.scanTypes.HALF_DAY,
                scanTaskId: -1
            }),
            operation = proxy.createOperation('create', {
                records: [request],
                scope: me,
                callback: function(records, operation, success) {
                    var record = records[0];
                    me.populateHalfDayScanTaskState(record.get('scanTaskId'));
                }
            });
        proxy.read(operation);
    },

    populateHalfDayScanTaskState: function(taskId) {
        var me = this;
        jewelry.model.ScanTaskModel.load(taskId, {
            scope: me,
            success: function(record, operation) {
                me.fillTaskInfoToViewModel(record);

                var taskState = record.get('taskState');
                if (taskState == 'SCHEDULED' || taskState == 'IN_PROGRESS') {
                    var runner = new Ext.util.TaskRunner();
                    runner.start({
                        run: me.populateHalfDayScanTaskState,
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