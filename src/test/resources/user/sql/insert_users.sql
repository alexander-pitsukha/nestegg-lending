insert into users (id,create_date,modify_date,device_token,disabled,nickname,phone_number,push_active,automatic_debiting,verified_phone_code,avatar_id)
values (0,'2021-04-08 22:29:31.410267',null,'admin',false,null,null,false,false,null,null);
insert into users (id,create_date,modify_date,device_token,disabled,nickname,phone_number,push_active,automatic_debiting,verified_phone_code,avatar_id)
values (1,'2021-04-08 23:29:31.410267',null,'qwertyuiop1234567890',false,null,null,false,false,null,null);
insert into users (id, create_date, modify_date, automatic_debiting, avatar_id, device_token, disabled, nickname, phone_number, push_active, verified_phone_code)
values (2, '2021-04-14 23:54:46.075', null, false, null, 'asdfghjkl1234567890', false, null, '+375297841842', false, '1842');
alter sequence users_id_seq restart with 10000;
