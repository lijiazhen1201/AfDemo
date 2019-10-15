package cn.appoa.aframework.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Md5加密工具类
 */
public class Md5Utils {

    private static String[] strDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e",
            "f"};

    private Md5Utils() {
    }

    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    /**
     * 32位Md5加密
     *
     * @param strObj
     * @return
     */
    public static String GetMD5Code32(String strObj) {
        String resultString = null;
        try {
            resultString = new String(strObj);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString;
    }

    /**
     * 16位Md5加密
     *
     * @param strObj
     * @return
     */
    public static String GetMD5Code16(String strObj) {
        String resultString = null;
        try {
            resultString = new String(strObj);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString.substring(8, 24);
    }

    /**
     * 短信加密token
     *
     * @param phone 手机号
     * @return
     */
    public static String getSmsToken(String phone) {
        String token = phone;
        // 获取当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String d = phone + sdf.format(new Date());
        // 32位md5加密（小写）
        String d32 = GetMD5Code32(d);
        token = //
                // 6,7,5,3,1,8,4,5,
                d32.substring(6, 7)//
                        + d32.substring(7, 8)//
                        + d32.substring(5, 6)//
                        + d32.substring(3, 4)//
                        + d32.substring(1, 2)//
                        + d32.substring(8, 9)//
                        + d32.substring(4, 5)//
                        + d32.substring(5, 6)//
                        // 9,17,17,15,12,15,13,10,
                        + d32.substring(9, 10)//
                        + d32.substring(17, 18)//
                        + d32.substring(17, 18)//
                        + d32.substring(15, 16)//
                        + d32.substring(12, 13)//
                        + d32.substring(15, 16)//
                        + d32.substring(13, 14)//
                        + d32.substring(10, 11)//
                        // 21,22,22,17,24,24,18,19,
                        + d32.substring(21, 22)//
                        + d32.substring(22, 23)//
                        + d32.substring(22, 23)//
                        + d32.substring(17, 18)//
                        + d32.substring(24, 25)//
                        + d32.substring(24, 25)//
                        + d32.substring(18, 19)//
                        + d32.substring(19, 20)//
                        // 25,29,30,28,31,29,27,31
                        + d32.substring(25, 26)//
                        + d32.substring(29, 30)//
                        + d32.substring(30, 31)//
                        + d32.substring(28, 29)//
                        + d32.substring(31)//
                        + d32.substring(29, 30)//
                        + d32.substring(27, 28)//
                        + d32.substring(31)//
                        + "";
        return token;
    }

}
