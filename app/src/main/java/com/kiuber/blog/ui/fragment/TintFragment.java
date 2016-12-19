package com.kiuber.blog.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kiuber.blog.R;
import com.kiuber.blog.bean.Tint;
import com.kiuber.blog.ui.activity.MainActivity;
import com.kiuber.blog.ui.activity.NewActivity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Kiuber on 2016/12/19.
 */

public class TintFragment extends Fragment implements View.OnClickListener {

    private View view;
    private ListView mLvAll;
    private List<Tint> mTintList;
    private MyAdapter myAdapter;
    private LinearLayout mLlNew;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tint, null);
        initView();
        queryData();
        return view;
    }

    private void initView() {
        mLvAll = (ListView) view.findViewById(R.id.lv_all);
        mLvAll.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("确定删除" + mTintList.get(position).getContent() + "？");
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteData(position);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
                return false;
            }
        });

        view.findViewById(R.id.ll_new).setOnClickListener(this);

    }

    private void deleteData(final int position) {
        Tint tint = new Tint();
        tint.delete(mTintList.get(position).getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                    mTintList.remove(position);
                    myAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void queryData() {
        BmobQuery<Tint> tintBmobQuery = new BmobQuery<>();
        tintBmobQuery.order("-createdAt");
        tintBmobQuery.findObjects(new FindListener<Tint>() {
            @Override
            public void done(List<Tint> list, BmobException e) {
                if (e == null) {
                    mTintList = list;
                    myAdapter = new MyAdapter();
                    mLvAll.setAdapter(myAdapter);
                } else {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_new:
                startActivity(new Intent(getContext(), NewActivity.class));
                break;
        }
    }


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTintList.size();
        }

        @Override
        public Tint getItem(int position) {
            return mTintList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            View view = null;
            if (viewHolder == null) {
                viewHolder = new ViewHolder();
                view = View.inflate(getContext(), R.layout.item_lv_all, null);
                viewHolder.initView(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.initData(position);
            return view;
        }

        class ViewHolder {

            private TextView mTvDevice;
            private TextView mTvAddress;
            private TextView mTvContent;
            private TextView mTvCreatedAt;
            private TextView mTvUpdatedAt;
            private View view;

            private void initView(View view) {
                mTvDevice = (TextView) view.findViewById(R.id.tv_device);
                mTvAddress = (TextView) view.findViewById(R.id.tv_address);
                mTvContent = (TextView) view.findViewById(R.id.tv_content);
                mTvCreatedAt = (TextView) view.findViewById(R.id.tv_created_at);
                mTvUpdatedAt = (TextView) view.findViewById(R.id.tv_updated_at);
            }

            private void initData(final int position) {
                mTvDevice.setText(getItem(position).getDevice());
                mTvAddress.setText(getItem(position).getAddress());
                mTvContent.setText(getItem(position).getContent());
                mTvCreatedAt.setText(getItem(position).getCreatedAt());
                mTvUpdatedAt.setText(getItem(position).getUpdatedAt());
                mTvContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeContent(position);
                    }
                });
            }

            private void changeContent(final int position) {
                view = View.inflate(getContext(), R.layout.view_change_content, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(view);

                TextView mTvOldContent = (TextView) view.findViewById(R.id.tv_old_content);
                final EditText mEtNewContent = (EditText) view.findViewById(R.id.et_new_content);
                Button mBtnChange = (Button) view.findViewById(R.id.btn_change);

                mTvOldContent.setText(getItem(position).getContent());

                Dialog dialog = builder.create();
                dialog.show();


                mBtnChange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateContent2Bmob(getItem(position).getObjectId(), mEtNewContent);
                    }
                });
            }

            private void updateContent2Bmob(String objectId, EditText mEtNewContent) {
                Tint tint = new Tint();
                tint.setContent(mEtNewContent.getText().toString());
                tint.update(objectId, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(getContext(), "更新成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }
    }
}
