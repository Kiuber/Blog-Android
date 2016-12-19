package com.kiuber.blog.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kiuber.blog.R;
import com.kiuber.blog.bean.FileBean;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kiuber on 2016/12/19.
 */
public class FileActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private TextView mTvPath;
    private ListView mLvFile;
    private List<FileBean> beanList;
    private FileAdapter fileAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        initView();
        initData();
    }

    private void initView() {
        mTvPath = (TextView) findViewById(R.id.tv_path);
        mLvFile = (ListView) findViewById(R.id.lv_file);
    }

    private void initData() {
        if (hasSDCardMounted()) {
            String sdcard1 = Environment.getExternalStorageDirectory().getAbsoluteFile().getParentFile().getParentFile() + "/sdcard1/";
            beanList = getFileModel(sdcard1);
            if (beanList != null) {
                fileAdapter = new FileAdapter();
                mLvFile.setAdapter(fileAdapter);
                mLvFile.setOnItemClickListener(this);
            }
        } else {
            Toast.makeText(this, "SDCard is not mounted!", Toast.LENGTH_SHORT).show();
        }
    }

    private List<FileBean> getFileModel(String path) {
        File file = new File(path);
        if (isExsit(file)) {
            if (file.canRead()) {
                mTvPath.setText(path);
                List<FileBean> folderBeanList = new ArrayList<>();
                List<FileBean> fileBeanList = new ArrayList<>();

                for (File f :
                        file.listFiles()) {
                    FileBean bean = new FileBean();
                    if (f.isDirectory()) {
                        bean.setFile_name(f.getName() + "/");
                        bean.setFile_time(formatUnixTime(f.lastModified()));
                        if (f.getAbsoluteFile().listFiles() != null) {
                            int length = f.getAbsoluteFile().listFiles().length;
                            bean.setFile_folder_num(length);
                        }
                        folderBeanList.add(bean);
                    } else {
                        bean.setFile_name(f.getName());
                        bean.setFile_time(formatUnixTime(f.lastModified()));

                        fileBeanList.add(bean);
                    }
                }

                //Collections.sort(folderBeanList, Collator.getInstance(Locale.ENGLISH));
                //Collections.sort(fileBeanList, Collator.getInstance(Locale.ENGLISH));

                for (int i = 0; i < fileBeanList.size(); i++) {
                    folderBeanList.add(fileBeanList.get(i));
                }

                // 不是根目录加上返回
                if (!path.equals("/")) {
                    FileBean fileBean = new FileBean();
                    fileBean.setFile_name("../");
                    folderBeanList.add(0, fileBean);
                }

                return folderBeanList;
            } else {
                Toast.makeText(this, "文件夹不可读", Toast.LENGTH_SHORT).show();
                return null;
            }
        } else {
            // 点击的文件
            return null;
        }
    }

    private String formatUnixTime(long unix) {
        if (unix != 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String result = sdf.format(new Date(unix));
            return result;
        } else {
            return "";
        }
    }

    private ArrayList<String> getFileName(String path) {
        ArrayList<String> fileName = new ArrayList<>();
        File file = new File(path);
        if (isExsit(file)) {
            if (file.canRead()) {
                mTvPath.setText(path);
                // 不是根目录加上返回
                if (!path.equals("/")) {
                    fileName.add("../");
                }
                for (File f :
                        file.listFiles()) {
                    if (f.isFile()) {
                        fileName.add(f.getName());
                    } else if (f.isDirectory()) {
                        fileName.add(f.getName() + "/");
                    }
                }
                return fileName;
            } else {
                Toast.makeText(this, "文件夹不可读", Toast.LENGTH_SHORT).show();
                return null;
            }
        } else {
            // 点击的文件
            return null;
        }
    }

    private boolean isExsit(File file) {
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }


    private boolean hasSDCardMounted() {
        String state = Environment.getExternalStorageState();
        if (state != null && state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FileBean fileBean = beanList.get(position);
        if (fileBean.getFile_name().contains("../")) {
            backParent();
        } else if (fileBean.getFile_name().contains("/")) {
            Log.d("TAG", "onItemClick: " + mTvPath.getText().toString() + fileBean.getFile_name());
            List<FileBean> fileBeanList1 = getFileModel(mTvPath.getText().toString() + fileBean.getFile_name());
            if (fileBeanList1 != null) {
                beanList = fileBeanList1;
                fileAdapter.notifyDataSetChanged();
            }
        } else {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(fileBean.getFile_name())), "*/*");
            startActivity(intent);
        }
    }

    private void backParent() {
        String path = mTvPath.getText().toString();
        File file = new File(path);
        path = file.getParent();
        List<FileBean> fileBeanList1;
        // 不是根目录不加:"/"
        if (!path.equals("/")) {
            fileBeanList1 = getFileModel(path + "/");
            Log.d("TAG", "backParent: " + fileBeanList1);
        } else {
            fileBeanList1 = getFileModel(path);
        }
        beanList = fileBeanList1;
        fileAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mTvPath.getText().toString().equals("/")) {
            finish();
            return true;
        } else {
            backParent();
            return false;
        }
    }

    class FileAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return beanList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (viewHolder == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(FileActivity.this, R.layout.item_lv_file, null);
                viewHolder.initView(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.initData(position);
            return convertView;
        }

        class ViewHolder {
            private TextView mTvFileName;
            private TextView mTvFileTime;
            private TextView mTvFileLength;

            private void initView(View view) {
                mTvFileName = (TextView) view.findViewById(R.id.tv_file_name);
                mTvFileTime = (TextView) view.findViewById(R.id.tv_file_time);
                mTvFileLength = (TextView) view.findViewById(R.id.tv_file_length);
            }

            private void initData(int position) {
                FileBean bean = beanList.get(position);
                mTvFileName.setText(bean.getFile_name());
                mTvFileTime.setText(bean.getFile_time());
                if (!bean.getFile_name().equals("../")) {
                    if (bean.getFile_name().contains("/")) {
                        mTvFileLength.setText(bean.getFile_folder_num() + " 项");
                        Drawable drawable = null;
                        drawable = getResources().getDrawable(R.drawable.ic_folder_open_black_24dp);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        mTvFileName.setCompoundDrawables(drawable, null, null, null);
                    } else {
                        Drawable drawable = null;
                        drawable = getResources().getDrawable(R.drawable.ic_attachment_black_24dp);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        mTvFileName.setCompoundDrawables(drawable, null, null, null);
                    }
                }
            }
        }
    }
}
