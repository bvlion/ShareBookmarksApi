INSERT INTO notifications (title, subject, target_date)
VALUES
  ('ご利用いただきありがとうございます。', 'この度は数あるアプリの中から当アプリを選んでいただき、誠にありがとうございます。\n何かお困りのことがございましたら「お問い合わせ」よりお気軽にお問い合わせください。', NOW());

INSERT INTO etc (type, lang, message)
VALUES
  (1, 'ja', 'この利用規約（以下，「本規約」といいます。）は，当アプリケーションの管理人（以下，「管理人」といいます。）がこのアプリケーション上で提供するサービス（以下，「本サービス」といいます。）の利用条件を定めるものです。ご利用ユーザーの皆さま（以下，「ユーザー」といいます。）には，本規約に従って，本サービスをご利用いただきます。'),
  (1, 'en', 'These terms of use define the terms of use for the services provided by the administrator of this application. All registered users are required to use this application in accordance with these terms of use.'),
  (2, 'ja', 'このアプリケーション上で提供するサービス（以下，「本サービス」といいます。）においてご利用ユーザーの皆さま（以下，「ユーザー」といいます。）に関する情報を以下のとおり取り扱います。'),
  (2, 'en', 'In the services provided on this application, the Company will handle information about users as follows.');