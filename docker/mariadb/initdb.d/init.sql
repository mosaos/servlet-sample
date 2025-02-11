CREATE TABLE `users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `uuid` BINARY(16) NOT NULL DEFAULT UNHEX(REPLACE(UUID(),'-','')),
  `user_id` VARCHAR(100) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `user_name` VARCHAR(100),
  `email` VARCHAR(100) NOT NULL UNIQUE,
  `is_admin` TINYINT(1) DEFAULT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX users_uuid_idx(uuid)
);

INSERT INTO users (user_id, password, user_name, email, is_admin) 
  VALUES ('admin', '$2y$12$TKubBzAs4MBuEU.5DwXiZe9.Z/9chC49dYU2/yqZy1xvdk0Hx0yk6', 
          'System Administrator', 'admin@localhost', '1');

CREATE TABLE `notes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `uuid` BINARY(16) NOT NULL DEFAULT UNHEX(REPLACE(UUID(),'-','')),
  `user_id` INT NOT NULL,
  `title` VARCHAR(255) NOT NULL,
  `content` TEXT NOT NULL,
  `tags` VARCHAR(255) NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX notes_uuid_idx(uuid)
);
