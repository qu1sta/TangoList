package tk.tkconq.tangolist

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

/**
 * MediaPlayerの状態を示す列挙体
 */
enum class MediaState {
    IDLE, INITIALIZED, PREPARED, STARTED, PAUSED, STOPPED
}

/**
 * 状態を保存するMediaPlayer
 * @param state 状態
 */
class MyMediaPlayer(var state: MediaState) : MediaPlayer() {

    override fun stop() {
        super.stop()
        state = MediaState.STOPPED
    }

    override fun start() {
        super.start()
        state = MediaState.STARTED
    }

    override fun pause() {
        super.pause()
        state = MediaState.PAUSED
    }

    override fun setDataSource(context: Context?, uri: Uri?) {
        super.setDataSource(context, uri)
        state = MediaState.INITIALIZED
    }

    override fun reset() {
        super.reset()
        state = MediaState.IDLE
    }
}
