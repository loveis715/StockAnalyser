Ext.define('jewelry.model.StockAnalyseTaskRequestModel', {
    extend: 'Ext.data.Model',

    fields: [{
        name: 'analyseTaskId',
        type: 'number'
    }, {
        name: 'stockCode',
        type: 'string'
    }]
});