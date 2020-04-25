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
                                   (20, 'Burnley'),
                                   (21, 'Barnsley'),
                                   (22, 'Birmingham'),
                                   (23, 'Blackburn'),
                                   (24, 'Brentford'),
                                   (25, 'Bristol City'),
                                   (26, 'Cardiff'),
                                   (27, 'Charlton'),
                                   (28, 'Derby'),
                                   (29, 'Fulham'),
                                   (30, 'Huddersfield'),
                                   (31, 'Hull'),
                                   (32, 'Leeds');


insert into competitions(id, name, type) values (1, 'Premier League', 0);
insert into competitions(id, name, type) values (2, 'FA Cup', 1);

insert into team_competition(teams_id, competitions_id) values (1,1),
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
                                                               (20, 1),
                                                                (1, 2),
                                                                (2, 2),
                                                                (3, 2),
                                                                (4, 2),
                                                                (5, 2),
                                                                (6, 2),
                                                                (7, 2),
                                                                (8, 2),
                                                                (9, 2),
                                                                (10, 2),
                                                                (11, 2),
                                                                (12, 2),
                                                                (13, 2),
                                                                (14, 2),
                                                                (15, 2),
                                                                (16, 2),
                                                                (17, 2),
                                                                (18, 2),
                                                                (19, 2),
                                                                (20, 2),
                                                               (21, 2),
                                                               (22, 2),
                                                               (23, 2),
                                                               (24, 2),
                                                               (25, 2),
                                                               (26, 2),
                                                               (27, 2),
                                                               (28, 2),
                                                               (29, 2),
                                                               (30, 2),
                                                               (31, 2),
                                                               (32, 2);


-- insert into games(id, team_home_id, team_away_id, competition_id, start_date, end_date)
-- values (1, 1, 2, 1, LOCALTIMESTAMP, LOCALTIMESTAMP),
--        (2, 3, 4, 1, LOCALTIMESTAMP, LOCALTIMESTAMP),
--        (3, 5, 6, 1, LOCALTIMESTAMP, LOCALTIMESTAMP),
--        (4, 7, 8, 1, LOCALTIMESTAMP, LOCALTIMESTAMP);

-- insert into odds(id, type, value, games_id) values (1, 1, 1.5, 1),
--                                                    (2, 2, 3.2, 1),
--                                                    (3, 0, 5.2, 1),
--                                                    (4, 1, 2.3, 2),
--                                                    (5, 2, 3.0, 2),
--                                                    (6, 0, 4.1, 2),
--                                                    (7, 1, 1.2, 3),
--                                                    (8, 2, 3.2, 3),
--                                                    (9, 0, 6.7, 3),
--                                                    (10, 1, 1.2, 4),
--                                                    (11, 2, 2.2, 4),
--                                                    (12, 0, 7.2, 4);

-- insert into game_reports_football(id, goal_home, goal_away, corner_home, corner_away, offside_home, offside_away,
--                                   y_card_home, y_card_away, r_card_home, r_card_away) values
--
--                 (1, 3, 0, 5, 4, 2, 5, 3, 6, 5, 1),
--                 (2, 3, 0, 5, 4, 2, 5, 3, 6, 5, 1),
--                 (3, 3, 0, 5, 4, 2, 5, 3, 6, 5, 1),
--                 (4, 3, 0, 5, 4, 2, 5, 3, 6, 5, 1);

-- insert into results_football(id, game_id, game_report_id) values (1, 1, 1),
--                                                                  (2, 2, 2),
--                                                                  (3, 3, 3),
--                                                                  (4, 4, 4);

