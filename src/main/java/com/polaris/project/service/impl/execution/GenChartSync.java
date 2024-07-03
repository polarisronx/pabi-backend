package com.polaris.project.service.impl.execution;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.polaris.project.common.ErrorCode;
import com.polaris.project.exception.BusinessException;
import com.polaris.project.exception.GenChartException;
import com.polaris.project.exception.ThrowUtils;
import com.polaris.project.manager.AiManager;
import com.polaris.project.model.entity.Chart;
import com.polaris.project.model.enums.ChartStatusEnum;
import com.polaris.project.model.vo.BiResponse;
import com.polaris.project.service.ChartService;
import com.polaris.project.service.GenChartStrategy;
import com.polaris.project.utils.ChartUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.polaris.project.constant.CommonConstant.BI_MODEL_ID;
import static com.polaris.project.utils.ChartUtil.ChartDownloadable;

/**
 * 同步生成
 *
 * @author adorabled4
 * @className GenChartSync
 * @date : 2023/08/30/ 11:46
 **/
@Component(value = "gen_sync")
@Slf4j
//@Component(value = GenChartStrategyEnum.GEN_SYNC.getValue())
public class GenChartSync implements GenChartStrategy {

    @Resource
    ChartService chartService;

    @Resource
    AiManager aiManager;

//    @Resource
//    ChartLogService logService;
//
//    @Resource
//    PointService pointService;
    @Override
    public BiResponse executeGenChart(Chart chart) {
        // 系统预设 ( 简单预设 )
        /* 较好的做法是在系统（模型）层面做预设效果一般来说，会比直接拼接在用户消息里效果更好一些。*/
        /*
        分析需求：
        分析网站用户的增长情况
        原始数据：
        日期,用户数
        1号,10
        2号,20
        3号,30
        */
//        String result = aiManager.doChat(userInput.toString(), AIConstant.BI_MODEL_ID);
        try{
            String userInput = ChartUtil.buildUserInput(chart);
            String result = aiManager.doChat(BI_MODEL_ID,userInput);
            String[] split = result.split("【【【【【");
            // 第一个是 空字符串
            if (split.length < 3) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI 生成错误!");
            }
            // 图表代码
            String genChart = split[1].trim();
            // 分析结果
            String genResult = split[2].trim();
            // 更新数据到数据库
//            chart.setGenChart(genChart);
//            chart.setGenResult(genResult);
            chart.setStatus(ChartStatusEnum.SUCCEED.getStatus());
            chart.setExecMessage(ChartStatusEnum.SUCCEED.getMessage());
            try {
                genChart = ChartDownloadable(genChart);
            }catch (JsonProcessingException e){
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "AI生成异常!");
            }
            genChart = ChartUtil.compressJson(genChart);
            boolean save = chartService.updateById(chart);
            ThrowUtils.throwIf(!save, ErrorCode.SYSTEM_ERROR, "图表保存失败!");
            boolean syncResult = chartService.syncChart(chart,genChart,genResult);
            ThrowUtils.throwIf(!syncResult, ErrorCode.SYSTEM_ERROR, "图表同步失败!");
            // 记录生成日志
//            logService.recordLog(chart);
            // 封装返回结果
            BiResponse biResponse = new BiResponse();
            biResponse.setGenChart(genChart);
            biResponse.setChartId(chart.getId());
            biResponse.setGenResult(genResult);
            return biResponse;
        } catch (BusinessException e) {
            // 更新状态信息
            Chart updateChartResult = new Chart();
            updateChartResult.setId(chart.getId());
            updateChartResult.setStatus(ChartStatusEnum.FAILED.getStatus());
            updateChartResult.setExecMessage(e.getMessage());
            boolean updateResult = chartService.updateById(updateChartResult);
            // 记录生成日志
//            logService.recordLog(chart);
            if (!updateResult) {
                log.info("更新图表FAILED状态信息失败 , chatId:{}", updateChartResult.getId());
            }
//            pointService.sendCompensateMessage(chart.getUserId(), PointChangeEnum.GEN_CHART_FAILED_ADD);
            // 抛出异常进行日志打印
            throw new GenChartException(chart.getId(), e);
        }
    }
}
