package com.schlock.bot.services.commands.pokemon.quiz.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.entities.pokemon.GuessingStreak;
import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.commands.AbstractChatGameListenerService;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.commands.pokemon.quiz.WhosThatPokemonService;
import com.schlock.bot.services.database.adhoc.DatabaseManager;
import com.schlock.bot.services.database.pokemon.GuessingStreakDAO;
import com.schlock.bot.services.entities.base.UserManagement;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import com.schlock.bot.services.entities.pokemon.PokemonUtils;
import org.apache.tapestry5.ioc.Messages;

public class WhosThatPokemonServiceImpl extends AbstractChatGameListenerService implements WhosThatPokemonService
{
    private static final String START_COMMAND = "!whodat";

    protected static final String GAME_ALREADY_STARTED_KEY = "whodat-already-started";
    protected static final String WINNER_KEY = "whodat-winner";

    protected static final String DOUBLER_BONUS = "doubler-bonus";
    protected static final String STREAK_BONUS = "streak-bonus";

    private final PokemonManagement pokemonManagement;
    private final UserManagement userManagement;
    private final PokemonUtils pokemonUtils;

    private final DatabaseManager database;

    private final DeploymentConfiguration config;

    protected Pokemon currentPokemon;

    public WhosThatPokemonServiceImpl(PokemonManagement pokemonManagement,
                                      UserManagement userManagement,
                                      PokemonUtils pokemonUtils,
                                      DatabaseManager database,
                                      Messages messages,
                                      DeploymentConfiguration config)
    {
        super(messages);

        this.pokemonManagement = pokemonManagement;
        this.userManagement = userManagement;
        this.pokemonUtils = pokemonUtils;

        this.database = database;

        this.config = config;
    }

    public String getGameId()
    {
        return START_COMMAND.substring(1).trim();
    }

    public boolean isStarted()
    {
        return currentPokemon != null;
    }

    public boolean isAcceptRequest(String username, String message)
    {
        return isOn();
    }

    public boolean isTerminateAfterRequest()
    {
        return false;
    }

    protected ListenerResponse processRequest(String username, String in)
    {
        String command = in.toLowerCase();
        if (command.startsWith(START_COMMAND))
        {
            if (currentPokemon != null)
            {
                String hint = pokemonUtils.formatHint1(currentPokemon);

                return formatSingleResponse(GAME_ALREADY_STARTED_KEY, hint);
            }

            String params = command.substring(START_COMMAND.length()).trim();

            return startGame(params);
        }

        if (currentPokemon != null)
        {
            String text = command.toLowerCase().replaceAll(" ", "");
            String id = currentPokemon.getId().toLowerCase();

            if (text.contains(id))
            {
                return processWinner(username);
            }
        }
        return ListenerResponse.relayNothing();
    }

    private ListenerResponse startGame(String params)
    {
        if (pokemonManagement.isGenSearch(params))
        {
            currentPokemon = pokemonManagement.getRandomPokemonInGen(params);
        }
        else if (pokemonManagement.isRangeSearch(params))
        {
            currentPokemon = pokemonManagement.getRandomPokemonInRange(params);
        }
        else
        {
            currentPokemon = pokemonManagement.getRandomPokemon();
        }
        String response = pokemonUtils.formatHint1(currentPokemon);
        return ListenerResponse.relaySingle().addMessage(response);
    }

    private ListenerResponse processWinner(String username)
    {
        String mark = config.getCurrencyMark();
        String doublerMsg = "";
        String streakMsg = "";

        Integer points = config.getQuizCorrectPoints();
        User user = userManagement.getUser(username);

        if (user.hasDoubler())
        {
            points = user.modifyPointsWithDoubler(points);
            doublerMsg = " " + messages.format(DOUBLER_BONUS, user.getPointsDoubler().toString());
        }

        GuessingStreak streak = getCurrentStreak();
        if (user.getId().equals(streak.getUserId()))
        {
            streak.incrementStreak();

            points = incrementPointsWithStreak(points, streak.getStreakNumber());

            user.incrementStreakHighScore(streak.getStreakNumber());

            streakMsg = " " + messages.format(STREAK_BONUS, streak.getStreakNumber().toString());
        }
        else
        {
            streak.setNewUser(user);
        }

        user.incrementBalance(points);

        database.save(user, streak);

        String pokemonName = currentPokemon.getName();
        currentPokemon = null;

        String response = messages.format(WINNER_KEY, username, pokemonName, points.toString(), mark) + doublerMsg + streakMsg;
        return ListenerResponse.relaySingle().addMessage(response);
    }

    private GuessingStreak getCurrentStreak()
    {
        GuessingStreak streak = database.get(GuessingStreakDAO.class).get();
        if(streak == null)
        {
            streak = new GuessingStreak();
        }
        return streak;
    }

    protected Integer incrementPointsWithStreak(Integer points, Integer streak)
    {
        Integer decayMin = config.getQuizStreakDecayMinValue();
        Integer decayMax = config.getQuizStreakDecayMaxValue();

        Double pointsFactor = streak.doubleValue();

        if (streak > decayMin && streak <= decayMax)
        {
            Double difference = streak.doubleValue() - decayMin.doubleValue();

            pointsFactor = decayMin.doubleValue() + (difference / 2);
        }
        else if (streak > decayMax)
        {
            Double difference = decayMax.doubleValue() - decayMin.doubleValue();

            pointsFactor = decayMin.doubleValue() + (difference / 2);
        }

        Double newPoints = points * pointsFactor;
        return newPoints.intValue();
    }
}
