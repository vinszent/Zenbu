CREATE TABLE IF NOT EXISTS Anime
(
  id INT,
  malId INT,
  hbId INT,
  alId INT,
  adbId INT,
  description VARCHAR(5000),
  type VARCHAR(20),
  totalUnits INT,
  malImageUrl VARCHAR(200),
  hbImageUrl VARCHAR(200),
  alImageUrl VARCHAR(200),
  adbImageUrl VARCHAR(200),
  PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS AnimeUserData
(
  id INT,
  status VARCHAR(20),
  score DOUBLE,
  progress INT,
  lastChanged DATETIME,
  PRIMARY KEY(id),
  FOREIGN KEY(id) REFERENCES Anime(id)
);

CREATE TABLE IF NOT EXISTS Episode
(
  id INT,
  episode INT,
  crc32 VARCHAR(8),
  filepath VARCHAR(200),
  PRIMARY KEY (id, episode),
  FOREIGN KEY(id) REFERENCES AnimeUserData(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Title
(
  id INT,
  title VARCHAR(200),
  selected BOOLEAN,
  PRIMARY KEY (id, title),
  FOREIGN KEY(id) REFERENCES Anime(id)
);

CREATE TABLE IF NOT EXISTS Genre
(
  id INT,
  genre VARCHAR(50),
  PRIMARY KEY (id, genre),
  FOREIGN KEY(id) REFERENCES Anime(id)
);

CREATE TABLE IF NOT EXISTS VideoFlag
(
  id INT,
  episode INT,
  videoFlag VARCHAR(50),
  PRIMARY KEY(id, episode, videoFlag),
  FOREIGN KEY(id, episode) REFERENCES Episode(id, episode) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS AudioFlag
(
  id INT,
  episode INT,
  audioFlag VARCHAR(50),
  PRIMARY KEY(id, episode, audioFlag),
  FOREIGN KEY(id, episode) REFERENCES Episode(id, episode) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Subgroup
(
  id INT,
  episode INT,
  subgroup VARCHAR(100),
  PRIMARY KEY(id, episode, subgroup),
  FOREIGN KEY(id, episode) REFERENCES Episode(id, episode) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS HistoryItem
(
  id INT AUTO_INCREMENT,
  animeId INT,
  action VARCHAR(10),
  parameter VARCHAR(10),
  oldValue VARCHAR(10),
  newValue VARCHAR(10),
  created DATE,
  PRIMARY KEY(id),
  FOREIGN KEY(animeId) REFERENCES Anime(id)
);

CREATE TABLE IF NOT EXISTS Setting
(
  setting VARCHAR(100),
  value VARCHAR(500),
  PRIMARY KEY(setting)
);
