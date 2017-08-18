package com.binfenjiari.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/4/10
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class MsgDigests {

    public static String sha1(String input) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
            byte[] inputBytes = input.getBytes("UTF-8");
            digest.update(inputBytes, 0, inputBytes.length);
            byte[] hash = digest.digest();
            return encodeHex(hash);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.getMessage();
        }
        return "";
    }

    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(input.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte anArray : hash) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.getMessage();
        }
        return "";
    }

    public static String md5AndBase64(String input) {
        try {
            return Base64.encodeToString(md5(input).getBytes("UTF-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String hmacSha1(String input, String key)
    {
        try {
            SecretKeySpec secret = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secret);
            byte[] bytes = mac.doFinal(input.getBytes("UTF-8"));
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.getMessage();
        }
        return "";
    }

    private static String encodeHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder(bytes.length * 2);
        for (byte aByte : bytes) {
            if (((int) aByte & 0xff) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toString((int) aByte & 0xff, 16));
        }
        return hex.toString();
    }
}
