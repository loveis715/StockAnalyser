Ext.define('jewelry.view.unusual.UnusualPage', {
    extend: 'Ext.panel.Panel',
    requires: [
        'jewelry.view.unusual.UnusualList',
        'jewelry.view.unusual.UnusualPageController'
    ],

    xtype: 'jewelry.unusualPage',
    controller: 'unusualPage',

    layout: 'card',
    padding: '9 9 9 9',
    bodyStyle: 'background-color: transparent',

    switchToList: function() {
        this.removeAll();

        var list = Ext.create('jewelry.view.unusual.UnusualList', {
            width: '100%',
            height: '100%',
            margin: '3 3 3 3'
        });
        this.add(list);
    }
});