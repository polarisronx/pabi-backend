package com.polaris.project.service.impl.execution;


import com.polaris.project.common.ErrorCode;
import com.polaris.project.exception.BusinessException;
import com.polaris.project.exception.GenChartException;
import com.polaris.project.exception.ThrowUtils;
import com.polaris.project.manager.AiManager;
import com.polaris.project.manager.WebSocketServer;
import com.polaris.project.model.entity.Chart;
import com.polaris.project.model.entity.ChartDoc;
import com.polaris.project.model.enums.ChartStatusEnum;
import com.polaris.project.model.vo.BiResponse;
import com.polaris.project.service.ChartService;
import com.polaris.project.service.GenChartStrategy;
import com.polaris.project.utils.ChartUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

import static com.polaris.project.constant.CommonConstant.BI_MODEL_ID;
import static com.polaris.project.utils.MyWebSocketHandler.sendMessage;
import static com.polaris.project.utils.MyWebSocketHandler.webSocketMap;

/**
 * 线程池生成
 *
 * @author adorabled4
 * @className GenChartSync
 * @date 2023/08/30
 */
@Slf4j
@Component(value = "gen_thread_pool")
public class GenChartThreadPool implements GenChartStrategy {

    @Resource
    ChartService chartService;

    @Resource
    AiManager aiManager;




    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

//    @Resource
//    ChartLogService logService;
//
//    @Resource
//    PointService pointService;

    @Override
    public BiResponse executeGenChart(Chart chart) {
        try {
            CompletableFuture.runAsync(() -> {
                Chart genChartEntity = new Chart();
                String goal = chart.getGoal();
                String chartType = chart.getChartType();
                String csvData = chart.getChartData();

                genChartEntity.setId(chart.getId());
                genChartEntity.setStatus(ChartStatusEnum.RUNNING.getStatus());
                boolean b = chartService.updateById(genChartEntity);
                // 处理异常
                ThrowUtils.throwIf(!b, new BusinessException(ErrorCode.SYSTEM_ERROR, "修改图表状态信息失败 " + chart.getId()));
                // 获取CSV
                // 构造用户输入
                StringBuilder userInput = new StringBuilder("");
                // 拼接图表类型;
                String userGoal = goal;
                if (StringUtils.isNotBlank(chartType)) {
                    userGoal += ", 请使用 " + chartType;
                }
                userInput.append("分析需求: ").append('\n');
                userInput.append(userGoal).append("\n");
                userInput.append("原始数据：").append("\n");
                userInput.append(csvData).append("\n");
                // 系统预设 ( 简单预设 )
                /* 较好的做法是在系统（模型）层面做预设效果一般来说，会比直接拼接在用户消息里效果更好一些。*/
                String result = aiManager.doChat(BI_MODEL_ID,userInput.toString());
//                String result = aiManager.chatAndGenChart(goal,chartType,csvData);
                String[] split = result.split("【【【【【");
                // 第一个是 空字符串
                if (split.length < 3) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI 生成错误!");
                }
                // 图表代码
                String genChart = split[1].trim();
                // 分析结果
                String genResult = split[2].trim();
                String compressJson = ChartUtil.compressJson(genChart);
                // 更新数据
                chart.setStatus(ChartStatusEnum.SUCCEED.getStatus());
                chart.setStatus(ChartStatusEnum.SUCCEED.getStatus());
                boolean updateGenResult = chartService.updateById(chart);
                boolean syncResult = chartService.syncChart(chart, compressJson, genResult);
                ThrowUtils.throwIf(!updateGenResult && syncResult, ErrorCode.SYSTEM_ERROR, "生成图表保存失败!");
                // 记录调用结果
//                logService.recordLog(chart);
                ChartDoc chartDoc = chartService.getChartByChartId(chart.getId());
                sendMessage(String.valueOf(chartDoc.getUserId()),"您的[" + chart.getName() + "]生成成功 , 前往 我的图表 进行查看");
            }, threadPoolExecutor);
        } catch (BusinessException e) {
            // 更新状态信息
            Chart updateChartResult = new Chart();
            updateChartResult.setId(chart.getId());
            updateChartResult.setStatus(ChartStatusEnum.FAILED.getStatus());
            updateChartResult.setExecMessage(e.getMessage());
            boolean updateResult = chartService.updateById(updateChartResult);
            // 补偿积分
//            pointService.sendCompensateMessage(chart.getUserId(), PointChangeEnum.GEN_CHART_FAILED_ADD);
//            // 记录调用结果: 这里的recordLog不会与上面的冲突,如果上面的执行了那么图表的生成结果一定是成功,不会执行到这里
//            logService.recordLog(chart);
            if (!updateResult) {
                log.info("更新图表FAILED状态信息失败 , chatId:{}", updateChartResult.getId());
            }
            // 抛出异常进行日志打印
            throw new GenChartException(chart.getId(), e);
        }
        BiResponse biResponse = new BiResponse();
        biResponse.setChartId(chart.getId());
        return biResponse;
    }
}
