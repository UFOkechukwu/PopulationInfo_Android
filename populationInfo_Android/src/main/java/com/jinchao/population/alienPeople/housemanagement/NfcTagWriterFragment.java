package com.jinchao.population.alienPeople.housemanagement;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jinchao.population.MyApplication;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseDialogFragment;
import com.jinchao.population.base.BaseFragment;
import com.jinchao.population.base.NFCTagBaseFragment;
import com.jinchao.population.dbentity.HouseAddressOldBean;
import com.jinchao.population.dbentity.HouseAddressOldBean10;
import com.jinchao.population.dbentity.HouseAddressOldBean2;
import com.jinchao.population.dbentity.HouseAddressOldBean3;
import com.jinchao.population.dbentity.HouseAddressOldBean4;
import com.jinchao.population.dbentity.HouseAddressOldBean5;
import com.jinchao.population.dbentity.HouseAddressOldBean6;
import com.jinchao.population.dbentity.HouseAddressOldBean7;
import com.jinchao.population.dbentity.HouseAddressOldBean8;
import com.jinchao.population.dbentity.HouseAddressOldBean9;
import com.jinchao.population.utils.DatabaseUtil;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.widget.ValidateEidtText;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by OfferJiShu01 on 2017/2/27.
 */

@ContentView(R.layout.fragment_nfcwriter)
public class NfcTagWriterFragment extends BaseFragment {
    @ViewInject(R.id.tv_content)TextView tv_content;
    @ViewInject(R.id.btn_search)Button btn_search;
    @ViewInject(R.id.edt_content)ValidateEidtText edt_content;
    @ViewInject(R.id.tv_noresult)TextView tv_noresult;
    private DbUtils dbUtils;
    private String json="";
    public int database_tableNo=0;//当前登录账号使用的哪个数据库，0:未下载的地址库，1：表1,2：表2.。。。
    @Override
    public void onStart() {
        super.onStart();
        tv_content.setText("");
        edt_content.setText("");
        json="";
        if (((MyApplication)getActivity().getApplication()).database_tableNo==0){
            database_tableNo= DatabaseUtil.getNullDB(getActivity());
        }else{
            database_tableNo=((MyApplication)getActivity().getApplication()).database_tableNo;
        }
    }
    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        List<Fragment> fragmentList=getChildFragmentManager().getFragments();
        if (fragmentList==null)return;
        for (int i=0;i<fragmentList.size();i++){
            if (fragmentList.get(i)==null)return;
                ((BaseDialogFragment) fragmentList.get(i)).onNewIntent(intent);
        }

    }
    @Event(value ={ R.id.btn_write})
    private void writetoNFCTag(View view){
        if (TextUtils.isEmpty(json)){
            Toast.makeText(getActivity(),"请先查询房屋信息，再写入！",Toast.LENGTH_SHORT).show();
            return;
        }
        FragmentTransaction ft=getChildFragmentManager().beginTransaction();
        NfcWriterFragmentDialog nfcWriterFragmentDialog=NfcWriterFragmentDialog.newInstance(json);
        nfcWriterFragmentDialog.show(getChildFragmentManager(),"NFC_WRITE");
    }
    @Event(value = {R.id.btn_search})
    private void search(View view){
        String code =edt_content.getText().toString().trim();
        if (TextUtils.isEmpty(code)||code.length()!=6){
            Toast.makeText(getActivity(),"请输入6位的房屋编号",Toast.LENGTH_SHORT).show();
            return;
        }
        hidenSoftKeyBoard(edt_content);
        showProcessDialog("正在查询...");
        try {
            dbUtils= DeviceUtils.getDbUtils(getActivity());
            switch (database_tableNo){
                case 1:
                    HouseAddressOldBean houseAddressOldBean = dbUtils.findFirst(Selector.from(HouseAddressOldBean.class).where("scode", "like", code));
                    if (houseAddressOldBean!=null) {
                        SearchhasHouse(houseAddressOldBean.toString(),houseAddressOldBean.toJson(getActivity()));
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 2:
                    HouseAddressOldBean2 houseAddressOldBean2 = dbUtils.findFirst(Selector.from(HouseAddressOldBean2.class).where("scode", "like", code));
                    if (houseAddressOldBean2!=null) {
                        SearchhasHouse(houseAddressOldBean2.toString(),houseAddressOldBean2.toJson(getActivity()));
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 3:
                    HouseAddressOldBean3 houseAddressOldBean3 = dbUtils.findFirst(Selector.from(HouseAddressOldBean3.class).where("scode", "like", code));
                    if (houseAddressOldBean3!=null) {
                        SearchhasHouse(houseAddressOldBean3.toString(),houseAddressOldBean3.toJson(getActivity()));
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 4:
                    HouseAddressOldBean4 houseAddressOldBean4 = dbUtils.findFirst(Selector.from(HouseAddressOldBean4.class).where("scode", "like", code));
                    if (houseAddressOldBean4!=null) {
                        SearchhasHouse(houseAddressOldBean4.toString(),houseAddressOldBean4.toJson(getActivity()));
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 5:
                    HouseAddressOldBean5 houseAddressOldBean5 = dbUtils.findFirst(Selector.from(HouseAddressOldBean5.class).where("scode", "like", code));
                    if (houseAddressOldBean5!=null) {
                        SearchhasHouse(houseAddressOldBean5.toString(),houseAddressOldBean5.toJson(getActivity()));
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 6:
                    HouseAddressOldBean6 houseAddressOldBean6 = dbUtils.findFirst(Selector.from(HouseAddressOldBean6.class).where("scode", "like", code));
                    if (houseAddressOldBean6!=null) {
                        SearchhasHouse(houseAddressOldBean6.toString(),houseAddressOldBean6.toJson(getActivity()));
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 7:
                    HouseAddressOldBean7 houseAddressOldBean7 = dbUtils.findFirst(Selector.from(HouseAddressOldBean7.class).where("scode", "like", code));
                    if (houseAddressOldBean7!=null) {
                        SearchhasHouse(houseAddressOldBean7.toString(),houseAddressOldBean7.toJson(getActivity()));
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 8:
                    HouseAddressOldBean8 houseAddressOldBean8 = dbUtils.findFirst(Selector.from(HouseAddressOldBean8.class).where("scode", "like", code));
                    if (houseAddressOldBean8!=null) {
                        SearchhasHouse(houseAddressOldBean8.toString(),houseAddressOldBean8.toJson(getActivity()));
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 9:
                    HouseAddressOldBean9 houseAddressOldBean9 = dbUtils.findFirst(Selector.from(HouseAddressOldBean9.class).where("scode", "like", code));
                    if (houseAddressOldBean9!=null) {
                        SearchhasHouse(houseAddressOldBean9.toString(),houseAddressOldBean9.toJson(getActivity()));
                    }else{
                        SearchNoHouse();
                    }
                    break;
                case 10:
                    HouseAddressOldBean10 houseAddressOldBean10 = dbUtils.findFirst(Selector.from(HouseAddressOldBean10.class).where("scode", "like", code));
                    if (houseAddressOldBean10!=null) {
                        SearchhasHouse(houseAddressOldBean10.toString(),houseAddressOldBean10.toJson(getActivity()));
                    }else{
                        SearchNoHouse();
                    }
                    break;
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProcessDialog();
            }
        },200);

    }
    private void SearchNoHouse(){
        json="";
        tv_content.setText("");
        tv_noresult.setVisibility(View.VISIBLE);
        tv_noresult.setCompoundDrawablesWithIntrinsicBounds(null, getActivity().getResources().getDrawable(R.drawable.icon_noresult),null,null);
        tv_noresult.setText("无此房屋编号！");
    }
    private void SearchhasHouse(String str,String json){
        this.json=json;
        tv_noresult.setVisibility(View.GONE);
        tv_content.setText(str);
    }
}
