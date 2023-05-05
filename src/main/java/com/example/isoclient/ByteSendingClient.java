package com.example.isoclient;
import com.example.DataSingleton;
import com.example.ISO8583.entities.ISOMessage;
import com.example.ISO8583.exceptions.ISOException;
import com.example.ISO8583.utils.StringUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;

public class ByteSendingClient {
    private final String host;
    private final int port;
    private int length = 2;
    private static HelloController owner;

    public ByteSendingClient(String host, int port, HelloController owner) {
        this.host = host;
        this.port = port;
        this.owner = owner;
    }

    public void connect(ISOMessage message, DataSingleton data) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast("handler", new ByteSendingClientHandler());
                        }
                    });

            ChannelFuture f = bootstrap.connect().sync();

            ByteBuf messageBuf = initBuffer(message);
            ByteBuf clonedBuf;

            putMistake(messageBuf, data.getMistake_field(), data.getMistake_bitmap());
            changeBits(messageBuf, data.isIncorrect_mti_checkbox(), data.isOut_of_range_mti_checkbox(), data.isIncorrect_processing_code_checkbox());

            //Время задержки
            try {
                Thread.sleep((int)data.getTime_to_delay()*1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // Отправляем сообщение
            for (int i = 0; i<data.getNum_messages(); i++) {
                clonedBuf = messageBuf.copy(); // клонируем буфер
                f.channel().writeAndFlush(clonedBuf).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) {
                        if (future.isSuccess()) {
                            System.out.println("Message sent successfully");
                        } else {
                            System.err.println("Failed to send message: " + future.cause());
                        }
                    }
                });
            }
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    private void putMistake(ByteBuf buffer, int mistake_field, int mistake_bitmap)
    {
        for (int i = 0; i < mistake_field; i++)
        {
            int byte_number = new Random().nextInt(buffer.capacity() - 16) + 16;
            byte b = buffer.getByte(byte_number);
            b ^= 1 << new Random().nextInt(8);
            buffer.setByte(byte_number, b);
        }

        for (int i = 0; i < mistake_bitmap; i++)
        {
            int byte_number = new Random().nextInt(8) + 9;;
            byte b = buffer.getByte(byte_number);
            b ^= 1 << new Random().nextInt(8);
            buffer.setByte(byte_number, b);
        }
    }

    private void changeBits(ByteBuf buffer, boolean incorrect_mti_checkbox, boolean out_of_range_mti_checkbox, boolean incorrect_processing_code_checkbox)
    {
        byte[] bytes = {buffer.getByte(7), buffer.getByte(8)};
        String mti = StringUtil.fromByteArray(bytes);
        int byte_number = new Random().nextInt(4);
        char[] chars = mti.toCharArray();
        //mti 5-6 байты, получаются из hexStringToByteArray
        if (incorrect_mti_checkbox){
            int b = 0;
            do {
                switch (byte_number) {
                    case 0:
                        b = new Random().nextInt(3);
                        break;
                    case 1:
                        b = new Random().nextInt(8) + 1;
                        break;
                    case 2:
                        b = new Random().nextInt(7); // генерируем случайное число от 0 до 6
                        if (b >= 5) { // добавляем 5 для получения числа от 5 до 9
                            b += 3;
                        }
                        break;
                    case 3:
                        b = new Random().nextInt(6);
                        break;
                }
            } while (b == (int)(chars[byte_number] - '0'));

            chars[byte_number] = (char)(b + '0');
            buffer.setBytes(7, StringUtil.hexStringToByteArray(new String(chars)));
        }

        if (out_of_range_mti_checkbox) {
            int b = 0;
            switch (byte_number) {
                case 0:
                    b = (new Random().nextInt(7) + 3);
                    break;
                case 1:
                    b = (new Random().nextInt(2) + 9)%10;
                    break;
                case 2:
                    b = (new Random().nextInt(3) + 5);
                    break;
                case 3:
                    b = (new Random().nextInt(4) + 6);
                    break;
            }
            chars[byte_number] = (char)(b + '0');
            buffer.setBytes(7, StringUtil.hexStringToByteArray(new String(chars)));
        }

        if (incorrect_processing_code_checkbox){
            int process_byte_number = new Random().nextInt(3) + 17;
            byte b = buffer.getByte(process_byte_number);
            int randomDigit = new Random().nextInt(256); // генерируем случайную цифру от 0 до 255
            StringUtil.hexStringToByteArray(StringUtil.intToHexString(randomDigit));
            buffer.setByte(process_byte_number, StringUtil.hexStringToByteArray(StringUtil.intToHexString(randomDigit))[0]);
        }

    }

    private static class ByteSendingClientHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws ISOException, IOException {
            // Обработка ответа сервера
            ByteBuf responseBuf = (ByteBuf) msg;
            byte[] responseBytes = new byte[responseBuf.readableBytes()];
            responseBuf.readBytes(responseBytes);
            owner.ResponseMessage(responseBytes);
            System.out.println("Received response:");
            ctx.close();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
    }

    private ByteBuf initBuffer(ISOMessage isoMessage) {
        int len = isoMessage.getBody().length + isoMessage.getHeader().length;

        ByteBuf bf = Unpooled.buffer(len + length);

        if(length > 0)
        {
            byte[] mlen = ByteBuffer.allocate(4).putInt(len).array();
            bf.writeBytes(Arrays.copyOfRange(mlen, 2,4));
        }

        bf.writeBytes(isoMessage.getHeader());
        bf.writeBytes(isoMessage.getBody());

        return bf;
    }
}
