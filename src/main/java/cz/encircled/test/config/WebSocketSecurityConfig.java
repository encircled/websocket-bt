package cz.encircled.test.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.messaging.context.SecurityContextChannelInterceptor;

import static org.springframework.messaging.simp.SimpMessageType.CONNECT;
import static org.springframework.messaging.simp.SimpMessageType.MESSAGE;
import static org.springframework.messaging.simp.SimpMessageType.SUBSCRIBE;

/**
 * @author Vlad on 19-Feb-17.
 */
@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    public SecurityContextChannelInterceptor securityContextChannelInterceptor() {
        return super.securityContextChannelInterceptor();
    }

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .nullDestMatcher().authenticated()
                .simpTypeMatchers(CONNECT).permitAll()
                .simpSubscribeDestMatchers("/user/queue/errors").permitAll()
                .simpSubscribeDestMatchers("/user/queue/dashboard").hasRole("GUEST")
                .simpDestMatchers("/app/dashboard").hasRole("GUEST")
                .simpDestMatchers("/app/admin*//**").hasRole("ADMIN")
                .simpDestMatchers("/app*//**").hasRole("USER")
                .simpDestMatchers("/user/queue*//**").hasRole("USER")
                .simpSubscribeDestMatchers("/admin*//**").hasRole("ADMIN")
                .simpSubscribeDestMatchers("/user*//**", "/topic*//**").hasRole("USER")
                .simpTypeMatchers(MESSAGE, SUBSCRIBE).denyAll()
                .anyMessage().denyAll();
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }

}

