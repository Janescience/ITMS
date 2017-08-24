package system.management.information.itms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

public class SplashScreenActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ImageView imageView = (ImageView) findViewById(R.id.logo_splash);
        imageView.setImageResource(R.drawable.icon_it);

        Thread myThread = new Thread(){
            public void run(){
                try{
                    sleep(5000);

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);


                    finish();
                }catch (InterruptedException e){
                    e.printStackTrace();

                }
            }
        };
        myThread.start();
    }
}
