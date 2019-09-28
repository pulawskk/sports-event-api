package com.pulawskk.sportseventapi.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor

@Entity
@Table(name="teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(
            name = "team_competition",
            joinColumns = {@JoinColumn(name = "teams_id")},
            inverseJoinColumns = {@JoinColumn(name="competitions_id")}
    )
    private Set<Competition> competitions = new HashSet<>();

}