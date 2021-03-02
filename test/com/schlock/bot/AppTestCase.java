package com.schlock.bot;

import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.impl.DeploymentConfigurationImpl;
import org.apache.tapestry5.internal.services.MapMessages;
import org.apache.tapestry5.ioc.Messages;

import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class AppTestCase
{
    private DeploymentConfiguration config;
    private Messages messages;

    protected DeploymentConfiguration config()
    {
        if(config == null)
        {
            config = createDeploymentConfiguration();
            try
            {
                config.loadProperties();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return config;
    }

    protected DeploymentConfiguration createDeploymentConfiguration()
    {
        return DeploymentConfigurationImpl.createDeploymentConfiguration(DeploymentConfiguration.TEST);
    }

    protected Messages messages()
    {
        if(messages == null)
        {
            try
            {
                messages = createMessages();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return messages;
    }

    private Messages createMessages() throws IOException
    {
        Properties properties = new Properties();

        InputStream stream = new FileInputStream("web/WEB-INF/app.properties");
        if(stream != null)
        {
            try
            {
                properties.load(stream);
            }
            finally
            {
                stream.close();
            }
        }
        else
        {
            throw new FileNotFoundException("Can't find app.properties");
        }

        Map<String, String> map = new HashMap<>();

        for (Object o : properties.keySet())
        {
            String key = (String) o;
            map.put(key, properties.getProperty(key));
        }

        MapMessages messages = new MapMessages(Locale.ENGLISH, map);
        return messages;
    }
}
