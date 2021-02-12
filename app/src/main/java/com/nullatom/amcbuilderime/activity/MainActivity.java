package com.nullatom.amcbuilderime.activity;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.nullatom.amcbuilderime.R;

public class MainActivity extends Activity {
    public Button set_input_method_btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//设置main可见
        set_input_method_btn = findViewById(R.id.set_input_method_button);//获得按钮4
        set_input_method_btn.setOnClickListener(new OnClickListener() {
            //设置输入法按
            @Override
            public void onClick(View v) {
                setInputMethod();
                MainActivity.this.showToast("请将AMCBulder设置为默认输入法\n如果找不到AMCBuilder，请点击弹窗下方 [启用输入法] 开启AMCBuilder");
            }


        });
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT);
    }

    private void setInputMethod() {
        try {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showInputMethodPicker();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
