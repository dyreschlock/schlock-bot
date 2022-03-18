package com.schlock.bot.services.database.pokemon.impl;

import com.schlock.bot.entities.pokemon.ShinyDexEntryHisui;
import com.schlock.bot.services.database.AbstractBaseDAO;
import com.schlock.bot.services.database.pokemon.ShinyDexEntryHisuiDAO;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class ShinyDexEntryHisuiDAOImpl extends AbstractBaseDAO<ShinyDexEntryHisui> implements ShinyDexEntryHisuiDAO
{
    public ShinyDexEntryHisuiDAOImpl(SessionFactory sessionFactory)
    {
        super(ShinyDexEntryHisui.class, sessionFactory);
    }

    public List<ShinyDexEntryHisui> getInPokemonOrder()
    {
        String text = " from ShinyDexEntryHisui e " +
                        " order by o.pokemonNumber asc ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);
        List<ShinyDexEntryHisui> entries = query.list();

        session.getTransaction().commit();
        session.close();

        return entries;
    }

    public ShinyDexEntryHisui getEntryByPokemonId(String pokemonId)
    {
        String text = " from ShinyDexEntryHisui e " +
                        " where e.pokemonId = :pokemonId ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);
        query.setParameter("pokemonId", pokemonId);

        ShinyDexEntryHisui entry = singleResult(query);

        session.getTransaction().commit();
        session.close();

        return entry;
    }

    public Integer getRemainingShinyCountInFieldlands()
    {
        final String param = "inMassiveFieldlands";
        return getRemainingShinyCountInArea(param);
    }

    public Integer getRemainingShinyCountInMirelands()
    {
        final String param = "isMassiveMirelands";
        return getRemainingShinyCountInArea(param);
    }

    public Integer getRemainingShinyCountInCoastlands()
    {
        final String param = "isMassiveCoastlands";
        return getRemainingShinyCountInArea(param);
    }

    public Integer getRemainingShinyCountInHighlands()
    {
        final String param = "isMassiveHighlands";
        return getRemainingShinyCountInArea(param);
    }

    public Integer getRemainingShinyCountInIcelands()
    {
        final String param = "isMassiveIcelands";
        return getRemainingShinyCountInArea(param);
    }

    private Integer getRemainingShinyCountInArea(String param)
    {
        String text = " select count(e) from ShinyDexEntryHisui e" +
                " where e.haveShiny is false " +
                " and e." + param + " is true ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);
        Long count = (Long) query.list().get(0);

        session.getTransaction().commit();
        session.close();

        return count.intValue();
    }

    public Integer getRemainingShinyCountNotInMassive()
    {
        String text = " select count(e) from ShinyDexEntryHisui e" +
                " where e.haveShiny is false " +
                " and e.inMassiveFieldlands is false " +
                " and e.isMassiveMirelands is false " +
                " and e.isMassiveCoastlands is false " +
                " and e.isMassiveHighlands is false " +
                " and e.isMassiveIcelands is false ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);
        Long count = (Long) query.list().get(0);

        session.getTransaction().commit();
        session.close();

        return count.intValue();
    }
}
