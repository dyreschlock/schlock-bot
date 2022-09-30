package com.schlock.pocket;

import com.schlock.pocket.entites.PocketGame;
import com.schlock.pocket.services.database.PocketGameDAO;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CollectPocketThumbnails extends AbstractStandalongDatabaseApp
{
    private static final String URL = "https://raw.githubusercontent.com/libretro-thumbnails/";
    private static final String URL_BOXARTS = "/master/Named_Boxarts/";

    private static final String FILE_OUTPUT_LOCATION = "/Volumes/Pocket/_boxart/";


    void process()
    {
        List<PocketGame> games = pocketGameDAO().getByMissingImage();
        for(PocketGame game : games)
        {
            try
            {
                if (!game.isImageCopied())
                {
                    processGame(game);
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void processGame(PocketGame game) throws Exception
    {
        String coreRepo = game.getCore().getRepoName();
        String filename = URLEncoder.encode(game.getImageFilename(), StandardCharsets.UTF_8.toString()).replace("+", "%20");

        String URL_LOCATION = URL + coreRepo + URL_BOXARTS + filename;

        String OUTPUT_LOCATION = FILE_OUTPUT_LOCATION  + game.getCore().getCoreCode() + "/" + game.getImageFilename();

        final URL url = new URL(URL_LOCATION);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(OUTPUT_LOCATION);

        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

        fos.close();
        rbc.close();


        File imageFile = new File(OUTPUT_LOCATION);
        if (imageFile.exists())
        {
            game.setImageCopied(true);
            getSession().save(game);
        }
    }


    private PocketGameDAO pocketGameDAO()
    {
        return new PocketGameDAO(session);
    }

    public static void main(String[] args)
    {
        new CollectPocketThumbnails().run();
    }
}
