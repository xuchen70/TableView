package com.zero.tableview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TableTextView tableTextView = (TableTextView) findViewById(R.id.tableview);
        tableTextView.setOnTouchTableListener(new TableTextView.OnTouchTableListener() {
            @Override
            public void onTouchTable(int position, String clickContent) {
                Toast.makeText(MainActivity.this,"current content:"+clickContent+"  position:"+position,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
