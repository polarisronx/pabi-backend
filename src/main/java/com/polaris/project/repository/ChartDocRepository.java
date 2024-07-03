package com.polaris.project.repository;

import com.polaris.project.model.entity.ChartDoc;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author polaris
 * @version 1.0
 * ClassName ChartDocMapper
 * Package com.polaris.project.mapper
 * Description
 * @create 2024-06-26 16:13
 */
@Component
public interface ChartDocRepository extends MongoRepository<ChartDoc, String> {
    @Query("{'userId': ?0}")
    List<ChartDoc> findAllByUserId(long userId, Pageable pageable);


    long deleteAllByChartId(long chartId);

    @Query("{'chartId': ?0}")
    List<ChartDoc> findAllByChartId(long chartId);
}
