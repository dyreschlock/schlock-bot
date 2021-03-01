package com.schlock.bot;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.PathResource;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AppRunner
{
    private static final Integer PORT = 8088;

    public static void main(String[] args) throws Exception
    {
        System.setProperty("org.apache.tapestry.disable-caching", "false");
        System.setProperty("org.apache.tapestry.enable-reset-service", "true");


        Path webDirectory = new File("web").toPath();
        if (!Files.exists(webDirectory))
        {
            throw new FileNotFoundException("Can't find web dir: " + webDirectory);
        }

        WebAppContext context = new WebAppContext();
        context.setContextPath("/");
        context.setBaseResource(new PathResource(webDirectory.toAbsolutePath()));

        Server server = new Server(PORT);
        server.setHandler(context);
        server.start();
        server.join();
    }
}
