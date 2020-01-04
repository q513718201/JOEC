package com.lsu.joec;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lsu.joec.utils.DensityUtils;
import com.lsu.joec.utils.FileUtils;
import com.lsu.joec.utils.StatusBarUtil;
import com.lsu.joec.widget.MyWebview;
import com.lsu.joec.widget.ProgressDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;


public class MainNewActivity extends AppCompatActivity {

    private MyWebview webView;
    private Context mContext;
    private RxPermissions permissionsnew;
    private ProgressDialog mDialog;
    private LinearLayout linearLayout;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  DensityUtils.setStatusBarTransparent(this);
       // DensityUtils.setSystemUI(this,true);
       //StatusBarUtil.setRootViewFitsSystemWindows(this,false);
        StatusBarUtil.setTranslucentStatus(this);
        webView = findViewById(R.id.web_view);

        mContext = this;
        permissionsnew=new RxPermissions(this);
        mDialog=new ProgressDialog(this);
        JsInterface jsInterface = new JsInterface();
        webView.addJavascriptInterface(jsInterface, "JsInterface");
//        mBinding.webView.loadUrl("file:///android_asset/javascript.html");
       // webView.loadUrl("file:///android_asset/javascript.html");
        webView.loadUrl("http://w103.ratafee.nl/web/");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mDialog.dismiss();
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == 0) {
                    String result = data.getStringExtra("result");

                    webView.loadUrl("javascript:setScanAddress('"+result+"')");
        }
    }

    public class JsInterface {
        @JavascriptInterface
        public void goScan() {
            requestPermissions1();

        }

        @SuppressLint("CheckResult")
        @JavascriptInterface
        public void saveImg(String img) {


            permissionsnew.request(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE

            ).subscribe(aBoolean -> {
                if(aBoolean){
                    Bitmap bitmap = FileUtils.INSTANCE.base64ToPicture(img);
                    FileUtils.INSTANCE.saveBmp2Gallery(getApplicationContext(),bitmap,"qrcode");

                }
            });
        }

    }

    @SuppressLint("CheckResult")
    private void requestPermissions1() {


        permissionsnew.request(
                Manifest.permission.CAMERA

        ).subscribe(aBoolean -> {
            if(aBoolean){
                Intent intent = new Intent(getApplicationContext(), ScanCoinAddressActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }
    private long[] mHits = new long[2];
    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
            mHits[mHits.length - 1] = SystemClock.uptimeMillis();
            if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
                finish();
            } else {
                Toast.makeText(getApplicationContext(), R.string.tap_twice_2_exit, Toast.LENGTH_SHORT).show();
            }
        }

    }
}
