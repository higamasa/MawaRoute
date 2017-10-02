# まわルート
2015年KANAZAWAアプリ開発塾

## Description
ご飯を食べに行くときに毎回迷うので、近場のお店を適当に選んでルーレットで決めちゃうアプリ

## Features
* [ ] 行きたいと思ったお店の中からルーレットでランダムに行き先を決める
* [ ] 現在位置から半径300m~3km圏内のお店をリスト表示
* [ ] お店の検索・絞り込み機能

## Requirement
* **Android Studio 3.0 Beta** (Android studio 2.x以前のバージョンではビルドできません)
* Android Jelly Bean (API 16) or Later

## Installation

    $ git clone https://github.com/kiesproject/Android-kanazawa-2015

## Environment
### Kotlin
このアプリケーションではJavaからKotlinに移行中です。Kotlin使おうね!!

### DataBinding
[DataBinding](http://developer.android.com/intl/ja/tools/data-binding/guide.html)はAndroidアプリケーションにおけるMVVMアーキテクチャを実現するためのライブラリです。

例えば、`android:text` 属性にViewModelの変数を `@{}` で囲むとViewが描画されるときにActvityやFragmentなどのUIコントローラーを介さずに直接変数が呼び出されます。

```xml
<TextView
    android:id="@+id/txt_place"
    android:layout_marginEnd="@dimen/spacing_xsmall"
    android:layout_marginRight="@dimen/spacing_xsmall"
    android:layout_marginTop="@dimen/spacing_xsmall"
    android:text="@{viewModel.shop.name}" />
```

`@BindingAdapter` のアノテーションを付けることで独自の属性を追加できます。
ただし、`@BindingAdapter` はstaticメソッドである必要があるため、Kotlinで書く場合は `@JvmStatic` アノテーションをつける必要があります。

以下の例ではImageViewで用いる属性で、属性名を `app:loadImage` 、属性値には画像のURLを指定するように定義しています。

```kotlin
@JvmStatic
@BindingAdapter("loadImage")
fun loadImage(imageView: ImageView, imageUrl: String) {
  Picasso
      .with(imageView.context)
      .load(imageUrl)
      .into(imageView)
}
```

### Android Architecture Component
[Android Architecture Component](https://developer.android.com/topic/libraries/architecture/index.html)はGoogle I/O 2017で発表されたAndroidのアーキテクチャをサポートするライブラリ。

Android Architecture Componentには主に以下のような機能を持っています。

以下の機能のうち、このアプリではHandling Lifecycles、LiveData、ViewModelを使います。

#### 1. Handling Lifecycles
いちいち `onStart()` とかをoverrideしてメソッドを呼び出さなくてもObserverを設定すれば勝手にイベントを発行してくれるやつ。

GPSの処理をActvityやFragmentなどから完全に引き剥がすことができる(たぶん)。

詳しくはここを見るといいよ -> https://qiita.com/aroe_lemon/items/2a093a9e7524cc08101b

#### 2. LiveData
ViewとViewModel間でのデータの受け渡しをPub/Subでのメッセージングを提供します

RxJavaにあるBehaviorSubjectの上位版

BehaviorSubjectではコンポーネント破棄時に `onComplete()` を呼ばなければメモリリークしてしまうのに対し、LiveDataは明示的に `onComplete()` を呼ばなくても自動的にメモリを開放してくれる。

* Publish(メッセージを送る側)

```kotlin
class Publish {

  val liveData = MutableLiveData<String>()

  fun setString(str: String) {
    liveData.value = str
  }
}
```

* Subscribe(メッセージを受け取る側)

```kotlin
class Subscribe : AppCompatActivity() {
  
  val pub = Publish()
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    // 
    pub.liveData.observe(this, Observer<String> { data -> // dataはString型
      // PublishのsetString()を呼び出すたびにLogが出力される
      Log.d("log", data)
    })
  }
}
```

#### 3. ViewModel
画面回転時のUI破棄におけるUIの状態を自動で復元してくれるコンポーネント

ViewModelのインスタンスはすべて `ViewModelProviders()` によって提供されます。
これによってViewModelが画面回転などによって破棄されても `ViewModelProviders()` が自動でViewModelを生成します。

Kotlinでは `by lazy` を使うことできれいに書くことができます。

```kotlin
private val viewModel: ExampleViewModel by lazy {
	ViewModelProviders.of(this).get(ExampleViewModel::class.java) 
}
```

#### 4. Room
SQLiteの良さげなObject Mapper。長いので解説はなし

こことか見ればいいと思うよ -> https://tech.recruit-mp.co.jp/mobile/post-12311/
