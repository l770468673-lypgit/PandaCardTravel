package com.xlu.uis;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.pandacard.teavel.R;
import com.pandacard.teavel.apps.MyApplication;
import com.pandacard.teavel.utils.LUtils;
import com.pandacard.teavel.utils.OnItemClickListener;
import com.pandacard.teavel.utils.ToastUtils;
import com.xlu.adapters.MeituanAdapter;
import com.xlu.po.BaseIndexPinyinBean;
import com.xlu.po.MeiTuanBean;
import com.xlu.po.MeituanHeaderBean;
import com.xlu.po.MeituanTopHeaderBean;
import com.xlu.po.MyEvent;
import com.xlu.utils.CommonAdapter;
import com.xlu.utils.Constance;
import com.xlu.utils.HeaderRecyclerAndFooterWrapperAdapter;
import com.xlu.utils.NetWorkHelper;
import com.xlu.utils.ViewHolder;
import com.xlu.widgets.IndexBar;
import com.xlu.widgets.SuspensionDecoration;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by giant on 2017/7/28.
 */

public class ActivitySelectCity extends AppCompatActivity {
    //        implements AMapLocationListener {
    private static final String TAG = "zxt";
    private Context mContext;
    private RecyclerView mRv;
    private MeituanAdapter mAdapter;
    private HeaderRecyclerAndFooterWrapperAdapter mHeaderAdapter;
    private LinearLayoutManager mManager;
    SharedPreferences sp;
    SharedPreferences spCity;
    TextView titleTile;
    //设置给InexBar、ItemDecoration的完整数据集
    private List<BaseIndexPinyinBean> mSourceDatas;
    //头部数据源
    private List<MeituanHeaderBean> mHeaderDatas;
    //主体部分数据源（城市数据）
    private List<MeiTuanBean> mBodyDatas;

    private SuspensionDecoration mDecoration;

    /**
     * 右侧边栏导航区域
     */
    private IndexBar mIndexBar;

    /**
     * 显示指示器DialogText
     */
    private TextView mTvSideBarHint;
    //声明mLocationOption对象
//    public AMapLocationClientOption mLocationOption;
    //声明mLocationClient对象
//    public AMapLocationClient mLocationClient;

    private void initLoacation() {
//        mLocationOption = new AMapLocationClientOption();
//        mLocationClient = new AMapLocationClient(this);
//        //设置定位监听
//        mLocationClient.setLocationListener(this);
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        mLocationOption.setNeedAddress(true);
//        mLocationOption.setOnceLocation(true);
//        mLocationOption.setWifiActiveScan(true);
//        mLocationOption.setMockEnable(false);
//        mLocationOption.setInterval(5000);
//        mLocationClient.setLocationOption(mLocationOption);
//        mLocationClient.startLocation();



//        mHeaderAdapter.setHeaderView(0, R.layout.meituan_item_header_top,
//                new MeituanTopHeaderBean("当前：" + MyApplication.city));
        mHeaderAdapter.setHeaderView(1, R.layout.meituan_item_header, mHeaderDatas.get(0));
//        mHeaderAdapter.setHeaderView(2, R.layout.meituan_item_header, mHeaderDatas.get(1));
//        mHeaderAdapter.setHeaderView(3, R.layout.meituan_item_header, mHeaderDatas.get(2));


        mRv.setAdapter(mHeaderAdapter);
        mRv.addItemDecoration(mDecoration = new SuspensionDecoration(this, mSourceDatas)
                .setmTitleHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics()))
                .setColorTitleBg(0xffefefef)
                .setTitleFontSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()))
                .setColorTitleFont(mContext.getResources().getColor(android.R.color.black))
                .setHeaderViewCount(mHeaderAdapter.getHeaderViewCount() - mHeaderDatas.size()));
        mRv.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

        //使用indexBar
        mTvSideBarHint = findViewById(R.id.tvSideBarHint);//HintTextView
        mIndexBar = findViewById(R.id.indexBar);//IndexBar
        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager)//设置RecyclerView的LayoutManager
                .setHeaderViewCount(mHeaderAdapter.getHeaderViewCount() - mHeaderDatas.size());
        if (NetWorkHelper.isNetWorkAvailble(this)) {
            loadCity();
        } else {
            String result = spCity.getString("city_list", "");
            if ("".equals(result)) {

            } else {
                String[] str1 = result.split("[:]");
                String cityStr = "";
                for (int i = 1; i < str1.length; i++) {
                    cityStr += str1[i] + ",";
                }
                initDatas(cityStr.split(","));
            }

        }

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city_new);
        sp = getSharedPreferences("current_city", Context.MODE_PRIVATE);
        spCity = getSharedPreferences("city_info", Context.MODE_PRIVATE);
