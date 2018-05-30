package tk.tkconq.tangolist

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter

/**
 * セルに表示するデータを制御するアダプタークラス
 */
class SentenceAdapter(data: OrderedRealmCollection<Sentence>?) :
        RealmBaseAdapter<Sentence>(data) {

    /**
     * Viewオブジェクトを保持するクラス
     * @param view: セルに表示するView
     */
    inner class ViewHolder(view: View) {
        val eng = view.findViewById<TextView>(R.id.eng_text)
        val jpn = view.findViewById<TextView>(R.id.jpn_text)
    }


    /**
     * リストビューのデータが必要になった際に呼ばれる
     * @param position: リストビューのセルの位置
     * @param convertView: 作成済みのセルを表すビュー
     * @param parent: 親のリストビュー
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View // セルを表すオブジェクト
        val viewHolder: ViewHolder // viewオブジェクトを保持するオブジェクト
        when (convertView) {
            null -> {
                val inflater = LayoutInflater.from(parent?.context)
                // XMLからViewを作成する
                view = inflater.inflate(R.layout.sentence_layout, parent, false)
                viewHolder = ViewHolder(view)
                view.tag = viewHolder // 高速化のためタグに保存しておく
            }
            else -> {
                view = convertView
                viewHolder = view.tag as ViewHolder // タグから取得することで高速に取得
            }
        }

        view.setBackgroundColor(Color.WHITE)

        // データのリスト
        adapterData?.run {
            // 描画したいデータを取得
            val sentence = get(position)
            viewHolder.eng.text = sentence.eng
            viewHolder.jpn.text = sentence.jpn
        }

        return view
    }

}
