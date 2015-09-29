Ext.define('jewelry.view.management.StockSyncingPanel', {
    extend: 'Ext.panel.Panel',
    requires: [
        'jewelry.view.management.StockSyncingViewModel'
    ],

    xtype: 'jewelry.stockSyncingPanel',
    layout: 'vbox',
    border: true,
    bodyPadding: '9 9 9 9',
    
    viewModel: {
        type: 'stockSynching'
    },

    defaults: {
        labelWidth: 150
    },

    items: [{
        xtype: 'label',
        height: 50,
        style: 'font-size: 30px',
        // label is just a label element in html, which does not take the height style
        // into layout calculation. So it does not participate in layout calculation
        // properly, and will always be 12px height. Use this padding to adjust layout
        padding: '10 0 0 0',
        text: jewelry.Messages.labels.stockSyncing
    }, {
        xtype: 'displayfield',
        fieldLabel: jewelry.Messages.labels.stockCountShanghai,
        bind: {
            value: '{stockCountShanghai}'
        }
    }, {
        xtype: 'displayfield',
        fieldLabel: jewelry.Messages.labels.stockCountShenzhen,
        bind: {
            value: '{stockCountShenzhen}'
        }
    }, {
        xtype: 'displayfield',
        fieldLabel: jewelry.Messages.labels.lastSyncTime,
    }, {
        xtype: 'button',
        text: jewelry.Messages.labels.startSync,
        bind: {
            visible: '{!isSyncing}'
        }
    }]
});