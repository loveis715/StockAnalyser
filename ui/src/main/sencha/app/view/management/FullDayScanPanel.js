Ext.define('jewelry.view.management.FullDayScanPanel', {
    extend: 'Ext.panel.Panel',
    requires: [
        'jewelry.view.management.FullDayScanViewModel',
        'jewelry.view.management.FullDayScanPanelController'
    ],

    controller: 'fullDayScanPanel',

    xtype: 'jewelry.fullDayScanPanel',
    layout: 'vbox',
    border: true,
    bodyPadding: '9 9 9 9',
    referenceHolder: true,

    viewModel: {
        type: 'fullDayScan'
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
        text: jewelry.Messages.labels.fullDayScan
    }, {
        xtype: 'displayfield',
        fieldLabel: jewelry.Messages.labels.unusualCount,
        bind: {
            value: '{unusualCount}'
        }
    }, {
        xtype: 'displayfield',
        fieldLabel: jewelry.Messages.labels.lastScanTime,
        bind: {
            value: '{lastScanTime}'
        }
    }, {
        xtype: 'button',
        text: jewelry.Messages.labels.startScan,
        handler: 'startFullDayScan',
        bind: {
            disabled: '{isScaning}'
        }
    }, {
        xtype: 'label',
        margin: '6 0 0 0',
        bind: {
            text: '{scanMsg}'
        }
    }]
});