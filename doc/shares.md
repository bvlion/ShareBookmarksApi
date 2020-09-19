# 共有情報 API

## get /shares/list

対象ユーザーの共有情報を取得

### request

内容 | 値 | 説明
:--|:--|:--
Authorization | Bearer | ヘッダに設定

```
http://127.0.0.1:8080/shares/list
```

### response

shares 配下にリスト形式

内容 | 値 | 説明
:--|:--|:--
remote_id | 1 | サーバー側の一意の ID
folder_id | 1 | サーバー側のフォルダとしている item ID
user_email | test@example.com | 共有ユーザーのメールアドレス
owner_type | 1 | 編集権限
updated | yyyy-MM-dd HH:mm:ss | 更新日時

```
{
    "shares": [
        {
            "remote_id": 4,
            "folder_id": 1,
            "user_email": "test2@test.com",
            "owner_type": 1,
            "updated": "2020-09-16 13:44:22"
        },
        {
            "remote_id": 3,
            "folder_id": 1,
            "user_email": "test@test.com",
            "owner_type": 1,
            "updated": "2020-09-16 12:44:22"
        }
    ]
}
```

## post /shares/save

共有情報を登録してサーバー側の一意の ID を返す

### request

内容 | 値 | 説明
:--|:--|:--
local_id | 1 | 端末での一意の ID
remote_id | 1 | サーバー側の一意の ID（サーバー側で保持していなければ null）
folder_id | 1 | サーバー側のフォルダとしている item ID
user_email | test@example.com | 共有ユーザーのメールアドレス
owner_type | 1 | 編集権限
updated | yyyy-MM-dd HH:mm:ss | 更新日時

```
http://127.0.0.1:8080/shares/save -d '[
{
        "local_id": 1,
        "remote_id": null,
        "folder_id": 1,
        "user_email": "test@test.com",
        "owner_type": 1,
        "updated": "2020-09-16 12:44:22"
    },
    {
        "local_id": 2,
        "remote_id": 4,
        "folder_id": 1,
        "user_email": "test2@test.com",
        "owner_type": 1,
        "updated": "2020-09-16 13:44:22"
    }
]'
```

### response

shares 配下にリスト形式

内容 | 値 | 説明
:--|:--|:--
local_id | 1 | 端末での一意の ID（POST した ID）
remote_id | 1 | サーバー側の一意の ID

```
{
    "shares": [
        {
            "local_id": 1,
            "remote_id": 3
        },
        {
            "local_id": 2,
            "remote_id": 4
        }
    ]
}
```

## delete /shares/delete

共有情報を削除する

### request

内容 | 値 | 説明
:--|:--|:--
delete_id | 1 | サーバー側の一意の ID

```
http://127.0.0.1:8080/shares/delete -d '[
    {"delete_id": 1},
    {"delete_id": 2}
]'
```

### response

削除した件数を返す

内容 | 値 | 説明
:--|:--|:--
delete_count | 4 | 削除した件数

```
{
  "delete_count" : 4
}
```
