package com.schlock.bot.entities.base;

import com.schlock.bot.entities.Persisted;

import javax.persistence.*;
import java.util.Date;

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

    @Column(name = "follow_date")
    private Date followDate;

    @Column(name = "doubler")
    private Integer pointsDoubler;

    public User()
    {
    }

    public User(String username)
    {
        this.username = username;
    }

    public void incrementBalance(Integer points)
    {
        Double newPoints = Math.pow(points, pointsDoubler);
        balance = balance + newPoints.intValue();
    }

    public void decrementBalance(Integer points)
    {
        balance = balance - points;
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

    public Date getFollowDate()
    {
        return followDate;
    }

    public void setFollowDate(Date followDate)
    {
        this.followDate = followDate;
    }

    public Integer getPointsDoubler()
    {
        return pointsDoubler;
    }

    public void setPointsDoubler(Integer pointsDoubler)
    {
        this.pointsDoubler = pointsDoubler;
    }
}
