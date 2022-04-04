package com.schlock.bot.entities.base;

import com.schlock.bot.entities.Persisted;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user")
public class User extends Persisted
{
    private static final String PRESTIGE_STAR = "★";

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "balance")
    private Long balance;

    @Column(name = "follow_date")
    private Date followDate;

    @Column(name = "prestige")
    private Integer prestige;

    @Column(name = "doubler")
    private Integer pointsDoubler;

    @Column(name = "highscore_points")
    private Long highScorePoints;

    @Column(name = "highscore_streak")
    private Integer highScoreStreak;

    public User()
    {
    }

    public User(String username)
    {
        this.username = username;
    }

    public void incrementBalance(Long points)
    {
        this.balance += points;

        incrementBalanceHighScore(this.balance);
    }

    public void incrementBalanceHighScore(Long balance)
    {
        if (this.highScorePoints == null)
        {
            this.highScorePoints = balance;
        }
        else if (balance > this.highScorePoints)
        {
            this.highScorePoints = balance;
        }
    }

    public void incrementStreakHighScore(Integer streak)
    {
        if (this.highScoreStreak == null)
        {
            this.highScoreStreak = streak;
        }
        else if (streak > this.highScoreStreak)
        {
            this.highScoreStreak = streak;
        }
    }

    public boolean hasDoubler()
    {
        return this.pointsDoubler > 1;
    }

    public Long modifyPointsWithDoubler(Long points)
    {
        //this is probably too much

        Long newPoints = points * pointsDoubler;
        return newPoints;
    }

    public void decrementBalance(Long points)
    {
        balance = balance - points;
    }

    public String getPrestigeLevel()
    {
        String stars = "";
        for(int i = 0; i < prestige; i++)
        {
            stars += PRESTIGE_STAR;
        }
        return stars;
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

    public Long getBalance()
    {
        return balance;
    }

    public void setBalance(Long balance)
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

    public Integer getPrestige()
    {
        return prestige;
    }

    public void setPrestige(Integer prestige)
    {
        this.prestige = prestige;
    }

    public Integer getPointsDoubler()
    {
        return pointsDoubler;
    }

    public void setPointsDoubler(Integer pointsDoubler)
    {
        this.pointsDoubler = pointsDoubler;
    }

    public Long getHighScorePoints()
    {
        return highScorePoints;
    }

    public void setHighScorePoints(Long highScorePoints)
    {
        this.highScorePoints = highScorePoints;
    }

    public Integer getHighScoreStreak()
    {
        return highScoreStreak;
    }

    public void setHighScoreStreak(Integer highScoreStreak)
    {
        this.highScoreStreak = highScoreStreak;
    }
}
