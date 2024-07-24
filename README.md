<h1 align="center">Polaris BI 智能数据分析平台</h1>
<p align="center"><strong>Pabi 是一个为用户和开发者提供安全可靠的数据分析服务的平台 🛠</strong></p>
<div align="center">
	<a target="_blank" href="https://github.com/polarisronx/Papi-backend">
    	<img alt="" src="https://github.com/qimu666/qi-api/badge/star.svg?theme=gvp"/>
	</a>
    <img alt="Maven" src="https://raster.shields.io/badge/Maven-3.8.2-red.svg"/>
    <img alt="SpringBoot" src="https://raster.shields.io/badge/SpringBoot-2.7+-green.svg"/>
    <a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/index.html">
        <img alt="" src="https://img.shields.io/badge/JDK-1.8+-green.svg"/>
	</a></div>


## 什么是 PaBI

基于Spring Boot + MQ + AIGC + React的智能数据分析平台。区别于传统B，用户只需要导入原始数据集、并输入分析诉求，就能自动生成可视化图表及分析结论，实现数据分析的降本增效(或者降低数据分析的人工成本、提高数据分析效率等)。

- 您可以前往 <a href="https://api.papi.icu">Pabi 智能数据分析平台</a> 在线尝试。

> 我们也推荐您前往我们新开发的 <a href="api.papi.icu">Polaris API</a> 在线开放平台，目前正在内测✨

## 源码指南

<a href="https://github.com/polarisronx/pabi-backend">Pabi 后端项目</a>

<a href="https://github.com/polarisronx/pabi-frontend">Pabi 前端项目</a>

## 快速启动

### 前端

环境要求：Node.js >= 16，yarn 或 npm 包依赖工具

1. 安装依赖：npm 或 yarn 均可

```bash
yarn install
npm install
```

2. 启动：npm 或 yarn 均可

```bash
yarn run start
npm run start
```

3. 部署：npm 或 yarn 均可

```bash
yarn run build
npm run build
```

### 后端

1. 执行sql目录下ddl.sql在数据库中生成对应库表；
2. 启动 Redis；
3. 修改项目配置application.yaml；
4. 启动rabbitmq，执行BiInitMain生成对应队列；
5. 启动后端项目。

## 主要技术选型

### 前端

- React 18.2.0
- Echarts 可视化库 5.5.0
- Ant Design Pro 开发脚手架 3.3.0
- Ant Design 组件库 5.3.3
- Ant Design Pro Components 2.6.48
- UmiJS 前端框架 4.1.1
- Openapi 生成工具 1.0.1

### 后端

- Java 1.8 （向上兼容）
- Springboot 2.7.0
- Hutool 5.8.16
- Redisson 3.21.3
- MySQL 8.0.31 （兼容5.x版本）
- Redis 7.2.0 （兼容低版本）
- Knife4j 4.0.0
- MongoDB 4.4
- Netty 4.1
- Rabbitmq 3.10
- EasyExcel 3.1.1
- ChatGPT 3.5

## 开发简介

### 项目结构

