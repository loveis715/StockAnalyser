Ext.define('jewelry.view.charts.ChartBase', {
    extend: 'Ext.container.Container',

    initComponent: function() {
        var me = this;
        Ext.getDoc().on({
            scope: me,
            mousemove: me.onMouseMoveInternal
        });

        this.callParent(arguments);
    },

    onMouseMoveInternal: function(e, target) {
        var mouseXY = e.getXY(),
            xy = this.getXY(),
            width = this.getWidth(),
            height = this.getHeight();
        if (mouseXY[0] < xy[0] || mouseXY[0] > xy[0] + width) {
            return;
        }

        if (mouseXY[1] < xy[1] || mouseXY[1] > xy[1] + height) {
            return;
        }

        this.onMouseMove({
            x: mouseXY[0] - xy[0],
            y: mouseXY[1] - xy[1]
        });
    },

    onMouseMove: function(position) {
    }
});