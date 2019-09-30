package com.pulawskk.sportseventapi.entity;

import lombok.Builder;
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
@Table(name="competitions")
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "competitions", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Team> teams = new HashSet<>();

    @Builder
    public Competition(Long id, String name, Set<Team> teams) {
        this.id = id;
        this.name = name;
        if(teams != null) {
            this.teams = teams;
        }
    }

    public void addNewTeam(Team newTeam) {
        if(this.teams == null) {
            this.teams = new HashSet<>();
        }
        this.teams.add(newTeam);
    }
}
