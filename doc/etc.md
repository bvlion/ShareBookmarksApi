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

## get /etc/{lang}/faq

よくある質問を取得

### request

内容 | 値 | 説明
:--|:--|:--
lang | ja | 取得言語

```
http://127.0.0.1:8080/etc/ja/faq
```

### response

faq 配下にリスト形式

内容 | 値 | 説明
:--|:--|:--
question | 画像が表示されません... | 質問内容
answer | 画像は各サイトのシェア用画像を取得しています... | 回答

```
{
  "faq" : [
    {
      "question" : "画像が表示されません。",
      "answer" : "画像は各サイトのシェア用画像を取得しています。<br>対象のサイトがシェア画像を設定していない場合は画像は表示されません。"
    }
  ]
}
```

## get /etc/ogp

OGP 画像 URL を取得

### request

内容 | 値 | 説明
:--|:--|:--
lang | url | 対象サイトの URL

```
http://localhost:8080/etc/ogp?url=https://www.ambitious-i.net/
```

### response

json 形式

内容 | 値 | 説明
:--|:--|:--
url | https://www.ambitious-i.net/img/main.jpg | 取得できない場合、および http の場合は null

```
{
  "url" : "https://www.ambitious-i.net/img/main.jpg"
}
```
