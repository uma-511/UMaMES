package me.zhengjie.terminal;

import lombok.Data;

import java.util.Vector;

/**
 * @Author: TJM
 * @Date: 2020/3/31 22:01
 */
@Data
public class PrintData {
    String ip;
    Vector<Vector<Byte>> frames;
    int curr;
    GobalSender gobalSender;

    public void addCurr(){
        curr++;
    }
}