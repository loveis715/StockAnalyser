Ext.define('jewelry.view.management.StockSyncingPanel', {
    extend: 'Ext.panel.Panel',
    requires: [
        'jewelry.view.management.StockSyncingViewModel'
    ],

    xtype: 'jewelry.stockSyncingPanel',
    layout: 'vbox',

    viewModel: {
        type: 'stockSynching'
    },

    items: [{
        xtype: 'label',
        height: 50,
        style: 'font-size: 30px',
        // label is just a label element in html, which does not take the height style
        // into layout calculation. So it does not participate in layout calculation
        // properly, and will always be 12px height. Use this padding to adjust layout
        padding: '10 0 0 0',
        bind: {
            text: '{labelText}'
        }
    }, {
        xtype: 'displayfield',
        fieldLabel: jewelry.Messages.labels.stockCount,
        bind: {
            value: '{stockCount}'
        }
    }, {
        xtype: 'displayfield',
        fieldLabel: jewelry.Messages.labels.lastSyncTime,
        bind: {
            value: '{lastSyncTime}'
        }
    }, {
        xtype: 'button',
        text: jewelry.Messages.labels.startSync,
        bind: {
            visible: '{!isSyncing}'
        }
    }, {
        xtype: 'label',
        bind: {
            text: '{syncingMsg}',
            visible: '{isSyncing}'
        }
    }, {
        xtype: 'button',
        text: jewelry.Messages.labels.endSync,
        bind: {
            visible: '{isSyncing}'
        }
    }]
});