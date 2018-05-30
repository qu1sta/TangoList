package tk.tkconq.tangolist

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ListView
import android.widget.Toast
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


/**
 * MainActivity
 */
class MainActivity : AppCompatActivity() {

    private lateinit var realm: Realm
    private lateinit var player: MyMediaPlayer
    private lateinit var soundManager: SoundManager
    private var prevPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // パーミッションの設定
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission()
        }

        // MediaPlayerの設定
        player = MyMediaPlayer(MediaState.IDLE)

        // 終了時の処理
        player.setOnCompletionListener {
            // 選択中だったviewの背景を戻す
            getViewByPosition(prevPosition, listView).setBackgroundColor(Color.WHITE)

            prevPosition += +1

            // 次の曲の再生
            player.reset()
            player.setDataSource(applicationContext, soundManager.sounds[prevPosition].uri)
            player.prepare()
            player.start()

            // 次のviewを選択
            listView.setSelection(prevPosition)
            // 選択中のviewの背景を変更
            getViewByPosition(prevPosition, listView).setBackgroundColor(Color.GREEN)
        }

        // 曲の取得
        soundManager = SoundManager(this)

        // Realmのインスタンスを取得
        realm = Realm.getDefaultInstance()

        // データが存在しない場合は初期値を挿入
        if (realm.where<Sentence>().count() == 0L) {
            // 初期値を挿入する
            DataManager().insertData(this, realm)
        }

        // DBからデータを全て取得する
        val sentences = realm.where<Sentence>().findAll()

        // リストのadapterにRealmBaseAdapterを設定する
        listView.adapter = SentenceAdapter(sentences)

        // タッチイベントの追加
        listView.setOnItemClickListener { _, view, position, _ ->

            // 選択中だったviewの背景を元に戻す
            getViewByPosition(prevPosition, listView).setBackgroundColor(Color.WHITE)

            prevPosition = position

            // タッチしたviewの背景を変更
            view.setBackgroundColor(Color.GREEN)

            // 曲の再生
            player.reset()
            player.setDataSource(applicationContext, soundManager.sounds[position].uri)
            player.prepare()
            player.start()
        }

        // FloatingActionButtonの設定
        fab.setOnClickListener {
            when (player.state) {
                MediaState.STARTED -> {
                    // 再生中は曲を止める
                    fab.setImageDrawable(ContextCompat.getDrawable(baseContext, android.R.drawable.ic_media_play))
                    player.pause()
                }
                MediaState.PAUSED -> {
                    // ストップ中は曲を再生する
                    fab.setImageDrawable(ContextCompat.getDrawable(baseContext, android.R.drawable.ic_media_pause))
                    player.start()
                }
            }
        }
    }

    /**
     * positionに対応したviewを返すメソッド
     */
    private fun getViewByPosition(pos: Int, listView: ListView): View {
        val firstListItemPosition = listView.firstVisiblePosition
        val lastListItemPosition = firstListItemPosition + listView.childCount - 1

        return when {
            pos < firstListItemPosition || pos > lastListItemPosition -> {
                listView.adapter.getView(pos, null, listView)
            }
            else -> {
                val childIndex = pos - firstListItemPosition
                listView.getChildAt(childIndex)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    /**
     * パーミッションをチェックする関数
     */
    private fun checkPermission() {
        // 許可していない場合
        if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 許可を求める
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this@MainActivity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
            } else {
                val toast = Toast.makeText(this, "アプリ実行に許可が必要です", Toast.LENGTH_SHORT)
                toast.show()
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        1000)
            }
        }
    }
}
