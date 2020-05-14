package com.xlu.fragments;

import android.annotation.SuppressLint;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.gson.reflect.TypeToken;
import com.pandacard.teavel.R;
import com.pandacard.teavel.apps.MyApplication;
import com.pandacard.teavel.utils.LUtils;
import com.xlu.adapters.AdapterGroup;
import com.xlu.adapters.AdapterMeiShi;
import com.xlu.adapters.AdapterRecomment;
import com.xlu.adapters.AdapterTicketList;
import com.xlu.adapters.AdapterZhuSu;
import com.xlu.po.Merchant1;
import com.xlu.po.MyEvent;
import com.xlu.po.ProductSpecal;
import com.xlu.utils.AppUtil;
import com.xlu.utils.Constance;
//import com.xlu.utils.DBUtil;
import com.xlu.utils.DBUtil;
import com.xlu.utils.JsonUtil;
import com.xlu.widgets.PullToRefreshLayout;
import com.xlu.widgets.SuperListView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.com.wideroad.BaseHttp;
import cn.com.wideroad.http.AjaxCallBack;
import cn.com.wideroad.http.AjaxParams;
import cn.com.wideroad.utils.StringUtil;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by giant on 2017/8/3.
 */
public class FragmentFindItem extends Fragment implements PullToRefreshLayout.OnRefreshListener {

    private static String TAG = "FragmentFindItem";
    List<Merchant1> datas;
    List<ProductSpecal> lists;
    View view;
    PullToRefreshLayout pullRefresh;
    SuperListView slv;
    BaseAdapter adapter;
    String requestString;
    int type;
    TextView tvTitle;

    public FragmentFindItem() {
    }

