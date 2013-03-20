/*
Initial database script to count worked hours per day.

workedHours and dailyMark in minutes
*/
create table workday (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
						workDate DATE,
						workedHours INTEGER,
						isClosed BOOLEAN,
						dailyMark INTEGER
);

/*
INSERT
insert into workday (workDate, workedHours, isClosed) values (strftime('%s','now'), hours, isClosed)
strftime('%s', 'now') computes the timestampt in the GMT timezone

REMOVE
delete from workday where _id = id

UPDATE 
update workday set isClosed = 'bool' where workedHours = 'integer';

SELECT
select * from workday where workDate < date('now')

*/

create table checkins (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
						workdayID INTEGER,
						checkinHour DATE,
						FOREIGN KEY (workdayID) REFERENCES workday(_id) ON DELETE CASCADE ON UPDATE CASCADE
);

/*

# INSERT
insert into checkins (workdayID, checkinHour) values (workdayID, strftime('%s','now'))
strftime('%s', 'now') computes the timestampt in the GMT timezone

# REMOVE
delete from checkins where workdayID = day

# UPDATE
update checkins set workdayID = previousValue where workdayID = newValue;

# SELECT
select * from checkins where workdayID = id

*/