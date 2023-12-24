package org.acme;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import jakarta.inject.Inject;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket")
public class SocketEndpoint {

    @Inject
    private UserSessionMap userSessionMap;

    @Inject
    private DataTransferMapper dataTransferMapper;

    @Channel("input")
    private Emitter<String> emitter; 

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("on open");
        userSessionMap.addUserSession(session.getId(), session);
        System.out.println(userSessionMap.getSize());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
      System.out.println("Received message: " + message);
      String serializedString = dataTransferMapper.getJsonLikeString(session.getId(), message);
      emitter.send(serializedString);
    }

    @OnClose
    public void onClose(Session session) {
        userSessionMap.removeUserSession(session.getId());
        System.out.println("on close");
        System.out.println(userSessionMap.getSize());
    }

}
