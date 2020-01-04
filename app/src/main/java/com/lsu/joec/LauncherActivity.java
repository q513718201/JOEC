package com.lsu.joec;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.lsu.joec.utils.RxBus;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * create by sanvar on 19-4-2
 */
public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.get().addSubscription(this, Observable.just(5)
                .delay(2000, TimeUnit.MILLISECONDS)
                .subscribe(integer -> {
                        startActivity(new Intent(this, MainNewActivity.class));
                    finish();
                }));
    }


}
