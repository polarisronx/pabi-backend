package com.polaris.project.mapper;

import com.polaris.project.model.entity.Chart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @Entity entity.model.com.polaris.project.Chart
 */
public interface ChartMapper extends BaseMapper<Chart> {

    List<Map<String, Object>> queryChartData(String querySql);
}




