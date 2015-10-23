Ext.define('jewelry.view.mystock.MyStockPage', {
    extend: 'Ext.panel.Panel',
    requires: [
        'jewelry.view.mystock.MyStockList',
        'jewelry.view.mystock.MyStockView',
        'jewelry.view.mystock.MyStockPageController'
    ],

    xtype: 'jewelry.myStockPage',
    controller: 'myStockPage',

    layout: 'card',
    padding: '9 9 9 9',
    bodyStyle: 'background-color: transparent',

    switchToList: function() {
        this.removeAll();

        var list = Ext.create('jewelry.view.mystock.MyStockList', {
            width: '100%',
            height: '100%',
            margin: '3 3 3 3',
            listeners: {
                editRecord: 'onViewRecord'
            }
        });
        this.add(list);
    },
    
    switchToViewer: function(recordId) {
        this.removeAll();

        var viewer = Ext.create('jewelry.view.mystock.MyStockView', {
            width: '100%',
            height: '100%',
            margin: '3 3 3 3',
            recordId: recordId,
            listeners: {
                editComplete: 'onEditComplete'
            }
        });
        this.add(viewer);
    }
});