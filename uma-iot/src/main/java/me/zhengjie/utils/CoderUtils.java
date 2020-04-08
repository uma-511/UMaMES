package me.zhengjie.utils;

import java.io.UnsupportedEncodingException;
import java.util.Vector;

public abstract class CoderUtils {

    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
                .byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
                .byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    public static byte[] HexString2Bytes(String src) {
        byte[] ret = new byte[8];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < 8; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    public static byte[] fromHexString(String hexString){
        String result="";
        hexString= hexString.toUpperCase();
        String hexDigital="0123456789ABCDEF";
        char[] hexs=hexString.toCharArray();
        byte[] bytes=new byte[hexString.length()/2];
        int n;
        for(int i=0;i<bytes.length;i++){
            n=hexDigital.indexOf(hexs[2*i])*16+hexDigital.indexOf(hexs[2*i]+1);
            bytes[i]=(byte)(n & 0xff);
        }
        return bytes;
//        try {
//            result=new String(bytes,"GB2312");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return result;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if(hex.length() < 2){
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }


    public static byte[] hexToByteArray(String inHex){
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1){
            //奇数
            hexlen++;
            result = new byte[(hexlen/2)];
            inHex="0"+inHex;
        }else {
            //偶数
            result = new byte[(hexlen/2)];
        }
        int j=0;
        for (int i = 0; i < hexlen; i+=2){
            result[j]=hexToByte(inHex.substring(i,i+2));
            j++;
        }
        return result;
    }

    public static byte hexToByte(String inHex){
        return (byte)Integer.parseInt(inHex,16);
    }

    public static void encoder(String msg){
        try {
            byte[] b=msg.getBytes("GB2312");
            System.out.println(cvtStr2Hex(bytesToHexFun1(b)));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String stringToHexStr(String value){
        String res="";
        try {
            byte[] b=value.getBytes("GB2312");
            res=cvtStr2Hex(bytesToHexFun1(b));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }finally {
            return res;
        }
    }

    public static String decoder(String hexString){
        String str=hexString.replaceAll("0x","");
        String res="";
        try {
            res=new String(hexToByteArray(str),"GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return res;
    }

    //将byte数组转成16进制字符串
    public static String bytesToHexFun1(byte[] bytes) {
        char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
                '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        // 一个byte为8位，可用两个十六进制位标识
        char[] buf = new char[bytes.length * 2];
        int a = 0;
        int index = 0;
        for(byte b : bytes) { // 使用除与取余进行转换
            if(b < 0) {
                a = 256 + b;
            } else {
                a = b;
            }
            buf[index++] = HEX_CHAR[a / 16];
            buf[index++] = HEX_CHAR[a % 16];
        }
        return new String(buf);
    }

    public static String cvtStr2Hex(String str) {
        if (str == null)
            return "";
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < str.length() / 2; i ++) {
            String sub = str.substring(i*2, i*2 + 2);
            sb.append(sub.toUpperCase() + " ");
        }
        return sb.toString();
    }

    public static byte[] cvtStr2Hex1(String str) {
        str=str.replaceAll(" ","");
        byte[] bytes = new byte[str.length()/2];
        for (int i = 0; i < str.length() / 2; i ++) {
            String sub = str.substring(i*2, i*2 + 2);
            bytes[i]=(byte)Integer.parseInt(sub.toLowerCase(),16);
        }
        return bytes;
    }

    public static Vector<Byte> cvtStr2Hex2(String str) {
        str=str.replaceAll(" ","");
        Vector<Byte> bytes = new Vector<Byte>();
        for (int i = 0; i < str.length() / 2; i ++) {
            String sub = str.substring(i*2, i*2 + 2);
            bytes.add((byte)Integer.parseInt(sub.toLowerCase(),16));
        }
        return bytes;
    }
}