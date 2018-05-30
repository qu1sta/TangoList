package tk.tkconq.tangolist

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * 英文と訳を保持するRealmObject
 */
open class Sentence : RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var eng: String = ""
    var jpn: String = ""
}
