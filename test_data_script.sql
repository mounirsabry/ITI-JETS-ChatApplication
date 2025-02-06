USE jets_projects_chatzone_db;

INSERT INTO AdminUser VALUES
(0, "admin", "admin");

INSERT INTO NormalUser 
(user_ID, display_name, phone_number, email, pic, password,
gender, country, birth_date, created_at, status, bio, is_admin_created,
is_password_valid) VALUES
(0, 'Mounir', '1111', 'mounir@email.com', NULL, 'mounir', 'MALE',
'EGYPT', NULL, DEFAULT, 'OFFLINE', NULL, FALSE, TRUE),
(0, 'Hashim', '2222', 'hashim@email.com', NULL, 'hashim', 'MALE',
'EGYPT', NULL, DEFAULT, 'OFFLINE', NULL, FALSE, TRUE),
(0, 'Salma', '3333', 'salma@email.com', NULL, 'salma', 'FEMALE',
'EGYPT', NULL, DEFAULT, 'OFFLINE', NULL, FALSE, TRUE),
(0, 'Mariam', '4444', 'mariam@email.com', NULL, 'mariam', 'FEMALE',
'EGYPT', NULL, DEFAULT, 'OFFLINE', NULL, FALSE, TRUE),
(0, 'User1', '5555', 'user1@email.com', NULL, 'user1', 'MALE',
'EGYPT', NULL, DEFAULT, 'OFFLINE', NULL, FALSE, TRUE),
(0, 'User2', '6666', 'user2@email.com', NULL, 'user2', 'FEMALE',
'EGYPT', NULL, DEFAULT, 'OFFLINE', NULL, FALSE, TRUE),
(0, 'User3', '7777', 'user3@email.com', NULL, 'user3', 'MALE',
'EGYPT', NULL, DEFAULT, 'OFFLINE', NULL, FALSE, TRUE),
(0, 'User4', '8888', 'user4@email.com', NULL, 'user4', 'MALE',
'EGYPT', NULL, DEFAULT, 'OFFLINE', NULL, FALSE, TRUE),
(0, 'User5', '9999', 'user5@email.com', NULL, 'user5', 'MALE',
'EGYPT', NULL, DEFAULT, 'OFFLINE', NULL, FALSE, TRUE);

# Friendship is two ways relationship, if user1 is in contact list of user2,
# then there must be two entries in the Contact table to represent this.
INSERT INTO CONTACT VALUES
(1, 2, 'OTHER'),
(2, 1, 'OTHER'),
(3, 4, 'OTHER'),
(4, 3, 'OTHER'),
(5, 6, 'OTHER'),
(6, 5, 'OTHER'),
(5, 7, 'OTHER'),
(7, 5, 'OTHER');

INSERT INTO ContactInvitation VALUES
(0, 1, 3, DEFAULT),
(0, 1, 4, DEFAULT),
(0, 2, 4, DEFAULT),
(0, 6, 7, DEFAULT);

INSERT INTO ContactMessage VALUES
(0, 1, 2, DEFAULT, 'Hello Hashim', FALSE, FALSE, NULL),
(0, 2, 1, DEFAULT, 'Hi Mounir, How are You?', FALSE, FALSE, NULL),
(0, 3, 4, DEFAULT, 'Hi Mariam', FALSE, FALSE, NULL),
(0, 4, 3, DEFAULT, 'Hello Salma', FALSE, FALSE, NULL);

INSERT INTO UsersGroup VALUES
(0, 'Chat-Zone-Dev-Team', 'Some basic description for the dev group.',
1, NULL, DEFAULT);

INSERT INTO UsersGroupMember VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4);

INSERT INTO UsersGroupMessage VALUES
(0, 1, 1, DEFAULT, 'Hello Everyone, This is Mounir.', FALSE, NULL),
(0, 1, 2, DEFAULT, 'Hello Everyone, This is Hashim.', FALSE, NULL),
(0, 1, 3, DEFAULT, 'Hello Everyone, This is Salma.', FALSE, NULL),
(0, 1, 4, DEFAULT, 'Hello Everyone, This is Mariam.', FALSE, NULL);

INSERT INTO Notification VALUES
(0, 1, 'STATUS_UPDATE', 'Hashim has became Online.', FALSE, DEFAULT),
(0, 2, 'STATUS_UPDATE', 'Mounir has became Online.', FALSE, DEFAULT),
(0, 3, 'STATUS_UPDATE', 'Mariam has became Online.', FALSE, DEFAULT),
(0, 4, 'STATUS_UPDATE', 'Salma has became Online.', FALSE, DEFAULT);

INSERT INTO Announcement VALUES
(0, 'First Test Announcement', 
'This is an announcement created to test the announcement functionality.', DEFAULT);

INSERT INTO AnnouncementSeen VALUES
(1, 1, FALSE),
(1, 2, FALSE),
(1, 3, FALSE),
(1, 4, FALSE),
(1, 5, FALSE),
(1, 6, FALSE),
(1, 7, FALSE),
(1, 8, FALSE),
(1, 9, FALSE);
