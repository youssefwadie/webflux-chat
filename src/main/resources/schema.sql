CREATE TABLE chat_messages
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    sender     VARCHAR(255) NOT NULL,
    content    TEXT         NOT NULL,
    created_at TIMESTAMP    NOT NULL
);
