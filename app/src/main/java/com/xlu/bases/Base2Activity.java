package com.xlu.bases;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.pandacard.teavel.R;
import com.xlu.po.MyEvent;

import butterknife.ButterKnife;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by giant on 2017/9/4.
 */

public abstract class Base2Activity extends AppCompatActivity {
    Context context;
    PermissionCall mPermissionCall;
    public final int permissionsRequestCode=110;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        context = this;
        setContentView(initView());
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }
   public  abstract int initView();


     /*处理权限问题*/

    /**
     * 对子类提供的申请权限方法
     * @param  permissions 申请的权限
     */
    public void requestRunTimePermissions(String[] permissions, PermissionCall call){
        if(permissions==null||permissions.length==0)
            return;
        mPermissionCall=call;
        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.M||checkPermissionGranted(permissions)){
            //提示已拥有权限
            mPermissionCall.requestSucess();
        }else{
            //申请权限
            requestPermission(permissions,permissionsRequestCode);
        }
    }
    public void requestPermission(final String[] permissions, final int permissionRequestCode){
        if(shouldShowRequestPermissionRationale(permissions)){
            new AlertDialog.Builder(this).setTitle(R.string.attention)
                    .setMessage(R.string.content_to_request_permission)
                    .setPositiveButton(R.string.ok_text, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Base2Activity.this,permissions,permissionRequestCode);
                        }
                    }).show();
        }else{
            ActivityCompat.requestPermissions(Base2Activity.this,permissions,permissionRequestCode);
        }
    }
    private boolean shouldShowRequestPermissionRationale(String[] permissions){
        boolean flag=false;
        for(String p:permissions){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,p)){
                flag=true;
                break;
            }
        }
        return flag;
    }
    public boolean checkPermissionGranted(String[] permissions){
        boolean result=true;
        for(String p:permissions){
            if(ActivityCompat.checkSelfPermission(this,p)!= PackageManager.PERMISSION_GRANTED){
                result=false;
                break;
            }
        }
        return result;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case permissionsRequestCode:
                if (grantResults.length > 0
                        && grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意，可以去放肆了。
                } else {
                    // 权限被用户拒绝了，洗洗睡吧。
                 mPermissionCall.refused();
//					if(shouldShowRequestPermissionRationale(permissions)){
//						new AlertDialog.Builder(this).setTitle(R.string.attention)
//								.setMessage(R.string.content_to_request_permission)
//								.setPositiveButton(R.string.ok_text, new DialogInterface.OnClickListener() {
//									@Override
//									public void onClick(DialogInterface dialog, int which) {
//										ActivityCompat.requestPermissions(AcivitySelectPhoto.this,permissions,requestCode);
//									}
//								}).show();
//					}else{
//						ActivityCompat.requestPermissions(this,permissions,requestCode);
//					}
                }
                break;
        }

    }

    public interface PermissionCall{
        //申请成功
        void requestSucess();
        //拒绝
        void refused();
    }
    // ---------------------------EventBus-------------------------------------
    @Subscribe
    public void onEvent(MyEvent e) {
    }

    @Subscribe
    public void onEventMainThread(MyEvent e) {
    }

    @Subscribe
    public void onEventBackgroundThread(MyEvent e) {
    }

    @Subscribe
    public void onEventAsync(MyEvent e) {
    }

    public void setmPermissionCall(PermissionCall mPermissionCall) {
        this.mPermissionCall = mPermissionCall;
    }
}
