Ext.define('jewelry.view.management.ManagementPage', {
    extend: 'Ext.panel.Panel',
    requires: [
        'jewelry.view.management.StockSyncingPanel',
        'morpho.view.management.ManagementPanelController'
    ],

    xtype: 'jewelry.managementPage',
    controller: 'management',
    referenceHolder: true,

    layout: 'border',
    padding: '12 12 12 12',

    items: [{
        xtype: 'jewelry.stockSyncingPanel',
        region: 'west',
        reference: 'shanghaiStock',
        width: '50%'
    }, {
        xtype: 'jewelry.stockSyncingPanel',
        region: 'center',
        reference: 'shenzhenStock',
        width: '50%'
    }],

    listeners: {
        activate: 'onActivate'
    }
});