    public FragmentFindItem(int type) {
        this.type = type;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().register(this);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_find_item, null);
        findView();
        if (type == Constance.GONGLUE_TYPE || type == Constance.XIANLU_TYPE) {
            requestString = "merchantPros";
        } else
            requestString = "merchantlist";
        initSeting();
        pullRefresh.setType(PullToRefreshLayout.ListViewType.all);
        pullRefresh.setOnRefreshListener(this);
        loadData(type);
        LUtils.d(TAG, "type==" + type + "requestString===" + requestString);
        return view;
    }

    private void findView() {
        pullRefresh = view.findViewById(R.id.pullrlt);
        slv = view.findViewById(R.id.mlv_find_item);
        tvTitle = view.findViewById(R.id.tv_all_title_name);
    }


    private void initSeting() {
        datas = new ArrayList<Merchant1>();
        lists = new ArrayList<ProductSpecal>();
        if (type == Constance.TICKET_TYPE) {   //门票 0
            adapter = new AdapterTicketList(datas, getActivity());
        } else if (type == Constance.JIUDIAN_TYPE) {  //酒店  2
            adapter = new AdapterZhuSu(getActivity(), datas);
        } else if (type == Constance.MEISHI_TYPE) {  //美食  3
            adapter = new AdapterMeiShi(datas, getActivity());
        } else if (type == Constance.XIANLU_TYPE) { // 线路  1
            adapter = new AdapterGroup(getActivity(), lists);
        } else if (type == Constance.TECHAN_TYPE) {
//            adapter = new AdapterSimpleBuy(getActivity(), lists);
        } else if (type == Constance.GONGLUE_TYPE) { //攻略  5
            adapter = new AdapterRecomment(getActivity(), lists);
        }
//        else if (type == Constance.DAOLAN_TYPE) { //导览6
//            //            adapter = new AdapterRecomment(getActivity(), lists);
//        }
//        lrl.setChildView(slv);
//        slv.setAdapter(adapter);
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//        loadData(type);
//    }

    private void loadDataMore(final int type) {
        BaseHttp http = new BaseHttp();
        AjaxParams params = new AjaxParams();
        if (type == Constance.JIUDIAN_TYPE)
            params.put("type", "酒店");
        else if (type == Constance.MEISHI_TYPE) {
            params.put("type", "餐饮");
        } else if (type == Constance.TICKET_TYPE) {
            params.put("type", "门票");
        } else if (type == Constance.XIANLU_TYPE) {
            params.put("type", "专车");
        } else if (type == Constance.GONGLUE_TYPE) {
            params.put("type", "线路攻略");
        }
        params.put("jindu", MyApplication.jingdu + "");
        params.put("weidu", MyApplication.weidu + "");
        params.put("page", page + "");

        LUtils.d(TAG, "jindu==" + MyApplication.jingdu);
        LUtils.d(TAG, "weidu==" + MyApplication.weidu);
        LUtils.d(TAG, "page==" + page + "");
        LUtils.d(TAG, "city==" + DBUtil.getCity(MyApplication.city).getBianhao() + "");
        if (DBUtil.getCity(MyApplication.city) != null) {
            params.put("city", DBUtil.getCity(MyApplication.city).getBianhao());
        } else {
            params.put("city", "");
        }
        LUtils.d(TAG, " 点击事件 ====" + type);
        http.post(Constance.HTTP_REQUEST_URL_BIZ + requestString, params,
                new AjaxCallBack<Object>() {

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        LUtils.d(TAG, "  = loadDataMore  =onFailure==");
                        // TODO Auto-generated method stub
                        super.onFailure(t, errorNo, strMsg);
                        pullRefresh.loadmoreFinish(PullToRefreshLayout.FAIL);
//                        pullRefresh.notifyAll();
//                        lrl.setLoading(false);
                    }

                    @Override
                    public void onSuccess(Object t) {
                        LUtils.d(TAG, "  = loadDataMore  =onSuccess==");
                        // TODO Auto-generated method stub
                        super.onSuccess(t);
                        try {
                            if (type == Constance.GONGLUE_TYPE || type == Constance.XIANLU_TYPE) {
                                List<ProductSpecal> list2 = JsonUtil.getList(t.toString(), ProductSpecal.class);
                                lists.addAll(list2);
                                if (list2.size() == 0) {
                                    AppUtil.showToastMsg(FragmentFindItem.this.getActivity(), "亲，数据加载完毕");
                                    page--;
                                    pullRefresh.loadmoreFinish(PullToRefreshLayout.SUCCEED_FINISHED);
                                } else {
                                    adapter.notifyDataSetChanged();
                                    pullRefresh.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                                }

                            } else {
                                List<Merchant1> list2 = JsonUtil
                                        .getList(t.toString(), Merchant1.class);
                                datas.addAll(list2);
                                if (list2.size() == 0) {
                                    AppUtil.showToastMsg(FragmentFindItem.this.getActivity(), "亲，数据加载完毕");
                                    page--;
                                    pullRefresh.loadmoreFinish(PullToRefreshLayout.SUCCEED_FINISHED);
                                } else {
                                    adapter.notifyDataSetChanged();
                                    pullRefresh.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                                }
                            }


//                            pullRefresh.setLoading(false);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }

                });

    }

    private void loadData(final int type) {
        slv.loading();
        BaseHttp http = new BaseHttp();
        AjaxParams params = new AjaxParams();
        if (type == Constance.JIUDIAN_TYPE)
            params.put("type", "酒店");
        else if (type == Constance.MEISHI_TYPE) {
            params.put("type", "餐饮");
        } else if (type == Constance.TICKET_TYPE) {
            params.put("type", "门票");
        } else if (type == Constance.XIANLU_TYPE) {
            params.put("type", "专车");
        } else if (type == Constance.GONGLUE_TYPE) {
            params.put("type", "线路攻略");
        }
        params.put("jindu", MyApplication.jingdu + "");
        params.put("weidu", MyApplication.weidu + "");
        params.put("page", page + "");
        // params.put("status", status);
        LUtils.d(TAG, "jindu==" + MyApplication.jingdu);
        LUtils.d(TAG, "weidu==" + MyApplication.weidu);
        LUtils.d(TAG, "page==" + page + "");
        LUtils.d(TAG, "type==" + type);

        if (DBUtil.getCity(MyApplication.city) != null) {
            params.put("city", DBUtil.getCity(MyApplication.city).getBianhao());
        } else {
            params.put("city", "");
        }
        LUtils.d(TAG, " 点击事件  loadData ====");
        http.post(Constance.HTTP_REQUEST_URL_BIZ + requestString, params,
                new AjaxCallBack<Object>() {

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        LUtils.d(TAG, " 点击事件  loadData ===onFailure =");
                        // TODO Auto-generated method stub
                        super.onFailure(t, errorNo, strMsg);
                        if (slv != null) {
                            slv.loadFail(0, "加载失败", "检查网络连接", "",
                                    new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            // TODO Auto-generated method stub
                                            loadData(type);
                                        }
                                    });
                            pullRefresh.refreshFinish(PullToRefreshLayout.FAIL);
                        }

                    }

                    int i = 0;

                    @Override
                    public void onSuccess(Object t) {
                        LUtils.d(TAG, " 点击事件  loadData ===onSuccess =");
                        // TODO Auto-generated method stub
                        super.onSuccess(t);
                        try {
                            Type types1 = new TypeToken<List<ProductSpecal>>() {

                            }.getType();
                            Type types2 = new TypeToken<List<Merchant1>>() {
                            }.getType();
                            datas.clear();
                            lists.clear();
                            if (type == Constance.XIANLU_TYPE || type == Constance.GONGLUE_TYPE) {
                                lists.addAll((List<ProductSpecal>) (JsonUtil.fromJsonToObject(StringUtil.removeNull(t), types1)));
                                i = i + 1;
                                LUtils.d(TAG, "====" + (JsonUtil.fromJsonToObject(StringUtil.removeNull(t), types1).toString()));
                                if (lists.size() == 0) {
                                    slv.loadFail(0, "暂无数据", "敬请期待", "",
                                            new View.OnClickListener() {

                                                @Override
                                                public void onClick(View v) {
                                                    // TODO Auto-generated method
                                                    // stub
                                                   loadData(type);
                                                }
                                            });
                                    pullRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
                                } else {
                                    LUtils.d(TAG, " 点击事件  onSuccess ==lists.size()!=0= =");
                                    slv.loadSuccess();
                                    slv.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    pullRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
                                }
                            } else {
                                datas.addAll((List<Merchant1>) JsonUtil
                                        .fromJsonToObject(StringUtil.removeNull(t),
                                                types2));
                                LUtils.d(TAG, "====" + (JsonUtil.fromJsonToObject(StringUtil.removeNull(t), types1)));
                                i = i + 1;
                                if (datas.size() == 0) {
                                    slv.loadFail(0, "暂无数据", "敬请期待", "",
                                            new View.OnClickListener() {

                                                @Override
                                                public void onClick(View v) {
                                                    // TODO Auto-generated method
                                                    // stub

                                                   loadData(type);
                                                }
                                            });
                                    pullRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
                                } else {
                                    slv.loadSuccess();
                                    slv.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    pullRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);

                                }
                            }


                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }

                });

    }


    int page = 1;

    @Subscribe
    public void onEventMainThread(MyEvent e) {
        LUtils.d(TAG, "=  onEventMainThread  ===");
        if (e.getId() == 1) {
            loadData(type);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().unregister(this);
        }

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        loadData(type);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        page++;
        loadDataMore(type);
    }
}
