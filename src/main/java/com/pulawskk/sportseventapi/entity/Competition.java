package com.pulawskk.sportseventapi.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor

@Entity
@Table(name="competitions")
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "competitions")
    private Set<Team> teams;

}
