Ext.define('jewelry.view.unusual.UnusualViewController', {
    extend: 'Ext.app.ViewController',
    requires: [
        'jewelry.model.ScanTaskModel'
    ],

    alias: 'controller.unusualView',

    init: function() {
        var me = this,
            view = this.getView(),
            recordId = view.getConfig('recordId');
        jewelry.model.ScanTaskModel.load(recordId, {
            scope: me,
            success: function(record, operation) {
                var viewModel = view.getViewModel();
                viewModel.set('startTime', record.get('formattedStartTime'));
                viewModel.set('endTime', record.get('formattedEndTime'));
                viewModel.set('scanType', record.get('scanType'));
                viewModel.set('taskState', record.get('taskState'));
                viewModel.set('unusualCount', record.get('unusualCount'));

                var resultGrid = view.lookupReference('resultGrid'),
                    resultStore = resultGrid.getStore();
                resultStore.removeAll();

                resultStore.setData(record.get('results'));
            }
        });
    }
});