Ext.define('jewelry.model.MyStockModel', {
    extend: 'Ext.data.Model',
    requires: [
        'jewelry.proxy.MyStockProxy'
    ],

    proxy: Ext.create('jewelry.proxy.MyStockProxy'),

    fields: [{
        name: 'id',
        type: 'number'
    }, {
        name: 'lockVersion',
        type: 'number'
    }, {
        name: 'addTime',
        type: 'number'
    }, {
        name: 'stock'
    }, {
        name: 'stockNotes'
    }, {
        name: 'stockName',
        type: 'string',
        calculate: function(data) {
            if (data.stock) {
                return data.stock.name;
            } else {
                return '';
            }
        }
    }, {
        name: 'stockCode',
        type: 'string',
        calculate: function(data) {
            if (data.stock) {
                return data.stock.code;
            } else {
                return '';
            }
        }
    }, {
        name: 'formattedAddTime',
        type: 'string',
        calculate: function(data) {
            if (data.addTime) {
                var date = new Date(data.addTime);
                return Ext.util.Format.date(date, 'm/d/Y h:iA');
            } else {
                return '';
            }
        }
    }]
});