#select * from sftengdb.user u join sftengdb.address on u.address_addressid=addressid

#select username from user u where u.username like 'userb'

#select * from donationStatus

#select * from address a where ((a.addrLine1 like '111') and (a.city like '2'))

#update user set password='newpw' where email='user1@domain.com';

#delete from address where addressid=5;

#delete from user where email='bjones@fakemail.ru';

#select password from user where email='user1@domain.com';

select * from user;
#select * from address;
#select * from donation;# d join address a on d.addressid=a.addressid;
#select * from donation;










