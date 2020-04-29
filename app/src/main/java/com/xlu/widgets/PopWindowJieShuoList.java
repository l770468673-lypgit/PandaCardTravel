package com.xlu.widgets;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pandacard.teavel.R;
import com.xlu.po.Jieshuo;
import com.xlu.po.Zone;
import com.xlu.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giant on 2017/9/6.
 */

public class PopWindowJieShuoList extends PopupWindow implements View.OnClickListener{
    Context context;
    private TextView tvName;
    private EditText etJieShuoSearch;
    private ImageView ivCloseWindow;
    private ListView lv;
    private View convertView;
    private List<Jieshuo> jieshuos;
    private List<Jieshuo> datas;
    private Zone zone;
    PopJieShuoAdapter adapter;

    public PopWindowJieShuoList(Context context, final List<Jieshuo> jieshuos, Zone zone) {
        super(context);
        this.context=context;
        this.jieshuos=jieshuos;
        this.zone=zone;
        datas=new ArrayList<Jieshuo>();
        datas.addAll(jieshuos);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView=inflater.inflate(R.layout.popwindow_jieshuo_list,null);
        this.setContentView(convertView);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ScreenUtil.dip2px(context,300));
        tvName= (TextView) convertView.findViewById(R.id.tv_pop_jieshuo_name);
        ivCloseWindow= (ImageView) convertView.findViewById(R.id.iv_close_pop);
        lv= (ListView) convertView.findViewById(R.id.lv_jieshuo);
        etJieShuoSearch= (EditText) convertView.findViewById(R.id.et_search_jieshuo);
        ivCloseWindow.setOnClickListener(this);
        tvName.setText(zone.getName()+"("+ jieshuos.size()+"个景点)");
        adapter=new PopJieShuoAdapter(context,datas);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mListener!=null){
                    mListener.onItemOnClick(datas.get(position));
                }
            }
        });
        etJieShuoSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                List<Jieshuo> temp=new ArrayList<Jieshuo>();
                for(int i=0;i<jieshuos.size();i++){
                   if(isChild(jieshuos.get(i).getName(),etJieShuoSearch.getText().toString().trim())){
                     temp.add(jieshuos.get(i));
                   }
                }
               if(datas.size()!=0){
                   datas.clear();
                   datas.addAll(temp);
               }
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        etJieShuoSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(etJieShuoSearch.getText().toString().trim().isEmpty()){
                    if(datas.size()!=0)
                        datas.clear();
                    datas.addAll(jieshuos);
                    adapter.notifyDataSetChanged();
                }

            }
        });

    }
    private boolean isChild(String parent, String child){
        if(parent.indexOf(child)==-1){
            return false;
        }else{
            return true;
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_close_pop:
                this.dismiss();
                break;
        }
    }

    public interface OnMyItemOnClickListener{
        void onItemOnClick(Jieshuo jieshuo);
    }
    private OnMyItemOnClickListener mListener;

    public void setmListener(OnMyItemOnClickListener mListener) {
        this.mListener = mListener;
    }

}

class PopJieShuoAdapter extends BaseAdapter {
    Context context;
    List<Jieshuo> list;

    public PopJieShuoAdapter(Context context, List<Jieshuo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JieShuoViewHolder holder=null;
        if(convertView==null){
            holder=new JieShuoViewHolder();
            convertView=  LayoutInflater.from(context).inflate(R.layout.pop_jieshuo_item,null);
            holder.tv= (TextView) convertView.findViewById(R.id.tv_item_jieshuos);
            convertView.setTag(holder);
        }else{
            holder= (JieShuoViewHolder) convertView.getTag();
        }
        holder.tv.setText(list.get(position).getName());
        return convertView;
    }
    class JieShuoViewHolder{
        TextView tv;
    }

}
