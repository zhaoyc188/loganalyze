package com.yjl.analyze;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang.StringUtils;

public class Main {

    private static Map<String, Integer> all = new TreeMap<>(); // 保存全量请求的url数量
    private static Map<String, Integer> timeout = new TreeMap<>(); // 保存耗时请求的url数量
    private static Integer times = 1000; // 超时时间，毫秒

    public static void main(String[] args) throws IOException {
        if (args == null || args.length == 0) {
            System.out.println("1.第一个参数为日志所在目录的绝对路径或者具体日志文件的绝对路径");
            System.out.println("2.第二个参数为分析结果的输出文件");
            System.out.println("3.第三个参数为超时时间，单位毫秒");
            return;
        }

        String outFile = "result.csv";
        File dir = new File("C:\\Users\\Administrator\\Desktop\\");

        // 日志文件
        if (args != null && args.length >= 1) {
            dir = new File(args[0]);
        }

        // 输出文件
        if (args != null && args.length >= 2) {
            outFile = args[1];
        }

        // 超时时间设置
        if (args != null && args.length >= 3) {
            if (StringUtils.isNumeric(args[2])) {
                times = Integer.parseInt(args[2]);
            }
        }

        System.out.println("开始分析...");


        FileWriter fw = new FileWriter(outFile);

        if (dir.isFile()) {
            findTimeoutUrl(dir);
        } else {
            File[] files = dir.listFiles();
            for (File file : files) {
                // 只处理log文件
                if (file.getName().endsWith(".log")) {
                    findTimeoutUrl(file);
                }
            }
        }

        // 写入文件：耗时url总请求次数和超时次数
        fw.write("请求rul,请求总次数,超时请求次数,超时占比\r\n");
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : timeout.entrySet()) {
            sb = new StringBuilder();
            sb.append(entry.getKey()).append(","); // url
            sb.append(all.get(entry.getKey())).append(",");// 总请求次数
            sb.append(entry.getValue()).append(",");// 超时请求次数
            sb.append(entry.getValue() * 1.0 / all.get(entry.getKey())).append("\r\n");
            fw.write(sb.toString());
        }

        // 写入动作
        fw.flush();
        fw.close();

        System.out.println("分析完成，输出结果：" + outFile);

    }

    /**
     * 日志分析，分析结果保存到map中
     * 
     * @param logFile
     * @throws IOException
     */
    public static void findTimeoutUrl(File logFile) throws IOException {
        FileReader reader = new FileReader(logFile);
        BufferedReader br = new BufferedReader(reader);

        String str = null;
        while ((str = br.readLine()) != null) {

            // 解析请求时间和url
            String time = str.substring(str.lastIndexOf(" ")).trim();
            String url = str.substring(str.indexOf("\"") + 1, str.lastIndexOf("\"")).split(" ")[1];

            // 计算url全部请求次数
            Integer cou = all.get(url);
            all.put(url, cou == null ? 1 : ++cou);

            // 计算耗时url请求次数
            if (StringUtils.isNumeric(time) && Integer.parseInt(time) > times) {
                cou = timeout.get(url);
                timeout.put(url, cou == null ? 1 : ++cou);
            }
        }

        br.close();
        reader.close();
    }

}
