package com.wenboy.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ConnectSessionHandler extends ChannelInboundHandlerAdapter {
    //服务器的连接被建立后调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("active:"+ctx.channel().id().asLongText());
        super.channelActive(ctx);
    }
    //断开连接后调用
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("inactive:"+ctx.channel().id().asLongText());
        super.channelInactive(ctx);
    }
    //捕获一个异常时调用
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
