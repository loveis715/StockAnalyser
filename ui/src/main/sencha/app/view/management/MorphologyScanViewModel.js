Ext.define('jewelry.view.management.MorphologyScanViewModel', {
    extend: 'Ext.app.ViewModel',

    alias: 'viewmodel.morphologyScan',

    data: {
        unusualCount: 0,
        lastScanTime: '',
        isScaning: false,
        scanMsg: ''
    }
});