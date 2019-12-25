package me.zhengjie.terminal.command;

import me.zhengjie.utils.CoderUtils;

public class SendCommand extends BaseCommand {

    public String switchScreen(String screenId){
        return getSwitchScreen() + " " + screenId + " " + getFunctionEndingFrame();
    }

    public String setTextValue(String screenId,String textId,String value){
        if(value.length()>0){
            value=" " + CoderUtils.stringToHexStr(value).trim() + " ";
        }else{
            value=" ";
        }
        return getSetValue() + " " + screenId + " " + textId + value + getFunctionEndingFrame();
    }

    public String getTextValue(String screenId,String textId){
        return getGetValue() + " " + screenId + " " + textId + " " + getFunctionEndingFrame();
    }

    public String setFontColor(String screenId,String textId,String value){
        return getSetValue() + " " + screenId + " " + textId + " " + value + " " + getFunctionEndingFrame();
    }

    public String setControlStatus(String screenId,String controlId,String value){
        return getSetControlStatus() + " " + screenId + " " + controlId + " " + value + " " + getFunctionEndingFrame();
    }

    public String setControlEnable(String screenId,String controlId){
        String value = "01";
        return setControlStatus(screenId,controlId,value);
    }

    public String setControlDisable(String screenId,String controlId){
        String value = "00";
        return setControlStatus(screenId,controlId,value);
    }
}