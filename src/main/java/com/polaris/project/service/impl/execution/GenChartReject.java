package com.polaris.project.service.impl.execution;


import com.polaris.project.common.ErrorCode;
import com.polaris.project.exception.BusinessException;
import com.polaris.project.model.entity.Chart;
import com.polaris.project.model.vo.BiResponse;
import com.polaris.project.service.GenChartStrategy;
import org.springframework.stereotype.Component;

/**
 * 拒绝策略
 *
 * @author adorabled4
 * @className GenChartSync
 * @date 2023/08/30
 */
@Component(value = "gen_reject")
public class GenChartReject implements GenChartStrategy {

    @Override
    public BiResponse executeGenChart(Chart chart) {
        throw new BusinessException(ErrorCode.TOO_MANY_REQUEST, "服务器繁忙,请稍后重试!");
    }
}
