insert into teams(id, name) select 1, 'Chelsea' where not exists (select * from teams where id = 1);
insert into teams(id, name) select 2, 'Arsenal' where not exists (select * from teams where id = 2);
insert into teams(id, name) select 3, 'Manchester United' where not exists (select * from teams where id = 3);
insert into teams(id, name) select 4, 'Manchester City' where not exists (select * from teams where id = 4);
insert into teams(id, name) select 5, 'Newcastle United' where not exists (select * from teams where id = 5);
insert into teams(id, name) select 6, 'West Ham United' where not exists (select * from teams where id = 6);
insert into teams(id, name) select 7, 'Crystal Palace' where not exists (select * from teams where id = 7);
insert into teams(id, name) select 8, 'Aston Villa' where not exists (select * from teams where id = 8);
insert into teams(id, name) select 9, 'Norwich' where not exists (select * from teams where id = 9);
insert into teams(id, name) select 10, 'Leicester' where not exists (select * from teams where id = 10);
insert into teams(id, name) select 11, 'Southampton' where not exists (select * from teams where id = 11);
insert into teams(id, name) select 12, 'Sheffield United' where not exists (select * from teams where id = 12);
insert into teams(id, name) select 13, 'Tottenham' where not exists (select * from teams where id = 13);
insert into teams(id, name) select 14, 'Brighton' where not exists (select * from teams where id = 14);
insert into teams(id, name) select 15, 'Everton' where not exists (select * from teams where id = 15);
insert into teams(id, name) select 16, 'Liverpool' where not exists (select * from teams where id = 16);
insert into teams(id, name) select 17, 'Watford' where not exists (select * from teams where id = 17);
insert into teams(id, name) select 18, 'Wolverhampton Wanders' where not exists (select * from teams where id = 18);
insert into teams(id, name) select 19, 'Bournemouth' where not exists (select * from teams where id = 19);
insert into teams(id, name) select 20, 'Burnley' where not exists (select * from teams where id = 20);
insert into teams(id, name) select 21, 'Barnsley' where not exists (select * from teams where id = 21);
insert into teams(id, name) select 22, 'Birmingham' where not exists (select * from teams where id = 22);
insert into teams(id, name) select 23, 'Blackburn' where not exists (select * from teams where id = 23);
insert into teams(id, name) select 24, 'Brentford' where not exists (select * from teams where id = 24);
insert into teams(id, name) select 25, 'Bristol City' where not exists (select * from teams where id = 25);
insert into teams(id, name) select 26, 'Cardiff' where not exists (select * from teams where id = 26);
insert into teams(id, name) select 27, 'Charlton' where not exists (select * from teams where id = 27);
insert into teams(id, name) select 28, 'Derby' where not exists (select * from teams where id = 28);
insert into teams(id, name) select 29, 'Fulham' where not exists (select * from teams where id = 29);
insert into teams(id, name) select 30, 'Huddersfield' where not exists (select * from teams where id = 30);
insert into teams(id, name) select 31, 'Hull' where not exists (select * from teams where id = 31);
insert into teams(id, name) select 32, 'Leeds' where not exists (select * from teams where id = 32);

insert into competitions(id, name, type) select 1, 'Premier League', 0 where not exists (select * from competitions where id = 1);
insert into competitions(id, name, type) select 2, 'FA Cup', 1 where not exists (select * from competitions where id = 2);

