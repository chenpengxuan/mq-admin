/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.mq.admin.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CipherUtil {

    public static final String KEY_MD5 = "MD5";

    /**
     * MD5加密
     *
     * @param data
     * @return
     */
    public static String encryptMD5(String data) {

        try {
            MessageDigest md = MessageDigest.getInstance(KEY_MD5);
            md.update(data.getBytes());

            byte b[] = md.digest();
            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }

                buf.append(Integer.toHexString(i));
            }

            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        System.out.println(CipherUtil.encryptMD5("admin"));
    }

}
