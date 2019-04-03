CREATE TABLE IF NOT EXISTS application_user (
  user_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  username VARCHAR(32) NOT NULL,
  password VARCHAR(32) NOT NULL,
  permission ENUM('USER', 'ADMIN') NOT NULL,
  status ENUM('ALLOWED', 'BANNED') NOT NULL,
  points INT NOT NULL DEFAULT 0,
  PRIMARY KEY (user_id),
  UNIQUE INDEX username_UNIQUE (username ASC),
  UNIQUE INDEX password_UNIQUE (password ASC)
 );

CREATE TABLE IF NOT EXISTS question (
  question_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  author_id INT UNSIGNED NOT NULL,
  title VARCHAR(32) NOT NULL,
  text TEXT NOT NULL,
  creation_date DATE NOT NULL,
  score INT NOT NULL DEFAULT 0,
  PRIMARY KEY (question_id),
  INDEX author_id_idx (author_id ASC),
  CONSTRAINT author_id
    FOREIGN KEY (author_id)
    REFERENCES application_user (user_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS answer (
  answer_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  author_id_fk INT UNSIGNED NOT NULL,
  question_id_fk INT UNSIGNED NOT NULL,
  text TEXT NOT NULL,
  creation_date DATE NOT NULL,
  score INT NOT NULL DEFAULT 0,
  PRIMARY KEY (answer_id),
  INDEX author_id_fk_idx (author_id_fk ASC),
  INDEX question_id_fk_idx (question_id_fk ASC),
  CONSTRAINT author_id_fk
    FOREIGN KEY (author_id_fk)
    REFERENCES application_user (user_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT question_id_fk
    FOREIGN KEY (question_id_fk)
    REFERENCES question (question_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS tag (
  tag_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  title VARCHAR(32) NOT NULL,
  PRIMARY KEY (tag_id),
  UNIQUE INDEX title_UNIQUE (title ASC)
);


CREATE TABLE IF NOT EXISTS question_tags (
  question_id_fk1 INT UNSIGNED NOT NULL AUTO_INCREMENT,
  tag_id_fk INT UNSIGNED NOT NULL,
  PRIMARY KEY (question_id_fk1, tag_id_fk),
  INDEX tag_id_fk_idx (tag_id_fk ASC),
  CONSTRAINT question_id_fk1
    FOREIGN KEY (question_id_fk1)
    REFERENCES question (question_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT tag_id_fk
    FOREIGN KEY (tag_id_fk)
    REFERENCES tag (tag_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS vote_question (
  vote_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_gives_id INT UNSIGNED NOT NULL,
  user_gets_id INT UNSIGNED NOT NULL,
  question_voted_id INT UNSIGNED NOT NULL,
  vote_type TINYINT NOT NULL,
  PRIMARY KEY (vote_id),
  INDEX user_gives_id_idx (user_gives_id ASC),
  INDEX user_gets_id_idx (user_gets_id ASC),
  INDEX question_voted_id_idx (question_voted_id ASC),
  CONSTRAINT user_gives_id
    FOREIGN KEY (user_gives_id)
    REFERENCES application_user (user_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT user_gets_id
    FOREIGN KEY (user_gets_id)
    REFERENCES application_user (user_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT question_voted_id
    FOREIGN KEY (question_voted_id)
    REFERENCES question (question_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);



CREATE TABLE IF NOT EXISTS vote_answer (
  vote_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_gives_answer_id INT UNSIGNED NOT NULL,
  user_gets_answer_id INT UNSIGNED NOT NULL,
  answer_voted_id INT UNSIGNED NOT NULL,
  vote_type TINYINT NOT NULL,
  PRIMARY KEY (vote_id),
  INDEX user_gives_answer_id_idx (user_gives_answer_id ASC),
  INDEX user_gets_answer_id_idx (user_gets_answer_id ASC),
  INDEX answer_voted_id_idx (answer_voted_id ASC),
  CONSTRAINT user_gives_answer_id
    FOREIGN KEY (user_gives_answer_id)
    REFERENCES application_user (user_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT user_gets_answer_id
    FOREIGN KEY (user_gets_answer_id)
    REFERENCES application_user (user_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT answer_voted_id
    FOREIGN KEY (answer_voted_id)
    REFERENCES answer (answer_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);