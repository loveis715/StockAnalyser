Ext.define('jewelry.view.test.TestScanViewViewModel', {
    extend: 'Ext.app.ViewModel',

    alias: 'viewmodel.testScanView',

    data: {
        startTime: '',
        endTime: '',
        taskState: '',
        unusualCount: '',
        results: []
    }
});