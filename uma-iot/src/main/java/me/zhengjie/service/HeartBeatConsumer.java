package me.zhengjie.service;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.uma_mes.domain.HeartBeat;
import me.zhengjie.uma_mes.service.HeartBeatService;
import me.zhengjie.uma_mes.service.dto.HeartBeatDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Slf4j
@Component
public class HeartBeatConsumer {
    private static final Logger logger = LoggerFactory.getLogger(HeartBeatConsumer.class);
    @Autowired
    HeartBeatService heartBeatService;

    public HeartBeatDTO createHeartBeatRecord(String ip, String port){
        HeartBeat heartBeat = new HeartBeat();
        heartBeat.setIp(ip);
        heartBeat.setPort(port);
        heartBeat.setCreateTime(new Timestamp(System.currentTimeMillis()));
        HeartBeatDTO heartBeatResult = heartBeatService.create(heartBeat);
        return heartBeatResult;
    }

    public void updateSendTime(HeartBeatDTO heartBeatDTO){
        HeartBeat heartBeat = new HeartBeat();
        heartBeatDTO.setSendTime(new Timestamp(System.currentTimeMillis()));
        BeanUtils.copyProperties(heartBeatDTO,heartBeat);
        heartBeatService.update(heartBeat);
    }

    public void updateResponseTime(HeartBeatDTO heartBeatDTO){
        HeartBeat heartBeat = new HeartBeat();
        heartBeatDTO.setResponseTime(new Timestamp(System.currentTimeMillis()));
        BeanUtils.copyProperties(heartBeatDTO,heartBeat);
        heartBeatService.update(heartBeat);
    }
}