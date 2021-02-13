# ShareBookmarksApi

[ShareBookmarks](https://github.com/bvlion/ShareBookmarks) 用 API 

## FW

- [Ktor](https://ktor.io/)
- [Exposed](https://github.com/JetBrains/Exposed/wiki/DSL)

## 環境構築

### インストール

- IntelliJ
- Docker for Mac

### 実行

- `docker-compose up -d db` でローカル DB 起動
- `gradle -> application -> run`

### テスト

- `docker-compose up -d test-db` でローカル DB 起動
- `gradle -> verification -> test`

## EndPoint

- [users](/doc/users.md)
- [notifications](/doc/notifications.md)
- [shares](/doc/shares.md)
- [items](/doc/items.md)
- [etc](/doc/etc.md)

## テスト

master にプッシュすると GitHub Actions によって GitHub Pages にアップされる  
[テスト結果](https://bvlion.github.io/ShareBookmarksApi/index.html)