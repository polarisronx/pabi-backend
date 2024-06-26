package com.polaris.project.utils;



import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author polaris
 * @version 1.0
 * ClassName ChannelHandlerPool
 * Package com.polaris.project.utils
 * Description 通道组池，管理所有websocket连接
 * @create 2024-06-04 19:42
 */
public class ChannelHandlerPool {

    public ChannelHandlerPool(){}

    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

}


