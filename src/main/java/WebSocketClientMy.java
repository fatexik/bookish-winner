
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@WebSocket
public class WebSocketClientMy {
    private Session session;
    private javax.websocket.Session sessionClient;
    CountDownLatch latch= new CountDownLatch(1);

    WebSocketClientMy(javax.websocket.Session sessionClient){
        this.sessionClient = sessionClient;
    }

    @OnWebSocketMessage
    public void onText(Session session, String message) throws IOException {
        System.out.println(message);
        sessionClient.getBasicRemote().sendText(message);
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        this.session = session;
        System.out.println(session.isOpen());
        System.out.println("session open !");
        latch.countDown();
    }

    public void sendMessage(String s) {
        try {
            session.getRemote().sendString(s);
        } catch (IOException e){
            System.out.println(e);
        }
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}