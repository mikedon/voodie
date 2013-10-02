package com.voodie.domain.election;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Voodie
 * User: MikeD
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
public class District {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    private String name;

    // ---------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
