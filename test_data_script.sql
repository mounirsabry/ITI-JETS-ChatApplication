USE jets_projects_chatzone_db;

INSERT INTO AdminUser VALUES
(0, "admin", "admin");

INSERT INTO NormalUser 
(user_ID, display_name, phone_number, email, pic, password,
gender, country, birth_date, created_at, status, bio, is_admin_created,
is_password_valid) VALUES
(0, 'Mounir', '01080', 'mounir@email.com', NULL, '01080', 'MALE',
'EGYPT', NULL, DEFAULT, 'OFFLINE', '', FALSE, TRUE),
(0, 'Hashim', '01090', 'hashim@email.com', NULL, '01090', 'MALE',
'EGYPT', NULL, DEFAULT, 'OFFLINE', '', FALSE, TRUE),
(0, 'Salma', '01070', 'salma@email.com', NULL, '01070', 'FEMALE',
'EGYPT', NULL, DEFAULT, 'OFFLINE', '', FALSE, TRUE),
(0, 'Mariam', '01060', 'mariam@email.com', NULL, '01060', 'FEMALE',
'EGYPT', NULL, DEFAULT, 'OFFLINE', '', FALSE, TRUE),
(0, 'User1', '5555', 'user1@email.com', NULL, 'user1', 'MALE',
'EGYPT', NULL, DEFAULT, 'OFFLINE', '', FALSE, TRUE),
(0, 'User2', '6666', 'user2@email.com', NULL, 'user2', 'FEMALE',
'EGYPT', NULL, DEFAULT, 'OFFLINE', '', FALSE, TRUE),
(0, 'User3', '7777', 'user3@email.com', NULL, 'user3', 'MALE',
'EGYPT', NULL, DEFAULT, 'OFFLINE', '', FALSE, TRUE),
(0, 'User4', '8888', 'user4@email.com', NULL, 'user4', 'MALE',
'EGYPT', NULL, DEFAULT, 'OFFLINE', '', FALSE, TRUE),
(0, 'User5', '9999', 'user5@email.com', NULL, 'user5', 'MALE',
'EGYPT', NULL, DEFAULT, 'OFFLINE', '', FALSE, TRUE);

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
(0, 1, 3, 'OTHER', DEFAULT),
(0, 1, 4, 'OTHER', DEFAULT),
(0, 2, 4, 'OTHER', DEFAULT),
(0, 6, 7, 'OTHER', DEFAULT);

INSERT INTO ContactMessage VALUES
(0, 1, 2, DEFAULT, 'Hello Hashim', FALSE, FALSE, NULL),
(0, 2, 1, DEFAULT, 'Hi Mounir, How are You?', FALSE, FALSE, NULL),
(0, 3, 4, DEFAULT, 'Hi Mariam', FALSE, FALSE, NULL),
(0, 4, 3, DEFAULT, 'Hello Salma', FALSE, FALSE, NULL);

INSERT INTO UsersGroup VALUES
(0, 'Group1', 'Some basic description for the dev group.',
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
(0, 1, 'CONTACT_INVITATION', 'You have sent Hashim a conatct invitaion.', FALSE, DEFAULT),
(0, 2, 'CONTACT_INVITATION', 'Mounir has sent you a contact invitation.', FALSE, DEFAULT),
(0, 1, 'CONTACT_INVITATION', 'Hashim has accepted your contact invitation, Hashim was added to your contacts list.', FALSE, DEFAULT),
(0, 2, 'CONTACT_INVITATION', 'You have accepted Mounir contact invitation, Mounir was added to your contact list.', FALSE, DEFAULT);

INSERT INTO Notification VALUES
(0, 3, 'CONTACT_INVITATION', 'Mariam has sent you a contact invitation.', FALSE, DEFAULT),
(0, 4, 'CONTACT_INVITATION', 'You have sent Salma a contact invitation.', FALSE, DEFAULT),
(0, 3, 'CONTACT_INVITATION', 'You have accepted Mariam contact invitation, Mariam was added to your contact list.', FALSE, DEFAULT),
(0, 4, 'CONTACT_INVITATION', 'Salma has accepted your contact invitation, Salma was added to your contact list.', FALSE, DEFAULT);

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
