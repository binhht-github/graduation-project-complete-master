create database graduation_pro_1;

use graduation_pro_1;

create table roles
(
	id varchar(255) primary key,
    name nvarchar(255) not null 
);

create table accounts
(
	id bigint auto_increment primary key,
    password nvarchar(255) not null,
    email varchar(255) not null unique,
    telephone varchar(13) not null unique,
    update_at timestamp,
    role_id varchar(255) not null,
    delete_at bit default 1,
    constraint FK_accounts_roles foreign key(role_id) references roles(id)
);

create table verifycation_token
(
	id bigint auto_increment primary key,
    account_id bigint not null,
    token nvarchar(255) not null,
    type varchar(30) not null,
    create_at timestamp not null,
    expires_at timestamp not null,
    cerified timestamp,
	constraint FK_verifycation_token_account foreign key(account_id) references accounts(id)
);

create table posts
(
	id bigint auto_increment primary key,
    title nvarchar(2000) not null,
    content nvarchar(2000) not null,
    image varchar(255) not null,
    account_id bigint not null,
    delete_at bit,
    constraint FK_post_account foreign key(account_id) references accounts(id)
);

create table comments
(
	id bigint auto_increment primary key,
    content nvarchar(2000) not null,
    image varchar(255) not null,
    create_at timestamp,
    post_id bigint not null,
    account_id bigint not null,
    delete_at bit,
    constraint FK_comments_accounts foreign key(account_id) references accounts(id),
    constraint FK_comments_posts foreign key(post_id) references posts(id)
);

 create table provinces
 (
	 id varchar(10) not null primary key,
	 name varchar(255) not null,
	 type varchar(30) not null
 );
 
  create table districts
 (
	 id varchar(10) not null primary key,
	 name nvarchar(255) not null,
	 type varchar(30) not null,
	 province_id varchar(10) not null,
     constraint FK_districts_provinces foreign key(province_id) references provinces(id)
 );
 
 create table communes
(
	id varchar(10) not null primary key,
	name varchar(255) not null,
	type varchar(255) not null,
    district_id varchar(10) not null,
	constraint FK_communes_districts foreign key(district_id) references districts(id)
);

create table customer_profile
(
	id bigint auto_increment primary key,
    account_id bigint not null,
    image varchar(255) not null,
    fullname nvarchar(255) not null,
    birthday datetime not null,
    gender bit not null,
    communes_id varchar(10) not null,
    telephone varchar(13) not null,
    story nvarchar(255),
    create_at timestamp,
    update_at timestamp,
    delete_at bit,
    constraint FK_customer_profile_accounts foreign key(account_id) references accounts(id),
    constraint FK_customer_profile_communes foreign key(communes_id) references communes(id)
);

create table e_wallet
(
	id bigint auto_increment primary key,
    balance double not null,
    customer_id bigint not null,
    constraint FK_e_wallet_customer_profile foreign key(customer_id) references customer_profile(id)
);

create table history_wallet
(
	id bigint auto_increment primary key,
    create_at timestamp not null,
    wallet_id bigint not null,
    description nvarchar(255) not null,
    constraint FK_history_wallet_e_wallet foreign key(wallet_id) references e_wallet(id)
);

create table likes
(
	id bigint not null primary key auto_increment,
	create_at timestamp not null,
    post_id bigint not null,
    account_id bigint not null,
    constraint FK_likes_posts foreign key(post_id) references posts(id),
    constraint FK_likes_accounts foreign key(account_id) references accounts(id)
);

create table dentist_profile
(
	id bigint not null primary key auto_increment,
    account_id bigint not null,
	image varchar(255) not null,
	cccd varchar(15) not null,
	full_name nvarchar(255) not null,
	birthday timestamp not null,
	gender bit not null,
	communes_id varchar(10) not null,
	telephone varchar(13) not null,
	exp nvarchar(255) not null,
	create_at timestamp not null,
	update_at timestamp,
	delete_at bit,
    constraint FK_dentist_profile_communes foreign key(communes_id) references communes(id),
    constraint FK_dentist_profile_accounts foreign key(account_id) references accounts(id)
);

 create table schedule_time
 (
	 id bigint not null primary key auto_increment,
	 day_of_week timestamp not null,
	 start time not null,
	 end time not null,
     dentist_id bigint not null,
	 delete_at bit,
     constraint FK_schedule_time_dentist_profile foreign key(dentist_id) references dentist_profile(id)
 );

create table booking
(
	id bigint auto_increment primary key,
    dentist_id bigint not null,
    customer_id bigint not null,
    booking_date timestamp not null,
    description nvarchar(255) not null,
    status int not null,
    schedule_time_id bigint not null,
    constraint FK_booking_customer_profile foreign key(customer_id) references customer_profile(id),
    constraint FK_booking_dentist_profile foreign key(dentist_id) references dentist_profile(id),
    constraint FK_booking_schedule_time foreign key(schedule_time_id) references schedule_time(id)
);


 create table voucher
 (
	 id varchar(255) not null primary key,
	 content nvarchar(255) not null,
	 image varchar(255) not null,
	 sale double not null,
	 start timestamp not null,
	 end timestamp not null,
	 create_at timestamp not null,
	 delete_at bit
 );

create table service
(
	id bigint auto_increment primary key,
    content nvarchar(2000) not null,
    image varchar(255) not null,
    name nvarchar(255) not null,
    price double not null,
    create_at timestamp not null,
    delete_at bit
);

create table booking_detail
(
	id bigint primary key auto_increment,
    booking_id bigint not null,
    service_id bigint not null,
    voucher_id varchar(255), -- có hoặc không
	price double not null,
	constraint FK_booking_detail_service foreign key(service_id) references service(id),
	constraint FK_booking_detail_voucher foreign key(voucher_id) references voucher(id),
	constraint FK_booking_detail_booking foreign key(booking_id) references booking(id)
);

insert into roles (id,name) values('ADMIN','ROLE_ADMIN');
insert into roles (id,name) values('CUSTOMER','ROLE_CUSTOMER');
insert into roles (id,name) values('DENTIST','ROLE_DENTIST');
insert into roles (id,name) values('RECEPTIONIST','ROLE_RECEPTIONIST');


DELIMITER $$
CREATE FUNCTION getCountBooking(id bigint) RETURNS int
    DETERMINISTIC
BEGIN
	declare tong integer;
	SELECT COUNT(bk.ID) INTO tong
    FROM BOOKING bk
	WHERE bK.CUSTOMER_ID = id and bk.status =1;
RETURN tong;
END$$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION getRoelByAccount(id bigint) RETURNS nvarchar(20)
    DETERMINISTIC
BEGIN
	declare role nvarchar(20);
	select acc.role_id into role
    from accounts acc 
	WHERE acc.id = id and acc.delete_at = 0;
RETURN roel;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE getAccountLimit(a int, b int)
BEGIN
   SELECT *  FROM accounts
   limit a,b;
END $$
DELIMITER ;
drop PROCEDURE getAccountLimit;
call getAccountLimit(0,2);