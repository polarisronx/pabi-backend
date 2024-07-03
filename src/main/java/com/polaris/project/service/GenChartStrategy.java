package com.polaris.project.service;


import com.polaris.project.model.entity.Chart;
import com.polaris.project.model.vo.BiResponse;

/**
 * @author adorabled4
 * @className GenChartStrategy
 * @date : 2023/08/30/ 11:41
 **/
public interface GenChartStrategy {

    /**
     * 执行图表生成
     *
     * @param chart 表实体
     * @return {@link BiResponse}
     */
    BiResponse executeGenChart(Chart chart);
}
