package cz.encircled.test.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.config.StompBrokerRelayRegistration;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @author Vlad on 19-Feb-17.
 */
@Configuration
@EnableWebSocketMessageBroker
@ComponentScan({"cz.encircled.test.service", "cz.encircled.test.controller"})
public class WebSocketBrokerConfig extends AbstractWebSocketMessageBrokerConfigurer implements WebSocketMessageBrokerConfigurer {

    @Value("${activemq.url}")
    private String activemqUrl;

    @Value("${activemq.port:61613}")
    private Integer activemqPort;

    @Value("${use.ws:true}")
    private Boolean useWebSockets;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/auction").withSockJS().setWebSocketEnabled(useWebSockets);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        StompBrokerRelayRegistration relay = registry.enableStompBrokerRelay("/topic", "/queue");
        relay.setRelayHost(activemqUrl);
        relay.setRelayPort(activemqPort);
    }

}
