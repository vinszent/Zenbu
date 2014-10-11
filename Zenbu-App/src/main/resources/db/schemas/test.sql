CREATE TABLE IF NOT EXISTS Test
(
  id INT NOT NULL,
  episode INT NOT NULL,
  PRIMARY KEY (id, episode)
);

CREATE TABLE IF NOT EXISTS Test1
(
  id INT NOT NULL,
  episode INT NOT NULL,
  subgroup VARCHAR(80) NOT NULL,
  FOREIGN KEY (id, episode) REFERENCES Test(id, episode),
  PRIMARY KEY (id, episode, subgroup)
);