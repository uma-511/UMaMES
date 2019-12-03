package me.zhengjie.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "uma.iot")
@Data
public class IotConfig {
    /**
     * 切换界面
     */
    private String switchScreen="EE B1 00";
    /**
     * 设置控件值
     */
    private String setValue="EE B1 01";
    /**
     * 获取控件值
     */
    private String getValue="EE B1 11";
    /**
     * 点击按钮
     */
    private String buttonClick="EE B1 11";
    /**
     * 设置边框颜色
     */
    private String setBoxColor="EE B1 18";
    /**
     * 设置字体颜色
     */
    private String setFontColor="EE B1 19";
    /**
     * 服务端发送指令的固定结尾
     */
    private String functionEndingFrame="FF FC FF FF";
    /**
     * 服务端接收按钮点击事件指令的固定结尾
     */
    private String buttonEventEndingFrame="FF FC FF FF";
    /**
     * 服务端接收文本值变化事件指令的固定结尾
     */
    private String inputEventEndingFrame="00 FF FC FF FF";
}