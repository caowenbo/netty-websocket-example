package com.wenboy.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;

/*
    服务入口程序
    Netty WebSocket server
 */
public class App {
    /*监听端口
    Listen port*/
    private final static Integer PORT=9999;

    public static void main(String[] args){
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
    }
}
