Ext.define('jewelry.model.ScanResultModel', {
    extend: 'Ext.data.Model',

    fields: [{
        name: 'stock',
        type: 'auto'
    }, {
        name: 'tags',
        type: 'string'
    }, {
        name: 'stockName',
        calculate: function(data) {
            if (data.stock != null) {
                return data.stock.name;
            } else {
                return '';
            }
        }
    }, {
        name: 'stockCode',
        calculate: function(data) {
            if (data.stock != null) {
                return data.stock.code;
            } else {
                return '';
            }
        }
    }, {
        name: 'stockCategory',
        calculate: function(data) {
            if (data.stock != null) {
                return data.stock.stockCategory;
            } else {
                return '';
            }
        }
    }, {
        name: 'unusualCases',
        calculate: function(data) {
            if (data.tags != null) {
                return data.tags;
            } else {
                return '';
            }
        }
    }]
});