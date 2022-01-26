DROP DATABASE IF EXISTS foster_kittens;
CREATE DATABASE foster_kittens;
USE foster_kittens;

SET SQL_SAFE_UPDATES = 0;
    
CREATE TABLE foster_parent (
	foster_parent_id   INT            NOT NULL, 
    parent_name         VARCHAR(255)    NOT NULL,
    email              VARCHAR(255)   NOT NULL,
    CONSTRAINT foster_parent_pk PRIMARY KEY (foster_parent_id)
);

INSERT INTO foster_parent
 VALUES (0, 'abby dennin', 'adennin@fakemail.com'),
		(1, 'john smith','jsmith@fakemail.com'),
        (2, 'jane doe',' jdoe@fakemail.com');

CREATE TABLE coordinator (
	coordinator_id     INT           NOT NULL,
    coordinator_name         VARCHAR(255)   NOT NULL,
    email              VARCHAR(255)  NOT NULL,
    CONSTRAINT coordinator_pk PRIMARY KEY (coordinator_id)
);

INSERT INTO coordinator
 VALUES (0, 'sarah etkin', 'setkin@fakemail.com'),
		(1, 'steve stevenson', 'sstevenson@fakemail.com');

CREATE TABLE shelter (
	shelter_id         INT           NOT NULL,
    shelter_name       VARCHAR(255)   NOT NULL,
    coordinator_id     INT           NOT NULL,
    CONSTRAINT shelter_pk PRIMARY KEY (shelter_id),
    CONSTRAINT shelter_fk_coordinator FOREIGN KEY (coordinator_id)
		REFERENCES coordinator (coordinator_id)
        ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO shelter
 VALUES (0, 'westchester humane society', 0),
		(1, 'pet rescue', 1);

CREATE TABLE treatment (
	treatment_id        INT             NOT NULL,
    medicine_name       VARCHAR(255)    NOT NULL,
    medicine_dose       DOUBLE          NOT NULL,
    medicine_frequency  VARCHAR(255)    NOT NULL,
    CONSTRAINT treatment_pk PRIMARY KEY (treatment_id)
);

INSERT INTO treatment 
VALUES (0, 'clavamox', .10, 'twice a day, 14 days'), 
       (1, 'orbax', .25, 'once a day, 14 days'),
       (2, 'eye cream', .05, 'twice a day until clear'),
       (3, 'panacur', .3, 'once a day, 10 days'),
       (4, 'albon', .25, 'twice a day, 7 days'),
       (5, 'metronidazole', .20, 'once a day, 10 days');

CREATE TABLE kitten (
	kitten_id       INT          AUTO_INCREMENT,
    foster_parent_id       INT     NULL,
    shelter_id      INT          NULL,
    treatment_id    INT          NULL,
    kitten_name     VARCHAR(255)  NOT NULL,
    approx_age      INT          NOT NULL,
    color           VARCHAR(255)  NOT NULL,
    social_status   TEXT         NOT NULL,
    in_shelter      TINYINT(1)   NULL,
    in_foster       TINYINT(1)   NULL,
    adopted         TINYINT(1)   NULL,
	CONSTRAINT foster_kitten_pk PRIMARY KEY (kitten_id),
	CONSTRAINT kitten_fk_parent
    FOREIGN KEY (foster_parent_id) REFERENCES foster_parent (foster_parent_id)
    ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT kitten_fk_shelter
    FOREIGN KEY (shelter_id) REFERENCES shelter (shelter_id)
    ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO kitten
VALUES (0, 0, 0, 0, 'toby', 6, 'black + white', 'shy', 0, 0, 1),
	   (0, 0, 0, 1, 'dwight', 6, 'black + white', 'social', 0, 0, 1),
       (0, 0, 0, 2, 'tigger', 5, 'orange', 'timid', 1, 0, 0),
       (0, 0, 0, 3, 'louise', 6, 'black + white', 'shy', 1, 0, 0),
       (0, 1, 1, 4, 'lucy', 7, 'black + white', 'social', 0, 1, 0),
       (0, 2, 1, 5, 'pepper', 7, 'black + white', 'social', 0, 1, 0),
       (0, 0, 0, 3, 'wanda', 5, 'black', 'feral', 0, 1, 0);

CREATE TABLE bonds (
	kitten1_id INT NOT NULL,
	kitten2_id INT NOT NULL, 
	CONSTRAINT bonds_pk PRIMARY KEY (kitten1_id, kitten2_id)
);

INSERT INTO bonds 
VALUES (1, 2);

CREATE TABLE users (
    username VARCHAR(255) NOT NULL,
    user_password VARCHAR(255) NOT NULL,
    -- 0 represents foster coordinator, 1 represents foster parent
    user_type INT NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (username)
);

INSERT INTO users
VALUES ('adennin', 'password1', 1),
	   ('setkin', 'password0', 0);
       
-- Add Kitten
DELIMITER $$ 
CREATE PROCEDURE ADD_KITTEN(
	IN shelter_id  INT,
	IN kname VARCHAR(255),
    IN kage  INT, 
    IN kcolor VARCHAR(255),
    IN kstatus TEXT
)
BEGIN
	INSERT INTO kitten 
    VALUE (0, 0, shelter_id, 0, kname, kage, kcolor, kstatus, 1, 0, 0);
    COMMIT;
END$$
DELIMITER ;

-- Add Parent
DELIMITER $$ 
CREATE PROCEDURE ADD_PARENT(
	IN pname VARCHAR(255),
    IN email VARCHAR(255)
)
BEGIN
	INSERT INTO foster_parent 
    VALUE (0, pname, email);
    COMMIT;
END$$
DELIMITER ;

-- Add Bond (for kittens)
DELIMITER $$ 
CREATE PROCEDURE ADD_BOND(
	IN kitten_1 INT,
    IN kitten_2 INT
)
BEGIN
	INSERT INTO bonds
    VALUE (kitten_1, kitten_2);
    COMMIT;
END$$
DELIMITER ;

-- View all kittens
DELIMITER $$ 
CREATE PROCEDURE VIEW_ALL_KITTENS(
)
BEGIN
	SELECT kitten_id, kitten_name FROM kitten;
END$$
DELIMITER ;

-- View all kittens in shelter
DELIMITER $$ 
CREATE PROCEDURE VIEW_AVAILABLE_KITTENS(
)
BEGIN
	SELECT kitten_id, kitten_name FROM kitten WHERE in_shelter = 1;
END$$
DELIMITER ;

-- View all kittens that have been adopted
DELIMITER $$ 
CREATE PROCEDURE VIEW_ADOPTED_KITTENS(
)
BEGIN
	SELECT kitten_id, kitten_name FROM kitten WHERE adopted = 1;
END$$
DELIMITER ;

-- View all kittens that have fostered by a given parent
DELIMITER $$ 
CREATE PROCEDURE VIEW_PARENTS_KITTENS(
	IN parent_id INT
)
BEGIN
	SELECT kitten_id, kitten_name FROM kitten WHERE foster_parent_id = parent_id;
END$$
DELIMITER ;

-- View a foster parent's current kittens
DELIMITER $$ 
CREATE PROCEDURE VIEW_PARENTS_CURR_KITTENS(
	IN parent_id INT
)
BEGIN
	SELECT kitten_id, kitten_name FROM kitten 
    WHERE foster_parent_id = parent_id AND in_foster = 1;
END$$
DELIMITER ;

-- View a kitten's information
DELIMITER $$ 
CREATE PROCEDURE VIEW_KITTEN_INFO(
	IN kitten_id INT
)
BEGIN
	SELECT kitten_name, kitten.foster_parent_id, kitten.shelter_id,
    approx_age, color, social_status, parent_name, shelter_name 
    FROM kitten, foster_parent, shelter
    WHERE kitten_id = kitten.kitten_id 
    AND kitten.foster_parent_id = foster_parent.foster_parent_id
    AND kitten.shelter_id = shelter.shelter_id;
END$$
DELIMITER ;

-- View a kitten's treatment
DELIMITER $$ 
CREATE PROCEDURE VIEW_KITTEN_TREATMENT(
	IN kitten_id INT
)
BEGIN
	SELECT kitten_name, medicine_name, medicine_dose, medicine_frequency
    FROM kitten, treatment
    WHERE kitten.kitten_id = kitten_id
    AND kitten.treatment_id = treatment.treatment_id;
END$$
DELIMITER ;

-- Find a kitten based on its name
DELIMITER $$ 
CREATE PROCEDURE FIND_KITTEN_BY_NAME(
	IN kname VARCHAR(255)
)
BEGIN
	SELECT kitten_id, kitten_name FROM kitten 
    WHERE kitten_name = kname;
END$$
DELIMITER ;

-- View if a kitten has a bond
DELIMITER $$ 
CREATE PROCEDURE VIEW_BOND(
	IN kitten_id VARCHAR(255)
)
BEGIN
	SELECT kitten_id, kitten1_id, kitten2_id FROM kitten, bonds
    WHERE kitten1_id = kitten_id OR kitten2_id = kitten_id;
END$$
DELIMITER ;

-- Update that a kitten is adopted
DELIMITER $$ 
CREATE PROCEDURE KITTEN_ADOPTED(
	IN kitten_id INT
)
BEGIN
	UPDATE kitten SET adopted = 1 WHERE kitten.kitten_id = kitten_id;
    COMMIT;
END$$
DELIMITER ;

-- Assigns a kitten to a foster parent
DELIMITER $$ 
CREATE PROCEDURE ASSIGN_PARENT(
	IN kitten_id INT,
    IN parent_id INT
)
BEGIN
	UPDATE kitten SET foster_parent_id = parent_id WHERE kitten_id = kitten_id;
    UPDATE kitten SET in_shelter = 0 WHERE kitten_id = kitten_id;
    UPDATE kitten SET in_foster = 1 WHERE kitten_id = kitten_id;
    COMMIT;
END$$
DELIMITER ;

-- Update a kitten's name
DELIMITER $$ 
CREATE PROCEDURE UPDATE_KNAME(
	IN kitten_id INT,
    IN new_name VARCHAR(255)
)
BEGIN
	UPDATE kitten SET kitten_name = new_name WHERE kitten_id = kitten_id;
    COMMIT;
END$$
DELIMITER ;

-- Update a kitten's name
DELIMITER $$ 
CREATE PROCEDURE UPDATE_KAGE(
	IN kitten_id INT,
    IN new_age INT
)
BEGIN
	UPDATE kitten SET approx_age = new_age WHERE kitten.kitten_id = kitten_id;
    COMMIT;
END$$
DELIMITER ;

-- Delete a kitten
DELIMITER $$ 
CREATE PROCEDURE DELETE_KITTEN(
	IN kitten_id INT
)
BEGIN
	DELETE FROM kitten WHERE kitten.kitten_id = kitten_id;
    COMMIT;
END$$
DELIMITER ;

-- Delete a foster parent
DELIMITER $$ 
CREATE PROCEDURE DELETE_PARENT(
	IN parent_id INT
)
BEGIN
	DELETE FROM foster_parent WHERE foster_parent_id = parent_id;
    COMMIT;
END$$
DELIMITER ;