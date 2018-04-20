package local.hal.st32.android.mylibrary50054;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class LibraryParentDataAccess {

    /**
     * すべてを表示する
     *
     * @param context
     * @return
     */
    public static Cursor findByAll(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "Select * From LibraryList order by updateLine desc";

        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }
    /**
     * 小説だけをすべてを表示する
     *
     * @param context
     * @return
     */
    public static Cursor ListNovel(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "Select * From LibraryList Where category='小説' order by updateLine desc";
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }
    /**
     * 漫画だけをすべてを表示する
     *
     * @param context
     * @return
     */
    public static Cursor ListCartoon(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "Select * From LibraryList Where category='漫画' order by updateLine desc";
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }
    /**
     * 主キーによる検索
     *
     * @param context コメテキスト
     * @param id      主キー値
     * @return 主キーに対応するデータを格納したMemoオブジェクト。対応するデータが存在しない場合null。
     */
    public  static LibraryList findByPK(Context context,int id) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = null;
        LibraryList result = null;
        String sql = "Select * From libraryList Where _id = " +id;

        try {
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()){
                int idxName = cursor.getColumnIndex("name");
                int idxCategory = cursor.getColumnIndex("category");
                int idxImage = cursor.getColumnIndex("image");
                int idxGenre = cursor.getColumnIndex("Genre");
                int idxAuthor = cursor.getColumnIndex("author");
                int idxPublisher = cursor.getColumnIndex("publisher");
                int idxUpdate = cursor.getColumnIndex("updateLine");

                String name = cursor.getString(idxName);
                String category = cursor.getString(idxCategory);
                int image = cursor.getInt(idxImage);
                String author = cursor.getString(idxAuthor);
                String genre = cursor.getString(idxGenre);
                String publisher = cursor.getString(idxPublisher);
                String update = cursor.getString(idxUpdate);

                result = new LibraryList();
                result.set_id(id);
                result.set_name(name);
                result.setImage(image);
                result.setCategory(category);
                result.setGenre(genre);
                result.setAuthor(author);
                result.setPublisher(publisher);
                result.setUpdate(update);
            }
        }
        catch (Exception ex){
            Log.e("ERROR",ex.toString());
        }
        finally {
            db.close();
        }
        return result;
    }
    /**
     * 本の情報を新規登録するメソッド。
     *
     * @param context コメテキスト
     * @param name 本名前
     * @param category カテゴリー
     * @param libraryAuthor 作成者
     * @param libraryPublisher 出版社
     * @param update 更新日
     */
    public static void ParentInsert(Context context,String name, String category,int image,String genre ,String libraryAuthor, String libraryPublisher, String update) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String sql ="Insert into libraryList (name,category,image,genre,author,publisher,updateLine) Values (?,?,?,?,?,?,?)";
        try {
            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.bindString(1, name);
            stmt.bindString(2, category);
            stmt.bindLong(3, image);
            stmt.bindString(4, genre);
            stmt.bindString(5, libraryAuthor);
            stmt.bindString(6, libraryPublisher);
            stmt.bindString(7, update);

            stmt.executeInsert();
        }
        catch (Exception ex) {
            Log.e("ERROR", ex.toString());
        }
        finally {
            db.close();
        }
    }
    /**
     * 本の更新日を更新するメソッド
     *
     * @param context コメテキスト
     * @param id 主キー値
     * @param update 更新日
     */
    public static void ParentUpdateLine(Context context,int id, String update) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String sql = "Update libraryList Set updateLine = ? Where _id = ?";
        try{
            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.bindString(1, update);
            stmt.bindLong(2, id);
            stmt.executeInsert();
        }
        catch (Exception ex) {
            Log.e("ERROR",ex.toString());
        }
        finally {
            db.close();
        }
    }
    /**
     * 本の情報を更新するメソッド
     *
     * @param context コメテキスト
     * @param name 本名前
     * @param  category カテゴリー
     * @param libraryAuthor 作成者
     * @param libraryPublisher 出版社
     * @param update 更新日
     *
     */
    public static void ParentUpdate(Context context,String name, String category,int image,String genre,String libraryAuthor, String libraryPublisher, String update ,int id) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String sql = "Update libraryList Set name=?,category=?,image=?,genre=?,author=?,publisher=?,updateLine = ? Where _id = ?";
        try{
            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.bindString(1, name);
            stmt.bindString(2, category);
            stmt.bindLong(3, image);
            stmt.bindString(4,genre);
            stmt.bindString(5, libraryAuthor);
            stmt.bindString(6, libraryPublisher);
            stmt.bindString(7, update);
            stmt.bindLong(8, id);
            stmt.executeInsert();
        }
        catch (Exception ex) {
            Log.e("ERROR",ex.toString());
        }
        finally {
            db.close();
        }
    }
    /**
     * 本情報を削除するメソッド。
     *
     * @param context コメテキスト
     * @param id 主キー値
     **/
    public static void Delete(Context context,int id) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String sql ="Delete from libraryList where _id=?";
        try {
            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.bindLong(1, id);
            stmt.executeInsert();
        }
        catch (Exception ex){
            Log.e("ERROR",ex.toString());
        }
        finally {
            db.close();
        }
    }

}
