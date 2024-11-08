SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS Comment;
DROP TABLE IF EXISTS Post;
DROP TABLE IF EXISTS Board;
DROP TABLE IF EXISTS User;




/* Create Tables */

CREATE TABLE Board
(
	board_id bigint NOT NULL,
	board_title varchar(50),
	PRIMARY KEY (board_id)
);


CREATE TABLE Comment
(
	comment_id bigint NOT NULL,
	post_id bigint NOT NULL,
	user_id bigint NOT NULL,
	content text,
	comment_time timestamp,
	PRIMARY KEY (comment_id)
);


CREATE TABLE Post
(
	post_id bigint NOT NULL,
	user_id bigint NOT NULL,
	board_id bigint NOT NULL,
	post_title varchar(50),
	post_time timestamp,
	update_time timestamp,
	post_like int,
	post_dislike int,
	img_path varchar(50),
	file bigint,
	PRIMARY KEY (post_id)
);


CREATE TABLE User
(
	user_id bigint NOT NULL,
	name varchar(20),
	password varchar(20),
	attendance int,
	PRIMARY KEY (user_id)
);



/* Create Foreign Keys */

ALTER TABLE Post
	ADD FOREIGN KEY (board_id)
	REFERENCES Board (board_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE Comment
	ADD FOREIGN KEY (post_id)
	REFERENCES Post (post_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE Comment
	ADD FOREIGN KEY (user_id)
	REFERENCES User (user_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE Post
	ADD FOREIGN KEY (user_id)
	REFERENCES User (user_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;



