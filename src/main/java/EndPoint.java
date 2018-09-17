
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.net.URI;
import java.util.HashMap;

@ServerEndpoint("/hello")
public class EndPoint {

    private static String dest = "ws://localhost:9998/example/";

    public EndPoint() {
        System.out.println("class loaded " + this.getClass());
    }

    private HashMap <String,WebSocketClientMy> webSocketClientMyMap = new HashMap<String, WebSocketClientMy>();
    private HashMap <String,WebSocketClient> webSocketClientMap = new HashMap<String, WebSocketClient>();
    public static HashMap<String,Session> sessionMap = new HashMap<String, Session>();

    @OnOpen
    public void onOpen(Session session) {
        System.out.printf("Session opened, id: %s%n", session.getId());
        sessionMap.put(session.getId(),session) ;
        WebSocketClient webSocketClient = new WebSocketClient();
        try {
            WebSocketClientMy webSocketClientMy = new WebSocketClientMy(session);
            webSocketClient.start();
            webSocketClientMyMap.put(session.getId(),webSocketClientMy);
            webSocketClientMap.put(session.getId(),webSocketClient);
            session.getBasicRemote().sendText("Hi there, we are successfully connected.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.printf("Message received. Session id: %s Message: %s%n",
                session.getId(), message);
        try {
            WebSocketClient webSocketClient = webSocketClientMap.get(session.getId());
            WebSocketClientMy webSocketClientMy = webSocketClientMyMap.get(session.getId());
            if (message.contains("/reg")) {
                String role = message.substring(5);
                System.out.println(role);
                String url = dest.concat(role);
                URI uri = new URI(url);
                ClientUpgradeRequest clientUpgradeRequest = new ClientUpgradeRequest();
                webSocketClient.connect(webSocketClientMy, uri, clientUpgradeRequest);
                webSocketClientMy.getLatch().await();
            } else {
                webSocketClientMy.sendMessage(message);
                session.getBasicRemote().sendText(String.format("We received your message: %s%n", message));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @OnError
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @OnClose
    public void onClose(Session session) {
        webSocketClientMap.remove(session.getId());
        webSocketClientMyMap.remove(session.getId());
        System.out.printf("Session closed with id: %s%n", session.getId());
    }
}