package com.siyehua.inputsoft;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.siyehua.inputlibrary.InputView;

public class StartActivity extends AppCompatActivity {
    private InputView inputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        final TextView textView = (TextView) findViewById(R.id.tv_input);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputView.show(textView.getText());
            }
        });
        inputView = InputView.getInsance(this).init(R.layout.item, true, true, false, new
                InputView.CallBack() {
            @Override
            public void textChange(CharSequence s) {
                textView.setText(s);
            }
        });
    }
}
