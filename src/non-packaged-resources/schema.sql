create materialized view github.user_suppress as select login,count(*) from github.repos_json group by 1 having count(*) > 1000;

create materialized view github.user_jsonb as
SELECT id, login, json::jsonb AS json
FROM github.user_json
WHERE login not in (select login from github.user_suppress);

create materialized view github.user as
select
    id,
    login,
    json->>'name'::text as name,
    json->>'company'::text as company,
    json->>'location'::text as location,
    json->>'email'::text as email,
    json->>'bio'::text as bio,
    json->>'blog'::text as blog,
    (json->>'site_admin'::text)::boolean as site_admin,
    (json->>'public_repos'::text)::int as public_repos,
    (json->>'public_gists'::text)::int as public_gists,
    (json->>'followers'::text)::int as followers,
    (json->>'following'::text)::int as following,
    (json->>'created_at'::text)::timestamp as created_at,
    (json->>'updated_at'::text)::timestamp as updated_at
from github.user_jsonb;

create materialized view github.organization as
select
    id,
    login,
    json->>'name'::text as name,
    json->>'company'::text as company,
    json->>'location'::text as location,
    json->>'email'::text as email,
    json->>'description'::text as description,
    json->>'blog'::text as blog,
    (json->>'has_organization_projects'::text)::boolean as has_organization_projects,
    (json->>'has_repository_projects'::text)::boolean as has_repository_projects,
    (json->>'public_repos'::text)::int as public_repos,
    (json->>'public_gists'::text)::int as public_gists,
    (json->>'followers'::text)::int as followers,
    (json->>'following'::text)::int as following,
    (json->>'created_at'::text)::timestamp as created_at,
    (json->>'updated_at'::text)::timestamp as updated_at
from github.org_jsonb;

create materialized view github.repos_jsonb as
SELECT id, login, name, json::jsonb AS json
FROM github.repos_json
WHERE login not in (select login from github.user_suppress);

create materialized view github.repository as
select
    id,
    name,
    json->>'full_name'::text as full_name,
    (json->>'private'::text)::boolean as is_private,
    json->>'description'::text as description,
    (json->>'fork'::text)::boolean as fork,
    (json->>'created_at'::text)::timestamp as created_at,
    (json->>'updated_at'::text)::timestamp as updated_at,
    (json->>'pushed_at'::text)::timestamp as pushed_at,
    json->>'homepage'::text as homepage,
    (json->>'size'::text)::int as size,
    (json->>'stargazers_count'::text)::int as stargazers_count,
    (json->>'watchers_count'::text)::int as watchers_count,
    json->>'language'::text as language,
    (json->>'has_issues'::text)::boolean as has_issues,
    (json->>'has_projects'::text)::boolean as has_projects,
    (json->>'has_downloads'::text)::boolean as has_downloads,
    (json->>'has_wiki'::text)::boolean as has_wiki,
    (json->>'has_pages'::text)::boolean as has_pages,
    (json->>'forks_count'::text)::int as forks_count,
    (json->>'archived'::text)::boolean as archived,
    (json->>'open_issues_count'::text)::int as open_issues_count,
    json->>'license' as license,
    (json->>'forks'::text)::int as forks,
    (json->>'open_issues'::text)::int as open_issues,
    (json->>'watchers'::text)::int as watchers,
    json->>'default_branch'::text as default_branch
from github.repos_jsonb;

create materialized view github.user_repo as
select github.user.id as user_id,repository_id
from
    github.user
natural join
    (select id as repository_id,substring(full_name from '^[^/]*')as login from github.repository) as foo;

create materialized view org_repo as
select organization.id as organization_id,repository_id
from
    github.organization
natural join
    (select id as repository_id,substring(full_name from '^[^/]*')as login from repository) as foo;

Script to direct load the WaPo JSON data into a binary JSON field:
    sed -e 's/\\/\\\\/g' <$a | psql -c "copy wapo_raw.blog_json(json) from stdin;" trec
    
create materialized view article as
select
    id,
    json->>'id'::text as trec_id,
    json->>'article_url'::text as article_url,
    json->>'title'::text as title,
    json->>'author'::text as author,
    to_timestamp((json->>'published_date'::text)::bigint/1000) as published_date,
    json->>'type'::text as type,
    json->>'source'::text as source
from article_json;
    
create materialized view blog as
select
    id,
    json->>'id'::text as trec_id,
    json->>'article_url'::text as article_url,
    json->>'title'::text as title,
    json->>'author'::text as author,
    to_timestamp((json->>'published_date'::text)::bigint/1000) as published_date,
    json->>'type'::text as type,
    json->>'source'::text as source
from blog_json;

create materialized view author_affiliation as
select
    doi,
    rank,
    affrank, affiliation->>'name' as affiliation
from (
    select doi,rank,t.*
    from
        author
    cross join lateral
        jsonb_array_elements((affiliation::jsonb)::jsonb) with ordinality as t(affiliation,affrank)
    ) as foo
;

