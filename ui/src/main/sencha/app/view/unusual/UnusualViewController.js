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

                var results = record.get('results');
                results = Ext.Array.sort(results, function(result1, result2) {
                    return result1.score > result2.score ? -1 : 1;
                });
                resultStore.setData(results);
            }
        });
    }
});