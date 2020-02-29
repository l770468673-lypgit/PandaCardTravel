package com.xlu.popwindows;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pandacard.teavel.R;
import com.pandacard.teavel.apps.MyApplication;
import com.xlu.po.TuanInfo;
import com.xlu.utils.Constance;
import com.xlu.utils.DensityUtil;


import java.util.Hashtable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by giant on 2017/7/21.
 */

public class PopwindowQrCode extends PopupWindow {
    private static int QR_WIDTH = 0;
    private static int QR_HEIGHT = 0;
    private Context context;
    private View conentView;

    @BindView(R.id.iv_pic)
    ImageView ivPic;

    @BindView(R.id.iv_qr_code)
    ImageView ivQrCode;

    @BindView(R.id.tv_name)
    TextView tvName;

    @BindView(R.id.tv_city)
    TextView tvCity;

    private TuanInfo tuanInfo;


    public PopwindowQrCode(Context context, TuanInfo tuanInfo) {
        this.context = context;
        this.tuanInfo = tuanInfo;
        QR_WIDTH = DensityUtil.dip2px(context, 130);
        QR_HEIGHT = QR_WIDTH;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.popwindow_qr_code, null);
        conentView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                PopwindowQrCode.this.dismiss();
            }
        });
        ButterKnife.bind(this, conentView);
        this.setContentView(conentView);
        this.setFocusable(false);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        tvName.setText(tuanInfo.getName());
        tvCity.setText(tuanInfo.getCity());
        ImageLoader.getInstance().displayImage(
                Constance.HTTP_URL + tuanInfo.getPic(), ivPic, MyApplication.circleOption);
        createQRImage(tuanInfo.getId() + "");
    }

    public void createQRImage(String url) {
        try {
            //判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1) {
                return;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            //显示到一个ImageView上面
            ivQrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
