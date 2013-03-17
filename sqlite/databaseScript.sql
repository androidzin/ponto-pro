/*
Initial database script to count worked hours per day.
*/
create table workday (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
						workDate DATE,
						workedHours REAL,
						isClosed BOOLEAN
);

/*
INSERT
insert into workday (workDate, workedHours, isClosed) values (strftime('%s','now'), hours, isClosed)
strftime('%s', 'now') computes the timestampt in the GMT timezone

REMOVE

UPDATE 

SELECT
select * from workday where workDate < date('now')

*/

create table checkins (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
						workdayID INTEGER,
						checkinHour DATE,
						FOREIGN KEY (workdayID) REFERENCES workday(_id)
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