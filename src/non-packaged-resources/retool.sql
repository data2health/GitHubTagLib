alter table org_json add tier int;
alter table user_json add tier int;
alter table repos_json add tier int;

update org_json set tier=1 where exists (select orgid from search_organization where orgid=id);
update user_json set tier=1 where exists (select uid from search_user where uid=id);
update repos_json set tier=1 where exists (select rid from search_repository where rid=id);

update repos_json set tier=2 where login in (select login from user_json where tier=1) and tier is null;
update repos_json set tier=2 where login in (select login from org_json where tier=1) and tier is null;
update user_json set tier=2 where login in (select login from repos_json where tier=1) and tier is null;
update org_json set tier=2 where login in (select login from repos_json where tier=1) and tier is null;

update repos_json set tier=3 where login in (select login from org_json where tier=2) and tier is null;
update repos_json set tier=3 where login in (select login from user_json where tier=2) and tier is null;
update user_json set tier=3 where login in (select login from repos_json where tier=2) and tier is null;
update org_json set tier=3 where login in (select login from repos_json where tier=2) and tier is null;

update org_json set tier=4 where exists (select organization_id from member,user_json where tier is not null and organization_id=org_json.id and user_id=user_json.id) and tier is null;
update user_json set tier=4 where exists (select user_id from member,org_json where tier is not null and organization_id=org_json.id and user_id=user_json.id) and tier is null;

update repos_json as par set tier=5 where exists (select parent.id from parent,repos_json as chi where parent.id=chi.id and parent_id=par.id and chi.tier is not null) and tier is null;

create schema github_attic;
create table github_attic.user_json as select * from user_json where tier is null;
create table github_attic.org_json as select * from org_json where tier is null;
create table github_attic.repos_json as select * from repos_json where tier is null;
create table github_attic.readme as select * from readme natural join repos_json where tier is null;

delete from user_json where tier is null;
delete from org_json where tier is null;
delete from repos_json where tier is null;

delete from readme where not exists (select id from repos_json where readme.id=repos_json.id);

