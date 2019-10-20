package com.pulawskk.sportseventapi.entity;

import com.pulawskk.sportseventapi.enums.CompetitionType;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private CompetitionType type;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "competitions", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Team> teams = new HashSet<>();

    @Builder
    public Competition(Long id, String name, Set<Team> teams, CompetitionType type) {
        this.id = id;
        this.name = name;
        this.type = type;
        if(teams != null) {
            this.teams = teams;
        }
    }

    public void addNewTeam(Team newTeam) {
        if(this.teams == null) {
            this.teams = new HashSet<>();
        }
        if(newTeam != null) {
            this.teams.add(newTeam);
        }
    }
}
