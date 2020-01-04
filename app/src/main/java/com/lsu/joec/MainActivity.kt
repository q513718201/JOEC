package com.lsu.joec

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat.startActivity

import com.lsu.joec.widget.ProgressDialog
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Build
import android.webkit.ValueCallback


class MainActivity : AppCompatActivity() {

    private lateinit var mDialog: ProgressDialog
    private lateinit var permissionsnew: RxPermissions

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permissionsnew = RxPermissions(this)
        mDialog = ProgressDialog(this)
        web_view.setBackgroundColor(Color.DKGRAY)
        val jsInterface = JsInterface(this)
        web_view.addJavascriptInterface(jsInterface, "JsInterface")
        web_view.loadUrl("http://192.168.3.66:8080")
        web_view.webViewClient = webClient

    }


    val webClient = object : WebViewClient() {
        //处理网页加载失败时
        override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
            //这里可以将异常信息也显示出来
            //            super.onReceivedError(view, errorCode, description, failingUrl);

        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            Log.d("junjun", "jia'zai'o ")
            mDialog.show()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            Log.d("junjun", "加载完成")
            mDialog.dismiss()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            val result = data?.getStringExtra("result")
            web_view.loadUrl("javascript:setScanAddress(\"+'result'+\")")
        }
    }

   class JsInterface(mainActivity: MainActivity) {
         var context=mainActivity
       var permissionsnew = RxPermissions(mainActivity)
//        @JavascriptInterface
//        fun getToken(): String {
////            val jsonObject = JsonObject()
////            jsonObject.addProperty("token", UserManager.getInstance().getAccessToken(mContext))
//            return jsonObject.toString()
//        }

        @SuppressLint("CheckResult")
        @JavascriptInterface
        fun goScan() {
            permissionsnew=RxPermissions(context)
            permissionsnew.request(
                    Manifest.permission.CAMERA

            ).subscribe { permission ->
                if (permission!!) {
                    val intent = Intent(context, ScanCoinAddressActivity::class.java)
                    context.startActivityForResult(intent, 0)
                } else {
                    showMissingPermissionDialog(context)
                }
            }
        }

       fun showMissingPermissionDialog(mainActivity:MainActivity) {
           val builder = AlertDialog.Builder(mainActivity)
           builder.setTitle("提示")
           builder.setMessage("当前应用缺少必要权限。请点击\"设置\"-\"权限\"-打开所需权限。")
           // 拒绝, 退出应用
           builder.setNegativeButton(
                   "取消"
           ) { dialog: DialogInterface?, which: Int ->

           }
           builder.setPositiveButton(
                   "设置"
           ) { dialog, which -> startAppSettings() }
           builder.setCancelable(false)
           builder.show()
       }

       /**
        * 启动应用的设置
        */
       fun startAppSettings() {
           val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
           intent.data = Uri.parse("package:" + "com.lsu.coolluck")
           context.startActivity(intent)
       }
       }



}
