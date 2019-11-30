package me.zhengjie.terminal.command;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReceiveCommand extends BaseCommand {

    public void receiveDispatch(String event){
        if(event.startsWith(getGetValue())) {
            if (event.endsWith(getInputEventEndingFrame())) {
                inputHandler();
            } else if(event.endsWith(getButtonEventEndingFrame())){
                buttonHandler();
            }
        }else if(event.startsWith(getScanValue())){
            scanHandle();
        }else{
            unknowEvent();
        }
    }

    public void inputHandler(){

    }

    public void buttonHandler(){

    }

    public void scanHandle(){

    }

    public void unknowEvent(){
        log.warn("unknow event");
    }
}