# その他 API

## get /

index

### request

内容 | 値 | 説明
:--|:--|:--

```
http://127.0.0.1:8080/
```

### response

notifications 配下にリスト形式

内容 | 値 | 説明
:--|:--|:--
\- | yyyy-MM-dd HH:mm:ss | 現在時刻

```
2020-09-25 07:52:12
```

## get /etc/{lang}/terms_of_use

利用規約を取得

### request

内容 | 値 | 説明
:--|:--|:--
lang | ja | 取得言語

```
http://127.0.0.1:8080/etc/ja/terms_of_use
```

### response

json 形式

内容 | 値 | 説明
:--|:--|:--
message | この利用規約（以下，「本規... | 利用規約

```
{
    "message" : "この利用規約（以下，「本規約... "
}
```

## get /etc/{lang}/privacy_policy

プライバシー・ポリシーを取得

### request

内容 | 値 | 説明
:--|:--|:--
lang | ja | 取得言語

```
http://127.0.0.1:8080/etc/ja/privacy_policy
```

### response

json 形式

内容 | 値 | 説明
:--|:--|:--
message | このアプリケーション上で提供するサ... | プライバシー・ポリシー

```
{
    "message" : "このアプリケーション上で提供するサ... "
}
```
