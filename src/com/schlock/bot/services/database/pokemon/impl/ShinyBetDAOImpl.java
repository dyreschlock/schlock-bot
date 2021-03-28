package com.schlock.bot.services.database.pokemon.impl;

import com.schlock.bot.entities.pokemon.ShinyBet;
import com.schlock.bot.services.database.pokemon.ShinyBetDAO;
import com.schlock.bot.services.database.AbstractBaseDAO;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class ShinyBetDAOImpl extends AbstractBaseDAO<ShinyBet> implements ShinyBetDAO
{
    public ShinyBetDAOImpl(SessionFactory sessionFactory)
    {
        super(ShinyBet.class, sessionFactory);
    }

    public List<ShinyBet> getByUsername(String username)
    {
        String text = " select b " +
                        " from ShinyBet b " +
                        " join b.user u " +
                        " where u.username = :name " +
                        " and b.shiny is null ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);
        query.setParameter("name", username);

        List<ShinyBet> bets = query.list();

        session.getTransaction().commit();
        session.close();

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

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);
        query.setParameter("name", username);
        query.setParameter("pokemon", pokemonId);

        ShinyBet bet = singleResult(query);

        session.getTransaction().commit();
        session.close();

        return bet;
    }

    public List<ShinyBet> getAllCurrent()
    {
        String text = " from ShinyBet b" +
                        " where b.shiny is null ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);

        List<ShinyBet> bets = query.list();

        session.getTransaction().commit();
        session.close();

        return bets;
    }
}
