package com.jinchao.population.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseFragment;
import com.jinchao.population.config.Constants;
import com.jinchao.population.entity.MaturityListBean;
import com.jinchao.population.mainmenu.MaturityWarningActivity;
import com.jinchao.population.mainmenu.RegistRentalHouseActivity;
import com.jinchao.population.mainmenu.SearchRentalHouseActivity;
import com.jinchao.population.realpopulation.RealPeopleSearchActivity;
import com.jinchao.population.realpopulation.SearchPeopleRealActivity;
import com.jinchao.population.utils.GsonTools;
import com.jinchao.population.utils.SharePrefUtil;
import com.jinchao.population.widget.BadgeView;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by OfferJiShu01 on 2016/8/9.
 */
@ContentView(R.layout.fragment_realpopulation)
public class RealFragment extends BaseFragment{
    public BadgeView badgeView;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        badgeView = new BadgeView(getActivity());
        badgeView.setTargetView(view.findViewById(R.id.iv_warning));
        badgeView.setTextSize(16);
        badgeView.setBadgeMargin(0,5,5,0);

    }
    @Event(value={R.id.iv_rentalhouseregist})
    private void rentalhouseregistClick(View view){
        Intent intent =new Intent(getActivity(), RegistRentalHouseActivity.class);
        intent.putExtra(Constants.IS_FROM_REALPOPULATION,true);
        startActivity(intent);
    }
    @Event(value={R.id.iv_regist})
    private void registClick(View view){
        Intent intent =new Intent(getActivity(), RealPeopleSearchActivity.class);
        startActivity(intent);
    }
    @Event(value={R.id.iv_personinfocheck})
    private void personinfocheckClick(View view){
        Intent intent =new Intent(getActivity(), SearchPeopleRealActivity.class);
        startActivity(intent);
    }
    @Event(value={R.id.iv_rentalhousecheck})
    private void rentalhousecheckClick(View view){
        Intent intent =new Intent(getActivity(), SearchRentalHouseActivity.class);
        intent.putExtra(Constants.IS_FROM_REALPOPULATION,true);
        startActivity(intent);
    }
    @Event(value={R.id.iv_warning})
    private void warningClick(View view){
        Intent intent =new Intent(getActivity(), MaturityWarningActivity.class);
        intent.putExtra(Constants.IS_FROM_REALPOPULATION,true);
        startActivity(intent);
    }
    @Event(value={R.id.iv_feedback})
    private void feedbackClick(View view){
        Toast.makeText(getActivity(),"建设中...",Toast.LENGTH_LONG).show();
    }
    @Override
    public void onStart() {
        super.onStart();
        SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String time =sDateFormat.format(new Date(System.currentTimeMillis()));
        if (SharePrefUtil.getString(getActivity(),Constants.YUJING_TIME_LIMIT,"").equals(time)){
            List<MaturityListBean.MaturePeopleOne> listpeople=(List<MaturityListBean.MaturePeopleOne>)SharePrefUtil.getObj(getActivity(),Constants.YUJING_LIST);
            if (listpeople!=null){
                badgeView.setBadgeCount(listpeople.size());
            }
        }else{
//            GetMaturityWarning();
        }
    }
    private void GetMaturityWarning() {
        RequestParams params = new RequestParams(Constants.URL + "syrkHouse.aspx");
        params.addBodyParameter("type", "get_people");
        params.addBodyParameter("user_id", MyInfomationManager.getUserName(getActivity()));

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println(result);
                try {
                    MaturityListBean maturityListBean= GsonTools.changeGsonToBean(result,MaturityListBean.class);
                    List<MaturityListBean.MaturePeopleOne> listpeople=new ArrayList<MaturityListBean.MaturePeopleOne>();
                    listpeople.clear();
                    if (maturityListBean.data.houselist!=null){
                        if (maturityListBean.data.houselist.size()!=0){
                            for (int i=0;i<maturityListBean.data.houselist.size();i++){
                                for (int j=0;j<maturityListBean.data.houselist.get(i).peoplelist.size();j++){
                                    listpeople.add(maturityListBean.data.houselist.get(i).peoplelist.get(j));
                                }
                            }
                        }
                    }
                    badgeView.setBadgeCount(listpeople.size());
                    SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                    String time =sDateFormat.format(new Date(System.currentTimeMillis()));
                    SharePrefUtil.saveString(getActivity(),Constants.YUJING_TIME_LIMIT,time);
                    SharePrefUtil.saveObj(getActivity(),Constants.YUJING_LIST,listpeople);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                GetMaturityWarning();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }
}
