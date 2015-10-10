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
        startScan: '开始扫描',
        selectDate: '选择日期',
        ok: '完成',
        search: '搜索',
        stockAnalysis: '个股分析',
        addToMyStock: '加入自选',
        removeFromMyStock: '移出自选'
    },
    headers: {
        startTime: '开始时间',
        endTime: '结束时间',
        scanType: '任务类型',
        taskStatus: '任务状态',
        unusualCount: '异常数量',
        stockName: '股票名称',
        stockCode: '股票代码',
        stockCategory: '交易所',
        score: '评分',
        unusualCases: '异常'
    },
    messages: {
        sycingTaskForShanghai: '正在同步上证股票列表...',
        sycingTaskForShenzhen: '正在同步深成股票列表...',
        currentProgress: '{0}%: 正在扫描股票{1}',
        notScanYet: '该日未执行扫描，请先执行股票异动扫描',
        view: '查看',
        inputStockName: '输入股票名称...'
    },
    tagNames: {
        VOL_INC: '增量',
        VOL_CON_INC: '连续增量',
        VOL_INC_DOWN: '放量下跌',
        VOL_INC_LOW: '跌停打开',
        VOL_INC_HIGH: '涨停打开',
        VOL_DEC_LOW: '跌停',
        PRI_LOW: '价格低位',
        PRI_HIGH: '价格高位',
        MASS_POS_TRAD: '堆量',
        MASS_NEG_TRAD: '杀跌',
        TRAD_RATIO_LOW: '换手率低'
    }
});