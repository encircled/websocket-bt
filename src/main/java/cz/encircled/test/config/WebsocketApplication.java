package cz.encircled.test.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.event.inbound.ApplicationEventListeningMessageProducer;
import org.springframework.integration.stomp.Reactor2TcpStompSessionManager;
import org.springframework.integration.stomp.StompSessionManager;
import org.springframework.integration.stomp.event.StompIntegrationEvent;
import org.springframework.integration.stomp.inbound.StompInboundChannelAdapter;
import org.springframework.integration.stomp.outbound.StompMessageHandler;
import org.springframework.integration.support.converter.PassThruMessageConverter;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.simp.stomp.Reactor2TcpStompClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;

@SpringBootApplication
@EnableSpringHttpSession
@EnableScheduling
@EnableIntegration
public class WebsocketApplication {

    @Value("${activemq.url}")
    private String activemqUrl;

    @Value("${activemq.port:61613}")
    private Integer activemqPort;

    public static void main(String[] args) {
        SpringApplication.run(WebsocketApplication.class, args);
    }

    @Bean
    public SessionRepository sessionRepository() {
        return new MapSessionRepository();
    }

    @Bean
    public Reactor2TcpStompClient stompClient() {
        Reactor2TcpStompClient stompClient = new Reactor2TcpStompClient(activemqUrl, activemqPort);
        stompClient.setMessageConverter(new PassThruMessageConverter());
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.afterPropertiesSet();
        stompClient.setTaskScheduler(taskScheduler);
        stompClient.setReceiptTimeLimit(5000);
        return stompClient;
    }

    @Bean
    public StompSessionManager stompSessionManager() {
        Reactor2TcpStompSessionManager stompSessionManager = new Reactor2TcpStompSessionManager(stompClient());
        stompSessionManager.setAutoReceipt(true);
        return stompSessionManager;
    }

    @Bean
    public PollableChannel stompInputChannel() {
        return new QueueChannel();
    }

    @Bean
    public StompInboundChannelAdapter stompInboundChannelAdapter() {
        StompInboundChannelAdapter adapter =
                new StompInboundChannelAdapter(stompSessionManager(), "/topic/myTopic");
        adapter.setOutputChannel(stompInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "stompOutputChannel")
    public MessageHandler stompMessageHandler() {
        StompMessageHandler handler = new StompMessageHandler(stompSessionManager());
        handler.setDestination("/topic/myTopic");
        return handler;
    }

    @Bean
    public PollableChannel stompEvents() {
        return new QueueChannel();
    }

    @Bean
    public ApplicationListener<ApplicationEvent> stompEventListener() {
        ApplicationEventListeningMessageProducer producer = new ApplicationEventListeningMessageProducer();
        producer.setEventTypes(StompIntegrationEvent.class);
        producer.setOutputChannel(stompEvents());
        return producer;
    }

}
