package com.schlock.pocket.entites;

public enum PocketCore
{
    NES("Nintendo_-_Nintendo_Entertainment_System", "nes","nes"),
    GAME_GEAR("Sega_-_Game_Gear", "gg", "gg"),
    SEGA_GENESIS("Sega_-_Mega_Drive_-_Genesis", "genesis", "md"),
    SEGA_SG1000("Sega_-_SG-1000", "sg1000", "sg"),
    SEGA_MASTER_SYSTEM("Sega_-_Master_System_-_Mark_III", "sms", "sms")
    ;

    //,
//    ng("SNK_-_Neo_Geo"),
//    snes("Nintendo_-_Super_Nintendo_Entertainment_System"),
//    gba("Nintendo_-_Game_Boy_Advance"),
//    genesis("Sega_-_Mega_Drive_-_Genesis")

    String repoName;
    String coreCode;
    String fileExtension;

    PocketCore(String repoName, String coreCode, String fileExtension)
    {
        this.repoName = repoName;
        this.coreCode = coreCode;
        this.fileExtension = fileExtension;
    }

    public String getRepoName()
    {
        return repoName;
    }

    public String getCoreCode()
    {
        return coreCode;
    }

    public String getFileExtension()
    {
        return fileExtension;
    }
}
