DROP DATABASE IF EXISTS jets_projects_chatzone_db;

CREATE DATABASE jets_projects_chatzone_db
CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;

USE jets_projects_chatzone_db;

CREATE TABLE AdminUser
(
    user_ID INT AUTO_INCREMENT PRIMARY KEY,
    display_name VARCHAR(20),
    password VARCHAR(255) NOT NULL
);

CREATE TABLE Country
(
    country_name VARCHAR(50) PRIMARY KEY
);

CREATE TABLE NormalUser
(
    user_ID INT AUTO_INCREMENT PRIMARY KEY,
    display_name VARCHAR(20) NOT NULL,
    phone_number VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    pic MEDIUMBLOB NULL DEFAULT NULL,
    password VARCHAR(255) NOT NULL,
    gender ENUM('MALE', 'FEMALE'),
    country VARCHAR(50) NOT NULL,
    birth_date DATE NULL DEFAULT NULL,
    created_at DATETIME NOT NULL
        DEFAULT CURRENT_TIMESTAMP,
    `status` ENUM('AVAILABLE', 'BUSY', 'AWAY', 'OFFLINE') 
		NOT NULL DEFAULT 'OFFLINE',
    bio TEXT,
    is_admin_created BOOLEAN DEFAULT FALSE,
    is_password_valid BOOLEAN DEFAULT TRUE,
    CONSTRAINT `normal_user_country`
        FOREIGN KEY (country)
        REFERENCES Country(country_name)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE INDEX normal_user_idx_phone_number ON NormalUser(phone_number);
CREATE INDEX normal_user_idx_email ON NormalUser(email);
CREATE INDEX normal_user_idx_status ON NormalUser(status);

CREATE TABLE CONTACT 
(
    first_ID INT,
    second_ID INT,
    category ENUM('FAMILY', 'WORK', 'FRIENDS', 'OTHER') NOT NULL,
    PRIMARY KEY(first_ID, second_ID),
    CONSTRAINT `contact_user1_ID`
        FOREIGN KEY (first_ID)
        REFERENCES NormalUser(user_ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT `contact_user2_ID`
        FOREIGN KEY (second_ID)
        REFERENCES NormalUser(user_ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE ContactInvitation
(
    invitation_ID INT AUTO_INCREMENT PRIMARY KEY,
    sender_ID INT NOT NULL,
    receiver_ID INT NOT NULL,
	category ENUM('FAMILY', 'WORK', 'FRIENDS', 'OTHER') NOT NULL,
    sent_at DATETIME NOT NULL 
        DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `contact_invitation_sender_ID`
        FOREIGN KEY(sender_ID)
        REFERENCES NormalUser(user_ID)
        ON UPDATE CASCADE   
        ON DELETE CASCADE,
    CONSTRAINT `contact_invitation_receiver_ID`
        FOREIGN KEY(receiver_ID)
        REFERENCES NormalUser(user_ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE ContactMessage
(
    message_ID INT AUTO_INCREMENT PRIMARY KEY,
    sender_ID INT NOT NULL,
    receiver_ID INT NOT NULL,
    sent_at DATETIME NOT NULL
        DEFAULT CURRENT_TIMESTAMP,
    content TEXT,
    is_read BOOLEAN DEFAULT FALSE,
    contains_file BOOLEAN DEFAULT FALSE,
    message_file MEDIUMBLOB NULL DEFAULT NULL,
    CONSTRAINT `contact_message_sender_ID`
        FOREIGN KEY (sender_ID)
        REFERENCES CONTACT(first_ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT `contact_message_receiver_ID`
        FOREIGN KEY (receiver_ID)
        REFERENCES CONTACT(second_ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE UsersGroup
(
    group_ID INT AUTO_INCREMENT PRIMARY KEY,
    group_name VARCHAR(20) NOT NULL, 
    group_desc VARCHAR(50),
    group_admin_ID INT NOT NULL,
    pic MEDIUMBLOB NULL DEFAULT NULL,
    created_at DATETIME NOT NULL
        DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `users_group_admin_ID`
        FOREIGN KEY (group_admin_ID)
        REFERENCES NormalUser(user_ID)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE UsersGroupMember
(
    group_ID INT NOT NULL,
    member_ID INT NOT NULL,
    PRIMARY KEY (group_ID, member_ID),
    CONSTRAINT `users_group_member_group_ID`
        FOREIGN KEY (group_ID)
        REFERENCES UsersGroup(group_ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT `users_group_member_member_ID`
        FOREIGN KEY (member_ID)
        REFERENCES NormalUser(user_ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE UsersGroupMessage
(
    message_ID INT AUTO_INCREMENT PRIMARY KEY,
	group_ID INT NOT NULL,
    sender_ID INT NOT NULL,
    sent_at DATETIME NOT NULL 
        DEFAULT CURRENT_TIMESTAMP,
    content TEXT,
    contains_file BOOLEAN DEFAULT FALSE,
    message_file MEDIUMBLOB NULL DEFAULT NULL,
    CONSTRAINT `users_group_message_sender_ID`
        FOREIGN KEY (sender_ID)
        REFERENCES UsersGroupMember(member_ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT `contact_message_group_ID`
        FOREIGN KEY (group_ID)
        REFERENCES UsersGroup(group_ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE Notification
(
    notification_ID INT AUTO_INCREMENT PRIMARY KEY,
    user_ID INT NOT NULL,
    `type` ENUM("STATUS_UPDATE", "CONTACT_INVITATION", "NONE") NOT NULL,
    content TEXT NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    sent_at DATETIME NOT NULL
        DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `notification_user_ID`
        FOREIGN KEY (user_ID)
        REFERENCES NormalUser(user_ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE Announcement
(
    announcement_ID INT AUTO_INCREMENT PRIMARY KEY,
    header VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    sent_at DATETIME NOT NULL 
        DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE AnnouncementSeen
(
    announcement_ID INT NOT NULL,
    user_ID INT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    CONSTRAINT `announcement_seen_announcement_ID`
        FOREIGN KEY (announcement_ID)
        REFERENCES Announcement(announcement_ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT `announcement_seen_user_ID`
        FOREIGN KEY (user_ID)
        REFERENCES NormalUser(user_ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

INSERT INTO Country (country_name) VALUES 
('ARUBA'),
('AFGHANISTAN'),
('ANGOLA'),
('ANGUILLA'),
('ALBANIA'),
('ANDORRA'),
('NETHERLANDS_ANTILLES'),
('UNITED_ARAB_EMIRATES'),
('ARGENTINA'),
('ARMENIA'),
('AMERICAN_SAMOA'),
('ANTARCTICA'),
('FRENCH_SOUTHERN_TERRITORIES'),
('ANTIGUA_AND_BARBUDA'),
('AUSTRALIA'),
('AUSTRIA'),
('AZERBAIJAN'),
('BURUNDI'),
('BELGIUM'),
('BENIN'),
('BURKINA_FASO'),
('BANGLADESH'),
('BULGARIA'),
('BAHRAIN'),
('BAHAMAS'),
('BOSNIA_AND_HERZEGOVINA'),
('BELARUS'),
('BELIZE'),
('BERMUDA'),
('BOLIVIA'),
('BRAZIL'),
('BARBADOS'),
('BRUNEI'),
('BHUTAN'),
('BOUVET_ISLAND'),
('BOTSWANA'),
('CENTRAL_AFRICAN_REPUBLIC'),
('CANADA'),
('COCOS_KEELING_ISLANDS'),
('SWITZERLAND'),
('CHILE'),
('CHINA'),
('COTE_D_IVOIRE'),
('CAMEROON'),
('CONGO_DEMOCRATIC_REPUBLIC'),
('CONGO'),
('COOK_ISLANDS'),
('COLOMBIA'),
('COMOROS'),
('CAPE_VERDE'),
('COSTA_RICA'),
('CUBA'),
('CHRISTMAS_ISLAND'),
('CAYMAN_ISLANDS'),
('CYPRUS'),
('CZECH_REPUBLIC'),
('GERMANY'),
('DJIBOUTI'),
('DOMINICA'),
('DENMARK'),
('DOMINICAN_REPUBLIC'),
('ALGERIA'),
('ECUADOR'),
('EGYPT'),
('ERITREA'),
('WESTERN_SAHARA'),
('SPAIN'),
('ESTONIA'),
('ETHIOPIA'),
('FINLAND'),
('FIJI_ISLANDS'),
('FALKLAND_ISLANDS'),
('FRANCE'),
('FAROE_ISLANDS'),
('MICRONESIA_FEDERATED_STATES'),
('GABON'),
('UNITED_KINGDOM'),
('GEORGIA'),
('GHANA'),
('GIBRALTAR'),
('GUINEA'),
('GUADELOUPE'),
('GAMBIA'),
('GUINEA_BISSAU'),
('EQUATORIAL_GUINEA'),
('GREECE'),
('GRENADA'),
('GREENLAND'),
('GUATEMALA'),
('FRENCH_GUIANA'),
('GUAM'),
('GUYANA'),
('HONG_KONG'),
('HEARD_ISLAND_AND_MCDONALD_ISLANDS'),
('HONDURAS'),
('CROATIA'),
('HAITI'),
('HUNGARY'),
('INDONESIA'),
('INDIA'),
('BRITISH_INDIAN_OCEAN_TERRITORY'),
('IRELAND'),
('IRAN'),
('IRAQ'),
('ICELAND'),
('ITALY'),
('JAMAICA'),
('JORDAN'),
('JAPAN'),
('KAZAKHSTAN'),
('KENYA'),
('KYRGYZSTAN'),
('CAMBODIA'),
('KIRIBATI'),
('SAINT_KITTS_AND_NEVIS'),
('SOUTH_KOREA'),
('KUWAIT'),
('LAOS'),
('LEBANON'),
('LIBERIA'),
('LIBYAN_ARAB_JAMAHIRIYA'),
('SAINT_LUCIA'),
('LIECHTENSTEIN'),
('SRI_LANKA'),
('LESOTHO'),
('LITHUANIA'),
('LUXEMBOURG'),
('LATVIA'),
('MACAO'),
('MOROCCO'),
('MONACO'),
('MOLDOVA'),
('MADAGASCAR'),
('MALDIVES'),
('MEXICO'),
('MARSHALL_ISLANDS'),
('MACEDONIA'),
('MALI'),
('MALTA'),
('MYANMAR'),
('MONGOLIA'),
('NORTHERN_MARIANA_ISLANDS'),
('MOZAMBIQUE'),
('MAURITANIA'),
('MONTSERRAT'),
('MARTINIQUE'),
('MAURITIUS'),
('MALAWI'),
('MALAYSIA'),
('MAYOTTE'),
('NAMIBIA'),
('NEW_CALEDONIA'),
('NIGER'),
('NORFOLK_ISLAND'),
('NIGERIA'),
('NICARAGUA'),
('NIUE'),
('NETHERLANDS'),
('NORWAY'),
('NEPAL'),
('NAURU'),
('NEW_ZEALAND'),
('OMAN'),
('PAKISTAN'),
('PANAMA'),
('PITCAIRN'),
('PERU'),
('PHILIPPINES'),
('PALAU'),
('PAPUA_NEW_GUINEA'),
('POLAND'),
('PUERTO_RICO'),
('NORTH_KOREA'),
('PORTUGAL'),
('PARAGUAY'),
('PALESTINE'),
('FRENCH_POLYNESIA'),
('QATAR'),
('REUNION'),
('ROMANIA'),
('RUSSIAN_FEDERATION'),
('RWANDA'),
('SAUDI_ARABIA'),
('SUDAN'),
('SENEGAL'),
('SINGAPORE'),
('SOUTH_GEORGIA_AND_SOUTH_SANDWICH_ISLANDS'),
('SAINT_HELENA'),
('SVALBARD_AND_JAN_MAYEN'),
('SOLOMON_ISLANDS'),
('SIERRA_LEONE'),
('EL_SALVADOR'),
('SAN_MARINO'),
('SOMALIA'),
('SAINT_PIERRE_AND_MIQUELON'),
('SAO_TOME_AND_PRINCIPE'),
('SURINAME'),
('SLOVAKIA'),
('SLOVENIA'),
('SWEDEN'),
('SWAZILAND'),
('SEYCHELLES'),
('SYRIA'),
('TURKS_AND_CAICOS_ISLANDS'),
('CHAD'),
('TOGO'),
('THAILAND'),
('TAJIKISTAN'),
('TOKELAU'),
('TURKMENISTAN'),
('EAST_TIMOR'),
('TONGA'),
('TRINIDAD_AND_TOBAGO'),
('TUNISIA'),
('TURKEY'),
('TUVALU'),
('TAIWAN'),
('TANZANIA'),
('UGANDA'),
('UKRAINE'),
('UNITED_STATES_MINOR_OUTLYING_ISLANDS'),
('URUGUAY'),
('UNITED_STATES'),
('UZBEKISTAN'),
('HOLY_SEE_VATICAN_CITY_STATE'),
('SAINT_VINCENT_AND_THE_GRENADINES'),
('VENEZUELA'),
('VIRGIN_ISLANDS_BRITISH'),
('VIRGIN_ISLANDS_U_S'),
('VIETNAM'),
('VANUATU'),
('WALLIS_AND_FUTUNA'),
('SAMOA'),
('YEMEN'),
('YUGOSLAVIA'),
('SOUTH_AFRICA'),
('ZAMBIA'),
('ZIMBABWE');