//        requestRunTimePermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION"});
//        requestRunTimePermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION"});

        mContext = this;

        mRv = findViewById(R.id.rv);
        titleTile = findViewById(R.id.tv_all_title_name);
        titleTile.setText("选择城市");
        titleTile.setVisibility(View.VISIBLE);
        mRv.setLayoutManager(mManager = new LinearLayoutManager(this));

        mSourceDatas = new ArrayList<>();
        mHeaderDatas = new ArrayList<>();
        List<String> locationCity = new ArrayList<>();
//        locationCity.add("定位中");
//        mHeaderDatas.add(new MeituanHeaderBean(locationCity, "定位城市", ""));
//        List<String> recentCitys = new ArrayList<>();
//        mHeaderDatas.add(new MeituanHeaderBean(recentCitys, "最近访问城市", ""));
        List<String> hotCitys = new ArrayList<>();
        mHeaderDatas.add(new MeituanHeaderBean(hotCitys, "热门城市", ""));
        mSourceDatas.addAll(mHeaderDatas);
        final List<String> tempCity = new ArrayList<String>();

        mAdapter = new MeituanAdapter(this, R.layout.meituan_item_select_city, mBodyDatas);

        mHeaderAdapter = new HeaderRecyclerAndFooterWrapperAdapter(mAdapter) {
            @Override
            protected void onBindHeaderHolder(ViewHolder holder, int headerPos, int layoutId, Object o) {
                switch (layoutId) {
                    case R.layout.meituan_item_header:
                        final MeituanHeaderBean meituanHeaderBean = (MeituanHeaderBean) o;
                        //网格
                        RecyclerView recyclerView = holder.getView(R.id.rvCity);


                        recyclerView.setAdapter(
                                new CommonAdapter<String>(mContext, R.layout.meituan_item_header_item, meituanHeaderBean.getCityList()) {
                                    @Override
                                    public void convert(ViewHolder holder, final String cityName) {
                                        holder.setText(R.id.tvName, cityName);
                                        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String[] temp = sp.getString("city", "0").split(",");
                                                if (temp.length == 2) {
                                                    sp.edit().clear().commit();
                                                    sp.edit().putString("city", cityName).commit();
                                                    spCity.edit().putString("city", cityName).commit();
                                                } else if (temp.length < 2 && !(temp.equals("0"))) {
                                                    String one = sp.getString("city", "0");
                                                    one += "," + cityName;
                                                    sp.edit().putString("city", one).commit();
                                                    spCity.edit().putString("city", cityName).commit();
                                                }
                                                if (cityName.endsWith("市")) {
                                                    MyApplication.city = cityName;
                                                    spCity.edit().putString("city", cityName).commit();
                                                } else {
                                                    if (cityName.equals("海南")) {
                                                        MyApplication.city = cityName + "省";
                                                        spCity.edit().putString("city", cityName).commit();
                                                    } else {
                                                        MyApplication.city = cityName;
                                                        spCity.edit().putString("city", cityName).commit();
                                                    }

                                                }
                                                EventBus.getDefault().post(new MyEvent(1));
                                                Intent intent = new Intent();
                                                if (cityName.endsWith("市")) {
                                                    intent.putExtra("city", cityName);
                                                    spCity.edit().putString("city", cityName).commit();
                                                } else {
                                                    if (cityName.equals("海南")) {
                                                        intent.putExtra("city", cityName + "省");
                                                        spCity.edit().putString("city", cityName + "省").commit();
                                                    } else {
                                                        intent.putExtra("city", cityName);
                                                        spCity.edit().putString("city", cityName).commit();
                                                    }
                                                }

                                                setResult(10, intent);
                                                ActivitySelectCity.this.finish();


                                            }
                                        });

                                    }
                                });

                        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
                        break;
                    case R.layout.meituan_item_header_top:
                        MeituanTopHeaderBean meituanTopHeaderBean = (MeituanTopHeaderBean) o;
                        holder.setText(R.id.tvCurrent, meituanTopHeaderBean.getTxt());
                        break;

                    default:
                        break;
                }
            }
        };
        initLoacation();
    }

    /**
     * 组织数据源
     *
     * @param data
     * @return
     */
    private void initDatas(final String[] data) {
        //延迟两秒 模拟加载数据中....
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBodyDatas = new ArrayList<>();
                for (int i = 0; i < data.length; i++) {
                    MeiTuanBean cityBean = new MeiTuanBean();
                    cityBean.setCity(data[i]);//设置城市名称
                    mBodyDatas.add(cityBean);
                }
                if (mBodyDatas != null) {
                    LUtils.d(TAG, "===mBodyDatas=========" + mBodyDatas.toString());
                    //先排序
                    mIndexBar.getDataHelper().sortSourceDatas(mBodyDatas);
                } else {
                    LUtils.d(TAG, "===mBodyDatas====== null   ===");
                    //先排序

                }


                mAdapter.setDatas(mBodyDatas);
                final List<String> tempCity = new ArrayList<String>();
                mAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                        String[] temp = sp.getString("city", "0").split(",");
                        if (temp.length == 2) {
                            sp.edit().clear().commit();
                            sp.edit().putString("city", mBodyDatas.get(position).getCity()).commit();
                            spCity.edit().putString("city", mBodyDatas.get(position).getCity()).commit();
                        } else if (temp.length < 2 && !(temp.equals("0"))) {
                            String one = sp.getString("city", "0");
                            one += "," + mBodyDatas.get(position).getCity();
                            sp.edit().putString("city", one).commit();
                            spCity.edit().putString("city", mBodyDatas.get(position).getCity()).commit();
                        }
                        Intent intent = new Intent();
                        if (mBodyDatas.get(position).getCity().endsWith("市")) {
                            intent.putExtra("city", mBodyDatas.get(position).getCity());
                            spCity.edit().putString("city", mBodyDatas.get(position).getCity()).commit();
                        } else {
                            if (mBodyDatas.get(position).getCity().equals("海南")) {
                                intent.putExtra("city", mBodyDatas.get(position).getCity() + "省");
                                spCity.edit().putString("city", mBodyDatas.get(position).getCity() + "省").commit();
                            } else {
                                intent.putExtra("city", mBodyDatas.get(position).getCity());
                                spCity.edit().putString("city", mBodyDatas.get(position).getCity()).commit();
                            }
                        }


                        if (mBodyDatas.get(position).getCity().endsWith("市")) {
                            MyApplication.city = mBodyDatas.get(position).getCity();
                        } else {
                            if (mBodyDatas.get(position).getCity().equals("海南")) {
                                MyApplication.city = mBodyDatas.get(position).getCity() + "省";
                            } else intent.putExtra("city", mBodyDatas.get(position).getCity());

                        }
                        EventBus.getDefault().post(new MyEvent(1));
                        setResult(10, intent);

                        ActivitySelectCity.this.finish();
                    }

                    @Override
                    public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                        return false;
                    }
                });
                mHeaderAdapter.notifyDataSetChanged();
                mSourceDatas.addAll(mBodyDatas);

                mIndexBar.setmSourceDatas(mSourceDatas)//设置数据
                        .invalidate();

                mDecoration.setmDatas(mSourceDatas);


            }
        }, 0);

        //延迟两秒加载头部
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
//                MeituanHeaderBean header1 = mHeaderDatas.get(0);
//                header1.getCityList().clear();
//                header1.getCityList().add(MyApplication.city);

