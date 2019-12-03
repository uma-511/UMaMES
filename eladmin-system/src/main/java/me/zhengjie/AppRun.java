package me.zhengjie;

import io.netty.channel.ChannelFuture;
import me.zhengjie.server.NettyTcpServer;
import me.zhengjie.utils.SpringContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Zheng Jie
 * @date 2018/11/15 9:20:19
 */
@EnableAsync
@SpringBootApplication
@EnableTransactionManagement
public class AppRun implements CommandLineRunner {
    @Autowired
    NettyTcpServer nettyTcpServer;

    public static void main(String[] args) {
        SpringApplication.run(AppRun.class, args);
    }

    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

    @Override
    public void run(String... args) throws Exception {
        //启动服务端
        ChannelFuture start = nettyTcpServer.start();

        //服务端管道关闭的监听器并同步阻塞,直到channel关闭,线程才会往下执行,结束进程
        start.channel().closeFuture().syncUninterruptibly();
    }
}
