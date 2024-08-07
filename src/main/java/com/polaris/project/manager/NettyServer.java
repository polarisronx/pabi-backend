package com.polaris.project.manager;

import com.polaris.project.utils.MyWebSocketHandler;
import com.polaris.project.utils.SslUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author polaris
 * @version 1.0
 * ClassName WebSocketConfig
 * Package com.polaris.project.config
 * Description
 * @create 2024-06-03 21:37
 */

@Configuration
@Slf4j
@Data
public class NettyServer {
//    @Value("${netty-server.port}")
    private int port=6848;
    private ChannelFuture channelFuture;
    //负责处理接受进来的链接
    private EventLoopGroup bossGroup;
    //负责处理已经被接收的连接上的I/O操作
    private EventLoopGroup workerGroup;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;


    @Async("threadPoolExecutor")
    public void start() throws Exception {


        SSLContext sslContext = SslUtil.createSSLContext("JKS",
                new ClassPathResource("pabi.jks").getInputStream(), "wws1j8oa");
        // SSLEngine 此类允许使用ssl安全套接层协议进行安全通信
        SSLEngine engine = sslContext.createSSLEngine();
        engine.setUseClientMode(false);
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.option(ChannelOption.SO_BACKLOG, 1024);
            sb.group(bossGroup, workerGroup) // 绑定线程池
                    .channel(NioServerSocketChannel.class) // 指定使用的channel
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 绑定客户端连接时候触发操作

                        @Override
                        protected void initChannel(SocketChannel ch) {
                            log.info("收到新连接");
//                            ch.pipeline().addLast(new SslHandler(engine)); // 设置SSL
                            //websocket协议本身是基于http协议的，所以这边也要使用http解编码器
                            ch.pipeline().addLast(new HttpServerCodec());
                            //以块的方式来写的处理器
                            ch.pipeline().addLast(new ChunkedWriteHandler());
                            // 这里设置5秒内没有从 Channel 读取到数据时会触发一个 READER_IDLE 事件。心跳保活
//                            ch.pipeline().addLast(new IdleStateHandler(30, 30, 0, TimeUnit.SECONDS));
                            ch.pipeline().addLast(new HttpObjectAggregator(8192));
                            ch.pipeline().addLast(new MyWebSocketHandler());
                            ch.pipeline().addLast(new WebSocketServerProtocolHandler("/websocket", null, true, 65536 * 10));


                        }
                    });// 绑定监听端口;
            channelFuture = sb.bind("0.0.0.0",6848).sync(); // 服务器异步创建绑定
            log.info(NettyServer.class + " 启动正在监听： " + channelFuture.channel().localAddress());
            channelFuture.channel().closeFuture().sync(); // 关闭服务器通道
        } finally {
            workerGroup.shutdownGracefully().sync(); // 释放线程池资源
            bossGroup.shutdownGracefully().sync();
        }
    }
    @PreDestroy
    public void stopServer(){
        if (channelFuture != null && !channelFuture.isDone()) {
            channelFuture.cancel(true);
        }
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}
