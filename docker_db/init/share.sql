CREATE TABLE share
(
    id INT AUTO_INCREMENT NOT NULL COMMENT 'ID',
    owner_user_id INT NOT NULL COMMENT '持ち主のユーザー ID',
    items_id INT NOT NULL COMMENT '対象フォルダ ID',
    share_user_id INT NOT NULL COMMENT '共有先のユーザー ID',
    owner_type INT NOT NULL COMMENT '編集権限可否',
    parent_id INT NOT NULL COMMENT '共有先ユーザーの親フォルダ ID',
    created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
    updated DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
    deleted DATETIME COMMENT '削除日時',
    PRIMARY KEY (id)
) COMMENT '共有情報';
