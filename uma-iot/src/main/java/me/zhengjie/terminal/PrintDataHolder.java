package me.zhengjie.terminal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: TJM
 * @Date: 2020/3/31 21:59
 */
public class PrintDataHolder {
    private Map<String,PrintData> tasks = new ConcurrentHashMap<>();

    private boolean running = false;

    private PrintDataHolder(){
        if(PrintTaskHolderHolder.printTaskHolder!=null){
            throw new RuntimeException("不允许创建多个实例");
        }
    }

    public static final PrintDataHolder getInstance(){
        return PrintTaskHolderHolder.printTaskHolder;
    }

    private static class PrintTaskHolderHolder{
        private static final PrintDataHolder printTaskHolder = new PrintDataHolder();
    }

    public void addData(String ip,PrintData printData) {
        tasks.put(ip,printData);
        start(ip);
    }

    public PrintData getTask(String ip) {
        if(tasks.containsKey(ip)){
            return tasks.get(ip);
        }else {
            return null;
        }
    }

    public void removeData(String ip) {
        tasks.remove(ip);
    }

    private void start(String ip){
        if(!running) {
            running = true;
            PrintData printTask = tasks.get(ip);
            if (printTask != null) {
                PrintExector.getInstance().print(ip);
            }
        }
    }
}