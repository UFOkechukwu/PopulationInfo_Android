package com.jinchao.population.mainmenu;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.jinchao.population.R;
import com.jinchao.population.base.BaseActiviy;
import com.jinchao.population.config.Constants;
import com.jinchao.population.view.StringWheel.OnEnsureClickListener;
import com.jinchao.population.view.NavigationLayout;
import com.jinchao.population.view.PopJLX;
import com.jinchao.population.view.PopJLX.OnJLXListClickListener;
import com.jinchao.population.view.StringWheel;
import com.jinchao.population.widget.ValidateEidtText;

@ContentView(R.layout.activity_edithouseaddress)
public class EidtHouseAddressActivity extends BaseActiviy{
	@ViewInject(R.id.edt_menpaihaodanwei)private EditText edt_menpaihaodanwei;
	@ViewInject(R.id.edt_fuhaodanwei)private EditText edt_fuhaodanwei;
	@ViewInject(R.id.edt_lou)private EditText edt_lou;
	@ViewInject(R.id.edt_zhuanghao)private EditText edt_zhuanghao;
	@ViewInject(R.id.edt_danyuan)private EditText edt_danyuan;
	@ViewInject(R.id.edt_shihaodanwei)private EditText edt_shihaodanwei;
	@ViewInject(R.id.edt_menpaihao)private EditText edt_menpaihao;
	@ViewInject(R.id.edt_fuhao)private EditText edt_fuhao;
	@ViewInject(R.id.edt_louhao)private EditText edt_louhao;
	@ViewInject(R.id.edt_shihao)private EditText edt_shihao;
	@ViewInject(R.id.edt_jieluxiang)private EditText edt_jieluxiang;
	private StringWheel stringWheel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NavigationLayout navigationLayout =(NavigationLayout) findViewById(R.id.navgation_top);
		navigationLayout.setCenterText("房屋地址编辑");
		navigationLayout.setLeftTextOnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		navigationLayout.setRightText("确定", new OnClickListener() {
			@Override
			public void onClick(View v) {
				ensure();
			}
		});
	}
	private void ensure(){
		String jieluxiang=edt_jieluxiang.getText().toString().trim();
		String menpaihao=edt_menpaihao.getText().toString().trim();
		String fuhao=edt_fuhao.getText().toString().trim();
		String louhao=edt_louhao.getText().toString().trim();
		String shihao=edt_shihao.getText().toString().trim();

		String menpaihaodanwei=edt_menpaihaodanwei.getText().toString().trim();
		String fuhaodanwei=edt_fuhaodanwei.getText().toString().trim();
		String loudanwei=edt_lou.getText().toString().trim();
		String louhaodanwei=edt_zhuanghao.getText().toString().trim();
		String danyuandanwei=edt_danyuan.getText().toString().trim();
		String shihaodanwei=edt_shihaodanwei.getText().toString().trim();
		if (jieluxiang.equals("")) {
			Toast.makeText(this, "请选择街路巷~", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!TextUtils.isEmpty(menpaihao)){
			if (TextUtils.isEmpty(menpaihaodanwei)){
				Toast.makeText(this, "请选择门牌号单位~", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		String menpaiTT="";
		String fuhaoTT="";
		String louhaoTT="";
		String shihaoTT="";
		if (!(menpaihao.equals(""))&&(!menpaihaodanwei.equals(""))) {
			menpaiTT=menpaihao+menpaihaodanwei;
		}

		if (!(fuhao.equals(""))&&(!fuhaodanwei.equals(""))) {
			fuhaoTT=fuhao+fuhaodanwei;
		}

		if (!(louhao.equals(""))&&(!louhaodanwei.equals(""))) {
			louhaoTT=loudanwei+louhao+louhaodanwei;
		}
		if (!(shihao.equals(""))&&(!shihaodanwei.equals(""))) {
			shihaoTT=shihao+shihaodanwei;
		}
//		if (menpaiTT.equals("")) {
//			Toast.makeText(EidtHouseAddressActivity.this, "请输入门牌号并选择单位", 0).show();
//			return;
//		}
		if (menpaihaodanwei.trim().equals("-")){
			if(fuhaoTT.trim().equals("")){
				Toast.makeText(EidtHouseAddressActivity.this, "请输入副号并选择单位", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		if(!louhao.equals("")){
			if (shihaoTT.equals("")) {
				Toast.makeText(EidtHouseAddressActivity.this, "请输入室号并选择单位", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		if (TextUtils.isEmpty(menpaiTT)&&TextUtils.isEmpty(louhaoTT)){
			Toast.makeText(EidtHouseAddressActivity.this, "门牌号与楼幢号不可同时为空！", Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent =new Intent();
		intent.putExtra("address",jieluxiang+menpaiTT+fuhaoTT+louhaoTT+danyuandanwei+shihaoTT);
		intent.putExtra("jieluxiang",jieluxiang);
		intent.putExtra("mph",menpaihao);
		intent.putExtra("mphdanwei",menpaihaodanwei);
		if (!TextUtils.isEmpty(fuhaoTT)) {
			intent.putExtra("fuhao",fuhao);
			intent.putExtra("fuhaodanwei",fuhaodanwei);
		}else{
			intent.putExtra("fuhao","");
			intent.putExtra("fuhaodanwei","");
		}
		intent.putExtra("loudanwei",loudanwei);
		if (!TextUtils.isEmpty(louhaoTT)) {
			intent.putExtra("louhao",louhao);
			intent.putExtra("louhaodanwei",louhaodanwei);
		}else{
			intent.putExtra("louhao","");
			intent.putExtra("louhaodanwei","");
		}
		intent.putExtra("danyuan",danyuandanwei);
		intent.putExtra("shihao",shihao);
		intent.putExtra("shihaodanwei",shihaodanwei);
		setResult(RESULT_OK, intent);
		finish();
	}
	@Event(R.id.edt_jieluxiang)
	private void jieluxiangClick(View view){
		PopJLX popJLX=new PopJLX(this, new OnJLXListClickListener() {
			@Override
			public void OnJLXClick(String str) {
				edt_jieluxiang.setText(str);
			}
		});
		popJLX.showPopupWindow(findViewById(R.id.root));
	}
	
	@Event(R.id.edt_menpaihaodanwei)
	private void menpaihaodanweiClick(View view){//门牌号
		if (getCurrentFocus()!=null) {
			((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		stringWheel=new StringWheel(this, Constants.MENPAIHAODANWEI, new OnEnsureClickListener() {
			@Override
			public void OnEnSureClick(String str) {
				str=str.equals("请选择")?"":str;
				edt_menpaihaodanwei.setText(str);
			}
		});
		stringWheel.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0); 
	}
	@Event(R.id.edt_fuhaodanwei)
	private void fuhaodanweiClick(View view){//副号
		if (getCurrentFocus()!=null) {
			((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		stringWheel=new StringWheel(this, Constants.FUHAODANWEI, new OnEnsureClickListener() {
			@Override
			public void OnEnSureClick(String str) {
				str=str.equals("请选择")?"":str;
				edt_fuhaodanwei.setText(str);
			}
		});
		stringWheel.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0); 
	}
	@Event(R.id.edt_lou)
	private void louClick(View view){//楼幢号
		if (getCurrentFocus()!=null) {
			((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		stringWheel = new StringWheel(this, Constants.LOUDANWEI, new OnEnsureClickListener() {
			@Override
			public void OnEnSureClick(String str) {
				str=str.equals("请选择")?"":str;
				edt_lou.setText(str);
			}
		});
		stringWheel.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0); 
	}
	@Event(R.id.edt_zhuanghao)
	private void zhuanghaoClick(View view){//楼号
		if (getCurrentFocus()!=null) {
			((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		stringWheel = new StringWheel(this, Constants.ZHUANGHAODANWEI, new OnEnsureClickListener() {
			@Override
			public void OnEnSureClick(String str) {
				str=str.equals("请选择")?"":str;
				edt_zhuanghao.setText(str);
			}
		});
		stringWheel.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0); 
	}
	@Event(R.id.edt_danyuan)
	private void danyuanClick(View view){//单元
		if (getCurrentFocus()!=null) {
			((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		stringWheel = new StringWheel(this, Constants.DANYUAN, new OnEnsureClickListener() {
			@Override
			public void OnEnSureClick(String str) {
				str=str.equals("请选择")?"":str;
				edt_danyuan.setText(str);
			}
		});
		stringWheel.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0); 
	}
	@Event(R.id.edt_shihaodanwei)
	private void shihaodanweiClick(View view){//室号
		if (getCurrentFocus()!=null) {
			((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		stringWheel = new StringWheel(this, Constants.SHIHAODANWEI, new OnEnsureClickListener() {
			@Override
			public void OnEnSureClick(String str) {
				str=str.equals("请选择")?"":str;
				edt_shihaodanwei.setText(str);
			}
		});
		stringWheel.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0); 
	}
}
