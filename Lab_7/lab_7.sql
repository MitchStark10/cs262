--
-- This SQL script builds a monopoly database, deleting any pre-existing version.
--
-- @author kvlinden
-- 	extended by Ethan Clark (elc3)
-- @version Summer, 2015
--

-- Drop previous versions of the tables if they they exist, in reverse order of foreign keys.
DROP TABLE IF EXISTS PlayerGame;
DROP TABLE IF EXISTS Properties;
DROP TABLE IF EXISTS Game;
DROP TABLE IF EXISTS Player;

-- Create the schema.
CREATE TABLE Game (
	ID integer PRIMARY KEY, 
	time timestamp
	);

CREATE TABLE Player (
	ID integer PRIMARY KEY, 
	emailAddress varchar(50) NOT NULL,
	name varchar(50),
	cash integer,
	pieceLocation integer
	);

CREATE TABLE PlayerGame (
	gameID integer REFERENCES Game(ID), 
	playerID integer REFERENCES Player(ID),
	score integer
	);

CREATE TABLE Properties (
	playerId integer REFERENCES Player(ID),
	propName varchar(50) PRIMARY KEY,
	gameID integer REFERENCES Game(ID),
	houses integer,
	hotel boolean
	);

-- Allow users to select data from the tables.
GRANT SELECT ON Game TO PUBLIC;
GRANT SELECT ON Player TO PUBLIC;
GRANT SELECT ON PlayerGame TO PUBLIC;
GRANT SELECT ON Properties TO PUBLIC;

-- Add sample records.
INSERT INTO Game VALUES (1, '2006-06-27 08:00:00');
INSERT INTO Game VALUES (2, '2006-06-28 13:20:00');
INSERT INTO Game VALUES (3, '2006-06-29 18:41:00');

INSERT INTO Player(ID, emailAddress, cash, pieceLocation) VALUES (1, 'me@calvin.edu', 1000, 10);
INSERT INTO Player VALUES (2, 'king@gmail.edu', 'The King', 1000, 20);
INSERT INTO Player VALUES (3, 'dog@gmail.edu', 'Dogbreath', 1000, 15);

INSERT INTO PlayerGame VALUES (1, 1, 0.00);
INSERT INTO PlayerGame VALUES (1, 2, 0.00);
INSERT INTO PlayerGame VALUES (1, 3, 2350.00);
INSERT INTO PlayerGame VALUES (2, 1, 1000.00);
INSERT INTO PlayerGame VALUES (2, 2, 0.00);
INSERT INTO PlayerGame VALUES (2, 3, 500.00);
INSERT INTO PlayerGame VALUES (3, 2, 0.00);
INSERT INTO PlayerGame VALUES (3, 3, 5500.00);

INSERT INTO Properties VALUES (1, 'Boardwalk', 1, false, 1);
INSERT INTO Properties VALUES (1, 'Baltic Avenue', 0, false, 1);
INSERT INTO Properties VALUES (2, 'Illinois Avenue', 3, true, 2);
INSERT INTO Properties VALUES (2, 'Kentucky Avenue', 2, false, 2);
INSERT INTO Properties VALUES (3, 'Ventnor Avenue', 1, false, 3);
INSERT INTO Properties VALUES (3, 'Virginia Avenue', 3, true, 3);


