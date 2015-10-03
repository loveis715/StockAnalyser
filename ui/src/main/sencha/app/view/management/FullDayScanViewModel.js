Ext.define('jewelry.view.management.FullDayScanViewModel', {
    extend: 'Ext.app.ViewModel',

    alias: 'viewmodel.fullDayScan',

    data: {
        unusualCount: 0,
        lastScanTime: '',
        isScaning: false,
        scanMsg: ''
    }
});