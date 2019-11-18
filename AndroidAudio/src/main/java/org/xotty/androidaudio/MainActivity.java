package org.xotty.androidaudio;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv=findViewById(R.id.tv);
        tv.setText("Hello ZWorld");
        ImageView imageView=findViewById(R.id.imv);
        imageView.setImageResource(R.drawable.mgirl2);
    }
}
