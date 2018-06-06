//连接事件管理
 EventLoopGroup bossGroup = new NioEventLoopGroup();
 //业务事件管理
 EventLoopGroup workerGroup = new NioEventLoopGroup();
 // 辅助启动器
 // 创建ServerBootStrap实例
 // ServerBootstrap 用于启动NIO服务端的辅助启动类，目的是降低服务端的开发复杂度
 ServerBootstrap bs = new ServerBootstrap();
 // 绑定Reactor线程池
 bs.group(bossGroup, workerGroup)
  // 设置并绑定服务端Channel,指定所使用的NIO传输的Channel
  .channel(NioServerSocketChannel.class)
  //这两个 Handler 不一样，
  // 前者（handler()）设置的 Handler 是服务端 NioServerSocketChannel的，
  // 后者（childHandler()）设置的 Handler
  // 是属于每一个新建的 NioSocketChannel 的
  .handler(new LoggingHandler())
  .childHandler(new InitializerHandler())
  .option(ChannelOption.SO_BACKLOG, 100)
  .childOption(ChannelOption.SO_KEEPALIVE, true);
 try {
     //绑定的服务器;sync 等待服务器关闭
     ChannelFuture channelFuture = bs.bind(PORT).sync();
     //关闭 channel 和 块，直到它被关闭
     channelFuture.channel().closeFuture().sync();

 } catch (InterruptedException e) {
     e.printStackTrace();
 } finally {
     //安全关闭
     workerGroup.shutdownGracefully();
     bossGroup.shutdownGracefully();
 }
 
ServerBootstrap
ServerBootstrap 为 Netty 服务端的启动辅助类，它提供了一系列的方法用于设置服务端启动相关的参数。

Channel
Channel 为 Netty 网络操作抽象类，它定义了一组功能，其提供的 API 大大降低了直接使用 Socket 类的复杂性。当然它也不仅仅只是包括了网络 IO 操作的基本功能，还包括一些与 Netty 框架相关的功能，包括获取该 Channel 的 EventLoop 等等。

EventLoopGroup
EventLoopGroup 为 Netty 的 Reactor 线程池，它实际上就是 EventLoop 的容器，而 EventLoop 为 Netty 的核心抽象类，它的主要职责是处理所有注册到本线程多路复用器 Selector 上的 Channel。

ChannelHandler
ChannelHandler 作为 Netty 的主要组件，它主要负责 I/O 事件或者 I/O 操作进行拦截和处理，它可以选择性地拦截和处理自己感觉兴趣的事件，也可以透传和终止事件的传递。

ChannelPipeline
ChannelPipeline 是 ChannelHandler 链的容器，它负责 ChannelHandler 的管理和事件拦截与调度。每当新建一个 Channel 都会分配一个新的 ChannelPepeline，同时这种关联是永久性的。