![image](https://github.com/polarisronx/Papi-backend/blob/master/doc/images/pabi-architecture.png)

## 主要功能

### 1 登录和注册

您可以 <a href="https://github.com/polarisronx/pabi-frontend">Pabi 智能数据分析平台 </a> 的登录和注册页面完成登录和注册，目前支持账号密码登录。

### 2 在线同步分析

您可以 <a href="https://github.com/polarisronx/pabi-frontend">Pabi 智能数据分析平台 </a> 的智能在线分析页面提交您的分析需求和原始数据，在线生成分析结论和可视化图表。（视服务器繁忙情况生成耗时可能会有波动，请耐心等待）

![](https://github.com/polarisronx/Papi-backend/blob/master/doc/images/add.png)

但请注意：上传的接口需要满足一定的规范，在上传前请务必仔细阅读文档 <a href="https://doc.papi.icu">Papi 开发者文档</a>

### 3 智能异步分析

您可以 <a href="https://github.com/polarisronx/pabi-frontend">Pabi 智能数据分析平台 </a> 的智能异步分析页面提交您的分析需求和原始数据，服务端会根据不同策略异步完成分析任务，并在完成后通知您。

在使用前确保与后端通讯服务器连接正常。

<img src="https://github.com/polarisronx/Papi-backend/blob/master/doc/images/QQ20240707210838.png" style="zoom: 67%;" />

![](https://github.com/polarisronx/Papi-backend/blob/master/doc/images/QQ20240707210947.png)

### 4 图表管理

- 查看已生成图表

- 管理未完成图表


- 管理全部图表

### 5 接口调用统计分析

目前功能还未完善，后续后会对所有用户开放。

### 6 个性化设置

目前功能还未完善，后续后会对所有用户开放。



### 拓展1：基于Netty-webSocket的消息推送

- #### **V1 采用WebSocket的客户端-服务器模型**

参考[SpringBoot2.0集成WebSocket，实现后台向前端推送信息_springboot集成websocket-CSDN博客](https://blog.csdn.net/moshowgame/article/details/80275084?ops_request_misc=%7B%22request%5Fid%22%3A%22164266765516780265476735%22%2C%22scm%22%3A%2220140713.130102334..%22%7D&request_id=164266765516780265476735&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~top_positive~default-1-80275084.pc_search_insert_es_download&utm_term=WebSocket&spm=1018.2226.3001.4187)

> Spring官网关于websocket的guide
>
> [Getting Started | Using WebSocket to build an interactive web application (spring.io)](https://spring.io/guides/gs/messaging-stomp-websocket)

**1 什么是websocket？**

WebSocket协议是基于TCP的一种新的网络协议。它实现了浏览器与服务器全双工(full-duplex)通信——允许服务器主动发送信息给客户端。非常适合作为**服务器推送**的技术

**2 为什么需要 WebSocket？**

HTTP 协议有一个缺陷：**通信只能由客户端发起**，HTTP 协议做不到服务器主动向客户端推送信息。HTTP 在每次请求结束后都会主动释放连接，因此HTTP连接是一种“**短连接**”。要保持客户端程序的在线状态，就需要向服务器轮询，即不断地向服务器发起连接请求，举例来说，我们想要查询当前的排队情况，只能是页面轮询向服务器发出请求，服务器返回查询结果。轮询的效率低，非常浪费资源（因为必须不停连接，或者 HTTP 连接始终打开）。因此WebSocket 就是这样发明的。

![](https://github.com/polarisronx/Papi-backend/blob/master/doc/images/20180510225115144.png)

**3 WebSocket的特点**

1）客户端发起的连接建立在 TCP 协议之上，服务器端的实现比较容易。
2）与 HTTP 协议有着良好的兼容性，握手阶段采用 HTTP 协议。做一个握手的动作浏览器和服务器之间就创建了持久性的连接，两者之间就直接可以进行双向数据传输。
3）数据格式比较轻量，性能开销小，通信高效。
4）可以发送文本，也可以发送二进制数据。
5）没有同源限制，客户端可以与任意服务器通信。
6）协议标识符是 ws（如果加密，则为wss），服务器网址就是 URL。

**4 与其他交互技术的对比**

1）**Http轮询**
长轮询就是客户端按照一个固定的时间定期向服务器发送请求，通常这个时间间隔的长度受到服务端的更新频率和客户端处理更新数据时间的影响。这种方式缺点很明显，就是浏览器要不断发请求到服务器以获取最新信息，造成服务器压力过大，占用宽带资源。
2）**使用 streaming AJAX**
streaming ajax是一种通过 ajax 实现的长连接维持机制。主要目的就是在数据传输过程中对返回的数据进行读取，并且不会关闭连接。
3）**iframe方式**
iframe 可以在页面中嵌套一个子页面，通过将 iframe 的 src 指向一个长连接的请求地址，服务端就能不断往客户端传输数据。

很多网站为了实现推送技术，所用的技术都是 Ajax 轮询。轮询是在特定的的时间间隔（如每1秒），由浏览器对服务器发出 HTTP 请求，然后由服务器返回最新的数据给客户端的浏览器。这种传统的模式带来很明显的缺点，即浏览器需要不断的向服务器发出请求，然而 HTTP 请求可能包含较长的头部，其中真正有效的数据可能只是很小的一部分，显然这样会浪费很多的带宽等资源。

WebSocket 使得客户端和服务器之间的数据交换变得更加简单，允许服务端主动向客户端推送数据。在 WebSocket API 中，浏览器和服务器只需要完成一次握手，两者之间就直接可以创建持久性的连接，并进行双向数据传输。

参考：https://blog.csdn.net/IT__learning/article/details/120220624

**4 落地实现**

**后端：**

（1）依赖包

Spring提供的websocket启动包

```xml
<dependency>  
    <groupId>org.springframework.boot</groupId>  
    <artifactId>spring-boot-starter-websocket</artifactId>  
</dependency> 
```

（2）配置 Socket

`ServerEndpointExporter` 是由Spring官方提供的标准实现，用于扫描`ServerEndpointConfig`配置类和@`ServerEndpoint`注解实例。使用规则也很简单：

```java
@Configuration  
public class WebSocketConfig {  	
    @Bean  
    public ServerEndpointExporter serverEndpointExporter() {  
        return new ServerEndpointExporter();  
    }  
}
```

（3）后端建立Server

```java
@ServerEndpoint("/wsServer/{userId}")
@Component
@Slf4j
public class WebSocketServer {
```

属性：

1 OnlineCount：【静态变量】记录当前在线连接数（线程安全的）

```java
private static AtomicInteger onlineCount = new AtomicInteger(0);
```

2 webSocketMap：【静态变量】记录用户ID与对应的客户端Socket对象（线程安全的）

```java
private static ConcurrentHashMap<String,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
```

3 其他属性：session、userId建立连接时传入

```java
private Session session;
```

```java
private String userId;
```

方法：

```java
/**
* 连接建立成功调用的方法
* */
@OnOpen
public void onOpen(Session session,@PathParam("userId") String userId) {
    this.session = session;
    this.userId=userId;
    if(webSocketMap.containsKey(userId)){
        webSocketMap.remove(userId);
        webSocketMap.put(userId,this);
        //加入map中
    }else{
        webSocketMap.put(userId,this);
        //加入set中
        addOnlineCount();
        //在线数加1
    }
    log.info("用户连接:"+userId+",当前在线人数为:" + getOnlineCount());

    try {
        sendMessage("连接成功");
    } catch (IOException e) {
        log.error("用户:"+userId+",网络异常!!!!!!");
    }
}

/**
* 连接关闭调用的方法
*/
@OnClose
public void onClose() {
    if(webSocketMap.containsKey(userId)){
        webSocketMap.remove(userId);
        //从set中删除
        subOnlineCount();
    }
    log.info("用户退出:"+userId+",当前在线人数为:" + getOnlineCount());
}

/**
* 收到客户端消息后调用的方法
*
* @param message 客户端发送过来的消息*/
@OnMessage
public void onMessage(String message, Session session) {
    log.info("用户消息:"+userId+",报文:"+message);
    //可以群发消息
    //消息保存到数据库、redis
    if(StringUtils.isNotBlank(message)){
        try {
            //解析发送的报文
            JSONObject jsonObject = JSON.parseObject(message);
            //追加发送人(防止串改)
            jsonObject.put("fromUserId",this.userId);
            String toUserId=jsonObject.getString("toUserId");
            //传送给对应toUserId用户的websocket
            if(StringUtils.isNotBlank(toUserId)&&webSocketMap.containsKey(toUserId)){
                webSocketMap.get(toUserId).sendMessage(jsonObject.toJSONString());
            }else{
                log.error("请求的userId:"+toUserId+"不在该服务器上");
                //否则不在这个服务器上，发送到mysql或者redis
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

/**
*
* @param session
* @param error
*/
@OnError
public void onError(Session session, Throwable error) {
    log.error("用户错误:"+this.userId+",原因:"+error.getMessage());
    error.printStackTrace();
}
/**
* 实现服务器主动推送
*/
public void sendMessage(String message) throws IOException {
    this.session.getBasicRemote().sendText(message);
}

/**
* 群发自定义消息
*/
public void sendMessage(String message, String userId){
    log.info("推送消息到客户端 " + userId + "，推送内容:" + message);
    try {
        WebSocketServer webSocketServer = webSocketMap.get(userId);
        if (webSocketServer != null) {
            webSocketServer.sendMessage(message);
        } else {
            log.error("请求的userId:" + userId + "不在该服务器上");
        }
    } catch (IOException e) {
        throw new BusinessException(ErrorCode.SYSTEM_ERROR,"推送消息失败");
    }
}

/**
* 发送自定义消息
* */
public static void sendInfo(String message,@PathParam("userId") String userId) throws IOException {
    log.info("发送消息到:"+userId+"，报文:"+message);
    if(StringUtils.isNotBlank(userId)&&webSocketMap.containsKey(userId)){
        webSocketMap.get(userId).sendMessage(message);
    }else{
        log.error("用户"+userId+",不在线！");
    }
}

public static synchronized int getOnlineCount() {
    return onlineCount.get();
}

public static synchronized void addOnlineCount() {
    WebSocketServer.onlineCount.getAndAdd(1);
}

public static synchronized void subOnlineCount() {
    WebSocketServer.onlineCount.getAndAdd(-1);
}
```

前端

Typescript + react

```java
const WebSocketComponent = (userId: any) => {

  useEffect(() => {
    const id = userId.userId;
    console.log(userId)
    // 创建WebSocket连接
    // const socket = new WebSocket(`ws://127.0.0.1:6848/api/websocket/\${userId}`);
    const url = 'ws://127.0.0.1:6848/websocket'+id
    console.log(url)
    const socket = new WebSocket(url);
    if(!socket){
        alert("您的浏览器不支持WebSocket协议！");
    }
    // 处理连接成功事件
    socket.onopen = () => {
      console.log('WebSocket连接已打开');
      socket.send('Hello, WebSocket!'); // 发送一条消息
      console.log('已发送消息');
    };

    // 处理接收到消息事件
    socket.onmessage = (event) => {
      const messageContent = event.data;
      console.log('收到消息：', messageContent);
      // 使用Ant Design的message组件显示消息
      message.success(messageContent,5);// 您的[]业务已处理完毕，请前往[]查看
    };

    // 处理连接关闭事件
    socket.onclose = () => {
      socket.send('Hello, WebSocket!'); // 发送一条消息
      console.log('已发送消息');
      console.log('WebSocket连接已关闭');
    };

    // 处理错误事件
    socket.onerror = (error) => {
      console.error('WebSocket发生错误：', error);
    };

    // 在组件卸载时关闭WebSocket连接
    return () => {
      socket.close();
    };
  }, [userId]);

  return;
}

export default WebSocketComponent;
```



- #### V2 Netty-WebSocket

> Netty 官网 [Netty: Home](https://netty.io/)

**1 什么是Netty？**

Netty 是由 JBOSS 提供的一个 Java 开源框架。Netty 提供异步的、基于事件驱动的网络应用程序框架，用以快速开发高性能、高可靠性的网络 IO 程序，是目前最流行的 NIO 框架。

**2 为什么要用Netty？**

首先，Netty天然地支持websocket。

1. **高性能**：Netty是一个专为高并发、高性能设计的网络应用程序框架。它利用非阻塞I/O和事件驱动模型，能够高效地处理大量并发连接，这对于需要维持长连接的WebSocket应用尤为重要。这通常意味着在处理大量实时通信时，Netty可以提供更低的延迟和更高的吞吐量。
2. **异步编程模型**：Netty的异步特性允许在不阻塞线程的情况下处理网络事件，这意味着服务器可以同时处理更多的请求，而无需为每个连接分配独立的线程，从而减少了资源消耗并提高了效率。
3. **稳定性与成熟度**：Netty作为一个成熟的开源项目，经过了广泛的实战检验，拥有活跃的社区支持和持续的更新维护。它解决了许多底层网络编程中的常见问题，如内存管理、线程模型优化等，使得开发者可以更专注于业务逻辑而非基础架构问题。
4. **简化开发复杂度**：相比原生WebSocket实现，Netty提供了更高级别的抽象，使得开发者能够以更简洁的代码实现复杂的网络通信功能。API的设计倾向于简洁易用，降低了开发门槛。

**3 落地实现**

后端：（Java）

（1）引入依赖

```xml
<!-- https://mvnrepository.com/artifact/io.netty/netty-all -->
<dependency>
    <groupId>io.netty</groupId>
    <artifactId>netty-all</artifactId>
    <version>4.1.100.Final</version>
</dependency>
```

（2）创建Netty服务端类NettyServer

让Spring来管理

```java
@Configuration
@Slf4j
@Data
public class NettyServer {
    ...
}
```

属性：

```java
@Value("${netty-server.port}")
private int port;// netty服务器的端口可以自己指定
private ChannelFuture channelFuture;// 用于异步管理Netty服务器的channel
//负责处理接受进来的链接
private EventLoopGroup bossGroup;
//负责处理已经被接收的连接上的I/O操作
private EventLoopGroup workerGroup;
@Resource
private ThreadPoolExecutor threadPoolExecutor;// 线程池，后面说明
```

方法：

```java
@Async("threadPoolExecutor")// 实现线程池，让这个方法异步执行
public void start() throws Exception {
    bossGroup = new NioEventLoopGroup();
    workerGroup = new NioEventLoopGroup();
    try {
        ServerBootstrap sb = new ServerBootstrap();
        sb.option(ChannelOption.SO_BACKLOG, 1024);
        sb.group(bossGroup, workerGroup) // 绑定线程池
            .channel(NioServerSocketChannel.class) // 指定使用的channel
            .localAddress(this.port)// 绑定监听端口
            .childHandler(new ChannelInitializer<SocketChannel>() { // 绑定客户端连接时候触发操作

                @Override
                protected void initChannel(SocketChannel ch) {
                    log.info("收到新连接");
                    //websocket协议本身是基于http协议的，所以这边也要使用http解编码器
                    ch.pipeline().addLast(new HttpServerCodec());
                    //以块的方式来写的处理器
                    ch.pipeline().addLast(new ChunkedWriteHandler());
                    ch.pipeline().addLast(new HttpObjectAggregator(8192));
                    ch.pipeline().addLast(new MyWebSocketHandler());
                    ch.pipeline().addLast(new WebSocketServerProtocolHandler("/websocket", null, true, 65536 * 10));
					// 客户端连接到服务端的地址：ip:port/websocket
                }
            });
        channelFuture = sb.bind().sync(); // 服务器异步创建绑定
        log.info(NettyServer.class + " 启动正在监听： " + channelFuture.channel().localAddress());
        channelFuture.channel().closeFuture().sync(); // 关闭服务器通道
    } finally {
        workerGroup.shutdownGracefully().sync(); // 释放线程池资源
        bossGroup.shutdownGracefully().sync();
    }
}
@PreDestroy // Server实例被销毁时执行的方法，主要是关闭资源
public void stopServer(){
    if (channelFuture != null && !channelFuture.isDone()) {
        channelFuture.cancel(true);
    }
    workerGroup.shutdownGracefully();
    bossGroup.shutdownGracefully();
}
```

（3）定义channelGroup

用来管理目前所有已连接的channel

```java
public class ChannelHandlerPool {
    public ChannelHandlerPool(){}
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
}
```

（4）定义MyWebSocketHandler类

这个类用于处理连接开启、关闭、发送消息等操作，类似于controller。

```java
@Slf4j
public class MyWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	// 用于绑定channel和用户的ID
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
//            sendAllMessage(frame.text());
            ctx.writeAndFlush(new TextWebSocketFrame("Hello,与服务器的连接已建立！"));
        }
        super.channelRead(ctx, msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {

    }

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
```

前端：

```java
const WebSocketComponent = (userId: any) => {

  useEffect(() => {
    const id = userId.userId;
    console.log(userId)
    // 创建WebSocket连接
    const url = 'ws://127.0.0.1:6848/websocket?id='+id
    console.log(url)
    const socket = new WebSocket(url);
    if(!socket){
        alert("您的浏览器不支持WebSocket协议！");
    }
    // 处理连接成功事件
    socket.onopen = () => {
      console.log('WebSocket连接已打开');
      socket.send('Hello, WebSocket!'); // 发送一条消息
      console.log('已发送消息');
    };

    // 处理接收到消息事件
    socket.onmessage = (event) => {
      const messageContent = event.data;
      console.log('收到消息：', messageContent);
      // 使用Ant Design的message组件显示消息
      message.success(messageContent,5)
      // 您的[]业务已处理完毕，请前往[]查看
    };

    // 处理连接关闭事件
    socket.onclose = () => {
      socket.send('Hello, WebSocket!'); // 发送一条消息
      console.log('已发送消息');
      console.log('WebSocket连接已关闭');
    };

    // 处理错误事件
    socket.onerror = (error) => {
      console.error('WebSocket发生错误：', error);
    };

    // 在组件卸载时关闭WebSocket连接
    return () => {
      socket.close();
    };
  }, [userId]);

  return;
}

export default WebSocketComponent;
```

**4 一些细节**

（1）客户端连接会记录客户端ip、连接的channel信息，但这些与业务无关，我们真正需要的是知道用户是谁，即用户ID。

所以前端在发起请求时，需要携带请求参数id，后端接收时MyWebSocketHandler会把用户ID和channel用Map绑定，后面服务器推送消息可以用ID直接找到对应的channel。

（2）NettyServer启动监听客户端连接会阻塞Springboot项目的其他业务，所以应该用线程池让Netty异步启动。

在springboot的主启动类

```java
@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@MapperScan("com.polaris.project.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableAsync
public class MainApplication implements ApplicationRunner {
    @Resource
    private NettyServer nettyServer;
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Override
    public void run (ApplicationArguments args) throws Exception{
        Log.info("Netty Server started on port: {}", String.valueOf(nettyServer.getPort()));
        nettyServer.start();
    }
}
```

前面已经在start()加了@Async。

（3）关于Netty心跳保活

在server添加空闲状态处理器

```java
// 这里设置5秒内没有从 Channel 读写数据时会触发一个 READER_IDLE 事件。
ch.pipeline().addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
```

可以参考[Netty 心跳机制详解_netty心跳机制-CSDN博客](https://blog.csdn.net/qq_33807380/article/details/134044727)

参考：

[SpringBoot2+Netty+WebSocket(netty实现websocket，支持URL参数)_netty websocket 加url参数-CSDN博客](https://zhengkai.blog.csdn.net/article/details/91552993)

[使用springBoot初始化启动netty创建的两个服务，解决只能启动一个，另一个不能执行问题_springboot 多模块项目 一个能启动,一个不能启动-CSDN博客](https://blog.csdn.net/ddd295569371/article/details/127015451)

[SpringBoot整合Netty(服务端)_springboot netty-CSDN博客](https://blog.csdn.net/m0_70554089/article/details/138992105)

![](https://github.com/polarisronx/Papi-backend/blob/master/doc/images/QQ20240707210838.png)

![](https://github.com/polarisronx/Papi-backend/blob/master/doc/images/QQ20240707210947.png)

- #### V3 分布式WebSocket TODO

在分布式环境中实现WebSocket的挑战主要包括以下几点：

1. 会话共享：在分布式系统中，用户的WebSocket连接可能与不同的服务器建立，这就要求系统能够在不同服务器间共享WebSocket会话信息，以便消息能够被正确地传递到所有相关的客户端。
2. 负载均衡：使用负载均衡可以提高系统的可用性和伸缩性。但是，当WebSocket请求在服务器之间负载均衡时，需要确保客户端可以与正确的服务器建立连接，并且能够接收到所有的消息。
3. 故障转移：在出现服务器故障时，系统需要能够将WebSocket会话无缝迁移到其他健康的服务器上，以保证服务的连续性。
4. 一致性：确保所有用户在任何时候看到的都是一致的消息状态，这对于实时通信非常重要。

为了解决这些挑战，可以采取以下几种策略：

1. 使用消息代理：通过引入一个中心化的消息代理（如RabbitMQ、Redis Pub/Sub等），可以让所有的服务器都连接到这个消息代理。当一个服务器需要发送消息时，它将消息发送到消息代理，然后由消息代理负责将消息分发到所有连接的客户端。这样可以确保消息的一致性和可靠性。
2. 共享会话存储：使用一个共享的会话存储（如数据库或内存数据网格）来保存WebSocket会话的状态。这样，即使客户端最初连接到的服务器发生故障，其他服务器也可以接管会话并继续处理消息。
3. 基于路由的负载均衡：使用智能负载均衡器（如Nginx、HAProxy等），它们可以根据特定的路由规则（如会话ID或用户ID）将WebSocket连接定向到特定的服务器。
4. 服务发现：在微服务架构中，可以使用服务发现机制来动态地找到负责特定会话的服务器，并将消息路由到那里。
5. WebSocket代理：使用专门的WebSocket代理服务器，它可以在多个后端服务器之间代理WebSocket连接，并确保消息的传递和会话的同步。
6. 应用层协议：设计应用层协议来处理分布式WebSocket的复杂性，例如通过引入心跳机制来检测连接的健康状况，并通过预定的协议来同步会话状态。

参考：[SpringBoot+Redis实现分布式WebSocket_分布式websocket实现方案-CSDN博客](https://blog.csdn.net/moshowgame/article/details/136826457)

### 拓展2：AOP+自定义注解+Redisson恶意请求拦截

恶意请求可能会导致系统资源的极度浪费，甚至造成系统崩溃或服务拒绝。

> #### **1 预防措施**

**从前端开始预防**

思维A：确实是一种办法，给前端 ➕ 验证码、短信验证，或者加上谷歌认证（用户说：我谢谢你哈，消防栓）。

思维B：再次思考下还是算了，这次不想动我的前端加上如何短信验证还消耗我的💴，本来就是一个练手项目，打住❌。

**人工干预**

思维A：哇！人工干预很累的欸，拜托。

思维B：那如果是定时人工检查进行干预处理，辅助其他检测手段呢，是不是感觉还行！

**使用网关给他预防**

思维A：网关！好像听起来不错。

思维B：不行！我项目都没有网关，单单为了黑子增加一个网关，否决❌。

**日志监控**

思维A：日志监控好像还不错欸，可以让系统日志的输出到时候统一监控，然后发短信告诉我们。

思维B：日志监控确实可以，发短信还是算了，拒绝一切花销哈❌。

**后端 AOP 自动检测**

思维A：我想到了！后端 AOP 拦截访问限流，通过自动检测将 IP + 用户ID 加入黑名单，让黑子无所遁形。我觉得可以我们来试试？

思维B：还等什么！来试试吧！

> #### 2 实现方案

**自定义注解**

1）获取拦截对象的标识 key，设为用户的账户 userAccount和IP。

2）限制频率 rageLimit，如果每秒超过 10 次就直接封禁。

3）加入黑名单 protectLimit，设为1，超过1次限频就加入黑名单。

4）获取后面回调的方法，会用反射来实现接口的调用，返回登录失败。

**切面类**

1. 从RequestContextHolder获取请求的远程IP，从接入点jointpoint获取userAccount作为封禁的key
2. 利用redisson获取限流对象rateLimiter，每秒颁发10个令牌
3. 已在黑名单用户直接快速失败
4. 访问频次正常的用户正常访问，超过限定频次的用户快速失败并加入黑名单。

```java
@Aspect
@Component
@Slf4j
public class RageLimitInterceptor {
    private final RedissonClient redissonClient;

    private RMapCache<String, Long> blacklist;

    // 用来存储用户ID与对应的RateLimiter对象
    private final Cache<String, RRateLimiter> userRateLimiters = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    public RageLimitInterceptor(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
        if (redissonClient != null) {
            log.info("Redisson object is not null, using Redisson...");
            // 使用 Redisson 对象执行相关操作
            // 个人限频黑名单24h
            blacklist = redissonClient.getMapCache("blacklist");
            blacklist.expire(24, TimeUnit.HOURS);// 设置过期时间
        } else {
            log.error("Redisson object is null!");
        }
    }

    /**
     * @Description 重用切入点：注解标注过的地方
     * @author polaris
     * @create 2024/6/10
     */
    @Pointcut("@annotation(com.polaris.project.annotation.BlackListInterceptor)")
    public void aopPoint() {
    }

    /**
     * @Description 拦截请求，进行限流处理
     * 对用户的userAccount作为key限流，超过10次每秒限流加入黑名单（24小时），黑名单内用户拒绝请求快速失败
     * @author polaris
     * @create 2024/6/10
     * @return {@link Object}
     */
    @Around("aopPoint() && @annotation(blacklistInterceptor)")
    public Object doRouter(ProceedingJoinPoint jp, BlackListInterceptor blacklistInterceptor) throws Throwable {
        String key = blacklistInterceptor.key();

        // 获取请求属性
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        //获取 IP
        String remoteHost = httpServletRequest.getRemoteHost();
        if (StringUtils.isBlank(key)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "拦截的 key 不能为空");
        }

        // 获取拦截字段
        String keyAttr;
        if (key.equals("default")) {
            keyAttr = StpUtil.getLoginId().toString();
        } else {
            keyAttr = getAttrValue(key, jp.getArgs()).getBytes(StandardCharsets.UTF_8).toString();
        }
        keyAttr=LIMIT_PREFIX + blacklistInterceptor.business() + ":" + keyAttr;
        log.info("aop attr {}", keyAttr);

        // 黑名单拦截
        if (blacklistInterceptor.protectLimit() != 0 && null != blacklist.getOrDefault(keyAttr, null) && (blacklist.getOrDefault(keyAttr, 0L) > blacklistInterceptor.protectLimit()
                ||blacklist.getOrDefault(remoteHost, 0L) > blacklistInterceptor.protectLimit())) {
            log.info("有小黑子被我抓住了！给他 24 小时封禁套餐吧：{}", keyAttr);
            return fallbackMethodResult(jp, blacklistInterceptor.fallbackMethod());
        }

        // 获取限流
        RRateLimiter rateLimiter;
        if (!userRateLimiters.asMap().containsKey(keyAttr)) {
            rateLimiter = redissonClient.getRateLimiter(keyAttr);
            // 设置RateLimiter的速率，每秒发放10个令牌
            rateLimiter.trySetRate(RateType.OVERALL, blacklistInterceptor.rageLimit(), 1, RateIntervalUnit.SECONDS);
            userRateLimiters.put(keyAttr, rateLimiter);
        } else {
            rateLimiter = userRateLimiters.getIfPresent(keyAttr);
        }

        // 限流拦截
        if (rateLimiter != null && !rateLimiter.tryAcquire()) {
            if (blacklistInterceptor.protectLimit() != 0) {
                //封标识
                long keys = blacklist.getOrDefault(keyAttr, 0L) + 1L;
                blacklist.put(keyAttr,keys);
                //封 IP
                long ips = blacklist.getOrDefault(remoteHost, 0L) + 1L;
                blacklist.put(remoteHost, ips);
            }
            log.info("你刷这么快干嘛黑子：{}", keyAttr);
            return fallbackMethodResult(jp, blacklistInterceptor.fallbackMethod());
        }

        // 返回结果放行
        return jp.proceed();
    }

    /**
     * @Description 利用反射将fallbackMethod方法作为限流后的返回值返回
     * @author polaris
     * @create 2024/6/10
     * @return {@link Object}
     */
    private Object fallbackMethodResult(JoinPoint jp, String fallbackMethod) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Signature sig = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) sig;
        Method method = jp.getTarget().getClass().getMethod(fallbackMethod, methodSignature.getParameterTypes());
        return method.invoke(jp.getThis(), jp.getArgs());
    }
}
```

**实测**

正常访问

![](https://github.com/polarisronx/Papi-backend/blob/master/doc/images/QQ20240610181110.png)

jmeter每秒20次请求

![](https://github.com/polarisronx/Papi-backend/blob/master/doc/images/QQ20240610181252.png)

部分成功

![](https://github.com/polarisronx/Papi-backend/blob/master/doc/images/QQ20240610183617.png)

部分失败

![](https://github.com/polarisronx/Papi-backend/blob/master/doc/images/QQ20240610183531.png)

![](https://github.com/polarisronx/Papi-backend/blob/master/doc/images/QQ20240610183656.png)





### 拓展3：用MongoDB存储文档数据

先了解一下mongoDB把

[绝非替代，全方位解读MySQL 与MongoDB的区别_mongodb和mysql的区别-CSDN博客](https://blog.csdn.net/javamyfriend/article/details/132246620)

springboot中如何使用MongoDB

[SpringBoot中MongoDB的使用_springboot mongodb-CSDN博客](https://blog.csdn.net/qq_30614345/article/details/131994743)

> 分库存储：

现在的一条数据包含用户上传的原始数据，还有AI生成的结论和图表数据，十分臃肿。考虑对表的数据进行垂直分库。

MongoDB是一个面向文档的NoSQL数据库，它以JSON样式的文档存储数据。这种灵活的数据模型使得可以轻松地存储和检索不同结构的数据。

MongoDB采用B树存储，支持索引，单条数据查询时速度比B+树快。

> document设计

1. 对于生成的图表，主要的操作更多是查询，也就是读>>写，这里使用MongoDB进行信息存储，标记为类ChartDoc。而图表的原始数据还是存放在MySQL，保留Chart类。

2. 对于删改操作：操作中有较多生成失败、图表重新生成更新的情况，用户在删改时，往往是要删除或修改生成的图表信息，而较少去删除和修改原始数据。为避免重复占用主键，也便于MySQL的数据和MongoDB的数据的对应关系，在ChartDoc类中增加version字段。用户可以更新生成一个新的版本，也可以删除一个旧的版本，但一般不直接删除整个分析任务。
3. 对于查询操作：在查找图表信息时也是往往去查看生成的图表而不是去查看原始数据，所以可以直接查询MongoDB返回目标、结论、图表等信息，而不用再查MySQL。

### 拓展4：策略模式：反向压力

反向压力实际上是 **流量控制** 的一种解决方案，可以使得调用方和处理方的能力相匹配，从而保护系统的各节点处于持续的正常工作状态。简言之，由服务端的状态反向调节客户端请求的执行。

> 要点1：服务实例的繁忙程度指标

衡量指标：

1. cpu使用率
2. 内存使用率
3. 磁盘IO使用率
4. 网络带宽使用率

将服务器的状态划分为

1. 非常高度负载 VeryHighLoad：CPU > 90%, 内存 > 80% or 磁盘IO>80% or 网络IO>90%
2. 高度负载 HighLoad：CPU > 60% or 内存 > 40% or 磁盘IO>40% or 网络IO>60%
3. 中度负载 MediumLoad：CPU > 30%, 内存 > 30% or 磁盘IO>30% or 网络IO>30%
4. 轻度负载 LightLoad：other

> 压力策略

针对服务器的不同负载情况，采用的压力策略：

1. 非常高度负载 VeryHighLoad：拒绝请求 Reject
2. 高度负载 HighLoad：加入消息队列中排队执行 MQ
3. 中度负载 MediumLoad：加入系统开辟的线程池中排队执行 Pool
4. 轻度负载 LightLoad：采用同步生成 Sync，请求交付服务器执行

service中注入了策略选择器StrategySelector实例，在selector中，map存放了不同策略的实例

```java
public GenChartStrategy selectStrategy(ServerLoadInfo info) {
    if (info.isVeryHighLoad()) {
        return strategyMap.get(GenChartStrategyEnum.GEN_REJECT.getValue());
    } else if (info.isHighLoad()) {
        return strategyMap.get(GenChartStrategyEnum.GEN_MQ.getValue());
    } else if (info.isMediumLoad()) {
        return strategyMap.get(GenChartStrategyEnum.GEN_THREAD_POOL.getValue());
    } else {
        return strategyMap.get(GenChartStrategyEnum.GEN_SYNC.getValue());
    }
}
```



### **后端启动项目端口冲突问题解决**

原因:Windows Hyper-V 虚拟化平台占用了端口
先使用: netsh interface ipv4 show excludedportrange protocol=tcp查看被占用的端口，然后选择一个没被占用的端口启动项目



### **EasyExcel读取数据**

得到的是Map类型的集合

```java
//读取数据
List<Map<Integer，String>> list = EasyExcel.read(file)
.excelType(ExcelTypeEnum.XLSX)
.sheet()
.headRowNumber(0)
.doReadSync();
```

应该转为LinkedHashMap，HashMap是乱序的，用这个更好





### **调用AI**

AI预设，提前告诉AI角色，功能，回复要求。openai是无状态的接口，每次都需要把系统的预设告诉AI。

在系统模型层面做预设会比直接把预设拼接在消息里效果更好

AI有上下文关联限制

#### **利用AI生成结论和图表**

AI不能直接生成图表，AI生成前端代码使用前端组件库（echarts）来显示图表

#### **AI提词技巧**

token有限，输入太多AI可能只接受少部分

1. **持续输入，持续优化**
2. **实例问答，给出预期的回答格式**
3. **数据压缩，提取关键词**（也可以让AI来提取关键词，让他用最少的字来表达数据）

第一行:日期: 1号，用户数: 10人       表头:日期，用户数

第二行:日期: 2号，用户数: 20人  -->1号,10

第三行:日期: 3号，用户数: 30人       2号,20

​                                                             3号,30

4. **控制输入格式**

如你给AI一个预设，会给他提供分析需求和数据，那么提问时就按照这个形式提供给AI

5. **控制输出格式**

用特殊的符号来分隔代码和结论

```bash
你是一个数据分析师和前端开发专家，接下来我会按照以下固定格式给你提供内容:
分析需求:
{数据分析的需求或者目标}
原始数据:
{csv格式的原始数据，用，作为分隔符}
请根据这两部分内容，按照以下指定格式生成内容（此外不要输出任何多余的开头、结尾、注释)
【【【【【
{前端Echarts V5的option配置对象js代码，合理地将数据进行可视化，不要生成任何多余的内容，比如注释}
【【【【【
{明确的数据分析结论、越详细越好，不要生成多余的注释}

