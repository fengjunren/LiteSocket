package com.litesocket.utils;

public class ByteUtil {
    public static int isOdd(int num)
    {
        return num & 0x1;
    }

    public static int HexToInt(String inHex)
    {
        return Integer.parseInt(inHex, 16);
    }

    public static byte HexToByte(String inHex)
    {
        return (byte)Integer.parseInt(inHex, 16);
    }

    public static String Byte2Hex(Byte inByte)
    {
        return String.format("%02x", new Object[] { inByte }).toUpperCase();
    }

    public static String ByteArrToHex(byte[] inBytArr)
    {
        StringBuilder strBuilder = new StringBuilder();
        int j = inBytArr.length;
        for (int i = 0; i < j; i++)
        {
            strBuilder.append(Byte2Hex(Byte.valueOf(inBytArr[i])));
            strBuilder.append("");
        }
        return strBuilder.toString();
    }

    public static int byteToInt2(byte[] b) {

        int mask=0xff;
        int temp=0;
        int n=0;
        for(int i=0;i<b.length;i++){
            n<<=8;
            temp=b[i]&mask;
            n|=temp;
        }
        return n;
    }

    public static int ByteArrToInt(byte[] inBytArr){
        return HexToInt(ByteArrToHex(inBytArr));
    }


    public static String ByteArrToHex(byte[] inBytArr, int offset, int byteCount)
    {
        StringBuilder strBuilder = new StringBuilder();
        int j = byteCount;
        for (int i = offset; i < j; i++)
        {
            strBuilder.append(Byte2Hex(Byte.valueOf(inBytArr[i])));
        }
        return strBuilder.toString();
    }

    public static byte[] HexToByteArr(String inHex)
    {
        int hexlen = inHex.length();
        byte[] result;
        if (isOdd(hexlen) == 1)
        {
            hexlen++;
            result = new byte[hexlen / 2];
            inHex = new StringBuilder().append("0").append(inHex).toString();
        } else {
            result = new byte[hexlen / 2];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2)
        {
            result[j] = HexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }
}
