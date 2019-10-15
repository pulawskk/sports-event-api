insert into teams(id, name) values (1, 'Chelsea'),
                                   (2, 'Arsenal'),
                                   (3, 'Manchester United'),
                                   (4, 'Manchester City'),
                                   (5, 'Newcastle United'),
                                   (6, 'West Ham United'),
                                   (7, 'Crystal Palace'),
                                   (8, 'Aston Villa'),
                                   (9, 'Norwich'),
                                   (10, 'Leicester'),
                                   (11, 'Southampton'),
                                   (12, 'Sheffield United'),
                                   (13, 'Tottenham'),
                                   (14, 'Brighton'),
                                   (15, 'Everton'),
                                   (16, 'Liverpool'),
                                   (17, 'Watford'),
                                   (18, 'Wolverhampton Wanders'),
                                   (19, 'Bournemouth'),
                                   (20, 'Burnley');

insert into competitions(id, name) values (1, 'Premier League');

insert into team_competition(teams_id, competitions_id) values (1, 1),
                                                               (2, 1),
                                                               (3, 1),
                                                               (4, 1),
                                                               (5, 1),
                                                               (6, 1),
                                                               (7, 1),
                                                               (8, 1),
                                                               (9, 1),
                                                               (10, 1),
                                                               (11, 1),
                                                               (12, 1),
                                                               (13, 1),
                                                               (14, 1),
                                                               (15, 1),
                                                               (16, 1),
                                                               (17, 1),
                                                               (18, 1),
                                                               (19, 1),
                                                               (20, 1);

insert into games(id, team_home_id, team_away_id, competition_id, start_date, end_date)
values (1, 1, 2, 1, LOCALTIMESTAMP, LOCALTIMESTAMP),
       (2, 3, 4, 1, LOCALTIMESTAMP, LOCALTIMESTAMP),
       (3, 5, 6, 1, LOCALTIMESTAMP, LOCALTIMESTAMP),
       (4, 7, 8, 1, LOCALTIMESTAMP, LOCALTIMESTAMP);

insert into odds(id, type, value, games_id) values (1, 1, 1.5, 1),
                                                   (2, 2, 3.2, 1),
                                                   (3, 3, 5.2, 1),
                                                   (4, 1, 1.5, 2),
                                                   (5, 2, 3.2, 2),
                                                   (6, 3, 5.2, 2),
                                                   (7, 1, 5.2, 3),
                                                   (8, 2, 5.2, 3),
                                                   (9, 3, 5.2, 3),
                                                   (10, 1, 5.2, 4),
                                                   (11, 2, 5.2, 4),
                                                   (12, 3, 5.2, 4);

insert into game_reports_football(id, goal_home, goal_away, corner_home, corner_away, offside_home, offside_away,
                                  y_card_home, y_card_away, r_card_home, r_card_away) values

                (1, 3, 0, 5, 4, 2, 5, 3, 6, 5, 1),
                (2, 3, 0, 5, 4, 2, 5, 3, 6, 5, 1),
                (3, 3, 0, 5, 4, 2, 5, 3, 6, 5, 1),
                (4, 3, 0, 5, 4, 2, 5, 3, 6, 5, 1);

insert into results_football(id, game_id, game_report_id) values (1, 1, 1),
                                                                 (2, 1, 1),
                                                                 (3, 1, 1),
                                                                 (4, 1, 1);