生成的内容：
【【【【【
...前端代码...
【【【【【
...结论...
```

#### 调用AI接口

1.直接调用OpenAI或者其他AI大模型官网的接口

优点：不经封装，最灵活，最原始

要钱，要梯子

使用：

- 请求头中加authorization加Key
- 找到对应的接口，一般是chat接口
- 发起Http请求









### 前端

表单，name对应了后端的字段属性，placeholder是表单栏的提示内容

![](https://github.com/polarisronx/Papi-backend/blob/master/doc/images/QQ20240428144855.png)

```tsx
useEffect( () => {
...逻辑...
},[ ...变量数组...])
```

页面初次加载和变量数组中的变量的值发生改变时会执行{}内的逻辑。

获取当前登录用户信息

```tsx
const { initialState } = useModel('@@initialstate');
const { currentUser } = initialState ?? {};
```

### 优化

- **安全性**：校验文件类型、校验大小、敏感信息校验

  可以接入腾讯云的图片万象数据审核（COS对象存储的审核功能

- **数据存储**：

  **分片上传**

  分库分表：用户的原始数据存在表内的一个字段难以维护，会让查询变慢，并且用户如果要查看原始数据，获取指定的字段，可以为每个图表单独开辟一个表来记录数据 chart_{图表ID}

  动态SQL

  **分库分表**：

  **1.水平分表**：水平分表是将同一张表中的数据按一定的规则划分到不同的物理存储位置上，以达到分摊单张表的数据及访问压力的目的。数据越多 B+ 树就越高，访问的性能就差，所以进行水平拆分。有基于hash-based分表和range-based分表。

  水平分表的**优点**

  （1）单个表的数据量减少，提高了读写性能，减少了单表的压力；

  （2）可以通过增加节点，提高系统的扩展性和容错性。

  水平分表的**缺点**

  （1）事务并发处理复杂度增加，需要增加分布式事务的管理，性能和复杂度都有所牺牲；

  （2）跨节点查询困难，需要设计跨节点的查询模块。

  **2.垂直分表**：垂直分表一般是将**<font color='red'>不常用的字段</font>**单独放在一张表、将**<font color='red'>大字段</font>**分一张表、把经常需要同时查出来的信息放一张表。这样做可以冷数据和热数据分开提高查询效率。

  **3.垂直分库**：指的是根据业务模块的不同，将不同的字段或表分到不同的数据库中。垂直分库基于数据库内核支持，对应用透明，无需额外的开发代码，易于维护升级。

  垂直分库的**优点**

  （1）减少单个数据库的数据量，提高系统的查询效率。·增加了系统的可扩展性，比水平分表更容易实现。

  垂直分库的**缺点**

  （1）不同数据库之间的维护和同步成本较高。

  （2）现有系统的改造存在一定的难度。
  
  （3）系统的性能会受到数据库之间互相影响的影响。
  
  4.水平分库
  
  （1）提高读写性能，因为减少了单一数据库的读写压力。
  
  （2）能提高存储容量。可以通过增加或减少数据库进行弹性伸缩。
  
  （3）提高容错性。当一个数据库故障了，别的数据库还能正常运行，只影响小部分数据查询。

3. **限流**：计算资源是有限的，甚至需要消耗成本

①控制次数，②控制单位时间内的并发量

限流阈值多大合适？要调研参考正常用户一般的使用频率 



**限流的思想**

(1)**固定窗口限流**

单位时间内限制次数：如1小时限制10个用户使用

优点：最简单

缺点：可能出现流量突刺，如前59分钟没有操作，最后1分钟有10个操作，第61分钟又来了10个操作，20个操作集中在两分钟内

举例：KFC每个整点提供30个汉堡

(2)**滑动窗口限流**

滑动窗口算法的关键是将大的时间窗口切分为多个较小的窗口片段，每个子窗口都有自己的计数限制。随着时间的推移，最旧的子窗口会被新的子窗口替代，形成一种“滑动”的效果。

优点：能避免流量突刺

缺点：相对复杂，限流效果受滑动单位影响

举例：KFC每10分钟提供5个汉堡

(3)**漏桶限流**（推荐）

以固定的速率处理请求（如每0.1秒处理一个请求），当请求桶满了以后，拒绝请求。

桶装待处理的请求，桶满了就不再接收，漏水指的是处理请求，并且是固定速率

优点：能一定程度上避免流量突刺，同时固定速率处理保证了服务器的安全

缺点：固定速率的缺点，没有办法按需调控，只能按顺序一个一个来。

举例：KFC能门店内容纳10个人，每2分钟提供给门店内的食客1个汉堡

(4)**令牌桶限流**（推荐）

系统生成一批令牌，用户操作前去取令牌，拥有令牌的用户就有资格进行操作（或与其他用户同时操作），拿不到令牌的只能等。

优点：能并发处理请求，性能更高

缺点：时间单位的令牌派发是需要斟酌的

举例：KFC能为门店内1~10号桌供餐，在没空出新的桌子前无法供餐

**如何实现？**

- 对于只有一台服务器的单机限流（本地限流）

  用第三方java库，google的Grava rateLimiter

- 对于有多台服务器的集群环境（分布式限流）、微服务

  ①把用户的使用记录集中在指定的存储中来统计，比如Redis，这样无论用户的请求落到哪台服务器，都以集中的数据存储内的数据为准

  ②在网关进行集中的限流和统计（比如Alibaba的Sentinel和spring cloud的Gateway）

举例：redission：

```java
RRedisLimiter tateLimiter = redissionClient.getRateLimiter(key);// 不同的key代表不同的限流器，对不同对象限流互不干扰
rateLimiter.trySetRate(RateType.OVERALL,2,1,RateIntervalUnit.SECOND);//p1:限流模式：overall对多服务器统一计数
// 每当一个操作来了，需要请求多少个请求令牌(permits)
boolean canOp = rateLimiter.tryAcquire(3);// p1:permits
if(!canOp) throw new BusinessException(ErrorCode.Too_MANY_REQUEST);
```

设置限流的粒度：设置key，针对接口、方法、用户

### 异步化

> 标准异步化的业务流程

1. 当用户要进行耗时很长的操作时，点击提交后，不需要在界面傻等，而是应该把这个任务保存到数据库中记录下来

2. 用户要执行新任务时

​		a.任务提交成功

​			i. 如果我们的程序还有多余的空闲线程，可以立刻去做这个任务

​			ii. 如果我们的程序的线程都在繁忙，无法继续处理，那就放到等待队列里

​		b.任务提交失败:比如我们的程序所有线程都在忙，任务队列满了

​			i. 拒绝掉这个任务，再也不去执行

​			ii.通过保存到数据库中的记录来看到提交失败的任务，并且在程序闲的时候，可以把任务从数据库中捞到程序里，再去执行

3. 程序（线程）从任务队列中取出任务依次执行，没完成意见事情就要修改一下任务的状态。
4. 用户可以查询任务的执行状态，或者在任务执行成功或失败时能够接收到通知（邮件、系统消息提示、短信）
5. 如果要执行的任务非常复杂，包含很多环节，在每完成一个小的任务时，要在程序（数据库）中进行记录，用户可以查看到任务执行到的状态。（进度条)

> 线程池

- 为什么需要线程池

线程的管理比较复杂（什么时候新增线程、什么时候减少空闲线程）

任务的存取比较复杂（什么时候接受任务，什么时候拒绝，不让任务被重复执行）

- 线程池的作用

帮助你轻松管理线程、协调任务的执行过程。

扩充：可以向线程池表达你的需求，比如最多只允许四个人同时执行任务。线程池就能自动为你进行管理。在任务紧急时，它会帮你将任务放入队列。而在任务不紧急或者还有线程空闲时，它会直接将任务交给空闲的线程，而不是放入队列。

- 线程池的实现

Spring的ThreadPoolTaskExecutor配合@Async注解实现。Java JUC并发编程包 ThreadPoolExecutor。

怎么确定线程池参数？考虑系统最脆弱的环节 

corePoolSize 核心线程数：一般情况下系统能同时工作的线程数（正式员工，随时就绪）

maximumPoolSize 最大线程数：极限情况下最多的线程数（加上临时员工）

keepAliveTime 空闲线程存活时间：非核心线程在没有任务的情况下多长时间删除（开除临时员工），从而释放无用的线程资源。

unit 时间单位：

workQueue 工作队列：用于存放线程要执行的任务（要设置队列长度）

threadFactory 线程工厂：控制每个线程的生成、现成的属性（比如线程名）

RejectExecutionHandle 拒绝策略：任务队列满的时候采取的措施

**<font color='red'>资源隔离策略</font>**：对于重要的任务（如VIP用户的操作）一个队列，对于普通的任务一个队列（普通用户）

对于VIP用户的限流策略也可以特别设置

**线程池工作过程**：

当任务数<corePoolSize，任务会被工作线程直接执行（而不会放入队列）（惰性加载核心线程）

当工作线程都在工作，还有额外的任务，就会把任务放入任务队列（不会马上增加临时线程）

当任务队列满了，会新增线程来处理任务（新的任务会被直接执行）

当任务队列满了，线程数达到最大且都处于工作状态，则会调用RejectExecutionHandle 规定的拒绝策略

当任务处理完毕，线程数>corePoolSize，新增的临时线程处于空闲状态超过 keepAliveTime 时间后，临时线程的资源就会被释放

**线程池参数设置的一般性经验**：

一般情况下，任务分为IO密集型和计算密集型两种。

**<font color='red'>计算密集型</font>**：吃CPU，比如音视频处理、图像处理、数学计算等，一般是设置corePoolSize为**<font color='red'>CPU的核数n＋1</font>**(空余线程，+1是为了预防某个线程被阻塞时，cpu可以调用其他线程)，可以让每个线程都能**充分利用CPU的每个核，而且线程之间不用频繁切换**（减少打架、减少开销)



**<font color='red'>lО密集型</font>**：吃网络带宽/内存硬盘/数据库访问/文件传输的读写资源，corePoolSize 可以设置大一点，一般经验值是**<font color='red'>2n或n/(1-阻塞系数)</font>**左右，但是建议以IO的能力为主（考虑IO设备数量和IO最大并发数）。（CPU工作量较小，在等IO，可能处于阻塞状态，所以尽量适当增加线程、异步IO、增加并发或设置缓存）。

经验只能作为参考，具体还是要根据业务。



优化点
1. guava Retrying重试
2. 如果说任务根本没提交到队列中(或者队列满了），是不是可以用定时任务把失败状态的图表放到队列中(补
    偿)
3. 给任务增加超时时间，超时标记为失败
4. AI随机生成的失败情况，在后端进行处理
5. 反向压力，根据下游服务的繁忙程度来选择当前系统的策略，最大化利用系统资源
6. 图表的刷新、自动刷新，获取图表的最新状态
7. 执行任务成功或失败，给用户发送消息（webSocket、Server side event）
8. 服务拆分（应用解耦）：把长耗时、消耗资源的任务单独抽取成程序，并且不影响主业务

> 开发

1.给chart表新增任务状态字段(比如排队中、执行中、已完成、失败)，任务执行信息字段(用于记录任务执行中、或者失败的一些信息)。

2.用户点击智能分析页的提交按钮时，先把图表立刻保存到数据库中，然后提交任务。

3.任务:先修改图表任务状态为“执行中”。等执行成功后，修改为“已完成”、保存执行结果;执行失败后，状态修改为“失败”，记录任务失败信息。

4.用户可以在图表管理页面查看所有图表(已生成的、生成中的、生成失败）的信息和状态。



### RabbitMQ消息队列集成

> 现在的系统的问题

（1）限流是局限于单机环境的，只针对单机的JVM环境，不适用于集群环境

集中的地方来管理任务下发

（2）任务存储在内存中，可能会丢失

可持久化存储的硬盘

（3）如果系统越来越复杂，任务种类越来越多，会需要很多的线程池等造成资源抢占

拆分系统+中间人，帮助连接多个系统

中间件：连接多个系统紧密协作

> 消息队列

优点：跨平台 +生产者和消费者解耦互不影响+异步处理+削峰填谷+可持久化+可扩展

发布订阅

