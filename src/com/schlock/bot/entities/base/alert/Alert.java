package com.schlock.bot.entities.base.alert;

import com.schlock.bot.entities.Persisted;
import com.schlock.bot.entities.base.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "alerts")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "class")
public abstract class Alert extends Persisted
{
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private AlertType type;

    @ManyToOne
    @JoinColumn(name = "user_id",
                    foreignKey = @ForeignKey(name = "ALERT_USER_ID_FK"))
    private User user;

    @Column(name = "requestDate")
    private Date requestDate;

    @Column(name = "finishDate")
    private Date finishDate;

    @Column(name = "message")
    private String message;


    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public AlertType getType()
    {
        return type;
    }

    public void setType(AlertType type)
    {
        this.type = type;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Date getRequestDate()
    {
        return requestDate;
    }

    public void setRequestDate(Date requestDate)
    {
        this.requestDate = requestDate;
    }

    public Date getFinishDate()
    {
        return finishDate;
    }

    public void setFinishDate(Date finishDate)
    {
        this.finishDate = finishDate;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
