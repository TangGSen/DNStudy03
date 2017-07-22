package com.sen.xutilsframwork;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.sen.xutilsframwork.annation.ContentView;
import com.sen.xutilsframwork.annation.OnClick;
import com.sen.xutilsframwork.annation.OnLongClick;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

//    @ViewInject(R.id.button)
//    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_main);
    }

    @OnLongClick({R.id.button,R.id.button2})
    public boolean onLongClicks(View view){
        if (view.getId()==R.id.button){
            Toast.makeText(this,"onLongClicks button1",Toast.LENGTH_SHORT).show();
        }else if(view.getId()==R.id.button2){
            Toast.makeText(this,"onLongClicks button2",Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    @OnClick({R.id.button,R.id.button2})
    public void onClicks(View view){
        if (view.getId()==R.id.button){
            Toast.makeText(this,"onclicks button",Toast.LENGTH_SHORT).show();
        }else if(view.getId()==R.id.button2){
            Toast.makeText(this,"onclicks button2",Toast.LENGTH_SHORT).show();
        }
    }

}
