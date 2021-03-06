package com.jinchao.population.main;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.Common;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jinchao.population.MyApplication;
import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.SysApplication;
import com.jinchao.population.config.Constants;
import com.jinchao.population.entity.HouseDataBaseType;
import com.jinchao.population.entity.MsgBean;
import com.jinchao.population.entity.VersionBean;
import com.jinchao.population.fragment.AlienFragment;
import com.jinchao.population.fragment.RealFragment;
import com.jinchao.population.fragment.RegistLocFragment;
import com.jinchao.population.fragment.SendDataFragment;
import com.jinchao.population.fragment.SettingFragment;
import com.jinchao.population.fragment.XunLuoFragment;
import com.jinchao.population.service.DownLoadService;
import com.jinchao.population.utils.CommonHttp;
import com.jinchao.population.utils.CommonUtils;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.utils.HouseTableInit;
import com.jinchao.population.utils.SharePrefUtil;
import com.jinchao.population.utils.network.NetWorkManager;
import com.jinchao.population.view.Dialog;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;
@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private long mExitTime;
    public ProgressDialog progressDialog;
    @ViewInject(R.id.toolbar) private Toolbar toolbar;
    @ViewInject(R.id.title)private TextView title;
    @ViewInject(R.id.tv_right)public TextView tv_right;
    @ViewInject(R.id.nav_view)NavigationView navigationView;
    @ViewInject(R.id.drawer_layout)DrawerLayout drawer;
    private Fragment currentFragment;
    MaterialDialog materialDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        SysApplication.getInstance().addActivity(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        TextView tv_name=(TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_name);
        getNewVersion();
        CommonHttp.getDataBaseType(this);
        if (!Common.init(this)){
            Toast.makeText(this, "身份证云终端开发包初始化失败！", Toast.LENGTH_SHORT).show();
            finish();
        }
        tv_name.setText(MyInfomationManager.getUserName(this)+"("+MyInfomationManager.getSQNAME(this)+")");
        String default_index=SharePrefUtil.getString(this,Constants.DEFAULT_INDEX,"外来人口信息采集");
        switch (default_index){
            case "外来人口信息采集" :
                title.setText("外来人口信息采集");
                currentFragment=new AlienFragment();
                changeFragment(currentFragment);
                break;
            case "实有人口调查" :
                title.setText("实有人口调查");
                currentFragment=new RealFragment();
                changeFragment(currentFragment);
                break;
            case "巡逻盘查" :
                title.setText("巡逻盘查");
                currentFragment=new XunLuoFragment();
                changeFragment(currentFragment);
                break;
            case "电动车检查" :
                title.setText("电动车检查");
                currentFragment=new XunLuoFragment();
                changeFragment(currentFragment);
                break;
            case "房屋位置信息录入" :
                title.setText("房屋位置信息采集");
                tv_right.setVisibility(View.VISIBLE);
                tv_right.setText("采集");
                currentFragment=new RegistLocFragment();
                changeFragment(currentFragment);
                break;
            default:
                break;
        }
//        if (((MyApplication)getApplication()).database_tableNo==0)return;
//        showProgressDialog("地址库更新中...");
//        HouseTableInit.getInstance(this,((MyApplication)getApplication()).database_tableNo,new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                switch (msg.what){
//                    case 1:
//                        dismissProgressDialog();
//                        break;
//                }
//            }
//        }).addtionalHouse();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMsg();
//        int latest_version=SharePrefUtil.getInt(MainActivity.this,Constants.LATEST_APP_VERSION,0);
//        String json=SharePrefUtil.getString(MainActivity.this,Constants.LATEST_APP_VERSION_JSON,"");
//        boolean isforce=SharePrefUtil.getBoolean(MainActivity.this,Constants.LATEST_APP_VERSION_FORCE,false);
//        if(latest_version!=0&&(latest_version>CommonUtils.getVersionCode(MainActivity.this))&&(!TextUtils.isEmpty(json))&&isforce){
//            parseVersion(json);
//        }
    }


    public void changeFragment(Fragment fragment){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.container,fragment);
        ft.commitAllowingStateLoss();
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Handle navigation view item clicks here.
        tv_right.setVisibility(View.GONE);
        int id = item.getItemId();
        if (id == R.id.nav_alien) {
            title.setText("外来人口信息采集");
            currentFragment=new AlienFragment();
            changeFragment(currentFragment);
        } else if (id == R.id.nav_shi) {
            Dialog.showForceDialog(this,"提示","实有人口已全部转至外来人口中，请使用外来人口采集", new Dialog.DialogClickListener() {
                @Override
                public void confirm() {
//                    navigationView.setCheckedItem(R.id.nav_alien);
                }

                @Override
                public void cancel() {

                }
            });
            title.setText("外来人口信息采集");
            currentFragment=new AlienFragment();
            changeFragment(currentFragment);
//            title.setText("实有人口采集");
//            currentFragment=new RealFragment();
//            changeFragment(currentFragment);
        } else if (id == R.id.nav_xunluo) {
            title.setText("巡逻盘查");
            currentFragment=new XunLuoFragment();
            changeFragment(currentFragment);
        } else if (id == R.id.nav_diandongche) {
            title.setText("电动车检查");
            currentFragment=new XunLuoFragment();
            changeFragment(currentFragment);
        } else if (id == R.id.nav_loc) {
            title.setText("房屋位置信息采集");
            tv_right.setVisibility(View.VISIBLE);
            tv_right.setText("采集");
            currentFragment=new RegistLocFragment();
            changeFragment(currentFragment);
        }else if (id == R.id.nav_send) {
            title.setText("数据发送");
            tv_right.setVisibility(View.VISIBLE);
            tv_right.setText("发送");
            currentFragment=new SendDataFragment();
            changeFragment(currentFragment);
        }else if (id == R.id.nav_setting) {
            title.setText("设置");
            currentFragment=new SettingFragment();
            changeFragment(currentFragment);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressWarnings("unused")
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }else {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Object mHelperUtils;
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                } else {
                    SysApplication.getInstance().exit();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getNewVersion(){
        RequestParams params=new RequestParams(Constants.URL+"VersionInfor.aspx");
        params.addBodyParameter("type","2");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("version", result);
                parseVersion(result);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                getNewVersion();
            }
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished() {}
        });
    }
    private void parseVersion(String result){
        try {
            final VersionBean versionBean= GsonTools.changeGsonToBean(result,VersionBean.class);
            if (versionBean.versionNum.trim().equals("")){
                getNewVersion();
                return;
            }
            String[] str =versionBean.versionNum.trim().split("\\.");
            String versionStr=str[0]+str[1]+str[2];
            int new_version =Integer.parseInt(versionStr);
            SharePrefUtil.saveInt(MainActivity.this,Constants.LATEST_APP_VERSION,new_version);
            SharePrefUtil.saveString(MainActivity.this,Constants.LATEST_APP_VERSION_URL,versionBean.desFile);
            SharePrefUtil.saveBoolean(MainActivity.this,Constants.LATEST_APP_VERSION_FORCE,versionBean.isForce);
            SharePrefUtil.saveString(MainActivity.this,Constants.LATEST_APP_VERSION_JSON,result);
            String uname=MyInfomationManager.getUserName(MainActivity.this);
            if (versionBean.versionNum.trim().equals("1.4.0")&& (uname.startsWith("lj")||uname.startsWith("zp")||uname.startsWith("zs")||uname.startsWith("zx")||uname.startsWith("ds")||uname.startsWith("xz"))){

                return;
            }
            if (new_version>CommonUtils.getVersionCode(MainActivity.this)) {
                String str_v= "发现新版本,下载并更新？";
                if (versionBean.isForce){
                    str_v="发现新版本v"+versionBean.versionNum.trim()+",该版本需立即更新，否则无法操作！";
                }
                if (versionBean.isForce){
                    Dialog.showRadioDialog(MainActivity.this, str_v, new Dialog.DialogClickListener() {
                        @Override
                        public void confirm() {
                            Intent serviceDownload=new Intent(MainActivity.this,DownLoadService.class);
                            if (CommonUtils.isServiceRunning(MainActivity.this, "com.jinchao.population.service.DownLoadService")) {
                                MainActivity.this.stopService(serviceDownload);
                            }
                            serviceDownload.putExtra("url", versionBean.desFile);
                            MainActivity.this.startService(serviceDownload);
                        }
                        @Override
                        public void cancel() {
                        }
                    });
                }else{
                    Dialog.showSelectDialog(MainActivity.this, str_v, new Dialog.DialogClickListener() {
                        @Override
                        public void confirm() {
                            Intent serviceDownload=new Intent(MainActivity.this,DownLoadService.class);
                            if (CommonUtils.isServiceRunning(MainActivity.this, "com.jinchao.population.service.DownLoadService")) {
                                MainActivity.this.stopService(serviceDownload);
                            }
                            serviceDownload.putExtra("url", versionBean.desFile);
                            MainActivity.this.startService(serviceDownload);
                        }
                        @Override
                        public void cancel() {
                            if (versionBean.isForce){
                                SysApplication.getInstance().exit();
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getMsg(){
        RequestParams params=new RequestParams(Constants.URL+"SendMsg.aspx?username="+ MyInfomationManager.getUserName(MainActivity.this));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    final MsgBean msgBean=GsonTools.changeGsonToBean(result,MsgBean.class);
                    if (msgBean.msgState.equals("1")){
                        Dialog.showSelectDialog(MainActivity.this,"新消息",msgBean.sendMsg, new Dialog.DialogClickListener() {
                            @Override
                            public void confirm() {
                            }
                            @Override
                            public void cancel() {
                            }
                        });
                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification notification = new NotificationCompat.Builder(MainActivity.this)
                                .setContentTitle("新消息")
                                .setSmallIcon(R.drawable.app_icon).build();
                        notification.flags |= Notification.FLAG_AUTO_CANCEL;
                        RemoteViews remote = new RemoteViews(getPackageName(), R.layout.status);
                        notification.contentView = remote;
                        notification.contentView.setTextViewText(R.id.tv_content, msgBean.sendMsg);
                        try {
                            mNotificationManager.notify(0,notification);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {}
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished() {}
        });
    }
    public void showProgressDialog(String toast){
        progressDialog = ProgressDialog.show(this,"",toast,true,false);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    public void dismissProgressDialog(){
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}
