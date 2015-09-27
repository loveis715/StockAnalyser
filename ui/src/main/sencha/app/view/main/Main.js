Ext.define('jewelry.view.main.Main', {
    extend: 'Ext.container.Container',
    requires: [
        'jewelry.view.front.FrontPage',
        'jewelry.view.knowledge.KnowledgePage',
        'jewelry.view.management.ManagementPage',
        'jewelry.view.mystock.MyStockPage',
        'jewelry.view.stock.StockPage',
        'jewelry.view.unusual.UnusualPage'
    ],

    xtype: 'app-main',
    referenceHolder: true,

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
                text: jewelry.Messages.labels.frontPage,
                cls: 'jewelry-header-button',
                border: false,
                height: 50,
                margin: '0 0 0 0',
                padding: '0 13 0 11',
                cardIndex: 0,
                scope: me,
                handler: me.switchToCard
            }, {
                text: jewelry.Messages.labels.unusual,
                cls: 'jewelry-header-button',
                border: false,
                height: 50,
                margin: '0 0 0 0',
                padding: '0 13 0 11',
                cardIndex: 1,
                scope: me,
                handler: me.switchToCard
            }, {
                text: jewelry.Messages.labels.myStock,
                cls: 'jewelry-header-button',
                border: false,
                height: 50,
                margin: '0 0 0 0',
                padding: '0 13 0 11',
                cardIndex: 2,
                scope: me,
                handler: me.switchToCard
            }, {
                text: jewelry.Messages.labels.stock,
                cls: 'jewelry-header-button',
                border: false,
                height: 50,
                margin: '0 0 0 0',
                padding: '0 13 0 11',
                cardIndex: 3,
                scope: me,
                handler: me.switchToCard
            }, {
                text: jewelry.Messages.labels.management,
                cls: 'jewelry-header-button',
                border: false,
                height: 50,
                margin: '0 0 0 0',
                padding: '0 13 0 11',
                cardIndex: 4,
                scope: me,
                handler: me.switchToCard
            }, {
                text: jewelry.Messages.labels.knowledge,
                cls: 'jewelry-header-button',
                border: false,
                height: 50,
                margin: '0 0 0 0',
                padding: '0 13 0 11',
                cardIndex: 5,
                scope: me,
                handler: me.switchToCard
            }]
        }, {
            region: 'center',
            xtype: 'panel',
            reference: 'cardPanel',
            layout: 'card',
            items: [{
                xtype: 'jewelry.frontPage'
            }, {
                xtype: 'jewelry.unusualPage'
            }, {
                xtype: 'jewelry.myStockPage'
            }, {
                xtype: 'jewelry.stockPage'
            }, {
                xtype: 'jewelry.managementPage'
            }, {
                xtype: 'jewelry.knowledgePage'
            }]
        }];

        this.callParent(arguments);
    },

    switchToCard: function(button) {
        var cardPanel = this.lookupReference('cardPanel'),
            layout = cardPanel.getLayout(),
            activeItem = layout.getActiveItem();
        if (activeItem.cardIndex != button.cardIndex) {
            layout.setActiveItem(button.cardIndex);
        }
    }
});
