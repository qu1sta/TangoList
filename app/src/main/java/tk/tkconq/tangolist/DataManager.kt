package tk.tkconq.tangolist

import android.content.Context
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

/**
 * DBにデータを挿入するクラス
 */
class DataManager {

    /**
     * データを挿入する
     * @param context アプリの状態を持つコンテキスト
     * @param realm Realmインスタンス
     */
    fun insertData(context: Context, realm: Realm) {
        val inputStream: InputStream = context.resources.assets.open("data.txt")
        val inputStreamReader = InputStreamReader(inputStream)
        val bufferReader = BufferedReader(inputStreamReader)

        var line = bufferReader.readLine()
        // 1行ずつ読み込みながら処理
        while (line != null) {
            val tokens = line.split(" - ")
            // データの挿入
            realm.executeTransaction {
                val maxValue = realm.where<Sentence>().max("id")
                val nextId = (maxValue?.toLong() ?: 0L) + 1
                val sentence = realm.createObject<Sentence>(nextId)
                sentence.eng = tokens[0]
                sentence.jpn = tokens[1]
            }
            line = bufferReader.readLine()
        }
    }

}
