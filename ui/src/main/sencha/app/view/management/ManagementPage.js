Ext.define('jewelry.view.management.ManagementPage', {
    extend: 'Ext.panel.Panel',
    requires: [
        'jewelry.view.management.StockSyncingPanel'
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
            // Panel for syncing the volume of stocks at night
            xtype: 'panel',
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
            xtype: 'panel',
            region: 'west',
            width: '50%',
            margin: '3 3 3 3',
            border: true
        }, {
            // Panel for watching the trading info of stocks
            xtype: 'panel',
            region: 'center',
            margin: '3 3 3 3',
            border: true
        }]
    }]
});