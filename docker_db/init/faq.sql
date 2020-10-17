CREATE TABLE faq
(
    id INT AUTO_INCREMENT NOT NULL COMMENT 'ID',
    lang CHAR(2) NOT NULL COMMENT '言語',
    question VARCHAR(512) NOT NULL COMMENT '質問',
    answer TEXT NOT NULL COMMENT '回答',
    deleted TINYINT(4) NOT NULL DEFAULT '0' COMMENT '削除フラグ',
    PRIMARY KEY (id)
) COMMENT 'よくある質問';