insert into team_competition(teams_id, competitions_id) select  1,1 where not exists (select * from team_competition where teams_id = 1 and competitions_id = 1);
insert into team_competition(teams_id, competitions_id) select  2, 1 where not exists (select * from team_competition where teams_id = 2 and competitions_id = 1);
insert into team_competition(teams_id, competitions_id) select  3, 1 where not exists (select * from team_competition where teams_id = 3 and competitions_id = 1);
insert into team_competition(teams_id, competitions_id) select  4, 1 where not exists (select * from team_competition where teams_id = 4 and competitions_id = 1);
insert into team_competition(teams_id, competitions_id) select  5, 1 where not exists (select * from team_competition where teams_id = 5 and competitions_id = 1);
insert into team_competition(teams_id, competitions_id) select  6, 1 where not exists (select * from team_competition where teams_id = 6 and competitions_id = 1);
insert into team_competition(teams_id, competitions_id) select  7, 1 where not exists (select * from team_competition where teams_id = 7 and competitions_id = 1);
insert into team_competition(teams_id, competitions_id) select  8, 1 where not exists (select * from team_competition where teams_id = 8 and competitions_id = 1);
insert into team_competition(teams_id, competitions_id) select  9, 1 where not exists (select * from team_competition where teams_id = 9 and competitions_id = 1);
insert into team_competition(teams_id, competitions_id) select  10, 1 where not exists (select * from team_competition where teams_id = 10 and competitions_id = 1);
insert into team_competition(teams_id, competitions_id) select  11, 1 where not exists (select * from team_competition where teams_id = 11 and competitions_id = 1);
insert into team_competition(teams_id, competitions_id) select  12, 1 where not exists (select * from team_competition where teams_id = 12 and competitions_id = 1);
insert into team_competition(teams_id, competitions_id) select  13, 1 where not exists (select * from team_competition where teams_id = 13 and competitions_id = 1);
insert into team_competition(teams_id, competitions_id) select  14, 1 where not exists (select * from team_competition where teams_id = 14 and competitions_id = 1);
insert into team_competition(teams_id, competitions_id) select  15, 1 where not exists (select * from team_competition where teams_id = 15 and competitions_id = 1);
insert into team_competition(teams_id, competitions_id) select  16, 1 where not exists (select * from team_competition where teams_id = 16 and competitions_id = 1);
insert into team_competition(teams_id, competitions_id) select  17, 1 where not exists (select * from team_competition where teams_id = 17 and competitions_id = 1);
insert into team_competition(teams_id, competitions_id) select  18, 1 where not exists (select * from team_competition where teams_id = 18 and competitions_id = 1);
insert into team_competition(teams_id, competitions_id) select  19, 1 where not exists (select * from team_competition where teams_id = 19 and competitions_id = 1);
insert into team_competition(teams_id, competitions_id) select  20, 1 where not exists (select * from team_competition where teams_id = 20 and competitions_id = 1);
insert into team_competition(teams_id, competitions_id) select  1, 2 where not exists (select * from team_competition where teams_id = 1 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  2, 2 where not exists (select * from team_competition where teams_id = 2 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  3, 2 where not exists (select * from team_competition where teams_id = 3 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  4, 2 where not exists (select * from team_competition where teams_id = 4 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  5, 2 where not exists (select * from team_competition where teams_id = 5 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  6, 2 where not exists (select * from team_competition where teams_id = 6 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  7, 2 where not exists (select * from team_competition where teams_id = 7 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  8, 2 where not exists (select * from team_competition where teams_id = 8 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  9, 2 where not exists (select * from team_competition where teams_id = 9 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  10, 2 where not exists (select * from team_competition where teams_id = 10 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  11, 2 where not exists (select * from team_competition where teams_id = 11 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  12, 2 where not exists (select * from team_competition where teams_id = 12 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  13, 2 where not exists (select * from team_competition where teams_id = 13 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  14, 2 where not exists (select * from team_competition where teams_id = 14 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  15, 2 where not exists (select * from team_competition where teams_id = 15 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  16, 2 where not exists (select * from team_competition where teams_id = 16 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  17, 2 where not exists (select * from team_competition where teams_id = 17 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  18, 2 where not exists (select * from team_competition where teams_id = 18 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  19, 2 where not exists (select * from team_competition where teams_id = 19 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  20, 2 where not exists (select * from team_competition where teams_id = 20 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  21, 2 where not exists (select * from team_competition where teams_id = 21 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  22, 2 where not exists (select * from team_competition where teams_id = 22 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  23, 2 where not exists (select * from team_competition where teams_id = 23 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  24, 2 where not exists (select * from team_competition where teams_id = 24 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  25, 2 where not exists (select * from team_competition where teams_id = 25 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  26, 2 where not exists (select * from team_competition where teams_id = 26 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  27, 2 where not exists (select * from team_competition where teams_id = 27 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  28, 2 where not exists (select * from team_competition where teams_id = 28 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  29, 2 where not exists (select * from team_competition where teams_id = 29 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  30, 2 where not exists (select * from team_competition where teams_id = 30 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  31, 2 where not exists (select * from team_competition where teams_id = 31 and competitions_id = 2);
insert into team_competition(teams_id, competitions_id) select  32, 2 where not exists (select * from team_competition where teams_id = 32 and competitions_id = 2);


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

