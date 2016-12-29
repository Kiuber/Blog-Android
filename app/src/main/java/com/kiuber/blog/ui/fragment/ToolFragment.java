package com.kiuber.blog.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kiuber.blog.R;
import com.kiuber.blog.ui.activity.SmsActivity;

/**
 * Created by Kiuber on 2016/12/28.
 */

public class ToolFragment extends Fragment implements View.OnClickListener {

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tool, null);
        initView();
        return view;
    }

    private void initView() {
        view.findViewById(R.id.btn_sms).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sms:
                startActivity(new Intent(getContext(), SmsActivity.class));
                break;
        }
    }
}
