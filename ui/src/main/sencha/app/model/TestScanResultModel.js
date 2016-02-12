Ext.define('jewelry.model.TestScanResultModel', {
    extend: 'Ext.data.Model',

    fields: [{
        name: 'stock',
        type: 'auto'
    }, {
        name: 'tag',
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
        name: 'priceChange',
        type: 'number'
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
    }]
});