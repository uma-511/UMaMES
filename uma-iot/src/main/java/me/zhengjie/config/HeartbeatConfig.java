package me.zhengjie.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@ConfigurationProperties(prefix = "uma.heartbeat")
@Data
public class HeartbeatConfig {

    /**
     * 心跳指令
     */
    private String heartbeatMsg = "YM0001";

    /**
     * 重连尝试次数
     */
    private int reconnectTimes = 2;

    /**
     * 读空闲超时
     */
    private int readerIdleTime = 0;

    /**
     * 写空闲超时
     */
    private int writererIdleTime = 0;

    /**
     * 全部空闲超时
     */
    private int allIdleTime = 0;

    /**
     * 空闲时间单位
     */
    private TimeUnit timeUnit = TimeUnit.SECONDS;
}