# AndroidOTPApplicationProject
1회용 바코드/QR코드를 활용한 출석 체크 어플리케이션(결제 시스템에도 응용 가능)





USED QUERY for MySQL

CREATE EVENT IF NOT EXISTS evt1_makestatus
ON SCHEDULE AT '2018-03-28 15:10:00'
COMMENT 'Make status at 23:59 daily'
DO
UPDATE KOPOCTC.managingDB SET status = 4 WHERE time_in is null AND time_out is null;

CREATE EVENT IF NOT EXISTS evt2_makestatus
ON SCHEDULE AT '2018-03-28 15:10:02'
COMMENT 'Make status at 23:59 daily'
DO
UPDATE KOPOCTC.managingDB SET status = 2 WHERE time_in is not null AND time_out is null;

CREATE EVENT IF NOT EXISTS evt3_makestatus
ON SCHEDULE AT '2018-03-28 15:10:04'
COMMENT 'Make status at 23:59 daily'
DO
UPDATE KOPOCTC.managingDB SET status = 1 WHERE time_in is null AND time_out is not null;

CREATE EVENT IF NOT EXISTS evt4_makestatus
ON SCHEDULE AT '2018-03-28 15:10:06'
COMMENT 'Make status at 23:59 daily'
DO
UPDATE KOPOCTC.managingDB SET status = 5 WHERE time_in is not null AND time_out is not null;

CREATE EVENT IF NOT EXISTS evt_save
ON SCHEDULE AT '2018-03-28 15:10:10'
COMMENT 'Sate status at 23:59 daily'
DO
INSERT INTO KOPOCTC.attendanceDB(_id,time_in,time_out,status,date) SELECT _id,time_in,time_out,status,date FROM KOPOCTC.managingDB;


CREATE EVENT IF NOT EXISTS evt_delete
ON SCHEDULE AT '2018-03-28 15:10:20'
COMMENT 'Delete managingDB at 23:58 daily'
DO
DELETE FROM KOPOCTC.managingDB;

CREATE EVENT IF NOT EXISTS evt_insert
ON SCHEDULE EVERY '1' DAY
STARTS '2018-03-29 00:00:01'
COMMENT 'Insert into managingDB at 23:59 daily'
DO
INSERT INTO KOPOCTC.managingDB(_id) SELECT _id FROM KOPOCTC.otpDB;
