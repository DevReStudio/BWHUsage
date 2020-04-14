package xyz.drstudio.bwhusage;


import android.app.Activity;

import com.kongzue.baseokhttp.HttpRequest;

import java.io.IOException;
import java.net.InetAddress;




public class CheckChinaWorking {
    //64.64.226.223
    private static String url  = "http://64.64.226.223/index.html";
    private static String ip = "64.64.226.223";

    public static String getIp() {
        return ip;
    }

    static boolean sendResquest() throws IOException {
        //HttpRequest.build(MainActivity.this, )

        return false;
    }
}