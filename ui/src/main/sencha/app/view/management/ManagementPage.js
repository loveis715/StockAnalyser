Ext.define('jewelry.view.management.ManagementPage', {
    extend: 'Ext.panel.Panel',
    requires: [
        'jewelry.view.management.StockSyncingPanel',
        'jewelry.view.management.TestScanPanel',
        'jewelry.view.management.HalfDayScanPanel',
        'jewelry.view.management.MorphologyScanPanel'
    ],

    xtype: 'jewelry.managementPage',
    referenceHolder: true,

    layout: 'border',
    padding: '9 9 9 9',
    bodyStyle: 'background-color: transparent',

    items: [{
        xtype: 'panel',
        layout: 'border',
        region: 'north',
        height: '50%',
        bodyStyle: 'background-color: transparent',
        items: [{
            xtype: 'jewelry.stockSyncingPanel',
            region: 'west',
            margin: '3 3 3 3',
            width: '50%'
        }, {
            // Panel for analysing morphology
            xtype: 'jewelry.morphologyScanPanel',
            region: 'center',
            margin: '3 3 3 3',
            border: true
        }]
    }, {
        xtype: 'panel',
        layout: 'border',
        region: 'center',
        bodyStyle: 'background-color: transparent',
        items: [{
            // Panel for watching the volume of stocks in noon
            xtype: 'jewelry.halfDayScanPanel',
            region: 'west',
            width: '50%',
            margin: '3 3 3 3',
            border: true
        }, {
            // Panel for syncing the volume of stocks at night
            xtype: 'jewelry.testScanPanel',
            region: 'center',
            margin: '3 3 3 3',
            border: true
        }]
    }]
});