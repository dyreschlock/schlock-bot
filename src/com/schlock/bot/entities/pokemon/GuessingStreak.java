package com.schlock.bot.entities.pokemon;

import com.schlock.bot.entities.Persisted;
import com.schlock.bot.entities.base.User;

import javax.persistence.*;

@Entity
@Table(name = "guessing_streak")
public class GuessingStreak extends Persisted
{
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "streakNumber")
    private Integer streakNumber;

    public GuessingStreak()
    {
    }

    public void incrementStreak()
    {
        this.streakNumber++;
    }

    public void setNewUser(User user)
    {
        this.userId = user.getId();
        this.streakNumber = 1;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Integer getStreakNumber()
    {
        return streakNumber;
    }

    public void setStreakNumber(Integer streakNumber)
    {
        this.streakNumber = streakNumber;
    }
}
