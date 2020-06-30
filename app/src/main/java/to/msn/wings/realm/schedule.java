package to.msn.wings.realm;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class schedule extends RealmObject {
    @PrimaryKey
    public long id;     // 予定を見分けるためのIDが必要
    public String name;
    public Date date;   //予定の日付
    public String work;    // 勤怠
    public String detail;   // 予定の詳細
}
