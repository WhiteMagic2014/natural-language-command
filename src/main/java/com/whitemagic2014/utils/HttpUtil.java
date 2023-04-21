package com.whitemagic2014.utils;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpUtil {

    private static boolean useCaches = false;
    private static int connectTimeOut = 10000;// default 10s
    private static int readTimeout = 30000;// default 30s

    public static String post(String path, JSON jsonParam) {
        URL url = null;
        try {
            url = new URL(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        HttpURLConnection connection = null;
        OutputStream os = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(useCaches);
            connection.setConnectTimeout(connectTimeOut);
            connection.setReadTimeout(readTimeout);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();

            os = connection.getOutputStream();
            os.write(jsonParam.toString().getBytes(StandardCharsets.UTF_8));
            os.flush();

            StringBuilder sb = new StringBuilder();
            if (connection.getResponseCode() == 200) {
                try (InputStream inputStream = connection.getInputStream();
                     InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                     BufferedReader bufferedReader = new BufferedReader(inputStreamReader);) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return sb.toString();
            } else {
                try (InputStream errorStream = connection.getErrorStream();
                     InputStreamReader inputStreamReader = new InputStreamReader(errorStream, StandardCharsets.UTF_8);
                     BufferedReader bufferedReader = new BufferedReader(inputStreamReader);) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                throw new RuntimeException(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
