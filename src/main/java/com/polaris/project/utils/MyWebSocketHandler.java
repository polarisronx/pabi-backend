package com.polaris.project.utils;

import com.alibaba.fastjson2.JSON;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author polaris
 * @version 1.0
 * ClassName MyWebSocketHandler
 * Package com.polaris.project.utils
 * Description
 * @create 2024-06-04 19:47
 */
@Slf4j
public class MyWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    public static final Map<String, ChannelHandlerContext> webSocketMap = new ConcurrentHashMap<>();



    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("与客户端建立连接，通道开启！");

        //添加到channelGroup通道组
        ChannelHandlerPool.channelGroup.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("与客户端断开连接，通道关闭！");
        //添加到channelGroup 通道组
        ChannelHandlerPool.channelGroup.remove(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //首次连接是FullHttpRequest，处理参数
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();

            Map paramMap=getUrlParams(uri);
            webSocketMap.put(paramMap.get("id").toString(),ctx);
            log.info("接收到的参数是：" + JSON.toJSONString(paramMap));

            //如果url包含参数，需要处理
            if(uri.contains("?")){
                String newUri=uri.substring(0,uri.indexOf("?"));
                log.info(newUri);
                request.setUri(newUri);
            }

        }else if(msg instanceof TextWebSocketFrame){
            //正常的TEXT消息类型
            TextWebSocketFrame frame=(TextWebSocketFrame)msg;
            log.info("客户端收到服务器数据：" +frame.text());
            ctx.writeAndFlush(new TextWebSocketFrame("Hello,与服务器的连接已建立！"));
        }
        super.channelRead(ctx, msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {

    }

//    /**
//     * 事件触发后会调用此方法
//     * @param ctx
//     * @param evt
//     * @throws Exception
//     */
//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        if (evt instanceof IdleStateEvent) {
//            IdleStateEvent e = (IdleStateEvent) evt;
//            if (e.state() == IdleState.READER_IDLE) {
//                String content = "服务端读事件触发，向客户端发送心跳包";
//                ByteBuf byteBuf = Unpooled.copiedBuffer(content, CharsetUtil.UTF_8);
//                // 发送心跳包
//                ctx.writeAndFlush(byteBuf);
//            } else if (e.state() == IdleState.WRITER_IDLE) {
//                String content = "服务端写事件触发，向客户端发送心跳包";
//                ByteBuf byteBuf = Unpooled.copiedBuffer(content, CharsetUtil.UTF_8);
//                // 发送心跳包
//                ctx.writeAndFlush(byteBuf);
//            } else if (e.state() == IdleState.ALL_IDLE) {
//                String content = "服务端的读/写事件触发，向客户端发送心跳包";
//                ByteBuf byteBuf = Unpooled.copiedBuffer(content, CharsetUtil.UTF_8);
//                // 发送心跳包
//                ctx.writeAndFlush(byteBuf);
//            }
//        }
//    }


    private void sendAllMessage(String message){
        //收到信息后，群发给所有channel
        ChannelHandlerPool.channelGroup.writeAndFlush( new TextWebSocketFrame(message));
    }

    public static void sendMessage(String id,String message){
        ChannelHandlerContext ctx = webSocketMap.get(id);
        if (ctx != null) {
            log.info("发送给客户端消息：" + message);
            ctx.channel().writeAndFlush(new TextWebSocketFrame(message));
        }
    }

    private static Map getUrlParams(String url){
        Map<String,String> map = new HashMap<>();
        url = url.replace("?",";");
        if (!url.contains(";")){
            return map;
        }
        if (url.split(";").length > 0){
            String[] arr = url.split(";")[1].split("&");
            for (String s : arr){
                String key = s.split("=")[0];
                String value = s.split("=")[1];
                map.put(key,value);
            }
            return  map;

        }else{
            return map;
        }
    }
}

