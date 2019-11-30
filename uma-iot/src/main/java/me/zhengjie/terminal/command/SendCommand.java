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

    public String setFontColor(String screenId,String textId,String value){
        return getSetValue() + " " + screenId + " " + textId + " " + value + " " + getFunctionEndingFrame();
    }
}