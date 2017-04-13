package com.wzy.wzytv.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by zy on 2016/10/18.
 */

public class TextTools {
    public static String fileToJson(File file) {
        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file),"UTF-8");
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            String a = "";
            while ((line = br.readLine()) != null) {
                line = line.replace(",", "\",\"url\":\"");
                line = "{\"title\":\"" + line + "\"},";
                a = a + line;
            }
            String s = "{\"status\":\"success\",\"tv_list\":[" + a.substring(0, a.length() - 1) + "]}";
            return s;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
