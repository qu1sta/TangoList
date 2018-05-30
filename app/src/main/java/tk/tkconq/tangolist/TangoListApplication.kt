package tk.tkconq.tangolist

import android.app.Application
import io.realm.Realm


/**
 * アプリ実行時に行う処理を記述するクラス
 */
class TangoListApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // DBの初期化
        Realm.init(this)
    }
}
