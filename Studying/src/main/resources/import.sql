INSERT INTO authority(authority) values ('ADMIN');
INSERT INTO authority(authority) values ('USER');

INSERT INTO user(username, password, account_non_expired, account_non_locked, credentials_non_expired, enabled) values('18366116016', '$2a$10$I2o3bRLfBIlFCYyPCXlBoeJxyHRqslzFLww1dfXZ0zRFBjfL7B0vm',1,1,1,1);

INSERT INTO USER_AUTH(users_username, authorities_authority) values('18366116016', 'USER')