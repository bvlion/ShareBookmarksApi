INSERT INTO notifications (title, subject, target_date)
VALUES
  ('ご利用いただきありがとうございます。', 'この度は数あるアプリの中から当アプリを選んでいただき、誠にありがとうございます。\n何かお困りのことがございましたら「お問い合わせ」よりお気軽にお問い合わせください。', NOW());

INSERT INTO term (type, lang, message)
VALUES
  (1, 'ja', 'この利用規約（以下，「本規約」といいます。）は，当アプリケーションの管理人（以下，「管理人」といいます。）がこのアプリケーション上で提供するサービス（以下，「本サービス」といいます。）の利用条件を定めるものです。ご利用ユーザーの皆さま（以下，「ユーザー」といいます。）には，本規約に従って，本サービスをご利用いただきます。<br><br>これはサンプルです。'),
  (1, 'en', 'These terms of use define the terms of use for the services provided by the administrator of this application. All registered users are required to use this application in accordance with these terms of use.<br><br>これはサンプルです。'),
  (2, 'ja', 'このアプリケーション上で提供するサービス（以下，「本サービス」といいます。）においてご利用ユーザーの皆さま（以下，「ユーザー」といいます。）に関する情報を以下のとおり取り扱います。<br><br>これはサンプルです。'),
  (2, 'en', 'In the services provided on this application, the Company will handle information about users as follows.<br><br>これはサンプルです。');

INSERT INTO faq (lang, question, answer)
VALUES
  ('ja', '画像が表示されません。', '画像は各サイトのシェア用画像を取得しています。<br>対象のサイトがシェア画像を設定していない場合は画像は表示されません。');