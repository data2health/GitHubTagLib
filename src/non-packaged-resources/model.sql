CREATE TABLE github.organization (
       id INT NOT NULL
     , login TEXT
     , name TEXT
     , company TEXT
     , location TEXT
     , email TEXT
     , description TEXT
     , blog TEXT
     , has_organization_projects BOOLEAN
     , has_repository_projects BOOLEAN
     , public_repos INT
     , public_gists INT
     , followers INT
     , following INT
     , created_at TIMESTAMP
     , updated_at TIMESTAMP
     , PRIMARY KEY (id)
);

CREATE TABLE github.repository (
       id INT NOT NULL
     , name TEXT
     , full_name TEXT
     , is_private BOOLEAN
     , description TEXT
     , fork BOOLEAN
     , created_at TIMESTAMP
     , updated_at TIMESTAMP
     , pushed_at TIMESTAMP
     , homepage TEXT
     , size INT
     , stargazers_count INT
     , watchers_count INT
     , language TEXT
     , has_issues BOOLEAN
     , has_projects BOOLEAN
     , has_downloads BOOLEAN
     , has_wiki BOOLEAN
     , has_pages BOOLEAN
     , forks_count INT
     , archived BOOLEAN
     , open_issues_count INT
     , license TEXT
     , forks INT
     , open_issues INT
     , watchers INT
     , default_branch TEXT
     , PRIMARY KEY (id)
);

CREATE TABLE github.search_term (
       id INT NOT NULL
     , term TEXT
     , PRIMARY KEY (id)
);

CREATE TABLE github.user (
       id INT NOT NULL
     , login TEXT
     , name TEXT
     , company TEXT
     , location TEXT
     , email TEXT
     , bio TEXT
     , blog TEXT
     , site_admin BOOLEAN
     , public_repos INT
     , public_gists INT
     , followers INT
     , following INT
     , created_at TIMESTAMP
     , updated_at TIMESTAMP
     , PRIMARY KEY (id)
);

CREATE TABLE github.member (
       user_id INT NOT NULL
     , organization_id INT NOT NULL
     , PRIMARY KEY (user_id, organization_id)
     , CONSTRAINT FK_member_1 FOREIGN KEY (user_id)
                  REFERENCES github.user (id)
     , CONSTRAINT FK_member_2 FOREIGN KEY (organization_id)
                  REFERENCES github.organization (id)
);

CREATE TABLE github.readme (
       id INT NOT NULL
     , readme TEXT
     , PRIMARY KEY (id)
     , CONSTRAINT FK_readme_1 FOREIGN KEY (id)
                  REFERENCES github.repository (id)
);

CREATE TABLE github.org_repo (
       organization_id INT NOT NULL
     , repository_id INT NOT NULL
     , PRIMARY KEY (organization_id, repository_id)
     , CONSTRAINT FK_org_repo_1 FOREIGN KEY (organization_id)
                  REFERENCES github.organization (id)
     , CONSTRAINT FK_org_repo_2 FOREIGN KEY (repository_id)
                  REFERENCES github.repository (id)
);

CREATE TABLE github.user_repo (
       user_id INT NOT NULL
     , repository_id INT NOT NULL
     , PRIMARY KEY (user_id, repository_id)
     , CONSTRAINT FK_user_repo_1 FOREIGN KEY (user_id)
                  REFERENCES github.user (id)
     , CONSTRAINT FK_user_repo_2 FOREIGN KEY (repository_id)
                  REFERENCES github.repository (id)
);

CREATE TABLE github.search_user (
       sid INT NOT NULL
     , uid INT NOT NULL
     , rank INT
     , PRIMARY KEY (sid, uid)
     , CONSTRAINT FK_search_user_1 FOREIGN KEY (sid)
                  REFERENCES github.search_term (id)
     , CONSTRAINT FK_search_user_2 FOREIGN KEY (uid)
                  REFERENCES github.user (id)
);

CREATE TABLE github.search_repository (
       sid INT NOT NULL
     , rid INT NOT NULL
     , rank INT
     , PRIMARY KEY (sid, rid)
     , CONSTRAINT FK_search_repository_1 FOREIGN KEY (sid)
                  REFERENCES github.search_term (id)
     , CONSTRAINT FK_search_repository_2 FOREIGN KEY (rid)
                  REFERENCES github.repository (id)
);

CREATE TABLE github.search_organization (
       sid INT NOT NULL
     , orgid INT NOT NULL
     , rank INT
     , PRIMARY KEY (sid, orgid)
     , CONSTRAINT FK_search_organization_1 FOREIGN KEY (sid)
                  REFERENCES github.search_term (id)
     , CONSTRAINT FK_search_organization_2 FOREIGN KEY (orgid)
                  REFERENCES github.organization (id)
);

CREATE TABLE github.commit (
       id INT NOT NULL
     , committed TIMESTAMP NOT NULL
     , name TEXT
     , email TEXT
     , user_id INT
     , login TEXT
     , message TEXT
     , PRIMARY KEY (id, committed)
     , CONSTRAINT FK_repo_commit_1 FOREIGN KEY (id)
                  REFERENCES github.repository (id)
);

CREATE TABLE github.parent (
       id INT NOT NULL
     , parent_id INT
     , parent_full_name TEXT
     , PRIMARY KEY (id)
     , CONSTRAINT FK_parent_1 FOREIGN KEY (id)
                  REFERENCES github.repository (id)
);

CREATE TABLE github.follows (
       follower INT NOT NULL
     , following INT NOT NULL
     , PRIMARY KEY (follower, following)
     , CONSTRAINT FK_follows_1 FOREIGN KEY (follower)
                  REFERENCES github.user (id)
     , CONSTRAINT FK_follows_2 FOREIGN KEY (following)
                  REFERENCES github.user (id)
);

