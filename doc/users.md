# ユーザー API

## post /users/auth

ユーザー登録（更新）を行い Token を返す

### request

内容 | 値 | 説明
:--|:--|:--
email | test@example.com | ユーザの Email
uid | test3r2trnmthjtyjr | Google から発行される HASH 値
fcm_token | test | 端末が払い出す token

```
http://127.0.0.1:8080/users/auth
-d '{ "email":"test@example.com", "fcm_token":"test" }'
```

### response

access_token を JSON で返す

内容 | 値 | 説明
:--|:--|:--
access_token | token68 | 有効期限 1時間の [Bearer トークン](https://ja.wikipedia.org/wiki/Bearer%E3%83%88%E3%83%BC%E3%82%AF%E3%83%B3)

```
{
  "access_token" : "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiYXVkIjoic2hhcmVib29rbWFya3MiLCJleHAiOjE1OTg3NTg0NDd9.C-Irgo599czcbemvJ0yKywL6M8w3oh2VI0Cgala-wQA"
}
```