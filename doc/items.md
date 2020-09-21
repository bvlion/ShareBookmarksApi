# ブックマーク API

## get /items/list

対象ユーザーのブックマークを取得

### request

内容 | 値 | 説明
:--|:--|:--
Authorization | Bearer | ヘッダに設定

```
http://127.0.0.1:8080/items/list
```

### response

items 配下にリスト形式

内容 | 値 | 説明
:--|:--|:--
remote_id | 1 | サーバー側の一意の ID
parent_id | 1 | サーバー側の親としている item ID
name | example | 名称
url | https://example.com | URL（フォルダの場合は NULL）
orders | 1 | 編集権限
updated | yyyy-MM-dd HH:mm:ss | 更新日時

```
{
    "items": [
        {
            "remote_id": 1,
            "parent_id": 1,
            "name": "example",
            "url": "https://example.com",
            "orders": 1,
            "updated": "2020-09-16 13:44:22"
        },
        {
            "remote_id": 2,
            "parent_id": 1,
            "name": "example folder",
            "url": null,
            "orders": 2,
            "updated": "2020-09-16 13:44:22"
        }
    ]
}
```

## post /items/save

ブックマークを登録してサーバー側の一意の ID を返す

### request

内容 | 値 | 説明
:--|:--|:--
local_id | 1 | 端末での一意の ID
remote_id | 1 | サーバー側の一意の ID（サーバー側で保持していなければ null）
name | example | 名称
url | https://example.com | URL（フォルダの場合は NULL）
orders | 1 | 編集権限
updated | yyyy-MM-dd HH:mm:ss | 更新日時

```
http://127.0.0.1:8080/items/save -d '[
{
        "local_id": 1,
        "remote_id": null,
        "name": "example",
        "url": "https://example.com",
        "orders": 1,
        "updated": "2020-09-16 12:44:22"
    },
    {
        "local_id": 2,
        "remote_id": 4,
        "name": "example folder",
        "url": null,
        "orders": 2,
        "updated": "2020-09-16 13:44:22"
    }
]'
```

### response

items 配下にリスト形式

内容 | 値 | 説明
:--|:--|:--
local_id | 1 | 端末での一意の ID（POST した ID）
remote_id | 1 | サーバー側の一意の ID

```
{
    "items": [
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

## put /items/parents

ブックマークの親情報を更新する

### request

内容 | 値 | 説明
:--|:--|:--
remote_id | 1 | 対象ブックマーク ID
parent_id | 1 | 親ブックマークの ID
is_share_folder | true | 共有されているフォルダであれば true

```
http://127.0.0.1:8080/items/parents -d '[
    {"remote_id": 1, "parent_id", 2, false},
    {"remote_id": 3, "parent_id", 0, true}
]'
```

### response

更新した件数を返す

内容 | 値 | 説明
:--|:--|:--
result_count | 4 | 更新した件数

```
{
  "result_count" : 4
}
```

## delete /items/delete

ブックマークを削除する

### request

内容 | 値 | 説明
:--|:--|:--
delete_id | 1 | サーバー側の一意の ID

```
http://127.0.0.1:8080/items/delete -d '[
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
