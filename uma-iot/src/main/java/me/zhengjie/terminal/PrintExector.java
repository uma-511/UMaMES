package me.zhengjie.terminal;

/**
 * @Author: TJM
 * @Date: 2020/3/31 22:05
 */
public class PrintExector {
    private PrintExector(){
        if(PrintExcetorHolder.printExcetor!=null){
            throw new RuntimeException("不允许创建多个实例");
        }
    }

    public static final PrintExector getInstance(){
        return PrintExcetorHolder.printExcetor;
    }

    private static class PrintExcetorHolder{
        private static final PrintExector printExcetor = new PrintExector();
    }

    public synchronized void print(String ip){
        PrintData printData = PrintDataHolder.getInstance().getTask(ip);
        if(null != printData) {
            int curr = printData.getCurr();

            if (curr == printData.getFrames().size()) {
                PrintDataHolder.getInstance().removeData(ip);
            } else {
                GobalSender gobalSender = printData.getGobalSender();
                gobalSender.send(printData.getFrames().get(curr), ip);
            }
        }
    }
}