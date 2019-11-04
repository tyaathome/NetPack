package com.tyaathome.netpack;

import android.os.Bundle;

import com.tyaathome.annotation.PackDown;
import com.tyaathome.annotation.NetPack;

import androidx.appcompat.app.AppCompatActivity;

@PackDown
@NetPack
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
