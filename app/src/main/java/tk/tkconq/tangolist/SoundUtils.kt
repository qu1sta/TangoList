package tk.tkconq.tangolist

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.util.*

/**
 * 音楽を表すクラス
 */
class Sound(val id: Long) {
    val uri: Uri
        get() = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
}

/**
 * 音楽を管理するクラス
 */
class SoundManager(context: Context) {

    val sounds: List<Sound> = getSounds(context)

    /**
     * 全ての音楽を取得するメソッド
     */
    private fun getSounds(context: Context): List<Sound> {

        // 音楽のリスト
        val localSounds = LinkedList<Sound>()

        // ContentResolverを用いてSDカードから音楽を取得
        val cr = context.contentResolver

        // 外部ストレージから音楽を検索
        val cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Audio.Media.IS_MUSIC + " = 1", null, null)

        // 検索に失敗した場合は空のリストを返す
        cursor ?: return localSounds

        if (cursor.moveToFirst()) {
            val idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID) // 曲のid
            val data = cursor.getColumnIndex(MediaStore.Audio.Media.DATA) // フォルダのpath

            do {
                val regex = Regex("tangolist")
                if (regex.containsMatchIn(cursor.getString(data))) {
                    localSounds.add(Sound(cursor.getLong(idColumn)))
                }
            } while (cursor.moveToNext())
        }

        // カーソルを閉じる
        cursor.close()

        return localSounds
    }
}
