package com.biu.modulebase.binfenjiari.communication;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * @author Lee
 * @Title: {使用HttpURLConnection实现多文件上传}
 * @Description:{描述}
 * @date 2016/3/23
 */
public class MultipartUploadUtil {
    String  urlString;
    HttpURLConnection conn;
    String boundary = "---------7d4a6d158c9"; // 定义数据分隔线
    Map<String, Object> textParams = new HashMap<>();
    Map<String, String> fileParams = new HashMap<>();
    DataOutputStream ds;

    public MultipartUploadUtil(String urlString,HashMap<String, Object> textParams,HashMap<String, String> fileParams){
        this.urlString =urlString;
        this.textParams =textParams;
        this.fileParams =fileParams;
    }

    // 发送数到服务器，返回一个字节包含服务器的返回结果的数组
    public String sendRequest() throws Exception {
        initConnection();
        try {
            conn.connect();
        } catch (SocketTimeoutException e) {
            // something
            throw new RuntimeException();
        }
        ds = new DataOutputStream(conn.getOutputStream());
        writeFileParams();
        writeStringParams();
        paramsEnd();
        InputStream in = conn.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int b;
        while ((b = in.read()) != -1) {
            out.write(b);
        }
        conn.disconnect();
        return new String(out.toByteArray());
    }
    //文件上传的connection的一些必须设置
    private void initConnection() throws Exception {
        URL url = new URL(urlString);
        conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setConnectTimeout(10000); //连接超时为10秒
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
    }
    //普通字符串数据
    private void writeStringParams() throws Exception {
        Set<String> keySet = textParams.keySet();
        for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
            String name = it.next();
            String value = textParams.get(name).toString();
            StringBuilder sb = new StringBuilder();
            sb.append("--").append(boundary).append("\r\n");
            sb.append("Content-Disposition: form-data; name=\"").append(name).append("\"\r\n");
            sb.append("\r\n");
            sb.append(encode(value));
            sb.append("\r\n");
            ds.writeBytes(sb.toString());
        }
    }
    //文件数据
    private void writeFileParams() throws Exception {
        Set<String> keySet = fileParams.keySet();
        for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
            String name = it.next();
            File file = new File(fileParams.get(name));
            StringBuilder sb = new StringBuilder();
            sb.append("--").append(boundary).append("\r\n");
            sb.append("Content-Disposition: form-data; name=\"").append(name).append("\"; filename=\"").append(encode(file.getName())).append("\"\r\n");
            sb.append("Content-Type:application/octet-stream").append("\r\n\r\n");//指定文件类型
            ds.writeBytes(sb.toString());
            ds.write(getBytes(file));//写入文件
            ds.writeBytes("\r\n"); //多个文件时，二个文件之间加上“\r\n”
        }
    }

    //把文件转换成字节数组
    private byte[] getBytes(File f) throws Exception {
        FileInputStream in = new FileInputStream(f);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int n;
        while ((n = in.read(b)) != -1) {
            out.write(b, 0, n);
        }
        in.close();
        return out.toByteArray();
    }
    //添加结尾数据
    private void paramsEnd() throws Exception {
        ds.writeBytes("--" + boundary + "--" + "\r\n");
        ds.writeBytes("\r\n");
    }
    // 对包含中文的字符串进行转码，此为UTF-8。服务器那边要进行一次解码
    private String encode(String value) throws Exception{
        return URLEncoder.encode(value, "UTF-8");
    }

}

