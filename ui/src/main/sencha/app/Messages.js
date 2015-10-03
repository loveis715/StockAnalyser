Ext.define('jewelry.Messages', {
    singleton: true,

    labels: {
        frontPage: '首页',
        unusual: '异动',
        myStock: '自选',
        stock: '个股',
        management: '管理',
        knowledge: '知识',
        stockSyncing: '股票同步',
        stockCountShanghai: '股票数量（上证）',
        stockCountShenzhen: '股票数量（深成）',
        unusualCount: '异常个股数量',
        lastSyncTime: '上次同步时间',
        startSync: '开始同步',
        fullDayScan: '整日交易扫描',
        lastScanTime: '上次扫描时间',
        startScan: '开始扫描'
    },
    messages: {
        sycingTaskForShanghai: '正在同步上证股票列表...',
        sycingTaskForShenzhen: '正在同步深成股票列表...',
        currentProgress: '{0}%: 正在扫描股票{1}'
    }
});