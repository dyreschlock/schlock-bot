package testapp;

import org.apache.tapestry5.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

public class TestApp {

    public static void main(String[] args) {
        try {
            // open websocket
            final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(new URI("wss://pubsub-edge.twitch.tv"));

            // add listener
            clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
                public void handleMessage(String message) {
                    System.out.println(message);
                }
            });

            JSONObject message = new JSONObject();
            message.accumulate("type", "PING");

            System.out.println(message.toString());

            // send message to websocket
            clientEndPoint.sendMessage(message.toString());

            // wait 5 seconds for messages from websocket
            Thread.sleep(20000);

        } catch (InterruptedException ex) {
            System.err.println("InterruptedException exception: " + ex.getMessage());
        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        }
    }
}
