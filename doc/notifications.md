# お知らせ API

## get /notifications/list

「お知らせ」に表示する内容を取得

### request

内容 | 値 | 説明
:--|:--|:--
Authorization | Bearer | ヘッダに設定

```
http://127.0.0.1:8080/notifications/list
```

### response

notifications 配下にリスト形式

内容 | 値 | 説明
:--|:--|:--
target_date | yyyy-MM-dd HH:mm:ss | 配信日時
title | ご利用いただき… | タイトル
subject | この度は… | 本文
url | https://... | 遷移先 URL（遷移させない場合は null）

```
{
    "notifications": [
        {
            "target_date": "2020-08-29 16:21:18",
            "title": "ご利用いただきありがとうございます。",
            "subject": "この度は数あるアプリの中から当アプリを選んでいただき、誠にありがとうございます。\n何かお困りのことがございましたら「お問い合わせ」よりお気軽にお問い合わせください。",
            "url": null
        },
        {
            "target_date": "2020-08-29 16:21:18",
            "title": "プレミアムユーザー限定",
            "subject": "プラミアム限定のお知らせです",
            "url": "https://example.com"
        }
    ]
}
```