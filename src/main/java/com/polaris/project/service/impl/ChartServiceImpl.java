package com.polaris.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mongodb.client.result.DeleteResult;
import com.polaris.project.constant.CommonConstant;
import com.polaris.project.manager.StrategySelector;
import com.polaris.project.model.dto.chart.ChartQueryRequest;
import com.polaris.project.model.entity.Chart;
import com.polaris.project.model.entity.ChartDoc;
import com.polaris.project.model.entity.ServerLoadInfo;
import com.polaris.project.model.vo.BiResponse;
import com.polaris.project.model.vo.ChartVO;
import com.polaris.project.repository.ChartDocRepository;
import com.polaris.project.service.ChartService;
import com.polaris.project.mapper.ChartMapper;
import com.polaris.project.service.GenChartStrategy;
import com.polaris.project.service.UserService;
import com.polaris.project.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
@Slf4j
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService{

    @Resource
    ChartDocRepository chartDocRepository;

    @Resource
    private UserService userService;
    @Resource
    @Lazy
    StrategySelector strategySelector;

    @Resource
    MongoTemplate mongoTemplate;
    @Override
    public boolean saveDocument (ChartDoc chart) {
        Long chartId = chart.getChartId();
        List<ChartDoc> charts = chartDocRepository.findAllByChartId(chartId);
        if (charts.size() != 0) {
            return updateDocument(chart);
        } else {
            ChartDoc save = chartDocRepository.save(chart);
            return true;
        }
    }

    @Override
    public boolean syncChart (Chart chartEntity, String genChart, String genResult) {
        ChartDoc chart = BeanUtil.copyProperties(chartEntity, ChartDoc.class);
        chart.setGenChart(genChart);
        chart.setGenResult(genResult);
        chart.setChartId(chartEntity.getId());
        Long chartId = chart.getChartId();
        List<ChartDoc> charts = chartDocRepository.findAllByChartId(chartId);
        if (charts.size() != 0) {
            return updateDocument(chart);
        } else {
            chart.setVersion(1);
            ChartDoc save = chartDocRepository.save(chart);
            return true;
        }
    }

    @Override
    public List<ChartDoc> listDocuments (long userId) {
        return chartDocRepository.findAllByUserId(userId, PageRequest.of(3, 1));
    }

    @Override
    public Page<Chart> listChartByUserId (ChartQueryRequest chartQueryRequest,HttpServletRequest request) {
        Long userId = chartQueryRequest.getUserId();
        if (userId == null) {
            userId = userService.getLoginUser(request).getId();
        }
        List<Chart> chartList = this.list(new QueryWrapper<Chart>().eq("userId", userId));
        PageRequest pageRequest = PageRequest.of(chartQueryRequest.getCurrent() - 1, chartQueryRequest.getPageSize(), Sort.by("creatTime").descending());
        return new PageImpl<>(new ArrayList<>(chartList), pageRequest, chartList.size());
    }

    @Override
    public Page<ChartDoc> getChartList (ChartQueryRequest chartQueryRequest,HttpServletRequest request) {
        // page size
        // 页号 每一页的大小
        // 这个API的页号是从0开始的
        // 默认按照时间降序
        PageRequest pageRequest = PageRequest.of(chartQueryRequest.getCurrent() - 1, chartQueryRequest.getPageSize(), Sort.by("creatTime").descending());
        Long userId = chartQueryRequest.getUserId();
        if (userId == null) {
            userId = userService.getLoginUser(request).getId();
        }
        String name = chartQueryRequest.getName();
        // 查找符合搜索名称的chart
        if (StringUtils.isNotBlank(name)) {
            // . 可以重复 0~n次 , 匹配所有满足的name
            String regex = ".*" + name + ".*";
            Query query = new Query();
            query.addCriteria(Criteria.where("userId").is(userId).and("name").regex(regex));
            query.with(pageRequest);
            List<ChartDoc> charts = mongoTemplate.find(query, ChartDoc.class);
            return excludeOldVersionAndBuildPage(charts, pageRequest);
        } else {
            List<ChartDoc> charts = chartDocRepository.findAllByUserId(userId, pageRequest);
            return excludeOldVersionAndBuildPage(charts, pageRequest);
        }
    }

    /**
     * 排除旧版本和构建返回Page
     *
     * @param charts   图表
     * @param pageable 可分页
     * @return {@link Page}<{@link ChartDoc}>
     */
    private Page<ChartDoc> excludeOldVersionAndBuildPage(List<ChartDoc> charts, Pageable pageable) {
        long count = chartDocRepository.count();
        // 排除旧版本号Chart
        Map<Long, ChartDoc> latestChartsMap = new HashMap<>();
        for (ChartDoc chart : charts) {
            Long chartId = chart.getChartId();
            // 当chartId 相同时 , 获取version较大的chart
            if (!latestChartsMap.containsKey(chartId) || chart.getVersion() > latestChartsMap.get(chartId).getVersion()) {
                latestChartsMap.put(chartId, chart);
            }
        }
        return new PageImpl<>(new ArrayList<>(latestChartsMap.values()), pageable, count);
    }

    @Override
    public ChartDoc getChartByChartId (long chartId) {
        List<ChartDoc> charts = chartDocRepository.findAllByChartId(chartId);
        if (charts.size() == 0) return null;
        if (charts.size() == 1) {
            return charts.get(0);
        }
        // 找到最新的版本
        int maxVersionIdx = 0;
        int maxVersion = Integer.MIN_VALUE;
        for (int i = 0; i < charts.size(); i++) {
            ChartDoc chart = charts.get(i);
            if (chart.getVersion() > maxVersion) {
                maxVersionIdx = i;
                maxVersion = chart.getVersion();
            }
        }
        return charts.get(maxVersionIdx);
    }

    @Override
    public boolean insertChart (Chart chartEntity) {
        try {
            ChartDoc chart = BeanUtil.copyProperties(chartEntity, ChartDoc.class);
            chart.setChartId(chartEntity.getId());
            chart.setVersion(ChartDoc.DEFAULT_VERSION);
            long chartId = chart.getChartId();
            Query query = new Query();
            query.addCriteria(Criteria.where("chartId").is(chartId));
            List<ChartDoc> charts = mongoTemplate.find(query, ChartDoc.class);
            // 是新的图表
            if (charts.size() == 0) {
                chartDocRepository.save(chart);
            } else {
                // 是需要更新的图表 : 获取新的版本号 => 保存
                int nextVersion = getNextVersion(charts);
                chart.setVersion(nextVersion);
                chartDocRepository.save(chart);
            }
            return true;
        } catch (RuntimeException e) {
            log.error("保存Chart到MongoDB失败 : {} , 异常信息:{} ", chartEntity, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteAllFromMongo (long id) {
        return chartDocRepository.deleteAllByChartId(id) != -1;
    }

    @Override
    public boolean updateDocument (ChartDoc chart) {
        try {
            // 不设置ID ,使用MongoDB自动的ObjectId
            chart.setId(null);
            List<ChartDoc> allByChartId = chartDocRepository.findAllByChartId(chart.getChartId());
            int nextVersion = getNextVersion(allByChartId);
            chart.setVersion(nextVersion);
            ChartDoc save = chartDocRepository.save(chart);
            return true;
        } catch (RuntimeException e) {
            log.error("更新文档失败: {},{}", e, chart);
            return false;
        }
    }

    /**
     * 获取下一个版本号
     *
     * @param charts 图表
     * @return int
     */
    private int getNextVersion(List<ChartDoc> charts) {
        int maxVersion = Integer.MIN_VALUE;
        for (int i = 0; i < charts.size(); i++) {
            ChartDoc chart = charts.get(i);
            if (chart.getVersion() > maxVersion) {
                maxVersion = chart.getVersion();
            }
        }
        return maxVersion + 1;
    }

    /**
     * 获取查询包装类
     *
     * @param chartQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Chart> getQueryWrapper (ChartQueryRequest chartQueryRequest, HttpServletRequest request) {
        QueryWrapper<Chart> queryWrapper = new QueryWrapper<>();
        if (chartQueryRequest == null) {
            return queryWrapper;
        }
        Long id = chartQueryRequest.getId();
        String name = chartQueryRequest.getName();
        String goal = chartQueryRequest.getGoal();
        String chartType = chartQueryRequest.getChartType();
        Long userId = userService.getLoginUser(request).getId();
        String sortField = chartQueryRequest.getSortField();
        String sortOrder = chartQueryRequest.getSortOrder();

        queryWrapper.eq(id != null && id > 0, "id", id);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.eq(StringUtils.isNotBlank(goal), "goal", goal);
        queryWrapper.eq(StringUtils.isNotBlank(chartType), "chartType", chartType);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public com.baomidou.mybatisplus.extension.plugins.pagination.Page<ChartVO> buildPage (com.baomidou.mybatisplus.extension.plugins.pagination.Page<Chart> page, List<ChartVO> chartVOS) {
        long total = page.getTotal();
        long current = page.getCurrent();
        long size = page.getSize();
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<ChartVO> newPage =
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(current, size);
        newPage.setTotal(total);
        newPage.setSize(size);
        newPage.setCurrent(current);
        newPage.setRecords(chartVOS);
        return newPage;
    }

    @Override
    public boolean deleteSingleFromMongo (long id, int version) {
        Query query = new Query();
        query.addCriteria(Criteria.where("chartId").is(id));
        query.addCriteria(Criteria.where("version").is(version));
        DeleteResult remove = mongoTemplate.remove(query, ChartDoc.class);
        // 按照前端的参数, 必定会存在一个对应的document , 如果没有就是删除失败了
        return remove.getDeletedCount() == 1;
    }

    @Override
    public BiResponse genChart (Chart chart, ServerLoadInfo info){
        GenChartStrategy genChartStrategy = strategySelector.selectStrategy(info);
        return genChartStrategy.executeGenChart(chart);
    }


}




