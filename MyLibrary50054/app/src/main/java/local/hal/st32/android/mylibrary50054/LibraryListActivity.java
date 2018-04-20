package local.hal.st32.android.mylibrary50054;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.TextView;

public class LibraryListActivity extends AppCompatActivity {
    /**
     *
     */
    private int dialog_id = 0;
    /**
     *
     */
    private static SimpleCursorTreeAdapter Adapter ;
    /**
     * リストビュー
     */
    private ExpandableListView _lvLibrary;
    /**
     * 新規登録モードを表す定数フィールド。
     */
    static final int MODE_INSERT = 1;
    /**
     * 更新モードを表す定数フィールド。
     */
    static final int MODE_EDIT = 2;
    /**
     * 削除モード表すモード(子要素)フィールド。
     */
    private final int CHILD_DELETE=3;
    /**
     * CHILDモード表すモード(子要素)フィールド。
     */
    private int MODE_CHILD =0;
    /**
     * プレファレンスファイルのファイル名
     */
    private static final String TOTAL_LIBRARY = "LibraryFile";
    /**
     * すべて表示を表す定数
     */
    private static final int SWITCH_ALL = 1;
    /**
     * 小説を表す定数
     */
    private static final int SWITCH_NOVEL = 2;
    /**
     * 漫画を表す定数
     */
    private static final int SWITCH_CARTOON = 3;
    /**
     *
     */
    private static int typeSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_list);

        ExpandableList();//メソッドを呼び出す
    }
    /**
     * 画面が繰り替わる毎に読み込むメソッド
     */
    @Override
    public void onResume(){
        Cursor cursor = null;
        setNewCursor(cursor);
        super.onResume();
    }
    /**
     * カーソルアダプタ内のカーソルを更新するメソッド
     */
    private void setNewCursor(Cursor cursor) {

        SharedPreferences settingsSwitch = getSharedPreferences(TOTAL_LIBRARY,MODE_PRIVATE);
        typeSwitch = settingsSwitch.getInt("Switch",SWITCH_ALL);
        switch (typeSwitch) {
            case 1:
                cursor = LibraryParentDataAccess.findByAll(LibraryListActivity.this);//「切り替え/すべて」
                break;
            case 2:
                cursor = LibraryParentDataAccess.ListNovel(LibraryListActivity.this);//「切り替え/小説」
                break;
            case 3:
                cursor = LibraryParentDataAccess.ListCartoon(LibraryListActivity.this);//「切り替え/漫画」
                break;
        }
        if(cursor == null) {
            cursor = LibraryParentDataAccess.findByAll(LibraryListActivity.this);
        }
        Adapter.changeCursor(cursor);
    }
    /**
     * ExpandableListを作るメソッド
     */
    public void ExpandableList() {
        Cursor cursor = null;
        Adapter = new ExpandableListAdapter(
                LibraryListActivity.this, cursor,
                R.layout.library_parent_view,
                new String[]{"name","author","category","image","updateLine","Genre"},
                new int[]{R.id.lv_library_name,R.id.lv_library_author,R.id.lv_library_category,R.id.img_icon,R.id.lv_update_line,R.id.lv_genre},
                R.layout.library_child_view,
                new String[]{"ChildName","Volume"},
                new int[]{R.id.lv_child_name,R.id.lv_Child_volume}
        );
        _lvLibrary = (ExpandableListView)findViewById(R.id.lv_Library);

        Adapter.setViewBinder(new CustomViewBinder());
        _lvLibrary.setAdapter(Adapter);
        registerForContextMenu(_lvLibrary);
    }
    /**
     *  ExpandableListAdapterのアダプター
     */
    public class ExpandableListAdapter extends SimpleCursorTreeAdapter {

        public ExpandableListAdapter(Context context, Cursor groupCursor,
                                     int groupLayout, String[] groupFrom, int[] groupTo,
                                     int childLayout, String[] childFrom, int[] childTo) {
            super(context, groupCursor, groupLayout, groupFrom, groupTo, childLayout, childFrom, childTo);
        }
        /**
         *子要素のCursorのメソッド
         *
         * @param groupCursor
         * @return
         */
        @Override
        protected Cursor getChildrenCursor(Cursor groupCursor) {
            int id = groupCursor.getInt(groupCursor.getColumnIndex("_id"));
            Cursor cursor = LibraryChildDataAccess.findByOneDate(LibraryListActivity.this, id);
            return cursor;
        }
    }
    /**
     * リストビューのカスタマイズビューバインダークラス。
     *
     * @author hiro
     */
    private class CustomViewBinder implements SimpleCursorTreeAdapter.ViewBinder {
        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
            int viewId = view.getId();
            switch (viewId){
                case R.id.lv_library_name:////親要素「タイトル」のR値
                    TextView tvTitle = (TextView) view;
                    String Library_name = (String) cursor.getString(columnIndex);
                    tvTitle.setText(Library_name);
                    return true;
                case R.id.lv_library_author://親要素「作者」のR値
                    TextView tvAuthor = (TextView) view;
                    String Library_author = (String) cursor.getString(columnIndex);
                    tvAuthor.setText(Library_author);
                    return true;
                case R.id.lv_library_category://親要素「カテゴリー」のR値
                    TextView tvCategory = (TextView) view;
                    String Library_category = (String) cursor.getString(columnIndex);
                    tvCategory.setText(Library_category);
                    return true;
                case R.id.lv_update_line://親要素「更新日」のR値
                    TextView tvUpdate = (TextView) view;
                    String Update_line = (String) cursor.getString(columnIndex);
                    tvUpdate.setText(Update_line);
                    return true;
                case R.id.lv_child_name://子要素「名前」のR値
                    TextView tvChildName = (TextView) view;
                    String childName = (String) cursor.getString(columnIndex);
                    tvChildName.setText(childName);
                    return true;
                case R.id.lv_Child_volume://子要素「巻」のR値
                    TextView tvChildVolume = (TextView) view;
                    String name = (String) cursor.getString(columnIndex);
                    tvChildVolume.setText(name);
                    return true;
                case R.id.img_icon:
                    int  iconNumber = cursor.getInt(columnIndex);
                    ImageView icon = (ImageView) view;
                    switch (iconNumber){
                        case 1:
                            icon.setImageResource(R.drawable.icon_catoonicon);
                            return true;
                        case 2:
                            icon.setImageResource(R.drawable.icon_novel);
                            return true;
                    }
                    return true;
                case R.id.lv_genre:
                    TextView tvGenre = (TextView) view;
                    String genre = (String) cursor.getString(columnIndex);
                    tvGenre.setText(genre);
            }
            return false;
        }
    }
    /**
     * コンテキストメニュー作成メソッド
     *
     * @param menu
     * @param view
     * @param menuInfo
     */
    @Override
    public  void  onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu,view,menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_library_menu, menu);

        long libraryPosition = ((ExpandableListView.ExpandableListContextMenuInfo) menuInfo).packedPosition;

        int library = ExpandableListView.getPackedPositionType(libraryPosition);
        if (library == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {//親要素のコンテキストメニューなのか比較

            MenuItem dItem = menu.findItem(R.id.menuContextChild_Change);
            dItem.setVisible(false);

            dItem = menu.findItem(R.id.menuContextChild_Delete);
            dItem.setVisible(false);

        }
        if (library == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {//子要素のコンテキストメニューなのか比較

            MenuItem dItem = menu.findItem(R.id.menuContextChild);
            dItem.setVisible(false);

            dItem = menu.findItem(R.id.menuContextParent);
            dItem.setVisible(false);

            dItem = menu.findItem(R.id.menuContextDelete);
            dItem.setVisible(false);
        }
    }
    /**
     * コンテキストメニューが選択されたときの処理
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();
        int listId = (int)info.id;
        dialog_id = (int)info.id;
        int itemId = item.getItemId();
        Intent intent;
        switch (itemId){
            case R.id.menuContextChild://親要素から子要素[追加]
                intent = new Intent(LibraryListActivity.this,ChildEditActivity.class);
                intent.putExtra("mode",MODE_INSERT);
                intent.putExtra("idNo", listId);
                startActivity(intent);
                break;
            case R.id.menuContextParent://親要素[編集]
                intent = new Intent(LibraryListActivity.this,LibraryListEditActivity.class);
                intent.putExtra("mode",MODE_EDIT);
                intent.putExtra("idNo", listId);
                startActivity(intent);
                break;
            case R.id.menuContextDelete://親要素[削除]
                Dialog();
                break;
            case R.id.menuContextChild_Change://子要素[変更]
                intent = new Intent(LibraryListActivity.this,ChildEditActivity.class);
                intent.putExtra("mode",MODE_EDIT);
                intent.putExtra("idNo", listId);
                startActivity(intent);
                break;
            case R.id.menuContextChild_Delete://子要素[削除]
                Dialog();
                MODE_CHILD=CHILD_DELETE;
                break;
        }

        return super.onContextItemSelected(item);
    }
    /**
     *  オプションメニューを作成するメソッド
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.library_options, menu);
        return true;
    }
    /**
     *  選択されたオプションメニューを表示するメソッド
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences settings = getSharedPreferences(TOTAL_LIBRARY,MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        Cursor cursor = null;
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.switching_All:
                cursor = LibraryParentDataAccess.findByAll(LibraryListActivity.this);//「切り替え/すべて」
                editor.putInt("Switch",SWITCH_ALL);
                break;
            case R.id.switching_novel:
                cursor = LibraryParentDataAccess.ListNovel(LibraryListActivity.this);//「切り替え/小説」
                editor.putInt("Switch",SWITCH_NOVEL);
                break;
            case R.id.switching_cartoon:
                cursor = LibraryParentDataAccess.ListCartoon(LibraryListActivity.this);//「切り替え/漫画」
                editor.putInt("Switch",SWITCH_CARTOON);
                break;
        }
        editor.commit();
        setNewCursor(cursor);
        return super.onOptionsItemSelected(item);
    }
    /**
     * FAB新規ボタンが押されたときのイベント処理用メソッド
     *
     * @param view 画面部品。
     */
    public void onFabNewTaskClick(View view) {
        Intent intent = new Intent(LibraryListActivity.this,LibraryListEditActivity.class);
        intent.putExtra("mode",MODE_INSERT);
        startActivity(intent);
    }
    /**
     * 削除ダイヤログを表示させるメソッド
     */
    public void Dialog() {
        AlertDialog.Builder builder = null;
        AlertDialog dialog = null;

        builder = new AlertDialog.Builder(LibraryListActivity.this);
        builder.setTitle("削除");
        builder.setMessage("この本を削除してよろしいですか?");
        builder.setPositiveButton("OK", new DialogButtonClickListener());
        builder.setNegativeButton("NG", new DialogButtonClickListener());
        dialog = builder.create();
        dialog.show();
    }
    /**
     * 通常ダイヤログのボタンが押されたときの処理が記述されたメソッドクラス。
     *
     * @author HIRO
     */
    private class  DialogButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    if(MODE_CHILD==CHILD_DELETE){
                        LibraryChildDataAccess.ChildDelete(LibraryListActivity.this, dialog_id);
                    }
                    else{
                        LibraryParentDataAccess.Delete(LibraryListActivity.this, dialog_id);
                    }
                    onResume();
                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    break;
                case  DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    }

}
