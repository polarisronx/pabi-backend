package com.polaris.project.utils;


import com.polaris.project.model.entity.ServerLoadInfo;
import com.sun.management.OperatingSystemMXBean;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;


/**
 * @author polaris
 * @className ServerMetricsUtil 服务器压力指标
 **/
@Slf4j
public class ServerMetricsUtil {
    private static OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
    private static MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

    private static final int TOTAL_BAND_WIDTH = 1000;   //网口带宽,Mbps


    /**
     * 获取当前服务器CPU使用占比
     *
     * @return CPU usage percentage.
     */
    public static double getCpuUsagePercentage() {
        return osBean.getProcessCpuLoad() * 100; // Convert to percentage
    }

    /**
     * 获取当前服务器内存使用占比
     *
     * @return Memory usage percentage.
     */
    public static double getMemoryUsagePercentage() {
        long usedMemory = memoryBean.getHeapMemoryUsage().getUsed();
        long maxMemory = memoryBean.getHeapMemoryUsage().getMax();

        return ((double) usedMemory / maxMemory) * 100; // Convert to percentage
    }
    /**
     * 获取当前服务器磁盘IO使用占比
     * linux中使用iostat命令获取磁盘IO使用率
     * @return Disk IO usage percentage.
     */
    public static double getDiskIOUsagePercentage () {
        log.info("开始收集磁盘IO使用率");
        double ioUsage = 0.0;
        Process pro = null;
        Runtime r = Runtime.getRuntime();
        try {
            String command = "iostat -d -x";
            pro = r.exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String line = null;
            int count =  0;
            while((line=in.readLine()) != null){
                if(++count >= 4){
                    String[] temp = line.split("\\s+");
                    if(temp.length > 1){
                        double util =  Double.parseDouble(temp[temp.length-1]);
                        ioUsage = Math.max(ioUsage, util);
                    }
                }
            }
            if(ioUsage > 0){
                log.info("本节点磁盘IO使用率为: " + ioUsage);
                ioUsage /= 100;
            }
            in.close();
            pro.destroy();
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.error("IoUsage发生InstantiationException. " + e.getMessage());
            log.error(sw.toString());
        }
        return ioUsage*100;
    }

    /**
     * 获取当前服务器网络IO使用占比
     * linux中/proc/net/dev文件中，第一行是总的流量信息，第二行是eth0的流量信息，第三行是lo的
     * @return Disk IO usage percentage.
     */
    public static double getNetworkUsagePercentage () {
        log.info("开始收集网络带宽使用率");
        double netUsage = 0.0f;
        Process pro1,pro2;
        Runtime r = Runtime.getRuntime();
        try {
            String command = "cat /proc/net/dev";
            //第一次采集流量数据
            long startTime = System.currentTimeMillis();
            pro1 = r.exec(command);
            BufferedReader in1 = new BufferedReader(new InputStreamReader(pro1.getInputStream()));
            String line = null;
            long inSize1 = 0, outSize1 = 0;
            while((line=in1.readLine()) != null){
                line = line.trim();
                if(line.startsWith("eth0")){
                    log.info(line);
                    String[] temp = line.split("\\s+");
                    inSize1 = Long.parseLong(temp[0].substring(5)); //Receive bytes,单位为Byte
                    outSize1 = Long.parseLong(temp[8]);             //Transmit bytes,单位为Byte
                    break;
                }
            }
            in1.close();
            pro1.destroy();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                log.error("NetUsage休眠时发生InterruptedException. " + e.getMessage());
                log.error(sw.toString());
            }
            //第二次采集流量数据
            long endTime = System.currentTimeMillis();
            pro2 = r.exec(command);
            BufferedReader in2 = new BufferedReader(new InputStreamReader(pro2.getInputStream()));
            long inSize2 = 0 ,outSize2 = 0;
            while((line=in2.readLine()) != null){
                line = line.trim();
                if(line.startsWith("eth0")){
                    log.info(line);
                    String[] temp = line.split("\\s+");
                    inSize2 = Long.parseLong(temp[0].substring(5));
                    outSize2 = Long.parseLong(temp[8]);
                    break;
                }
            }
            if(inSize1 != 0 && outSize1 !=0 && inSize2 != 0 && outSize2 !=0){
                double interval = (double)(endTime - startTime)/1000;
                //网口传输速度,单位为bps
                double curRate = (double)(inSize2 - inSize1 + outSize2 - outSize1)*8/(1000000*interval);
                netUsage = curRate/TOTAL_BAND_WIDTH;
                log.info("本节点网口速度为: " + curRate + "Mbps");
                log.info("本节点网络带宽使用率为: " + netUsage);
            }
            in2.close();
            pro2.destroy();
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.error("NetUsage发生InstantiationException. " + e.getMessage());
            log.error(sw.toString());
        }
        return netUsage;
    }

    public static ServerLoadInfo getLoadInfo(String profile) {
        double cpuUsagePercentage = getCpuUsagePercentage();
        double memoryUsagePercentage = getMemoryUsagePercentage();
        if(profile.equals("dev")){
            return new ServerLoadInfo(cpuUsagePercentage,memoryUsagePercentage,0.0,0.0);
        }
        double netUsage = getNetworkUsagePercentage();
        double IOUsage = getDiskIOUsagePercentage();
        return new ServerLoadInfo(cpuUsagePercentage,memoryUsagePercentage,netUsage,IOUsage);
    }
}