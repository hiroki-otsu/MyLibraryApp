package local.hal.st32.android.mylibrary50054;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class ChildEditActivity extends AppCompatActivity {
    /**
     * 新規登録モードが更新モードか表すフィールド。
     */
    private int _mode = LibraryListActivity.MODE_INSERT;
    /**
     * 更新モードの際、現在表示しているデータベース上の主キー値。
     */
    private int _idNo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_edit);

        Intent intent = getIntent();
        _mode = intent.getIntExtra("mode", LibraryListActivity.MODE_INSERT);

        //アクションバーに前画面に戻る機能をつける
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        _idNo = intent.getIntExtra("idNo", 0);

        LibraryList libraryData = LibraryParentDataAccess.findByPK(ChildEditActivity.this, _idNo);
        LibraryChild childData = LibraryChildDataAccess.findByPK(ChildEditActivity.this, _idNo);
        TextView tvLibraryName = (TextView) findViewById(R.id.tv_child_name);
        tvLibraryName.setText(libraryData.get_name());

        if (_mode == LibraryListActivity.MODE_INSERT) {

        } else {
            EditText etInputChildVolume = (EditText) findViewById(R.id.etInputLibraryVolume);
            Integer volume = childData.getVolume();
            etInputChildVolume.setText(volume.toString());
        }
    }
    /**
     * オプションメニューを作成するメソッド
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.library_edit_menu, menu);

        if (_mode == LibraryListActivity.MODE_INSERT) {
            MenuItem item = menu.findItem(R.id.actionDelete);
            item.setVisible(false);
        }
        return true;
    }
    /**
     * アクションバーの保存・削除が押されたときの処理
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Cursor cursor = null;
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();//前の画面に戻る
                return true;
            case R.id.actionDelete:
//                showDialog();//ダイヤログメソッド
                break;
            case R.id.actionSave:
                OnClickSave();//保存メソッド
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * アクションバーの保存が押されたときの処理
     */
    public void OnClickSave() {

        AlertDialog.Builder builder = null;
        AlertDialog dialog = null;

        EditText etInputLibraryVolume = (EditText) findViewById(R.id.etInputLibraryVolume);
        String libraryVolume = etInputLibraryVolume.getText().toString();
        if (libraryVolume.equals("")) {
            builder = new AlertDialog.Builder(ChildEditActivity.this);
            builder.setMessage("巻が入力されていません。\n入力してください!");
            builder.setPositiveButton("OK", new ChildEditActivity.SimpleDialogPositiveButtonClickListener());
            dialog = builder.create();
            dialog.show();
        } else {
            TextView tvLibraryChildName = (TextView) findViewById(R.id.tv_child_name);
            String libraryName = tvLibraryChildName.getText().toString();

            Calendar cal = Calendar.getInstance();
            int nowYear = cal.get(Calendar.YEAR);
            int nowMonth = cal.get(Calendar.MONTH);
            int nowDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            String update;

            if (nowMonth >= 10) {
                update =(nowYear + "年" + (nowMonth + 1) + "月" + nowDayOfMonth + "日");
            } else {
                update = (nowYear + "年" + "0" + (nowMonth + 1) + "月" + nowDayOfMonth + "日");
            }

            if (_mode == LibraryListActivity.MODE_INSERT) {
                LibraryParentDataAccess.ParentUpdateLine(ChildEditActivity.this, _idNo,update);
                LibraryChildDataAccess.ChildInsert(ChildEditActivity.this, _idNo,libraryName,libraryVolume);
            } else {
//                LibraryParentDataAccess.ChildInsert(ChildEditActivity.this, _idNo, libraryName, image, libraryAuthor, libraryPublisher, update);
            }
            finish();
        }
    }
    /**
     * シンプルダイヤログのOKが押されたときの処理が記述されたメソッドクラス。
     *
     * @author HIRO
     */
    private class SimpleDialogPositiveButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
        }
    }
}
