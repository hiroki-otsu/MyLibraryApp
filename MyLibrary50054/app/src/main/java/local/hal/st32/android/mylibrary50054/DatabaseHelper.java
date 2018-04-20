package local.hal.st32.android.mylibrary50054;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper  extends SQLiteOpenHelper {
    /**
     * データベースファイル名の定義フィールド。
     */
    private static final String DATABASE_NAME = "libraryList.db";
    /**
     * バージョン情報の定数フィールド。
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * コンストラクタ。
     *
     * @param context コンテキスト
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //ライブラリテーブル
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE libraryList(");
        sb.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append("name TEXT NOT NULL,");
        sb.append("category Text,");
        sb.append("image INTEGER,");
        sb.append("Genre TEXT,");
        sb.append("author Text,");
        sb.append("publisher Text,");
        sb.append("updateLine DATE ");
        sb.append(");");
        db.execSQL(sb.toString());
        sb.setLength(0);

        //子テーブル
        sb.append("CREATE TABLE LibraryChild(");
        sb.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append("parent_id INTEGER NOT NULL,");
        sb.append("ChildName TEXT NOT NULL,");
        sb.append("Volume INTEGER NOT NULL,");
        sb.append("FOREIGN KEY(parent_id) REFERENCES libraryList(_id)");
        sb.append(");");
        db.execSQL(sb.toString());
        sb.setLength(0);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
