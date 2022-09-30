package com.schlock.pocket;

import com.schlock.pocket.entites.PocketCore;
import com.schlock.pocket.entites.PocketGame;
import com.schlock.pocket.services.database.PocketGameDAO;

import java.io.File;
import java.io.FileFilter;

public class CreatePocketEntries extends AbstractStandalongDatabaseApp
{
    private static final String ASSET_LOCATION = "/Volumes/Pocket/Assets/";
    private static final String COMMON = "common/";

    private static final String UNSORTED = "__unsorted";
    private static final String ALL = "_all";

    void process()
    {
        try
        {
            for(PocketCore core : PocketCore.values())
            {
                processCore(core);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void processCore(PocketCore core)
    {
        String romLocation = ASSET_LOCATION + core.getCoreCode() + "/" + COMMON;

        FileFilter filter = new FileFilter()
        {
            @Override
            public boolean accept(File file)
            {
                boolean directory = file.isDirectory();
                boolean notIgnore = !UNSORTED.toLowerCase().equals(file.getName()) &&
                                        !ALL.toLowerCase().equals(file.getName());

                return directory && notIgnore;
            }
        };

        File coreDirectory = new File(romLocation);
        for(File file : coreDirectory.listFiles(filter))
        {
            processFolder(file, core);
        }
    }

    private void processFolder(File folder, PocketCore core)
    {
        FileFilter filter = new FileFilter()
        {
            public boolean accept(File file)
            {
                final String EXT = core.getFileExtension();

                boolean notDirectory = !file.isDirectory();
                boolean acceptibleFile = file.getName().endsWith(EXT);
                boolean notDotFile = !file.getName().startsWith(".");

                return notDirectory && acceptibleFile && notDotFile;
            }
        };

        for(File file : folder.listFiles(filter))
        {
            String filename = file.getName();

            PocketGame game = pocketGameDAO().getByFilename(filename);
            if (game == null)
            {
                game = PocketGame.createGame(file, core);

                getSession().save(game);
            }
        }
    }

    private PocketGameDAO pocketGameDAO()
    {
        return new PocketGameDAO(session);
    }

    public static void main(String[] args)
    {
        new CreatePocketEntries().run();
    }
}
