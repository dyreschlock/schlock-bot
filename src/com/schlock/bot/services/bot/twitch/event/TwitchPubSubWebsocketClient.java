package com.schlock.bot.services.bot.twitch.event;

import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.bot.twitch.TwitchChatBot;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.glassfish.tyrus.client.ClientManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import java.net.URI;

@ClientEndpoint
public class TwitchPubSubWebsocketClient
{
    private static final Logger log = LoggerFactory.getLogger(TwitchPubSubWebsocketClient.class);

    private static final String PUBSUB_URI = "wss://pubsub-edge.twitch.tv";

    private static final String TYPE_PARAM = "type";
    private static final String DATA_PARAM = "data";
    private static final String TOPICS_PARAM = "topics";
    private static final String AUTH_PARAM = "auth_token";

    private static final String PING = "PING";
    private static final String PONG = "PONG";
    private static final String RECONNECT = "RECONNECT";

    private static final String LISTEN = "LISTEN";
    private static final String POINTS_CHANNEL = "channel-points-channel-v1";
    private static final String SUBSCRIBER_CHANNEL = "channel-subscribe-events-v1";
//    private static final String FOLLOWER_CHANNEL = "";

    private final TwitchChatBot twitchBot;
    private final DeploymentConfiguration config;

    private final ClientManager client;
    private final URI endpointURI;

    private Session currentSession = null;

    public TwitchPubSubWebsocketClient(TwitchChatBot twitchBot,
                                       DeploymentConfiguration config)
    {
        this.twitchBot = twitchBot;
        this.config = config;

        this.client = ClientManager.createClient();
        try
        {
            this.endpointURI = new URI(PUBSUB_URI);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen(Session session)
    {
        log.info("opening websocket to " + endpointURI.toString());
        currentSession = session;
    }

    @OnClose
    public void onClose(Session session, CloseReason reason)
    {
        log.info("closing websocket, reason: " + reason.getReasonPhrase());
        currentSession = null;
    }

    @OnMessage
    public void onMessage(String message)
    {
        JSONObject response = new JSONObject(message);
        log.info("receiving... \r\n" + response.toString());

        if(response.containsKey(TYPE_PARAM))
        {
            String type = (String) response.get(TYPE_PARAM);
            if (type.equalsIgnoreCase(RECONNECT))
            {
                connectToServer();
            }
        }


    }


    public void startup() throws Exception
    {
//        System.out.println(listenJSON().toString());

        connectToServer();
        while(true)
        {
            Thread.sleep(4 * 60 * 1000);
            sendPing();
        }
    }


    private void connectToServer()
    {
        currentSession = null;

        try
        {
            client.connectToServer(this, endpointURI);

            Thread.sleep(1000);
            sendPing();

            Thread.sleep(1000);
            listenToChannels();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void sendPing()
    {
        log.info("sending ping...");

        sendMessage(pingJSON());
    }

    private void listenToChannels()
    {
        log.info("listening to channels... ");

        sendMessage(listenJSON());
    }

    private void sendMessage(JSONObject message)
    {
        currentSession.getAsyncRemote().sendText(message.toString());
    }




    private JSONObject pingJSON()
    {
        return new JSONObject().accumulate(TYPE_PARAM, PING);
    }

    public JSONObject listenJSON()
    {
        final String OAUTH = config.getTwitchOAuthWebsocketClientId();
        final String CHANNEL_NAME = config.getTwitchChannel().substring(1);

        JSONArray channels = new JSONArray();
        channels.put(0, POINTS_CHANNEL + "." + CHANNEL_NAME);
//        channels.put(1, SUBSCRIBER_CHANNEL + "." + CHANNEL_NAME);

        JSONObject listen = new JSONObject();
        listen.accumulate(TYPE_PARAM, LISTEN);

        JSONObject data = new JSONObject();
        data.accumulate(TOPICS_PARAM, channels);
        data.accumulate(AUTH_PARAM, OAUTH);

        listen.accumulate(DATA_PARAM, data);

        log.info("\r\n" + listen.toString());

        return listen;
    }
}
