Ext.define('jewelry.view.main.Main', {
    extend: 'Ext.container.Container',

    xtype: 'app-main',

    layout: {
        type: 'border'
    },

    initComponent: function() {
        var me = this;
        this.items = [{
            xtype: 'toolbar',
            cls: 'jewelry-header',
            region: 'north',
            width: '100%',
            height: 50,
            padding: '0 0 0 0',
            items: [{
                text: morpho.Messages.labels.frontPage,
                cls: 'jewelry-header-button',
                border: false,
                height: 50,
                margin: '0 0 0 0',
                padding: '0 8 0 6',
                cardIndex: 0
            }, {
                text: morpho.Messages.labels.unusual,
                cls: 'jewelry-header-button',
                border: false,
                height: 50,
                margin: '0 0 0 0',
                padding: '0 8 0 6',
                cardIndex: 1
            }, {
                text: morpho.Messages.labels.myStock,
                cls: 'jewelry-header-button',
                border: false,
                height: 50,
                margin: '0 0 0 0',
                padding: '0 8 0 6',
                cardIndex: 2
            }, {
                text: morpho.Messages.labels.stock,
                cls: 'jewelry-header-button',
                border: false,
                height: 50,
                margin: '0 0 0 0',
                padding: '0 8 0 6',
                cardIndex: 3
            }, {
                text: morpho.Messages.labels.management,
                cls: 'jewelry-header-button',
                border: false,
                height: 50,
                margin: '0 0 0 0',
                padding: '0 8 0 6',
                cardIndex: 4
            }, {
                text: morpho.Messages.labels.knowledge,
                cls: 'jewelry-header-button',
                border: false,
                height: 50,
                margin: '0 0 0 0',
                padding: '0 8 0 6',
                cardIndex: 5
            }]
        }, {
            region: 'center',
            xtype: 'panel',
            reference: 'cardPanel',
            layout: 'card'
        }];

        this.callParent(arguments);
    }
});
