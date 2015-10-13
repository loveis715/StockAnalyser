Ext.define('jewelry.view.charts.MinuteDataChart', {
    extend: 'jewelry.view.charts.ChartBase',
    requires: [
        'Ext.draw.Color',
        'Ext.draw.Container'
    ],

    xtype: 'jewelry.minuteDataChart',
    referenceHolder: true,

    width: 488,
    height: 406,
    dotSore: Ext.create('Ext.data.JsonStore', {
        fields: ['x', 'y'],
        data: []
    }),

    initComponent: function() {
        this.callParent(arguments);

        this.renderMinutePriceChart();
    },

    renderMinutePriceChart: function(prevPrice, scale) {
        var high = prevPrice != undefined && scale != undefined ? prevPrice + scale : 100,
            low = prevPrice != undefined && scale != undefined ? prevPrice - scale : 0;
        this.add({
            xtype: 'cartesian',
            margin: '3 3 3 3',
            width: 482,
            height: 402,
            insetPadding: '0 0 0 0',
            region: 'center',
            store: this.dotSore,
            axes: [{
                type: 'numeric',
                position: 'left',
                fields: 'price',
                minimum: low,
                maximum: high,
                style: {
                    axisLine: false,
                    majorTicks: false
                }
            }, {
                type: 'numeric',
                position: 'bottom',
                fields: 'index',
                minimum: 0,
                maximum: 241,
                style: {
                    axisLine: false,
                    majorTicks: false
                }
            }],
            series: [{
                type: 'line',
                axis: 'left',
                xField: 'index',
                yField: 'price',
                smooth: true,
                highlight: true,
                showMarkers: false
           }]
        });
    },

    renderMinutePrice: function(data, prevPrice) {
        var data = this.filterData(data),
            scale = this.getScale(data, prevPrice);
        this.dotSore.setData(data);

        this.removeAll();
        this.renderMinutePriceChart(prevPrice, scale);
    },

    filterData: function(data) {
        if (data == null) {
            return [];
        }

        data = Ext.Array.sort(data, function(record1, record2) {
            return record1.time > record2.time ? 1 : -1;
        });
        data = Ext.Array.filter(data, function(item) {
            return item.time > '09:29:00' && item.time < '15:01:00';
        });

        var index = 0;
        Ext.Array.each(data, function(item) {
            item.index = index;
            index++;
        });
        return data;
    },

    getScale: function(data, prevPrice) {
        var high = prevPrice,
            low = prevPrice;
        Ext.Array.each(data, function(item) {
            if (item.price > high) {
                high = item.price;
            }
            if (item.price < low) {
                low = item.price;
            }
        });

        var scale = prevPrice * 0.01;
        if (high - prevPrice > scale) {
            scale = high - prevPrice;
        }
        if (prevPrice - low > scale) {
            scale = prevPrice - low;
        }
        return scale;
    }
});