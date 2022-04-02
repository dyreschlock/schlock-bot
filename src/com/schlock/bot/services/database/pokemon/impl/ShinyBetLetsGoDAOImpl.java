package com.schlock.bot.services.database.pokemon.impl;

import com.schlock.bot.entities.pokemon.ShinyBetLetsGo;
import com.schlock.bot.services.database.pokemon.ShinyBetLetsGoDAO;
import com.schlock.bot.services.database.AbstractBaseDAO;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class ShinyBetLetsGoDAOImpl extends AbstractBaseDAO<ShinyBetLetsGo> implements ShinyBetLetsGoDAO
{
    public ShinyBetLetsGoDAOImpl(SessionFactory sessionFactory)
    {
        super(ShinyBetLetsGo.class, sessionFactory);
    }

    public List<ShinyBetLetsGo> getByUsername(String username)
    {
        String text = " select b " +
                        " from ShinyBetLetsGo b " +
                        " join b.user u " +
                        " where u.username = :name " +
                        " and b.shiny is null ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);
        query.setParameter("name", username);

        List<ShinyBetLetsGo> bets = query.list();

        session.getTransaction().commit();
        session.close();

        return bets;
    }

    public ShinyBetLetsGo getByUsernameAndPokemon(String username, String pokemonId)
    {
        String text = " select b " +
                        " from ShinyBetLetsGo b " +
                        " join b.user u " +
                        " where u.username = :name " +
                        " and b.pokemonId = :pokemon " +
                        " and b.shiny is null ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);
        query.setParameter("name", username);
        query.setParameter("pokemon", pokemonId);

        ShinyBetLetsGo bet = singleResult(query);

        session.getTransaction().commit();
        session.close();

        return bet;
    }

    public List<ShinyBetLetsGo> getAllCurrent()
    {
        String text = " from ShinyBetLetsGo b" +
                        " where b.shiny is null ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);

        List<ShinyBetLetsGo> bets = query.list();

        session.getTransaction().commit();
        session.close();

        return bets;
    }
}
