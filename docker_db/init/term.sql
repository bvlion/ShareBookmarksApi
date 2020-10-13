CREATE TABLE term
(
    id INT AUTO_INCREMENT NOT NULL COMMENT 'ID',
    type INT NOT NULL COMMENT 'メッセージタイプ',
    lang CHAR(2) NOT NULL COMMENT '言語',
    message TEXT NOT NULL COMMENT '本文',
    PRIMARY KEY (id)
) COMMENT '利用規約などのその他情報';
