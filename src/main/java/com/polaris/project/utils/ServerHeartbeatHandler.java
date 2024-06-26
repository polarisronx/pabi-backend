package com.polaris.project.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author polaris
 * @version 1.0
 * ClassName ServerHeartbeatHandler
 * Package com.polaris.project.utils
 * Description
 * @create 2024-06-06 12:02
 */
@Slf4j
public class ServerHeartbeatHandler extends ChannelInboundHandlerAdapter {
    /**
     * 事件触发后会调用此方法
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                String content = "服务端读事件触发，向客户端发送心跳包";
                ByteBuf byteBuf = Unpooled.copiedBuffer(content, CharsetUtil.UTF_8);
                // 发送心跳包
                ctx.writeAndFlush(byteBuf);
            } else if (e.state() == IdleState.WRITER_IDLE) {
                String content = "服务端写事件触发，向客户端发送心跳包";
                ByteBuf byteBuf = Unpooled.copiedBuffer(content, CharsetUtil.UTF_8);
                // 发送心跳包
                ctx.writeAndFlush(byteBuf);
            } else if (e.state() == IdleState.ALL_IDLE) {
                String content = "服务端的读/写事件触发，向客户端发送心跳包";
                ByteBuf byteBuf = Unpooled.copiedBuffer(content, CharsetUtil.UTF_8);
                // 发送心跳包
                ctx.writeAndFlush(byteBuf);
            }
        }
    }

    /**
     * 用于读取客户端发送的消息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("执行 channelRead");
        // 处理接收到的数据
        ByteBuf byteBuf = (ByteBuf) msg;
        try {
            // 将接收到的字节数据转换为字符串
            String message = byteBuf.toString(CharsetUtil.UTF_8);
            // 打印接收到的消息
            System.out.println("接收到客户端消息为: " + message);
            // 发送响应消息给客户端
            ctx.writeAndFlush(Unpooled.copiedBuffer("我是服务端，我收到你的消息啦~", CharsetUtil.UTF_8));
        } finally {
            // 释放ByteBuf资源
            ReferenceCountUtil.release(byteBuf);
        }
    }
}

