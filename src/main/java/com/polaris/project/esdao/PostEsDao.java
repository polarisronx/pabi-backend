package com.polaris.project.esdao;

import com.polaris.project.model.dto.post.PostEsDTO;

import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 帖子 ES 操作
 *
 * @author <a href="https://github.com/polarisronx">polaris</a>
 * @from <a href="https://papi.icu">北极星</a>
 */
public interface PostEsDao extends ElasticsearchRepository<PostEsDTO, Long> {

    List<PostEsDTO> findByUserId(Long userId);
}