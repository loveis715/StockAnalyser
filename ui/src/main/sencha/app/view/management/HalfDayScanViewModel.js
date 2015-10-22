Ext.define('jewelry.view.management.HalfDayScanViewModel', {
    extend: 'Ext.app.ViewModel',

    alias: 'viewmodel.halfDayScan',

    data: {
        unusualCount: 0,
        lastScanTime: '',
        isScaning: false,
        scanMsg: ''
    }
});