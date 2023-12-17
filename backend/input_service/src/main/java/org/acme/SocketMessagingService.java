package org.acme;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.RemoteEndpoint;
import jakarta.websocket.Session;

@ApplicationScoped
public class SocketMessagingService {

    @Inject
    private UserSessionMap userSessionMap;

    public void sendMessageToUser(String userId, String message) {
      Session session = userSessionMap.getUserSession(userId);
      if (session != null) {
          RemoteEndpoint.Async asyncRemote = session.getAsyncRemote();
          asyncRemote.sendText(message);
      }
    }
  
}
