Ext.define('jewelry.proxy.MyStockProxy', {
    extend: 'Ext.data.proxy.Rest',
    alias: 'proxy.myStockProxy',

    url: 'http://localhost:8080/jewelry-service/api/my_stocks',
    format: 'json',
    reader: {
        type: 'json'
    },
    writer: {
        type: 'json',
        writeAllFields: true
    }
});