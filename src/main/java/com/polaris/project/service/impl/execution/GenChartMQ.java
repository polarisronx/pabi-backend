package com.polaris.project.service.impl.execution;


import com.polaris.project.bizmq.BiMessageProducer;
import com.polaris.project.model.entity.Chart;
import com.polaris.project.model.vo.BiResponse;
import com.polaris.project.service.GenChartStrategy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 通过MQ异步消息生成
 *
 * @author adorabled4
 * @className GenChartSync
 * @date 2023/08/30
 */
@Component(value = "gen_mq")
public class GenChartMQ implements GenChartStrategy {

    @Resource
    BiMessageProducer biMessageProducer;


    @Override
    public BiResponse executeGenChart(Chart chart) {
        long newChartId = chart.getId();
        biMessageProducer.sendMessage(String.valueOf(newChartId));
        BiResponse biResponse = new BiResponse();
        biResponse.setChartId(newChartId);
        return biResponse;
    }
}
