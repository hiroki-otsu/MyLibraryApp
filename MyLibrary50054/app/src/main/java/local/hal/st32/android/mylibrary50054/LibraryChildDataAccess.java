package local.hal.st32.android.mylibrary50054;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class LibraryChildDataAccess {
    /**
     *
     * @param context
     * @param id
     * @return
     */
    public static Cursor findByOneDate(Context context, int id){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "SELECT * FROM LibraryChild where parent_id = "+ id + " ORDER BY Volume ASC";
        Cursor cursor = db.rawQuery(sql,null);
        return cursor;
    }
    /**
     * 主キーによる検索
     *
     * @param context コメテキスト
     * @param id      主キー値
     * @return 主キーに対応するデータを格納したMemoオブジェクト。対応するデータが存在しない場合null。
     */
    public  static LibraryChild findByPK(Context context,int id) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = null;
        LibraryChild result = null;
        String sql = "Select * From LibraryChild Where _id = " +id;

        try {
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()){
                int idxParentId = cursor.getColumnIndex("parent_id");
                int idxChildName = cursor.getColumnIndex("ChildName");
                int idxVolume = cursor.getColumnIndex("Volume");

                int parentId = cursor.getInt(idxParentId);
                String name = cursor.getString(idxChildName);
                int volume = cursor.getInt(idxVolume);

                result = new LibraryChild();

                result.set_id(id);
                result.setParent_id(parentId);
                result.setChildName(name);
                result.setVolume(volume);
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
     * 本を新規登録するメソッド。
     *
     * @param context コメテキスト
     * @param ParentId 本のID
     * @param libraryName 本名前
     * @param libraryVolume 巻
     */
    public static void ChildInsert(Context context,int ParentId,String libraryName, String libraryVolume) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String sql ="Insert into LibraryChild (parent_id,ChildName,Volume) Values (?,?,?)";
        try {
            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.bindLong(1, ParentId);
            stmt.bindString(2, libraryName);
            stmt.bindString(3, libraryVolume);

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
     * 巻情報を削除するメソッド。
     *
     * @param context コメテキスト
     * @param id 主キー値
     **/
    public static void ChildDelete(Context context,int id) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String sql ="Delete from LibraryChild where _id=?";
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
    /**
     * 巻情報を削除するメソッド。
     *
     * @param context コメテキスト
     * @param id 主キー値
     **/
    public static void Delete(Context context,int id) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String sql ="Delete from LibraryChild where parent_id=?";
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
