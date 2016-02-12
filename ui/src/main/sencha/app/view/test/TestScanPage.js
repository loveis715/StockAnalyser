Ext.define('jewelry.view.test.TestScanPage', {
    extend: 'Ext.panel.Panel',
    requires: [
        'jewelry.view.test.TestScanList',
        'jewelry.view.test.TestScanView',
        'jewelry.view.test.TestScanPageController'
    ],

    xtype: 'jewelry.testScanPage',
    controller: 'testScanPage',

    layout: 'card',
    padding: '9 9 9 9',
    bodyStyle: 'background-color: transparent',

    switchToList: function() {
        this.removeAll();

        var list = Ext.create('jewelry.view.test.TestScanList', {
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

        var viewer = Ext.create('jewelry.view.test.TestScanView', {
            width: '100%',
            height: '100%',
            margin: '3 3 3 3',
            recordId: recordId,
            listeners: {
                viewComplete: 'onViewComplete'
            }
        });
        this.add(viewer);
    }
});