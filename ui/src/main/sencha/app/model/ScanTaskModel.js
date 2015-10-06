Ext.define('jewelry.model.ScanTaskModel', {
    extend: 'Ext.data.Model',
    requires: [
        'jewelry.proxy.ScanTaskProxy'
    ],

    proxy: Ext.create('jewelry.proxy.ScanTaskProxy'),

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
        name: 'scanningStockName',
        type: 'string'
    }, {
        name: 'results'
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
    }, {
        name: 'unusualCount',
        calculate: function(data) {
            if (data.results != null) {
                return data.results.length;
            }
            return 0;
        }
    }]
});