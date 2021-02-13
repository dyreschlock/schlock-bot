package com.schlock.bot.entities.apps;

import com.schlock.bot.entities.Persisted;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User extends Persisted
{
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "balance")
    private Integer balance;

    public User()
    {
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public Integer getBalance()
    {
        return balance;
    }

    public void setBalance(Integer balance)
    {
        this.balance = balance;
    }
}
