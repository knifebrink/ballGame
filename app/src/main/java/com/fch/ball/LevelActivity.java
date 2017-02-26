package com.fch.ball;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class LevelActivity extends AppCompatActivity {
    private ImageButton[] btn=new ImageButton[4];
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        btn[0]=  (ImageButton) findViewById(R.id.level1Btn);
        btn[1]=  (ImageButton) findViewById(R.id.level2Btn);
        btn[0].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LevelActivity.this,GameActivity.class);
                intent.putExtra("level",1);
                startActivity(intent);
            }
        });
        btn[1].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LevelActivity.this,GameActivity.class);
                intent.putExtra("level",2);
                startActivity(intent);
            }
        });

    }

}
