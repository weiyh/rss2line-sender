CREATE TABLE user_profile (
  line_uid             VARCHAR(34) PRIMARY KEY   NOT NULL,
  line_name            VARCHAR(50)               NOT NULL,
  line_image           VARCHAR(200)              NOT NULL,
  valid                BOOLEAN                   NOT NULL,
  follow_time          TIMESTAMP,
  unfollow_time        TIMESTAMP,
  subscribe_start_time TIMESTAMP,
  subscribe_end_time   TIMESTAMP,
  update_time          TIMESTAMP                 NOT NULL DEFAULT CURRENT_TIMESTAMP
);