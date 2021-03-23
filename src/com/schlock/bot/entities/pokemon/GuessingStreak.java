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

    @ManyToOne
    @JoinColumn(name = "user_id",
            foreignKey = @ForeignKey(name = "GUESS_USER_ID_FK"))
    private User user;

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
        this.user = user;
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

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
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
