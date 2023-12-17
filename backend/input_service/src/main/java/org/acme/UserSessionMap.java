package org.acme;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.Session;

@ApplicationScoped
public class UserSessionMap {
    private final Map<String, Session> userSessionMap = new ConcurrentHashMap<>();

    public void addUserSession(String userId, Session session) {
        userSessionMap.put(userId, session);
    }

    public Session getUserSession(String userId) {
        return userSessionMap.get(userId);
    }

    public void removeUserSession(String userId) {
        userSessionMap.remove(userId);
    }

    public int getSize() {
      return userSessionMap.size();
    }
}
