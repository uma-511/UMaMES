package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.HeartBeat;
import me.zhengjie.uma_mes.service.dto.HeartBeatDTO;
import me.zhengjie.uma_mes.service.dto.HeartBeatQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Xie Ji Biao
* @date 2019-12-04
*/
public interface HeartBeatService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(HeartBeatQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<HeartBeatDTO>
    */
    List<HeartBeatDTO> queryAll(HeartBeatQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return HeartBeatDTO
     */
    HeartBeatDTO findById(Integer id);

    HeartBeatDTO create(HeartBeat resources);

    void update(HeartBeat resources);

    void delete(Integer id);

    void download(List<HeartBeatDTO> all, HttpServletResponse response) throws IOException;
}