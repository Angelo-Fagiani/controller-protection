package it.develhope.controllerprotection.salary.entities;

import it.develhope.controllerprotection.user.entities.User;

import javax.persistence.*;


@Entity
@Table
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private long amount;

    @OneToOne
    private User user;

    public Salary() {
    }

    public Salary(Long id, long amount, User user) {
        this.id = id;
        this.amount = amount;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
