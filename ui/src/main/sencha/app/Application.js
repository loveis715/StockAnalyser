Ext.define('jewelry.Application', {
    extend: 'Ext.app.Application',
    
    name: 'jewelry',
    requires: [
        'jewelry.Messages',
        'jewelry.Constants',
        'jewelry.view.stock.StockPage'// Force load the page to eagerly load its huge amont of dependencies
    ]
});
