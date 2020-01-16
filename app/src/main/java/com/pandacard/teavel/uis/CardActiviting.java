package com.pandacard.teavel.uis;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.hardware.Camera;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.alibaba.fastjson.serializer.StringCodec;
import com.arcsoft.face.ActiveFileInfo;
import com.arcsoft.face.AgeInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.GenderInfo;
import com.arcsoft.face.LivenessInfo;
import com.arcsoft.face.VersionInfo;
import com.arcsoft.face.enums.DetectFaceOrientPriority;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.RuntimeABI;
import com.crb.cttic.pay.card.bean.CardInfoGather;
import com.crb.cttic.pay.device.SmartCardNfcTag;
import com.crb.cttic.pay.utils.ReadCardUtils;
import com.eidlink.idocr.sdk.EidLinkSE;
import com.eidlink.idocr.sdk.EidLinkSEFactory;
import com.pandacard.teavel.ParamConst;
import com.pandacard.teavel.R;
import com.pandacard.teavel.arcesoft.CameraHelper;
import com.pandacard.teavel.arcesoft.CameraListener;
import com.pandacard.teavel.arcesoft.CompareResult;
import com.pandacard.teavel.arcesoft.ConfigUtil;
import com.pandacard.teavel.arcesoft.DrawHelper;
import com.pandacard.teavel.arcesoft.DrawInfo;
import com.pandacard.teavel.arcesoft.FaceHelper;
import com.pandacard.teavel.arcesoft.FaceListener;
import com.pandacard.teavel.arcesoft.FacePreviewInfo;
import com.pandacard.teavel.arcesoft.FaceRectView;
import com.pandacard.teavel.arcesoft.FaceServer;
import com.pandacard.teavel.arcesoft.LivenessType;
import com.pandacard.teavel.arcesoft.RecognizeColor;
import com.pandacard.teavel.arcesoft.RequestFeatureStatus;
import com.pandacard.teavel.arcesoft.RequestLivenessStatus;
import com.pandacard.teavel.https.HttpCallback;
import com.pandacard.teavel.https.HttpManager;
import com.pandacard.teavel.https.beans.ResourcesBean;
import com.pandacard.teavel.https.beans.SecurityCode;
import com.pandacard.teavel.https.beans.bean_addCards;
import com.pandacard.teavel.https.beans.bean_person;
import com.pandacard.teavel.https.beans.bindSuccessBean;
import com.pandacard.teavel.utils.Bitmap2NV21;
import com.pandacard.teavel.utils.Constants;
import com.pandacard.teavel.utils.HttpRetrifitUtils;
import com.pandacard.teavel.utils.KeyboardUtils;
import com.pandacard.teavel.utils.LUtils;
import com.pandacard.teavel.utils.ShareUtil;
import com.pandacard.teavel.utils.StatusBarUtil;
import com.pandacard.teavel.utils.TimerUtils;
import com.pandacard.teavel.utils.ToastUtils;
import com.pandacard.teavel.utils.readIDcardUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CardActiviting extends AppCompatActivity implements View.OnClickListener, ViewTreeObserver.OnGlobalLayoutListener, TextWatcher {
    private String TAG = "CardActiviting";
    private ImageView mSave_imageview_back;
    private TextView eeeererer;
    private TextView mChongzhinfc_textView;
    //    private String mcid = "12C1B00";//!!!!!!!!!!!!!!CID替换为分配的CID！！！！！！！！！！！！
    private String mcid = "1320D00";//!!!!!!!!!!!!!!CID替换为分配的CID！！！！！！！！！！！！
    private String ip = "testnidocr.eidlink.com";//云解码服务地址
    private int port = 9989;//云解码端口
    private PendingIntent pi;
    NfcAdapter nfcAdapter;

    private EidLinkSE eid;
    private int readCode;
    public final static int READ_CARD_START = 10000001;
    public final static int READ_CARD_SUCCESS = 30000003;
    public final static int READ_CARD_FAILED = 90000009;
    public final static int READ_CARD_DELAY = 40000004;
    //    private static final int HANDLER_MSG_FACE = 121201;

    public static String[][] TECHLISTS;
    public static IntentFilter[] FILTERS;
    private static final int MAX_DETECT_NUM = 10;
    /**
     * 当FR成功，活体未成功时，FR等待活体的时间
     */
    private static final int WAIT_LIVENESS_INTERVAL = 1000;
    /**
     * 失败重试间隔时间（ms）
     */
    private static final long FAIL_RETRY_INTERVAL = 1000;
    /**
     * 出错重试最大次数
     */
    private static final int MAX_RETRY_TIME = 3;

    private CameraHelper cameraHelper;
    private DrawHelper drawHelper;
    private Camera.Size previewSize;
    /**
     * 优先打开的摄像头，本界面主要用于单目RGB摄像头设备，因此默认打开前置
     */
    private Integer rgbCameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;

    /**
     * VIDEO模式人脸检测引擎，用于预览帧人脸追踪
     */
    private FaceEngine ftEngine;
    /**
     * 用于特征提取的引擎
     */
    private FaceEngine frEngine;
    /**
     * IMAGE模式活体检测引擎，用于预览帧人脸活体检测
     */
    private FaceEngine flEngine;

    private int ftInitCode = -1;
    private int frInitCode = -1;
    private int flInitCode = -1;
    private FaceHelper faceHelper;
    private List<CompareResult> compareResultList;
    //    private FaceSearchResultAdapter adapter;

    /**
     * 用于记录人脸识别相关状态
     */
    private ConcurrentHashMap<Integer, Integer> requestFeatureStatusMap = new ConcurrentHashMap<>();
    /**
     * 用于记录人脸特征提取出错重试次数
     */
    private ConcurrentHashMap<Integer, Integer> extractErrorRetryMap = new ConcurrentHashMap<>();
    /**
     * 用于存储活体值
     */
    private ConcurrentHashMap<Integer, Integer> livenessMap = new ConcurrentHashMap<>();
    /**
     * 用于存储活体检测出错重试次数
     */
    private ConcurrentHashMap<Integer, Integer> livenessErrorRetryMap = new ConcurrentHashMap<>();

    private CompositeDisposable getFeatureDelayedDisposables = new CompositeDisposable();
    private CompositeDisposable delayFaceTaskCompositeDisposable = new CompositeDisposable();
    /**
     * 相机预览显示的控件，可为SurfaceView或TextureView
     */
    private View previewView;
    /**
     * 绘制人脸框的控件
     */
    private FaceRectView faceRectView;

    //    private Switch switchLivenessDetect;

    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    /**
     * 识别阈值
     */
    private static final float SIMILAR_THRESHOLD = 0.5F;

    static {
        TECHLISTS = new String[][]{
                {IsoDep.class.getName()},
                {NfcV.class.getName()}, {NfcF.class.getName()},};

        try {
            FILTERS = new IntentFilter[]{
                    new IntentFilter(
                            NfcAdapter.ACTION_TECH_DISCOVERED, "*/*")};
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }
    }


    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case READ_CARD_START:
                    mCardact_pleaseon.setText(R.string.cardactive_idxcrad_start);
                    break;
                case READ_CARD_FAILED:
                    mCardact_pleaseon.setText(R.string.cardactive_idxcrad_failed);
                    break;
                case READ_CARD_SUCCESS:
                    // 本地人脸库初始化
                    FaceServer.getInstance().init(CardActiviting.this);
                    initEngine();
                    initCamera();
                    String reqid = (String) msg.obj;
                    LUtils.e(TAG, "   读卡成功    salt    " + reqid);
                    queryIdCards(reqid);
                    readCode = 0;
                    break;
                case READ_CARD_DELAY:
                    int ss = msg.arg1;
                    LUtils.e(TAG, "ss    " + ss);
                    break;
                default:
                    break;
            }
        }
    };
    //    private TextView mCardact_idnumn;
    //    private ImageView cardact_iamgv;
    private TextView mCardact_pleaseon;
    private Button mCardactive_nextstep;
    private RelativeLayout mRely_readcard;

    private ImageView mImage_facehead;
    private ImageView mImage_setimg;
    private RelativeLayout mRely_facehead;

    private FrameLayout mFramlay_allfaceview;
    private String code4num;

    /**
     * 所需的所有权限信息
     */
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE
    };
    private Button mFaceactive_nextstep;
    private ImageView mPandacard_image;
    private TextView mPandacard_pleaseon;
    private Button mPanda_nextstep;
    private RelativeLayout mRelay_pandacard;
    private TextView mPandacard_cardnum;
    private String mMAppletNo;
    private EditText mCard_bind_phonenum;

    private EditText mCard_bind_password;
    private Button mCard_bind_commit;
    private RelativeLayout mRelay_bindphone;
    private byte[] mNv21;
    private Bitmap mBit2map;
    private Button mCard_bind_yanzhengma;
    private String mVcode;
    private TextView mPhonenums;
    private String mName;
    private TextView mLly_okname;
    private Button mLly_bindsuccess;
    private Button mLly_bindfailed;
    private EditText mBind_card_checkcode;
    private LinearLayout mLly_yanzhengma;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_activiting);

        StatusBarUtil.setDrawable(this, R.drawable.mine_title_jianbian);
        activeEngine(null);
        // Activity启动后就锁定为启动时的方向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        initView();
        initNfc();
        initEid();

        ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
        readCode = 0;
    }

    /**
     * 初始化引擎
     */
    private void initEngine() {
        ftEngine = new FaceEngine();
        ftInitCode = ftEngine.init(this, DetectMode.ASF_DETECT_MODE_VIDEO, DetectFaceOrientPriority.ASF_OP_ALL_OUT,
                16, MAX_DETECT_NUM, FaceEngine.ASF_FACE_DETECT);

        frEngine = new FaceEngine();
        frInitCode = frEngine.init(this, DetectMode.ASF_DETECT_MODE_IMAGE, DetectFaceOrientPriority.ASF_OP_ALL_OUT,
                16, MAX_DETECT_NUM, FaceEngine.ASF_FACE_RECOGNITION);

        flEngine = new FaceEngine();
        flInitCode = flEngine.init(this, DetectMode.ASF_DETECT_MODE_IMAGE, DetectFaceOrientPriority.ASF_OP_ALL_OUT,
                16, MAX_DETECT_NUM, FaceEngine.ASF_LIVENESS);


        VersionInfo versionInfo = new VersionInfo();
        ftEngine.getVersion(versionInfo);
        LUtils.i(TAG, "initEngine:  init: " + ftInitCode + "  version:" + versionInfo);

        if (ftInitCode != ErrorInfo.MOK) {
            LUtils.i(TAG, "ftInitCode:  init: " + ftInitCode);
        } else {

            LUtils.i(TAG, "ftInitCode:  init: " + ftInitCode);
        }
        if (frInitCode != ErrorInfo.MOK) {

            LUtils.i(TAG, "frInitCode:  init: " + frInitCode);
        } else {
            LUtils.i(TAG, "frInitCode:  init: " + frInitCode);
        }
        if (flInitCode != ErrorInfo.MOK) {

            LUtils.i(TAG, "flInitCode:  init: " + flInitCode);
        } else {
            LUtils.i(TAG, "flInitCode:  init: " + flInitCode);
        }
    }

    /**
     * 激活引擎
     *
     * @param view
     */
    public void activeEngine(final View view) {

        if (view != null) {
            view.setClickable(false);
        }
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) {
                RuntimeABI runtimeABI = FaceEngine.getRuntimeABI();
                LUtils.i(TAG, "subscribe: getRuntimeABI() " + runtimeABI);
                int activeCode = FaceEngine.activeOnline(CardActiviting.this, Constants.APP_ID, Constants.SDK_KEY);
                emitter.onNext(activeCode);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer activeCode) {
                        ActiveFileInfo activeFileInfo = new ActiveFileInfo();
                        int res = FaceEngine.getActiveFileInfo(CardActiviting.this, activeFileInfo);
                        if (res == ErrorInfo.MOK) {
                            LUtils.i(TAG, "activeEngine " + res);
                        }
                        LUtils.i(TAG, "activeEngine " + res);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view != null) {
                            view.setClickable(true);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 销毁引擎，faceHelper中可能会有特征提取耗时操作仍在执行，加锁防止crash
     */
    private void unInitEngine() {
        if (ftInitCode == ErrorInfo.MOK && ftEngine != null) {
            synchronized (ftEngine) {
                int ftUnInitCode = ftEngine.unInit();
                LUtils.i(TAG, "unInitEngine: " + ftUnInitCode);
            }
        }
        if (frInitCode == ErrorInfo.MOK && frEngine != null) {
            synchronized (frEngine) {
                int frUnInitCode = frEngine.unInit();
                LUtils.i(TAG, "unInitEngine: " + frUnInitCode);
            }
        }
        if (flInitCode == ErrorInfo.MOK && flEngine != null) {
            synchronized (flEngine) {
                int flUnInitCode = flEngine.unInit();
                LUtils.i(TAG, "unInitEngine: " + flUnInitCode);
            }
        }
    }

    /*Android 标准初始化*/
    private void initNfc() {
        LUtils.e(TAG, "initNfc  1 ");
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            //            tv.setText("设备不支持NFC");
            return;
        }
        if (!nfcAdapter.isEnabled()) {
            // tv.setText("请在系统设置中先启用NFC功能");
            return;
        }
        pi = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

    }

    private void initView() {
        compareResultList = new ArrayList<>();
        WindowManager wm1 = this.getWindowManager();
        int width1 = wm1.getDefaultDisplay().getWidth() - 30;

        mSave_imageview_back = findViewById(R.id.chongzhinfc_imageview_back);
        mChongzhinfc_textView = findViewById(R.id.chongzhinfc_textView);
        mCard_bind_yanzhengma = findViewById(R.id.card_bind_yanzhengma);

        //        mCardact_idnumn = findViewById(R.id.cardact_idnumn);
        //        cardact_iamgv = findViewById(R.id.cardact_iamgv);
        mCardact_pleaseon = findViewById(R.id.cardact_pleaseon);
        mLly_okname = findViewById(R.id.lly_okname);
        mCardactive_nextstep = findViewById(R.id.cardactive_nextstep);


        // 三个布局
        mRely_readcard = findViewById(R.id.rely_readcard);
        mRelay_pandacard = findViewById(R.id.relay_pandacard);
        mRely_facehead = findViewById(R.id.rely_facehead);
        mRelay_bindphone = findViewById(R.id.relay_bindphone);

        //2 cameraview
        mFramlay_allfaceview = findViewById(R.id.framlay_allfaceview);
        faceRectView = findViewById(R.id.face_rect_view);
        previewView = findViewById(R.id.texture_preview);
        mFaceactive_nextstep = findViewById(R.id.faceactive_nextstep);
        mImage_facehead = findViewById(R.id.image_facehead);
        mImage_setimg = findViewById(R.id.image_setimg);

        //3 pandacard view
        //        mPandacard_image = findViewById(R.id.pandacard_image);
        mPandacard_pleaseon = findViewById(R.id.pandacard_readok);
        mPandacard_cardnum = findViewById(R.id.pandacard_cardnum);
        mPanda_nextstep = findViewById(R.id.panda_nextstep);
        mPhonenums = findViewById(R.id.phonenums);
        mLly_bindfailed = findViewById(R.id.lly_bindfailed);
        mBind_card_checkcode = findViewById(R.id.bind_card_checkcode);
        mLly_yanzhengma = findViewById(R.id.lly_yanzhengma);

        // 4绑定手机号 和号码
        mCard_bind_phonenum = findViewById(R.id.card_bind_phonenum);
        mCard_bind_password = findViewById(R.id.card_bind_password);
        mCard_bind_commit = findViewById(R.id.card_bind_commit);
        mLly_bindsuccess = findViewById(R.id.lly_bindsuccess);


        ViewGroup.LayoutParams layoutParamsf = mFramlay_allfaceview.getLayoutParams();
        layoutParamsf.width = width1;
        layoutParamsf.height = width1;

        ViewGroup.LayoutParams layoutParamsf2 = previewView.getLayoutParams();
        layoutParamsf2.width = width1 - 30;
        layoutParamsf2.height = width1 - 30;


        ViewGroup.LayoutParams layoutParams = mImage_facehead.getLayoutParams();
        layoutParams.width = width1;
        layoutParams.height = width1;

        mChongzhinfc_textView.setText(R.string.cardactive_title_eidact);
        mPandacard_pleaseon.setText(R.string.cardactive_idcardreadover);
        mCardact_pleaseon.setText(R.string.cardactive_idxcrad_read);

        mCard_bind_yanzhengma.setOnClickListener(this);
        mLly_bindfailed.setOnClickListener(this);
        mLly_bindsuccess.setOnClickListener(this);
        mSave_imageview_back.setOnClickListener(this);
        mCardactive_nextstep.setOnClickListener(this);
        mRely_readcard.setOnClickListener(this);
        mPanda_nextstep.setOnClickListener(this);
        mFaceactive_nextstep.setOnClickListener(this);

        mCard_bind_commit.setOnClickListener(this);
        eeeererer = findViewById(R.id.eeeererer);
        //        //在布局结束后才做初始化操作
        previewView.getViewTreeObserver().addOnGlobalLayoutListener(this);

        mBind_card_checkcode.addTextChangedListener(this);
    }

    /*eid读卡初始化,读卡必须初始化,具体参数见文档*/
    private void initEid() {
        try {
            eid = EidLinkSEFactory.getEidLinkSEForNfc(mHandler, this, mcid, ip, port, 1);
            LUtils.d(TAG, "eid===" + eid);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chongzhinfc_imageview_back:
            case R.id.lly_bindfailed:
                finish();
                break;
            case R.id.lly_bindsuccess:

                if (mPhonenums.getText().toString().trim().length() == 11 && mPandacard_cardnum.getText().toString().trim().length() > 1) {

                    AddCards();

                } else {
                    ToastUtils.showToast(this, "---------输入不正确 -----------");
                }
                break;
            case R.id.card_bind_yanzhengma:

                //                if (mCard_bind_phonenum.getText().toString().trim().length() == 11) {
                //                    TimerUtils.initTimer(this, mCard_bind_yanzhengma, 60000, 1000);
                //                    TimerUtils.TimerStart();
                //                    Call<SecurityCode> securityCode =
                //                            HttpManager.getInstance().getHttpClient().toSMSCode(mCard_bind_phonenum.getText().toString().trim());
                //                    securityCode.enqueue(new Callback<SecurityCode>() {
                //                        @Override
                //                        public void onResponse(Call<SecurityCode> call, Response<SecurityCode> response) {
                //                            SecurityCode body = response.body();
                //                            if (body != null) {
                //                                mVcode = body.getExtra().getVcode();
                //                            }
                //                        }
                //
                //                        @Override
                //                        public void onFailure(Call<SecurityCode> call, Throwable t) {
                //
                //                        }
                //                    });
                //                } else {
                //                    ToastUtils.showToast(this, R.string.login_wx_editokphone);
                //                }
                break;
            case R.id.card_bind_commit:
                String phonenum = mCard_bind_phonenum.getText().toString().trim();
                String password = mCard_bind_password.getText().toString().trim();

                break;
            case R.id.panda_nextstep:
                mRelay_pandacard.setVisibility(View.GONE);
                mRelay_bindphone.setVisibility(View.VISIBLE);
                if (ShareUtil.getString(HttpRetrifitUtils.SERNAME_PHONE) != null) {
                    mCard_bind_phonenum.setText(ShareUtil.getString(HttpRetrifitUtils.SERNAME_PHONE));
                }
                mPandacard_cardnum.setText(mMAppletNo);
                mPhonenums.setText(ShareUtil.getString(HttpRetrifitUtils.SERNAME_PHONE));
                mLly_okname.setText(mName);
                break;
            case R.id.faceactive_nextstep:
                mRelay_pandacard.setVisibility(View.VISIBLE);

                mRely_readcard.setVisibility(View.GONE);
                mRely_facehead.setVisibility(View.GONE);

                if (cameraHelper != null) {
                    cameraHelper.stop();
                }
                readCode = 0;
                break;
            case R.id.cardactive_nextstep:
                mRely_readcard.setVisibility(View.GONE);
                mRelay_pandacard.setVisibility(View.GONE);
                mRely_facehead.setVisibility(View.VISIBLE);

                break;
        }

    }

    public void commiyPhone() {

        Call<bindSuccessBean> resourcesBeanCall = HttpManager.getInstance().getHttpClient().mbindCard
                (mPandacard_cardnum.getText().toString(), mPhonenums.getText().toString(), mLly_okname.getText().toString().trim(), "");
        resourcesBeanCall.enqueue(new Callback<bindSuccessBean>() {
            @Override
            public void onResponse(Call<bindSuccessBean> call, Response<bindSuccessBean> response) {
                bindSuccessBean body = response.body();
                if (body != null) {
                    if (body.getCode() == 1) {
                        ToastUtils.showToast(CardActiviting.this, body.getMsg());

                        finish();
                        Intent intent = new Intent(CardActiviting.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        ToastUtils.showToast(CardActiviting.this, body.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<bindSuccessBean> call, Throwable t) {

            }
        });
    }

    public void AddCards() {
        Call<bean_addCards> bean_personCall = HttpManager.getInstance().getHttpClient().addCard(mMAppletNo, code4num);
        bean_personCall.enqueue(new Callback<bean_addCards>() {
            @Override
            public void onResponse(Call<bean_addCards> call, Response<bean_addCards> response) {
                if (response.body() != null) {
                    if (response.body() != null) {
                        int code = response.body().getCode();
                        if (code == 1) {
                            commiyPhone();
                        } else {
                            ToastUtils.showToast(CardActiviting.this, response.body().getMsg().toString());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<bean_addCards> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (eid != null) {
            eid.enableReaderMode(nfcAdapter, this);
        }

        //        eid.disableReaderMode();
        //        eid.ReadCard(1,callBack);
        try {
            LUtils.e(TAG, "onResume nfcAdapter  " + nfcAdapter);
            if (nfcAdapter != null) {
                nfcAdapter.enableForegroundDispatch(this, pi,
                        FILTERS, TECHLISTS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (null != nfcAdapter) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    //NFC 读卡，读卡过程中产生的状态和结果，用 Handler 传递结果。
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        SmartCardNfcTag ctticReader = SmartCardNfcTag.getInstance(intent);
        LUtils.i(TAG, "readCode===" + readCode);
        switch (readCode) {

            case 0:
                //读卡
                ReadCardUtils.getInstance().getReadCardInfo(CardActiviting.this, "", ctticReader, new ReadCardUtils.ReadCardUtilsListener() {
                    @Override
                    public void readCardFail(int errCode, String errMes) {
                        ToastUtils.showToast(CardActiviting.this, "读卡失败 errCode =" + errCode + "errMes=" + errMes.toString());
                        LUtils.i(TAG, errMes.toString());
                    }

                    @Override
                    public void readCardSuccess(CardInfoGather cardInfoGather) {
                        //                        ToastUtils.showToast(CardActiviting.this, "读卡成功" + cardInfoGather.toString());
                        setResult(RESULT_OK, new Intent().putExtra(ParamConst.CARD_INFO_GATHER, cardInfoGather));
                        LUtils.i(TAG, cardInfoGather.toString());
                        mMAppletNo = cardInfoGather.getPublicBasicInfo().getAppletNo();
                        mPandacard_pleaseon.setText(R.string.cardactive_idxcrad_ok);
                        mLly_yanzhengma.setVisibility(View.VISIBLE);

                    }
                });
                break;

            case 1:
                if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
                    eid.NFCreadCard(intent);
                }
                break;

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == ParamConst.READ_CARD_INFO_CODE) {
            CardInfoGather cardInfoGather = (CardInfoGather) data.getSerializableExtra(ParamConst.CARD_INFO_GATHER);
            if (null != cardInfoGather) {
                String cardId = cardInfoGather.getPublicBasicInfo().getAppletNo();
                LUtils.i(TAG, "card Id is " + cardInfoGather.getPublicBasicInfo().getAppletNo());

                ToastUtils.showToast(this, "卡号：" + cardId);
            }
        }
    }

    @Override
    protected void onDestroy() {

        if (cameraHelper != null) {
            cameraHelper.release();
            cameraHelper = null;
        }

        unInitEngine();
        if (faceHelper != null) {
            ConfigUtil.setTrackedFaceCount(this, faceHelper.getTrackedFaceCount());
            faceHelper.release();
            faceHelper = null;
        }
        if (getFeatureDelayedDisposables != null) {
            getFeatureDelayedDisposables.clear();
        }
        if (delayFaceTaskCompositeDisposable != null) {
            delayFaceTaskCompositeDisposable.clear();
        }

        FaceServer.getInstance().unInit();
        super.onDestroy();
    }

    private void initCamera() {
        eeeererer.setText(R.string.cardactive_infinding);
        final FaceListener faceListener = new FaceListener() {
            @Override
            public void onFail(Exception e) {
                LUtils.i(TAG, "onFail: " + e.getMessage());
            }

            //请求FR的回调
            @Override
            public void onFaceFeatureInfoGet(@Nullable final FaceFeature faceFeature, final Integer requestId, final Integer errorCode) {

                LUtils.i(TAG, "onFaceFeatureInfoGet: ");
                //FR成功
                if (faceFeature != null) {

                    Integer liveness = livenessMap.get(requestId);
                    if (liveness != null && liveness == LivenessInfo.ALIVE) {
                        searchFace(faceFeature, requestId);
                        LUtils.d(TAG, "活体通过" + Arrays.toString(faceFeature.getFeatureData()));
                        LUtils.d(TAG, "requestId=" + requestId);

                    } else {  //活体检测未出结果，或者非活体，延迟执行该函数
                        LUtils.d(TAG, "活体不通过");
                        if (requestFeatureStatusMap.containsKey(requestId)) {
                            Observable.timer(WAIT_LIVENESS_INTERVAL, TimeUnit.MILLISECONDS)
                                    .subscribe(new Observer<Long>() {
                                        Disposable disposable;

                                        @Override
                                        public void onSubscribe(Disposable d) {
                                            disposable = d;
                                            getFeatureDelayedDisposables.add(disposable);
                                        }

                                        @Override
                                        public void onNext(Long aLong) {
                                            onFaceFeatureInfoGet(faceFeature, requestId, errorCode);
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onComplete() {
                                            getFeatureDelayedDisposables.remove(disposable);
                                        }
                                    });
                        }
                    }

                } else { //特征提取失败
                    LUtils.d(TAG, "特征提取失败");
                    LUtils.d(TAG, "extractErrorRetryMap==" + extractErrorRetryMap);
                    LUtils.d(TAG, "requestId==" + requestId);

                    if (increaseAndGetValue(extractErrorRetryMap, requestId) > MAX_RETRY_TIME) {
                        extractErrorRetryMap.put(requestId, 0);

                        String msg;
                        // 传入的FaceInfo在指定的图像上无法解析人脸，此处使用的是RGB人脸数据，一般是人脸模糊
                        if (errorCode != null && errorCode == ErrorInfo.MERR_FSDK_FACEFEATURE_LOW_CONFIDENCE_LEVEL) {
                            msg = "low_confidence_level";
                        } else {
                            msg = "ExtractCode:" + errorCode;
                        }
                        faceHelper.setName(requestId, msg);
                        // 在尝试最大次数后，特征提取仍然失败，则认为识别未通过
                        requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
                        retryRecognizeDelayed(requestId);
                    } else {
                        requestFeatureStatusMap.put(requestId, RequestFeatureStatus.TO_RETRY);
                    }
                }
            }

            @Override
            public void onFaceLivenessInfoGet(@Nullable LivenessInfo livenessInfo, final Integer requestId, Integer errorCode) {
                LUtils.i(TAG, "onFaceLivenessInfoGet: ");
                if (livenessInfo != null) {
                    int liveness = livenessInfo.getLiveness();
                    livenessMap.put(requestId, liveness);
                    // 非活体，重试
                    LUtils.d(TAG, "onFaceLivenessInfoGet 非活体，重试 ");
                    LUtils.d(TAG, "liveness== " + liveness);
                    LUtils.d(TAG, "livenessInfo== " + livenessInfo);
                    if (liveness == LivenessInfo.NOT_ALIVE) {
                        faceHelper.setName(requestId, "非活体");
                        // 延迟 FAIL_RETRY_INTERVAL 后，将该人脸状态置为UNKNOWN，帧回调处理时会重新进行活体检测
                        retryLivenessDetectDelayed(requestId);
                    }
                } else {
                    LUtils.d(TAG, "onFaceLivenessInfoGet 活体，重试 ");
                    if (increaseAndGetValue(livenessErrorRetryMap, requestId) > MAX_RETRY_TIME) {
                        livenessErrorRetryMap.put(requestId, 0);
                        String msg;
                        // 传入的FaceInfo在指定的图像上无法解析人脸，此处使用的是RGB人脸数据，一般是人脸模糊
                        if (errorCode != null && errorCode == ErrorInfo.MERR_FSDK_FACEFEATURE_LOW_CONFIDENCE_LEVEL) {
                            msg = "人脸不可靠";
                        } else {
                            msg = "ProcessCode:" + errorCode;
                        }
                        faceHelper.setName(requestId, msg);
                        retryLivenessDetectDelayed(requestId);
                    } else {
                        livenessMap.put(requestId, LivenessInfo.UNKNOWN);
                    }
                }
            }


        };


        CameraListener cameraListener = new CameraListener() {
            @Override
            public void onCameraOpened(Camera camera, int cameraId, int displayOrientation, boolean isMirror) {
                Camera.Size lastPreviewSize = previewSize;
                previewSize = camera.getParameters().getPreviewSize();
                drawHelper = new DrawHelper(previewSize.width, previewSize.height, previewView.getWidth(), previewView.getHeight(), displayOrientation
                        , cameraId, isMirror, false, false);
                LUtils.i(TAG, "onCameraOpened: " + drawHelper.toString());
                // 切换相机的时候可能会导致预览尺寸发生变化
                if (faceHelper == null ||
                        lastPreviewSize == null ||
                        lastPreviewSize.width != previewSize.width || lastPreviewSize.height != previewSize.height) {
                    Integer trackedFaceCount = null;
                    // 记录切换时的人脸序号
                    if (faceHelper != null) {
                        trackedFaceCount = faceHelper.getTrackedFaceCount();
                        faceHelper.release();
                    }
                    faceHelper = new FaceHelper.Builder()
                            .ftEngine(ftEngine)
                            .frEngine(frEngine)
                            .flEngine(flEngine)
                            .frQueueSize(MAX_DETECT_NUM)
                            .flQueueSize(MAX_DETECT_NUM)
                            .previewSize(previewSize)
                            .faceListener(faceListener)
                            .trackedFaceCount(trackedFaceCount == null ? ConfigUtil.getTrackedFaceCount(CardActiviting.this.getApplicationContext()) : trackedFaceCount)
                            .build();
                }
            }

            @Override
            public void onPreview(final byte[] nv21, Camera camera) {
                if (faceRectView != null) {
                    faceRectView.clearFaceInfo();
                }
                LUtils.d(TAG, "onPreview");
                List<FacePreviewInfo> facePreviewInfoList = faceHelper.onPreviewFrame2(nv21);
                if (facePreviewInfoList != null && faceRectView != null && drawHelper != null) {
                    drawPreviewInfo(facePreviewInfoList);
                    LUtils.d(TAG, "drawPreviewInfo(facePreviewInfoList)");
                }
                if (facePreviewInfoList != null && facePreviewInfoList.size() > 0 && previewSize != null) {
                    LUtils.d(TAG, "facePreviewInfoList != null   ");
                    for (int i = 0; i < facePreviewInfoList.size(); i++) {
                        LUtils.d(TAG, "facePreviewInfoList.size(); i++ ");
                        Integer status = requestFeatureStatusMap.get(facePreviewInfoList.get(i).getTrackId());
                        /**
                         * 在活体检测开启，在人脸识别状态不为成功或人脸活体状态不为处理中（ANALYZING）且不为处理完成（ALIVE、NOT_ALIVE）时重新进行活体检测
                         */
                        if (status == null || status != RequestFeatureStatus.SUCCEED) {
                            LUtils.d(TAG, "RequestFeatureStatus.SUCCEED");
                            Integer liveness = livenessMap.get(facePreviewInfoList.get(i).getTrackId());
                            if (liveness == null ||
                                    (liveness != LivenessInfo.ALIVE &&
                                            liveness != LivenessInfo.NOT_ALIVE &&
                                            liveness != RequestLivenessStatus.ANALYZING)) {
                                LUtils.d(TAG, "liveness != LivenessInfo.ALIVE");
                                livenessMap.put(facePreviewInfoList.get(i).getTrackId(), RequestLivenessStatus.ANALYZING);
                                faceHelper.requestFaceLiveness(nv21, facePreviewInfoList.get(i).getFaceInfo(), previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, facePreviewInfoList.get(i).getTrackId(), LivenessType.RGB);
                            }
                        }
                        /**
                         * 对于每个人脸，若状态为空或者为失败，则请求特征提取（可根据需要添加其他判断以限制特征提取次数），
                         * 特征提取回传的人脸特征结果在{@link FaceListener#onFaceFeatureInfoGet(FaceFeature, Integer, Integer)}中回传
                         */
                        if (status == null
                                || status == RequestFeatureStatus.TO_RETRY) {
                            LUtils.d(TAG, "status == RequestFeatureStatus.TO_RETRY)");
                            requestFeatureStatusMap.put(facePreviewInfoList.get(i).getTrackId(), RequestFeatureStatus.SEARCHING);
                            faceHelper.requestFaceFeature(nv21, facePreviewInfoList.get(i).getFaceInfo(), previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, facePreviewInfoList.get(i).getTrackId());
                            //                          LUtils.i(TAG, "onPreview: fr start = " + System.currentTimeMillis() + " trackId = " + facePreviewInfoList.get(i).getTrackedFaceCount());
                        }
                    }
                }
            }

            @Override
            public void onCameraClosed() {
                LUtils.i(TAG, "onCameraClosed: ");
            }

            @Override
            public void onCameraError(Exception e) {
                LUtils.i(TAG, "onCameraError: " + e.getMessage());
            }

            @Override
            public void onCameraConfigurationChanged(int cameraID, int displayOrientation) {
                if (drawHelper != null) {
                    drawHelper.setCameraDisplayOrientation(displayOrientation);
                }
                LUtils.i(TAG, "onCameraConfigurationChanged: " + cameraID + "  " + displayOrientation);
            }
        };

        cameraHelper = new CameraHelper.Builder()
                .previewViewSize(new Point(previewView.getMeasuredWidth(), previewView.getMeasuredHeight()))
                .rotation(getWindowManager().getDefaultDisplay().getRotation())
                .specificCameraId(rgbCameraID != null ? rgbCameraID : Camera.CameraInfo.CAMERA_FACING_FRONT)
                .isMirror(false)
                .previewOn(previewView)
                .cameraListener(cameraListener)
                .build();
        cameraHelper.init();
        cameraHelper.start();
    }

    private void queryIdCards(String reqid) {
        Call<bean_person> objectCall = HttpManager.getInstance().getHttpClient2().QueryIdCard("101", reqid);
        objectCall.enqueue(new HttpCallback<bean_person>() {
            @Override
            protected boolean processData(bean_person bo) {
                if (bo.getErrorCode() == 0) {
                    bean_person.ExtraBean extra = bo.getExtra();
                    bean_person.ExtraBean.ResBean status = extra.getRes();
                    String idType = status.getIdType();
                    if ("01".equals(idType)) {
                        //  main_tv_idcard.setText(idType + "身份证");
                    } else {
                        // main_tv_idcard.setText(idType + "其他");
                    }

                    //                    String idnum = status.getIdnum();
                    //                    mCardact_idnumn.setText(idnum);
                    mName = status.getName();

                    mCardact_pleaseon.setText(R.string.cardactive_idxcrad_ok);
                    mCardactive_nextstep.setVisibility(View.VISIBLE);

                    byte[] bytes = readIDcardUtils.hexStr2byte(extra.getPicture());
                    Bitmap bitmap = readIDcardUtils.readByteMap(bytes);


                    mBit2map = Bitmap2NV21.bitmapZoomByScale(bitmap, 2, 2);
                    //                    cardact_iamgv.setImageBitmap(mBit2map);

                    mNv21 = Bitmap2NV21.bitmapToNv21(mBit2map, mBit2map.getWidth(), mBit2map.getHeight());
                    LUtils.d(TAG, "extra.nv21===" + mNv21);
                    LUtils.d(TAG, "extra.nv21===" + mNv21.length);

                    if (faceHelper != null) {
                        //requestI
                        registerFace(mNv21, faceHelper.onPreviewFrame(mNv21, mBit2map.getWidth(), mBit2map.getHeight()));

                    } else {
                        LUtils.d(TAG, "faceHelper == null");
                    }
                } else {
                    LUtils.d(TAG, "bo.getErrorCode()===" + bo.getErrorCode());
                }
                return true;
            }
        });

    }

    private void searchFace(final FaceFeature frFace, final Integer requestId) {
        Observable
                .create(new ObservableOnSubscribe<CompareResult>() {
                    @Override
                    public void subscribe(ObservableEmitter<CompareResult> emitter) {
                        LUtils.d(TAG, "subscribe = ObservableEmitter  ==");
                        //                      LUtils.i(TAG, "subscribe: fr search start = " + System.currentTimeMillis() + " trackId = " + requestId);
                        CompareResult compareResult = FaceServer.getInstance().getTopOfFaceLib(frFace);
                        //                      LUtils.i(TAG, "subscribe: fr search end = " + System.currentTimeMillis() + " trackId = " + requestId);
                        emitter.onNext(compareResult);

                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CompareResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LUtils.d(TAG, "onSubscribe ===");
                    }

                    @Override
                    public void onNext(CompareResult compareResult) {
                        LUtils.d(TAG, "onNext ===");
                        if (compareResult == null || compareResult.getUserName() == null) {
                            requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
                            faceHelper.setName(requestId, "VISITOR 1" + requestId);
                            return;
                        }

                        if (compareResult.getSimilar() > SIMILAR_THRESHOLD) {
                            boolean isAdded = false;
                            if (compareResultList == null) {
                                requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
                                faceHelper.setName(requestId, "VISITOR 2 " + requestId);
                                return;
                            }
                            for (CompareResult compareResult1 : compareResultList) {
                                if (compareResult1.getTrackId() == requestId) {
                                    isAdded = true;
                                    break;
                                }
                            }
                            if (!isAdded) {
                                //对于多人脸搜索，假如最大显示数量为 MAX_DETECT_NUM 且有新的人脸进入，则以队列的形式移除
                                if (compareResultList.size() >= MAX_DETECT_NUM) {
                                    compareResultList.remove(0);
                                    //                                    adapter.notifyItemRemoved(0);
                                }
                                //添加显示人员时，保存其trackId
                                compareResult.setTrackId(requestId);
                                compareResultList.add(compareResult);
                                //                                adapter.notifyItemInserted(compareResultList.size() - 1);
                            }
                            requestFeatureStatusMap.put(requestId, RequestFeatureStatus.SUCCEED);
                            //                            faceHelper.setName(requestId, "活体检测通过 " + compareResult.getUserName());
                            faceHelper.setName(requestId, "活体检测通过 ");

                            stopeid2trancard(requestId);

                        } else {
                            LUtils.d(TAG, "识别阈值===" + compareResult.getSimilar());
                            faceHelper.setName(requestId, "阈值低 " + compareResult.getSimilar());
                            retryRecognizeDelayed(requestId);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        faceHelper.setName(requestId, "onError ");
                        LUtils.d(TAG, "onError ===" + e);
                        retryRecognizeDelayed(requestId);
                    }

                    @Override
                    public void onComplete() {
                        LUtils.d(TAG, "onComplete ===");
                    }
                });
    }

    private void stopeid2trancard(Integer requestId) {

        mFaceactive_nextstep.setVisibility(View.VISIBLE);
        eeeererer.setText(R.string.cardactive_faceover);
        readCode = 0;
        eid.disableReaderMode();
        eid = null;

        clearLeftFace(faceHelper.onPreviewFrame(mNv21, mBit2map.getWidth(), mBit2map.getHeight()));
        unInitEngine();
        drawHelper = null;
        if (cameraHelper != null) {
            cameraHelper.stop();

        }

    }

    /**
     * 将map中key对应的value增1回传
     *
     * @param countMap map
     * @param key      key
     * @return 增1后的value
     */
    public int increaseAndGetValue(Map<Integer, Integer> countMap, int key) {
        if (countMap == null) {
            return 0;
        }
        Integer value = countMap.get(key);
        if (value == null) {
            value = 0;
        }
        countMap.put(key, ++value);
        return value;
    }

    /**
     * 延迟 FAIL_RETRY_INTERVAL 重新进行人脸识别
     *
     * @param requestId 人脸ID
     */
    private void retryRecognizeDelayed(final Integer requestId) {
        requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
        Observable.timer(FAIL_RETRY_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Long>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                        delayFaceTaskCompositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(Long aLong) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        // 将该人脸特征提取状态置为FAILED，帧回调处理时会重新进行活体检测
                        //                        faceHelper.setName(requestId, Integer.toString(requestId));
                        //                        faceHelper.setName(requestId,"");
                        LUtils.d(TAG, "Integer.toString(requestId)  - ====" + Integer.toString(requestId));
                        requestFeatureStatusMap.put(requestId, RequestFeatureStatus.TO_RETRY);
                        delayFaceTaskCompositeDisposable.remove(disposable);
                    }
                });
    }

    /**
     * 延迟 FAIL_RETRY_INTERVAL 重新进行活体检测
     *
     * @param requestId 人脸ID
     */
    private void retryLivenessDetectDelayed(final Integer requestId) {
        Observable.timer(FAIL_RETRY_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Long>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                        delayFaceTaskCompositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(Long aLong) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        // 将该人脸状态置为UNKNOWN，帧回调处理时会重新进行活体检测
                        //                        faceHelper.setName(requestId, Integer.toString(requestId));
                        livenessMap.put(requestId, LivenessInfo.UNKNOWN);
                        delayFaceTaskCompositeDisposable.remove(disposable);
                    }
                });
    }

    private void drawPreviewInfo(List<FacePreviewInfo> facePreviewInfoList) {
        List<DrawInfo> drawInfoList = new ArrayList<>();
        for (int i = 0; i < facePreviewInfoList.size(); i++) {
            String name = faceHelper.getName(facePreviewInfoList.get(i).getTrackId());
            LUtils.d(TAG, "drawPreviewInfo  -name ====" + name);
            Integer liveness = livenessMap.get(facePreviewInfoList.get(i).getTrackId());
            Integer recognizeStatus = requestFeatureStatusMap.get(facePreviewInfoList.get(i).getTrackId());
            LUtils.d(TAG, "void  drawPreviewInfo(facePreviewInfoList)");
            // 根据识别结果和活体结果设置颜色
            int color = RecognizeColor.COLOR_UNKNOWN;
            if (recognizeStatus != null) {
                if (recognizeStatus == RequestFeatureStatus.FAILED) {
                    color = RecognizeColor.COLOR_FAILED;
                }
                if (recognizeStatus == RequestFeatureStatus.SUCCEED) {
                    color = RecognizeColor.COLOR_SUCCESS;
                }
            }
            if (liveness != null && liveness == LivenessInfo.NOT_ALIVE) {
                color = RecognizeColor.COLOR_FAILED;
            }

            drawInfoList.add(new DrawInfo(drawHelper.adjustRect(facePreviewInfoList.get(i).getFaceInfo().getRect()),
                    GenderInfo.UNKNOWN, AgeInfo.UNKNOWN_AGE, liveness == null ? LivenessInfo.UNKNOWN : liveness, color,
                    name == null ? "" : name));

            LUtils.d(TAG, "facePreviewInfoList==" + facePreviewInfoList.get(i).getTrackId());
        }
        drawHelper.draw(faceRectView, drawInfoList);
    }

    private void registerFace(final byte[] nv21, final List<FacePreviewInfo> facePreviewInfoList) {
        //        if (registerStatus == REGISTER_STATUS_READY && facePreviewInfoList != null && facePreviewInfoList.size() > 0) {
        //            registerStatus = REGISTER_STATUS_PROCESSING;
        LUtils.d(TAG, "nv21==" + Arrays.toString(nv21));
        LUtils.d(TAG, "facePreviewInfoList==" + facePreviewInfoList.get(0).getFaceInfo().toString());

        if (facePreviewInfoList != null && facePreviewInfoList.size() > 0) {
            //                registerStatus = REGISTER_STATUS_PROCESSING;
            Observable.create(new ObservableOnSubscribe<Boolean>() {
                @Override
                public void subscribe(ObservableEmitter<Boolean> emitter) {

                    boolean success = FaceServer.getInstance().registerNv21(CardActiviting.this, nv21.clone(),
                            mBit2map.getWidth(), mBit2map.getHeight(),
                            facePreviewInfoList.get(0).getFaceInfo(), "registered " + faceHelper.getTrackedFaceCount());
                    emitter.onNext(success);

                    LUtils.d(TAG, " registered success --  getTrackedFaceCount()=== " + faceHelper.getTrackedFaceCount());

                }
            })
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Boolean success) {
                            String result = success ? "register success!" : "register failed!";
                            LUtils.d(TAG, " onNext ==result  " + result);
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            LUtils.d(TAG, " onNext ==result  " + e);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    /**
     * 删除已经离开的人脸
     *
     * @param facePreviewInfoList 人脸和trackId列表
     */
    private void clearLeftFace(List<FacePreviewInfo> facePreviewInfoList) {
        if (compareResultList != null) {
            for (int i = compareResultList.size() - 1; i >= 0; i--) {
                if (!requestFeatureStatusMap.containsKey(compareResultList.get(i).getTrackId())) {
                    compareResultList.remove(i);
                    //                    adapter.notifyItemRemoved(i);
                }
            }
        }
        if (facePreviewInfoList == null || facePreviewInfoList.size() == 0) {
            requestFeatureStatusMap.clear();
            livenessMap.clear();
            livenessErrorRetryMap.clear();
            extractErrorRetryMap.clear();
            if (getFeatureDelayedDisposables != null) {
                getFeatureDelayedDisposables.clear();
            }
            return;
        }
        Enumeration<Integer> keys = requestFeatureStatusMap.keys();
        while (keys.hasMoreElements()) {
            int key = keys.nextElement();
            boolean contained = false;
            for (FacePreviewInfo facePreviewInfo : facePreviewInfoList) {
                if (facePreviewInfo.getTrackId() == key) {
                    contained = true;
                    break;
                }
            }
            if (!contained) {
                requestFeatureStatusMap.remove(key);
                livenessMap.remove(key);
                livenessErrorRetryMap.remove(key);
                extractErrorRetryMap.remove(key);
            }
        }


    }

    @Override
    public void onGlobalLayout() {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        code4num = null;
        if (s.length() == 4) {
            mPanda_nextstep.setVisibility(View.VISIBLE);
            KeyboardUtils.hideKeyboard(this);
            LUtils.d(TAG, "s.length()===" + s.length());
            code4num = s.toString();
        }
    }
}