//                MeituanHeaderBean header2 = mHeaderDatas.get(1);
//                String[] str = sp.getString("city", "合肥市").split(",");
//                List<String> recentCitys = new ArrayList<>();
//                for (int i = 0; i < str.length; i++) {
//                    if (!(str[i].equals("0")))
//                        recentCitys.add(str[i]);
//                }

//                header2.setCityList(recentCitys);

                MeituanHeaderBean header3 = mHeaderDatas.get(0);
                List<String> hotCitys = new ArrayList<>();
                hotCitys.add("张家界市");
                hotCitys.add("黄山市");
                hotCitys.add("海南");
                hotCitys.add("重庆市");
                hotCitys.add("北京市");
                header3.setCityList(hotCitys);

                mHeaderAdapter.notifyItemRangeChanged(1, 3);

            }
        }, 0);

    }

    /**
     * 更新数据源
     *
     * @param view
     */
    public void updateDatas(View view) {
        for (int i = 0; i < 5; i++) {
            mBodyDatas.add(new MeiTuanBean("东京"));
            mBodyDatas.add(new MeiTuanBean("大阪"));
        }
        //先排序
        mIndexBar.getDataHelper().sortSourceDatas(mBodyDatas);
        mSourceDatas.clear();
        mSourceDatas.addAll(mHeaderDatas);
        mSourceDatas.addAll(mBodyDatas);

        mHeaderAdapter.notifyDataSetChanged();
        mIndexBar.invalidate();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    protected void loadCity() {
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(3600 * 24000);
        RequestParams params = new RequestParams();
        params.addBodyParameter("sid", MyApplication.deviceId);
        LUtils.d(TAG, "sid===" + MyApplication.deviceId);
        http.send(HttpRequest.HttpMethod.GET, Constance.HTTP_REQUEST_URL + "citylist", params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                // TODO Auto-generated method stub
                try {
                    spCity.edit().putString("city_list", responseInfo.result).commit();
                    String[] str1 = responseInfo.result.split("[:]");
                    String cityStr = "";
                    for (int i = 1; i < str1.length; i++) {
                        cityStr += str1[i] + ",";
                    }
                    String[] acity = cityStr.split(",");
                    initDatas(cityStr.split(","));


                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });
    }


    private final int permissionsRequestCode = 110;

    /*处理权限问题*/


    public void requestRunTimePermissions(String[] permissions) {
        if (permissions == null || permissions.length == 0)
            return;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkPermissionGranted(permissions)) {
            //提示已拥有权限

        } else {
            //申请权限
            requestPermission(permissions, permissionsRequestCode);
        }
    }

    public void requestPermission(final String[] permissions, final int permissionRequestCode) {
        if (shouldShowRequestPermissionRationale(permissions)) {
            new AlertDialog.Builder(this).setTitle(R.string.attention)
                    .setMessage(R.string.content_to_request_permission)
                    .setPositiveButton(R.string.ok_text, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(ActivitySelectCity.this, permissions, permissionRequestCode);
                        }
                    }).show();
        } else {
            ActivityCompat.requestPermissions(ActivitySelectCity.this, permissions, permissionRequestCode);
        }
    }

    private boolean shouldShowRequestPermissionRationale(String[] permissions) {
        boolean flag = false;
        for (String p : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ActivitySelectCity.this, p)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public boolean checkPermissionGranted(String[] permissions) {
        boolean result = true;
        for (String p : permissions) {
            if (ActivityCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                result = false;
                break;
            }
        }
        return result;
    }

    private boolean checkGranted(int[] grantResults) {
        boolean result = true;
        for (int grant : grantResults) {
            if (grant != PackageManager.PERMISSION_GRANTED) {
                result = false;
                break;
            }
        }
        return result;
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case permissionsRequestCode:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意，可以去放肆了。
                } else {
                    // 权限被用户拒绝了，洗洗睡吧。
                    ToastUtils.showToast(this, "权限被禁止请修改相关设置");
                    ActivitySelectCity.this.finish();

                }
                break;
        }
    }


}