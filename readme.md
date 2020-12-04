**【t4j-boot】**

# ■概要

## これ is 何？

神ライブラリである `twitter4j` を使った趣味ツール。  
以前、単独の pure java で実装してたんだけど、これまた神FWである `SpringBoot` を使ってリライトしたもの。

## ざっくりいうと

twitter4j と SpringBoot でなんか色々やりたい事を詰め込んだ **ごった煮** みたいなツール。

ちなみにこれは単独のツールではなく、  
**Ubuntu(WSL)からcronで叩いてファイルに保存** したり、  
そこからまた **C#で作った別なツール** で色々してたり、  
ちょっとした **ツール群を構成する一部** であります。

## SpringBootといえば

[SpringBootに入門する為の助走本 - Qiita](https://qiita.com/sugaryo/items/5695bfcc21365f429767)


# ■ざっくり変更履歴

## TweetList

`application.properties` にJSON文字列として定義してたが、  
`src/resource/data/tweet.json` に変更してconfig系クラスも修正した。

外部定義データをjsonで置いとくパターンとして助走本Tipsに追記したい。

## ツイートメッセージのテンプレート化

Thymeleafのテンプレートエンジン（プレーンテキストモード）で、
Tweetするメッセージ本文を外部定義化。

一般的にはメールテンプレートとしてこういう形にして利用するケースが想定されるやつ。

### テンプレートエンジンのDI管理Beanバグ（解決済み）

単純にプレーンテキストとしてのMessage用のテンプレートエンジンをBean登録しちゃったせいで、  
ThymeleafがView用のテンプレートエンジンを見失ってViewResolverが画面をレンダリングできなくなった。

HTML用設定のエンジンもBean登録し、且つこいつを `@Primary` 指定して回避した。  
また、プレーンテキスト用のエンジンにはBean管理名を指定し、  
インジェクションポイントに `@Qualifier` を指定した。

## タグ付きツイート機能

特定のタグを付けて連投するのにDeckの操作でいちいちタグをコピペするのが面倒だったので、連投用の画面機能を作った。

# ■このあとやりたい改良メモ

- いわゆるfeatureリスト
    - ~~メッセージがリテラルベタ書きなのでどうにかしたい。~~
        - Thymeleafテンプレートを使用する形で突貫リファクタリングした。
        - 関連処理はもう少し良い感じ（メソッドの羅列がアレなので・・・）にしたい。
    - readme自体ももうちょっと真面目に書きたい。
    - gmailでメール飛ばす何かの機能も作りたい。
    - 画面がのっぺらぼう過ぎるので流石に何かやりたい。
        - bootstrapを試しに適用してみた。
    - 機能追加：retweets/categoryのGETエンドポイントを追加したい。
        - 追加したかった機能はすぐ作れたんだが、それよりコード改善したい所が沢山出て来たのでリファクタリングもすすめる。
        - 取り敢えずコントローラを分割。
        - URLマッピングもアレなので、エンドポイント設計も見直す。
        - その他、API機能をキックするだけの画面機能も軽く作っておきたい。
    - ~~エンドポイント設計がかなり酷いので、リソースベースで上手い事整理する。~~
        - コントローラを分割、ついでに内部処理をコード整理。
        - 全体的にエンドポイントを改善。（破壊的変更）

