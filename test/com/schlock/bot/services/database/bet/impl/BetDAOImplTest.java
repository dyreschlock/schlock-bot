package com.schlock.bot.services.database.bet.impl;

import com.schlock.bot.services.database.DatabaseTest;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class BetDAOImplTest extends DatabaseTest
{

    @BeforeEach
    public void setup() throws Exception
    {
        databaseSetup();
    }
}