Ext.define('jewelry.view.management.TestScanViewModel', {
    extend: 'Ext.app.ViewModel',

    alias: 'viewmodel.testScan',

    data: {
        unusualCount: 0,
        lastScanTime: '',
        isScaning: false,
        scanMsg: ''
    }
});