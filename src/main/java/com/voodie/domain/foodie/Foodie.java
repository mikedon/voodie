package com.voodie.domain.foodie;

import com.google.common.collect.Lists;
import com.voodie.domain.election.District;
import com.voodie.domain.election.Vote;
import com.voodie.domain.identity.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Voodie
 * User: MikeD
 */
@Entity
public class Foodie {

    public static Long DEFAULT_KARMA = 5L;

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    private Long karma;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @OneToMany(mappedBy = "foodie")
    private List<Vote> votes = Lists.newArrayList();

    @ManyToOne(optional = false)
    @NotNull
    private District homeDistrict;

    // ---------------------------------

    public Long getKarma() {
        return karma;
    }

    public void setKarma(Long karma) {
        this.karma = karma;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public District getHomeDistrict() {
        return homeDistrict;
    }

    public void setHomeDistrict(District homeDistrict) {
        this.homeDistrict = homeDistrict;
    }
}
