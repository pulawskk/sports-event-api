package com.pulawskk.sportseventapi.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor

@Entity
@Table(name="teams")
public class Team {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "team_competition",
            joinColumns = {@JoinColumn(name = "teams_id")},
            inverseJoinColumns = {@JoinColumn(name="competitions_id")}
    )
    @JsonManagedReference
    private Set<Competition> competitions = new HashSet<>();

    @Builder
    public Team(Long id, String name, Set<Competition> competitions) {
        this.id = id;
        this.name = name;
        if(competitions != null) {
            this.competitions = competitions;
        }
    }

    public void addCompetition(Competition competition) {
        if(this.competitions == null) {
            this.competitions = new HashSet<>();
        }
        if(competition != null) {
            this.competitions.add(competition);
        }
    }

    public Team(String name) {
        this.name = name;
    }

    public void removeCompetitionByName(String name) {
        Optional<Competition> competition = competitions.stream()
                .filter(comp -> comp.getName().equals(name))
                .findAny();
        Competition competitionToRemove = competition.orElseGet(null);
        competitions.remove(competitionToRemove);
    }
}
