Ext.define('jewelry.view.mystock.MyStockView', {
    extend: 'Ext.panel.Panel',
    requires: [
        'jewelry.proxy.MyStockProxy',
        'jewelry.model.MyStockModel',
        'jewelry.view.mystock.MyStockViewController'
    ],

    xtype: 'jewelry.myStockView',
    controller: 'myStockView',

    bodyStyle: 'background-color: transparent',

    initComponent: function() {
        var me = this;

        this.bbar = ['->', {
            xtype: 'button',
            text: jewelry.Messages.labels.ok,
            handler: function() {
            }
        }, {
            xtype: 'button',
            text: jewelry.Messages.labels.cancel,
            handler: function() {
                me.fireEvent('viewComplete');
            }
        }];

        this.callParent(arguments);
    }
});