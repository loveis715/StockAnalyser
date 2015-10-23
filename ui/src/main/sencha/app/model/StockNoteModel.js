Ext.define('jewelry.model.StockNoteModel', {
    extend: 'Ext.data.Model',

    fields: [{
        name: 'id',
        type: 'number'
    }, {
        name: 'addTime'
    }, {
        name: 'title',
        type: 'string'
    }, {
        name: 'content',
        type: 'string'
    }, {
        name: 'noteCategory',
        type: 'string'
    }]
});