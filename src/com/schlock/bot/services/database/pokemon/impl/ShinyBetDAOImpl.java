package com.schlock.bot.services.database.pokemon.impl;

import com.schlock.bot.entities.pokemon.ShinyBet;
import com.schlock.bot.services.database.pokemon.ShinyBetDAO;
import com.schlock.bot.services.database.AbstractBaseDAO;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

public class ShinyBetDAOImpl extends AbstractBaseDAO<ShinyBet> implements ShinyBetDAO
{
    public ShinyBetDAOImpl(Session session)
    {
        super(ShinyBet.class, session);
    }

    public List<ShinyBet> getByUsername(String username)
    {
        String text = " select b " +
                        " from ShinyBet b " +
                        " join b.user u " +
                        " where u.username = :name " +
                        " and b.shiny is null ";

        Query query = session.createQuery(text);
        query.setParameter("name", username);

        List<ShinyBet> bets = query.list();
        return bets;
    }

    public ShinyBet getByUsernameAndPokemon(String username, String pokemonId)
    {
        String text = " select b " +
                        " from ShinyBet b " +
                        " join b.user u " +
                        " where u.username = :name " +
                        " and b.pokemonId = :pokemon " +
                        " and b.shiny is null ";

        Query query = session.createQuery(text);
        query.setParameter("name", username);
        query.setParameter("pokemon", pokemonId);

        ShinyBet bet = singleResult(query);
        return bet;
    }

    public List<ShinyBet> getAllCurrent()
    {
        String text = " from ShinyBet b" +
                        " where b.shiny is null ";

        Query query = session.createQuery(text);

        List<ShinyBet> bets = query.list();
        return bets;
    }
}