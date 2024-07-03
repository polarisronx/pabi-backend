package com.polaris.project.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.polaris.project.model.dto.chart.ChartQueryRequest;
import com.polaris.project.model.entity.Chart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.polaris.project.model.entity.ChartDoc;
import com.polaris.project.model.entity.ServerLoadInfo;
import com.polaris.project.model.vo.BiResponse;
import com.polaris.project.model.vo.ChartVO;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 */
public interface ChartService extends IService<Chart> {


    boolean saveDocument (ChartDoc chart);

    boolean syncChart (Chart chartEntity, String genChart, String genResult);

    List<ChartDoc> listDocuments (long userId);

    Page<ChartDoc> getChartList (ChartQueryRequest chartQueryRequest,HttpServletRequest request);

    Page<Chart> listChartByUserId (ChartQueryRequest chartQueryRequest,HttpServletRequest request);

    ChartDoc getChartByChartId (long chartId);

    boolean insertChart (Chart chartEntity);

    boolean deleteAllFromMongo (long id);

    boolean updateDocument (ChartDoc chart);

    QueryWrapper<Chart> getQueryWrapper (ChartQueryRequest chartQueryRequest, HttpServletRequest request);

    com.baomidou.mybatisplus.extension.plugins.pagination.Page<ChartVO> buildPage (com.baomidou.mybatisplus.extension.plugins.pagination.Page<Chart> page, List<ChartVO> chartVOS);

    boolean deleteSingleFromMongo (long id, int version);
    /**
     * 通过AI生成图表
     *
     * @param chart 表实体
     * @param info        信息
     * @return {@link BiResponse}
     */
    BiResponse genChart(Chart chart, ServerLoadInfo info);
}
