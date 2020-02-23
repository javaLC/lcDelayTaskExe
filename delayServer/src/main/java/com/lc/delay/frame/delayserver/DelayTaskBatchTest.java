package com.lc.delay.frame.delayserver;

import ch.qos.logback.core.net.SyslogOutputStream;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

/**
 * @author liuchong
 * @version DelayTaskBatchTest.java, v 0.1 2020年02月23日 17:18
 */
public class DelayTaskBatchTest {

    public static void main(String[] args) {

        String url = "http://localhost:8081/submitTask?";
        for (int i = 0; i < 100; i++) {
            String param = "taskName=periodTask&delay=" + (new Random().nextInt(100) + 200);
            BufferedReader in = null;
            try {
                URL u = new URL(url + param);
                URLConnection conn = u.openConnection();
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                String result = "";
                while ((line = in.readLine()) != null) {
                    result += line;
                }
                System.out.println(url + param + "的结果：" + result);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e) {

                    }
                }
            }
            try {
                Thread.sleep(10);
            } catch (Exception e) {

            }
        }

    }

}
