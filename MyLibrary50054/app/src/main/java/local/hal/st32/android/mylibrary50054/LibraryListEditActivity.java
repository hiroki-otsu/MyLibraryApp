package local.hal.st32.android.mylibrary50054;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Calendar;

public class LibraryListEditActivity extends AppCompatActivity {
    /**
     *
     */
    RadioGroup radioGroup;
    /**
     * 新規登録モードが更新モードか表すフィールド。
     */
    private int _mode = LibraryListActivity.MODE_INSERT;
    /**
     * 更新モードの際、現在表示しているデータベース上の主キー値。
     */
    private int _idNo = 0;
    /**
     *
     */
    CheckBox music;
    CheckBox academy;
    CheckBox love ;
    CheckBox comedy;
    private  int image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_list_edit);

        music = (CheckBox) findViewById(R.id.cb_music);
        academy = (CheckBox) findViewById(R.id.cb_academy);
        love = (CheckBox) findViewById(R.id.cb_love);
        comedy = (CheckBox) findViewById(R.id.cb_comedy);

        Intent intent = getIntent();
        _mode = intent.getIntExtra("mode", LibraryListActivity.MODE_INSERT);

        radioGroup = (RadioGroup) findViewById(R.id.rg_novel_cartoon);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1) {
                    // 選択されているラジオボタンの取得
                    RadioButton radioButton = (RadioButton) findViewById(checkedId);

                    // ラジオボタンのテキストを取得
                    String text = radioButton.getText().toString();
                }
            }
        });

        //アクションバーに前画面に戻る機能をつける
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (_mode == LibraryListActivity.MODE_INSERT) {
            TextView tvTitleEdit = (TextView) findViewById(R.id.tvTitleEdit);
            tvTitleEdit.setText(R.string.tv_title_insert);
        } else {
            _idNo = intent.getIntExtra("idNo", 0);
            LibraryList libraryData = LibraryParentDataAccess.findByPK(LibraryListEditActivity.this, _idNo);

            String str =libraryData.getCategory();
            if (str.equals("漫画")){
                RadioButton rb_cartoon = (RadioButton) findViewById(R.id.rb_cartoon);
                rb_cartoon.setChecked(true);
            }
            else {
                RadioButton rb_novel = (RadioButton) findViewById(R.id.rb_novel);
                rb_novel.setChecked(true);
            }

            String[] genreArray =libraryData.getGenre().split(" ",-1);

            for (int i=0;i<genreArray.length;i++){
                if(genreArray[i].equals("音楽")){
                    music = (CheckBox) findViewById(R.id.cb_music);
                    music.setChecked(true);
                }
                if(genreArray[i].equals("学園")){
                    academy = (CheckBox) findViewById(R.id.cb_academy);
                    academy.setChecked(true);
                }
                if(genreArray[i].equals("恋愛・ラブコメ")){
                    love = (CheckBox) findViewById(R.id.cb_love);
                    love.setChecked(true);
                }
                if(genreArray[i].equals("コメディー")){
                    comedy = (CheckBox) findViewById(R.id.cb_comedy);
                    comedy.setChecked(true);
                }
            }
            EditText etInputLibraryName = (EditText) findViewById(R.id.etInputLibraryTitle);
            etInputLibraryName.setText(libraryData.get_name());

            EditText etInputLibraryAuthor = (EditText) findViewById(R.id.etInputAuthor);
            etInputLibraryAuthor.setText(libraryData.getAuthor());

            EditText etInputLibraryPublisher = (EditText) findViewById(R.id.etInputPublisher);
            etInputLibraryPublisher.setText(libraryData.getPublisher());
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
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();//前の画面に戻る
                return true;
            case R.id.actionDelete:
                Dialog();//ダイヤログメソッド
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

        int checkedId = radioGroup.getCheckedRadioButtonId();

        // 選択されているラジオボタンの取得
        RadioButton radioButton = (RadioButton) findViewById(checkedId);// (Fragmentの場合は「getActivity().findViewById」にする)
        // ラジオボタンのテキストを取得
        String category = radioButton.getText().toString();

        if(category.equals("漫画")){
            image=1;
        }else {
            image=2;
        }
        EditText etInputLibraryName = (EditText) findViewById(R.id.etInputLibraryTitle);
        String libraryName = etInputLibraryName.getText().toString();
        if (libraryName.equals("")) {
            builder = new AlertDialog.Builder(LibraryListEditActivity.this);
            builder.setMessage("「本のタイトル」が入力されていません。\n入力してください!");
            builder.setPositiveButton("OK", new SimpleDialogPositiveButtonClickListener());
            dialog = builder.create();
            dialog.show();
        } else {
            EditText etInputLibraryAuthor = (EditText) findViewById(R.id.etInputAuthor);
            String libraryAuthor = etInputLibraryAuthor.getText().toString();

            EditText etInputLibraryPublisher = (EditText) findViewById(R.id.etInputPublisher);
            String libraryPublisher = etInputLibraryPublisher.getText().toString();

            Calendar cal = Calendar.getInstance();
            int nowYear = cal.get(Calendar.YEAR);
            int nowMonth = cal.get(Calendar.MONTH);
            int nowDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            String update =null;
            if (nowMonth >= 10) {
                update =(nowYear + "年" + (nowMonth + 1) + "月" + nowDayOfMonth + "日");
            } else {
                update = (nowYear + "年" + "0" + (nowMonth + 1) + "月" + nowDayOfMonth + "日");
            }
            String genre="";
            if (music.isChecked()){
                genre+=music.getText().toString()+" ";
            }else {
                genre.replace("音楽","");
            }
            if (academy.isChecked()){
                genre+=academy.getText().toString()+" ";
            }else {
                genre.replace("学園","");
            }
            if (love.isChecked()){
                genre+=love.getText().toString()+" ";
            }else {
                genre.replace("恋愛・ラブコメ","");
            }
            if (comedy.isChecked()){
                genre+=comedy.getText().toString()+" ";
            }else {
                genre.replace("コメディー","");
            }

                if (_mode == LibraryListActivity.MODE_INSERT) {
                LibraryParentDataAccess.ParentInsert(LibraryListEditActivity.this, libraryName, category,image,genre,libraryAuthor, libraryPublisher, update);
            } else {
                LibraryParentDataAccess.ParentUpdate(LibraryListEditActivity.this, libraryName, category, image,genre,libraryAuthor, libraryPublisher, update, _idNo);
            }
            finish();
        }
    }
    /**
     * 削除ダイヤログを表示させるメソッド
     */
    public void Dialog() {
        AlertDialog.Builder builder = null;
        AlertDialog dialog = null;

        builder = new AlertDialog.Builder(LibraryListEditActivity.this);
        builder.setTitle("削除");
        builder.setMessage("この本を削除してよろしいですか?");
        builder.setPositiveButton("OK", new LibraryListEditActivity.DialogButtonClickListener());
        builder.setNegativeButton("NG", new LibraryListEditActivity.DialogButtonClickListener());
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
                    LibraryParentDataAccess.Delete(LibraryListEditActivity.this, _idNo);//親
                    LibraryChildDataAccess.ChildDelete(LibraryListEditActivity.this, _idNo);//子
                    finish();
                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    break;
                case  DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
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

