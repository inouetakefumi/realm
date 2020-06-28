package to.msn.wings.realm;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import java.util.Date;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("schedule.realm")
                .schemaVersion(1)
                .build();

        mRealm = Realm.getInstance(config);

        //データを10日初期登録
        try {
	    for(int i = 0;i<10;i++){

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

                    Calendar c = Calendar.getInstance();

                    //当日日付を取得
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day= c.get(Calendar.DATE);

                    c.set(year, month , day+i);
                    schedule.date = c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1)+ "/" + c.get(Calendar.DATE);
                }
            });
        }
        }
        catch (IllegalArgumentException e) {
            // 不正な日付の場合の処理
        }

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

                        schedule.work = "テレワーク";
                        schedule.detail = "１０時打ち合わせ";

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
                        schedule.work += "＜更新＞";
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
