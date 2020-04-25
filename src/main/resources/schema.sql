create table if not exists competitions
(
    id bigint not null
        constraint competitions_pkey
            primary key,
    name varchar(255),
    type integer
);

create table if not exists teams
(
    id bigint not null
        constraint teams_pkey
            primary key,
    name varchar(255)
);

create table if not exists team_competition
(
    teams_id bigint not null
        constraint fkrspsko4o9t060xftek92jhja2
            references teams,
    competitions_id bigint not null
        constraint fki4fpj0p2lc8i4pyqdig12r0r5
            references competitions,
    constraint team_competition_pkey
        primary key (teams_id, competitions_id)
);

create table if not exists games
(
    id bigint not null
        constraint games_pkey
            primary key,
    end_date timestamp,
    start_date timestamp,
    status integer,
    unique_id varchar(255),
    competition_id bigint
        constraint fkd1tiqqn931xtfs8hcah2rtac1
            references competitions,
    team_away_id bigint
        constraint fkcyg3yr3xebsasxr8l0q9j2ft3
            references teams,
    team_home_id bigint
        constraint fkd2crt5k5ob7rw2yux2i93vnnr
            references teams
);

create table if not exists odds
(
    id bigint not null
        constraint odds_pkey
            primary key,
    type integer,
    value numeric(19,2),
    games_id bigint
        constraint fkpa9sxy385vbyb2cqnpay65oy
            references games
);

create table if not exists game_reports_football
(
    id bigint not null
        constraint game_reports_football_pkey
            primary key,
    corner_away integer,
    corner_home integer,
    goal_away integer,
    goal_home integer,
    offside_away integer,
    offside_home integer,
    r_card_away integer,
    r_card_home integer,
    y_card_away integer,
    y_card_home integer
);


create table if not exists results_football
(
    id bigint not null
        constraint results_football_pkey
            primary key,
    game_id bigint
        constraint fkd7xqi0ya6wjroih8c42p7rwgp
            references games,
    game_report_id bigint
        constraint fkpdn0o4c0yi0nov16qwk0fj8ai
            references game_reports_football
);
