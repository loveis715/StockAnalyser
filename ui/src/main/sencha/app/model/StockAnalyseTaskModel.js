Ext.define('jewelry.model.StockAnalyseTaskModel', {
    extend: 'Ext.data.Model',
    requires: [
        'jewelry.proxy.StockAnalyseTaskProxy'
    ],

    proxy: Ext.create('jewelry.proxy.StockAnalyseTaskProxy'),

    fields: [{
        name: 'taskState',
        type: 'string'
    }, {
        name: 'percentage',
        type: 'number'
    }, {
        name: 'startTime',
        type: 'number'
    }, {
        name: 'endTime',
        type: 'number'
    }, {
        name: 'stock'
    }, {
        name: 'resultTags'
    }, {
        name: 'formattedStartTime',
        type: 'string',
        calculate: function(data) {
            if (data.startTime) {
                var date = new Date(data.startTime);
                return Ext.util.Format.date(date, 'm/d/Y h:iA');
            } else {
                return '';
            }
        }
    }, {
        name: 'formattedEndTime',
        type: 'string',
        calculate: function(data) {
            if (data.endTime) {
                var date = new Date(data.endTime);
                return Ext.util.Format.date(date, 'm/d/Y h:iA');
            } else {
                return '';
            }
        }
    }]
});