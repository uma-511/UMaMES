package me.zhengjie.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TerminalUtils {
    public static String getScreenId(String msg,String before,String after){
        String screenId = "";
        screenId = msg.replaceAll(before,"").replaceAll(after,"").trim().substring(0,5);
        log.info("screenId:"+screenId);
        return screenId;
    }

    public static String getControlId(String msg,String before,String after){
        String controlId = "";
        controlId = msg.replaceAll(before,"").replaceAll(after,"").trim().substring(6,11);
        log.info("controlId:"+controlId);
        return controlId;
    }

    public static String getTextValue(String msg,String before,String after){
        msg = msg.replaceAll(before,"").replaceAll(after,"").trim();
        if(msg.length()<16){
            return "";
        }
        log.info(Integer.toString(msg.length()));
        String screnCommand=msg.substring(15);
        String value= CoderUtils.decoder(screnCommand.replaceAll(" ",""));
        log.info("input value:"+value);
        return value;
    }
}