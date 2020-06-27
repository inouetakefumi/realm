package to.msn.wings.realm;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import io.realm.Realm;
import io.realm.RealmResults;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Realm mRealm;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRealm = Realm.getDefaultInstance();

        mTextView = (TextView) findViewById(R.id.textView);
        Button create = (Button) findViewById(R.id.create);
        Button read = (Button) findViewById(R.id.read);
        Button update = (Button) findViewById(R.id.update);
        Button delete = (Button) findViewById(R.id.delete);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Number max = realm.where(schedule.class).max("id");
                        long newId = 0;
                        if(max != null) { // nullチェック
                            newId = max.longValue() + 1;
                        }

                        schedule schedule
                                = realm.createObject(schedule.class, newId);
                        schedule.date = new Date();
                        schedule.title = "登録テスト";
                        schedule.detail = "スケジュールの詳細情報です";

                        // 保存するスケジュールをTextViewに表示します。
                        mTextView.setText("登録しました\n"
                                + schedule.toString());
                    }
                });
            }
        });

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<schedule> schedules
                                = realm.where(schedule.class).findAll();

                        mTextView.setText("取得");
                        for (schedule schedule :
                                schedules) {
                            String text = mTextView.getText() + "\n"
                                    + schedule.toString();
                            mTextView.setText(text);
                        }
                    }
                });
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        schedule schedule = realm.where(schedule.class)
                                .equalTo("id", 0)
                                .findFirst();
                        schedule.title += "＜更新＞";
                        schedule.detail += "＜更新＞";

                        mTextView.setText("更新しました\n"
                                + schedule.toString());

                    }
                });
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Number min = realm.where(schedule.class).min("id");
                        if (min != null) { // nullチェック

                            schedule schedule = realm.where(schedule.class)
                                    .equalTo("id", min.longValue())
                                    .findFirst();

                            schedule.deleteFromRealm();

                            mTextView.setText("削除しました\n"
                                    + schedule.toString());
                        }
                    }
                });
            }
        });



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mRealm.close();
    }
}
