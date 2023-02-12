alter table votes drop column vote;
alter table votes add column "like" integer not null default 0;
alter table votes add column dislike integer not null default 0;