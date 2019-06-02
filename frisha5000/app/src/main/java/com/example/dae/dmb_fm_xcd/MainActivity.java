package com.example.dae.dmb_fm_xcd;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static android.view.View.GONE;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/*
 *  MainActivity : tab Layout 생성 및 Action Bar option 생성
 */




public class MainActivity extends Activity {

    //
    STRUCT_LIST.ENSEMBLE_DESC ENS_DESC, NEXT_ENS_DESC;

    // bool dmb channel list
    boolean gFicDumpEn = FALSE;
    int firCrcTotCnt = 0;
    /// usb command list
    final static int DMB_TS_START   = 00;
    final static int DMB_TS_STOP    = 10;
    final static int FM_TS_START    = 02;
    final static int FM_TS_STOP     = 20;
    ////


    final static int m_iArrayCnt = 132;


    int flag_b_box = -1;

    SMonErrorDab errStatus_dab;

    boolean[] rxTsiFlag =  {false, false, false, false};

    int FmSchRes = 0;
    int gFmScanDone = 0;


    int[] iPtrNilai = new int[133];
    Boolean[] rxFicFlag =  {false, false, false};
    byte[] tsiData = new byte[188];
    byte[] tsiDoubleData = new byte[376];
    byte[] ficData = new byte[384];

    int temp0;
    int temp1;
    int temp2;
    int temp3;

    int i_val;
    int q_val;

    int fm_bt_set_seq = 0;

    int gDmbSnr;
    int gFmsnr;

    float fTmp;
    int iTmp,fTmp_int;

    long Spower = 0;
    int tSpecCnt = 0;
    float SpwrLog;
    int ii = 0;

    int snr_sum = 30;

    int b_box_loc = 0;

    int strength,quality;
    /*
     * dmb 채널 리스트
     */
    int dmb_channel_list[] = {
            175280,177008,178736,
            181280,183008,184736,
            187280,189008,190736,
            193280,195008,196736,
            199280,201008,202736,
            205280,207008,208736,
            211280,213008,214736};
    /*
     * fm 채널 리스트
     */
    int fm_channel_list[] = {
            88100, 88300, 88500, 88700,  88900,  89100, 89300, 89500, 89700,  89900,  90100,  90300,  90500,  90700, 90900, 91100,  91300,  91500,  91700,  91900,  92100,  92300, 92500, 92700,  92900,  93100,  93300,  93500,  93700,  93900, 94100, 94300,  94500,  94700,  94900,  95100,  95300,  95500, 95700, 95900,  96100,  96300,  96500,  96700,  96900,  97100, 97300, 97500,  97700,  97900,  98100,  98300,  98500,  98700, 98900, 99100,  99300,  99500,  99700,  99900,  100100,  100300, 100500, 100700,  100900,  101100, 101300, 101500, 101700, 101900, 102100, 102300,  102500,  102700, 102900, 103100, 103300, 103500, 103700, 103900,  104100,  104300, 104500, 104700, 104900, 105100, 105300, 105500,  105700,  105900, 106100, 106300, 106500, 106700, 106900, 107100,  107300,  107500, 107700, 107900};
    /*
     * fm search 결과 저장 리스트
     */
    int[] chk_channel_list = new int[60];
    /*
     * fm search 결과 리스트를 이용한 TUNE 가능 리스트
     */
    int[] chk_blue_channel_list = new int[60];
    /*
     * Constallation var
     */

    /*
     *  Error status
     */
    static PointsGraphSeries<DataPoint> series;
    int dmb_frequency = 0;
    int fm_frequency = 0;
    int dmb_dotCount = 0;
    int fm_dotCount = 0;
    int dmb_barCount = 0;
    int fm_barCount = 0;
    /*
     * TextView
     */
    //dmb
    TextView txt_dmb_strength;
    TextView txt_dmb_quality;
    //fm
    TextView txt_fm_strength;
    TextView txt_fm_quality;
    /*
     * ImageView
     */
    //dmb
    ImageView imageView_dmb_connect;
    ImageView imageView_dmb_signal;
    //fm
    ImageView imageView_fm_connect;
    ImageView imageView_fm_signal;
    /*
     * Spinner
     */
    Spinner spinner_dmb_list;
    Spinner spinner_fm_list;
    /*
     *  GraphView
     */
    //DMB
    GraphView graphView_dmb_spec;
    GraphView graphView_dmb_con;
    //FM
    GraphView graphView_fm_spec;
    GraphView graphView_fm_con;

    /*
     *  Progress Bar
     */
    ProgressBar progressBar_dmb;
    ProgressBar progressBar_fm;

    /*
     *  define
     */
    public static final boolean LOCK = TRUE;
    public static final boolean UNLOCK = FALSE;
    /*
     * on init dialog
     */
    public static int mDrmFirstInit = 1;
    public static int gDrmInitFlag = 0;
    public static int mDabMonitorType = 0;
    /*
     *   usb 관련 변수
     */
    public static UsbService usbService;
    private TextView display;
    private EditText editText;
    private MyHandler mHandler;
    /*
     *   Button
     */
    Button bt_dmb;
    Button bt_fm;
    //DMB
    Button bt_dmb_ch;
    Button bt_dmb_search;
    Button bt_dmb_start;
    Button bt_dmb_col;
    Button bt_dmb_spec;
    Button bt_dmb_search_start;
    Button bt_dmb_search_stop;
    //FM
    Button bt_fm_ch;
    Button bt_fm_search;
    Button bt_fm_start;
    Button bt_fm_col;
    Button bt_fm_spec;
    Button bt_fm_search_start;
    Button bt_fm_search_stop;
    /*
     * DMB channel list
     */

    Button bt_dmb_ch_list[] = new Button[21];
    boolean start_receive_dmb_signal = false;

    boolean bool_start_flag = false;
    boolean bool_dmb_ch_list[] = new boolean[21];
    int ch_seq = 0;
    /*
     * FM channel list
     */
    Button bt_fm_ch_list[] = new Button[60];
    boolean start_receive_fm_signal = false;
    boolean bool_fm_start_flag = false;
    boolean bool_fm_ch_list[] = new boolean[60];
    int ch_fm_seq = 0;

    /*
     *   DMB - Layout
     */
    LinearLayout layout_dmb;
    LinearLayout layout_device;
    LinearLayout layout_signal;
    LinearLayout layout_display;
    LinearLayout layout_service;
    LinearLayout layout_search;
    /*
     *    FM - Layout
     */
    LinearLayout layout_fm;
    LinearLayout layout_fm_device;
    LinearLayout layout_fm_signal;
    LinearLayout layout_fm_display;
    LinearLayout layout_fm_search;
    /*
     *  flag
     */
    boolean bool_dmb_fm = false;           //dmb , fm 클릭 여부 false : dmb  ture : fm

    boolean bool_dmb_col_spec = false;    //  성좌도 : false 스펙트럼  : true
    boolean bool_dmb_ch_search = false;   // 채널 : false 검색 : true

    boolean bool_fm_col_spec = false;    //  성좌도 : false 스펙트럼  : true
    boolean bool_fm_ch_search = false;   // 채널 : false 검색 : true

    boolean bool_dmb_search_start = false; // 멈춤 : false 시작 : true
    boolean bool_fm_search_start = false;
    // send data
    String data;

    /*
     * Textview
     */
    TextView textview;

    TextView txt_service_list;

    /*
     *  check handler
     */
    Handler hander = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Initialize Button
        // Channel page
        bt_dmb = (Button)findViewById(R.id.bt_dmb);
        bt_fm = (Button)findViewById(R.id.bt_fm);
        ////////////////////////////////
        //          DMB BUTTON       //
        ///////////////////////////////
        bt_dmb_ch = (Button)findViewById(R.id.bt_dmb_ch);
        bt_dmb_search = (Button)findViewById(R.id.bt_dmb_search);
        bt_dmb_start = (Button)findViewById(R.id.bt_dmb_start);
        bt_dmb_col = (Button)findViewById(R.id.bt_dmb_col);
        bt_dmb_spec = (Button)findViewById(R.id.bt_dmb_spec);
        //Channel Search page
        bt_dmb_search_start = (Button)findViewById(R.id.bt_dmb_channel_search);
        bt_dmb_search_stop = (Button)findViewById(R.id.bt_dmb_channel_search_stop);
        ////////////////////////////////
        //          FM BUTTON       //
        ///////////////////////////////
        bt_fm_ch = (Button)findViewById(R.id.bt_fm_ch);
        bt_fm_search = (Button)findViewById(R.id.bt_fm_search);
        bt_fm_start = (Button)findViewById(R.id.bt_fm_start);
        bt_fm_col = (Button)findViewById(R.id.bt_fm_col);
        bt_fm_spec = (Button)findViewById(R.id.bt_fm_spec);
        //Channel Search page
        bt_fm_search_start = (Button)findViewById(R.id.bt_fm_channel_search);
        bt_fm_search_stop = (Button)findViewById(R.id.bt_fm_channel_search_stop);
        bt_fm_search_start.setClickable(false);
        bt_fm_search_stop.setClickable(false);
        //initialize dmb ch button
        bt_dmb_ch_list[0] = (Button)findViewById(R.id.bt_dmb_ch1);
        bt_dmb_ch_list[1] = (Button)findViewById(R.id.bt_dmb_ch2);
        bt_dmb_ch_list[2] = (Button)findViewById(R.id.bt_dmb_ch3);
        bt_dmb_ch_list[3] = (Button)findViewById(R.id.bt_dmb_ch4);
        bt_dmb_ch_list[4] = (Button)findViewById(R.id.bt_dmb_ch5);
        bt_dmb_ch_list[5] = (Button)findViewById(R.id.bt_dmb_ch6);
        bt_dmb_ch_list[6] = (Button)findViewById(R.id.bt_dmb_ch7);
        bt_dmb_ch_list[7] = (Button)findViewById(R.id.bt_dmb_ch8);
        bt_dmb_ch_list[8] = (Button)findViewById(R.id.bt_dmb_ch9);
        bt_dmb_ch_list[9] = (Button)findViewById(R.id.bt_dmb_ch10);
        bt_dmb_ch_list[10] = (Button)findViewById(R.id.bt_dmb_ch11);
        bt_dmb_ch_list[11] = (Button)findViewById(R.id.bt_dmb_ch12);
        bt_dmb_ch_list[12] = (Button)findViewById(R.id.bt_dmb_ch13);
        bt_dmb_ch_list[13] = (Button)findViewById(R.id.bt_dmb_ch14);
        bt_dmb_ch_list[14] = (Button)findViewById(R.id.bt_dmb_ch15);
        bt_dmb_ch_list[15] = (Button)findViewById(R.id.bt_dmb_ch16);
        bt_dmb_ch_list[16] = (Button)findViewById(R.id.bt_dmb_ch17);
        bt_dmb_ch_list[17] = (Button)findViewById(R.id.bt_dmb_ch18);
        bt_dmb_ch_list[18] = (Button)findViewById(R.id.bt_dmb_ch19);
        bt_dmb_ch_list[19] = (Button)findViewById(R.id.bt_dmb_ch20);
        bt_dmb_ch_list[20] = (Button)findViewById(R.id.bt_dmb_ch21);


        //initialize fm ch button
        bt_fm_ch_list[0] = (Button)findViewById(R.id.bt_fm_ch1);
        bt_fm_ch_list[1] = (Button)findViewById(R.id.bt_fm_ch2);
        bt_fm_ch_list[2] = (Button)findViewById(R.id.bt_fm_ch3);
        bt_fm_ch_list[3] = (Button)findViewById(R.id.bt_fm_ch4);
        bt_fm_ch_list[4] = (Button)findViewById(R.id.bt_fm_ch5);
        bt_fm_ch_list[5] = (Button)findViewById(R.id.bt_fm_ch6);
        bt_fm_ch_list[6] = (Button)findViewById(R.id.bt_fm_ch7);
        bt_fm_ch_list[7] = (Button)findViewById(R.id.bt_fm_ch8);
        bt_fm_ch_list[8] = (Button)findViewById(R.id.bt_fm_ch9);
        bt_fm_ch_list[9] = (Button)findViewById(R.id.bt_fm_ch10);
        bt_fm_ch_list[10] = (Button)findViewById(R.id.bt_fm_ch11);
        bt_fm_ch_list[11] = (Button)findViewById(R.id.bt_fm_ch12);
        bt_fm_ch_list[12] = (Button)findViewById(R.id.bt_fm_ch13);
        bt_fm_ch_list[13] = (Button)findViewById(R.id.bt_fm_ch14);
        bt_fm_ch_list[14] = (Button)findViewById(R.id.bt_fm_ch15);
        bt_fm_ch_list[15] = (Button)findViewById(R.id.bt_fm_ch16);
        bt_fm_ch_list[16] = (Button)findViewById(R.id.bt_fm_ch17);
        bt_fm_ch_list[17] = (Button)findViewById(R.id.bt_fm_ch18);
        bt_fm_ch_list[18] = (Button)findViewById(R.id.bt_fm_ch19);
        bt_fm_ch_list[19] = (Button)findViewById(R.id.bt_fm_ch20);
        bt_fm_ch_list[20] = (Button)findViewById(R.id.bt_fm_ch21);
        bt_fm_ch_list[21] = (Button)findViewById(R.id.bt_fm_ch22);
        bt_fm_ch_list[22] = (Button)findViewById(R.id.bt_fm_ch23);
        bt_fm_ch_list[23] = (Button)findViewById(R.id.bt_fm_ch24);
        bt_fm_ch_list[24] = (Button)findViewById(R.id.bt_fm_ch25);
        bt_fm_ch_list[25] = (Button)findViewById(R.id.bt_fm_ch26);
        bt_fm_ch_list[26] = (Button)findViewById(R.id.bt_fm_ch27);
        bt_fm_ch_list[27] = (Button)findViewById(R.id.bt_fm_ch28);
        bt_fm_ch_list[28] = (Button)findViewById(R.id.bt_fm_ch29);
        bt_fm_ch_list[29] = (Button)findViewById(R.id.bt_fm_ch30);
        bt_fm_ch_list[30] = (Button)findViewById(R.id.bt_fm_ch31);
        bt_fm_ch_list[31] = (Button)findViewById(R.id.bt_fm_ch32);
        bt_fm_ch_list[32] = (Button)findViewById(R.id.bt_fm_ch33);
        bt_fm_ch_list[33] = (Button)findViewById(R.id.bt_fm_ch34);
        bt_fm_ch_list[34] = (Button)findViewById(R.id.bt_fm_ch35);
        bt_fm_ch_list[35] = (Button)findViewById(R.id.bt_fm_ch36);
        bt_fm_ch_list[36] = (Button)findViewById(R.id.bt_fm_ch37);
        bt_fm_ch_list[37] = (Button)findViewById(R.id.bt_fm_ch38);
        bt_fm_ch_list[38] = (Button)findViewById(R.id.bt_fm_ch39);
        bt_fm_ch_list[39] = (Button)findViewById(R.id.bt_fm_ch40);
        bt_fm_ch_list[40] = (Button)findViewById(R.id.bt_fm_ch41);
        bt_fm_ch_list[41] = (Button)findViewById(R.id.bt_fm_ch42);
        bt_fm_ch_list[42] = (Button)findViewById(R.id.bt_fm_ch43);
        bt_fm_ch_list[43] = (Button)findViewById(R.id.bt_fm_ch44);
        bt_fm_ch_list[44] = (Button)findViewById(R.id.bt_fm_ch45);
        bt_fm_ch_list[45] = (Button)findViewById(R.id.bt_fm_ch46);
        bt_fm_ch_list[46] = (Button)findViewById(R.id.bt_fm_ch47);
        bt_fm_ch_list[47] = (Button)findViewById(R.id.bt_fm_ch48);
        bt_fm_ch_list[48] = (Button)findViewById(R.id.bt_fm_ch49);
        bt_fm_ch_list[49] = (Button)findViewById(R.id.bt_fm_ch50);
        bt_fm_ch_list[50] = (Button)findViewById(R.id.bt_fm_ch51);
        bt_fm_ch_list[51] = (Button)findViewById(R.id.bt_fm_ch52);
        bt_fm_ch_list[52] = (Button)findViewById(R.id.bt_fm_ch53);
        bt_fm_ch_list[53] = (Button)findViewById(R.id.bt_fm_ch54);
        bt_fm_ch_list[54] = (Button)findViewById(R.id.bt_fm_ch55);
        bt_fm_ch_list[55] = (Button)findViewById(R.id.bt_fm_ch56);
        bt_fm_ch_list[56] = (Button)findViewById(R.id.bt_fm_ch57);
        bt_fm_ch_list[57] = (Button)findViewById(R.id.bt_fm_ch58);
        bt_fm_ch_list[58] = (Button)findViewById(R.id.bt_fm_ch59);
        bt_fm_ch_list[59] = (Button)findViewById(R.id.bt_fm_ch60);

        bt_dmb.setOnClickListener(onClickListener);
        bt_fm.setOnClickListener(onClickListener);
        //dmb
        bt_dmb_ch.setOnClickListener(onClickListener);
        bt_dmb_search.setOnClickListener(onClickListener);
        bt_dmb_start.setOnClickListener(onClickListener);
        bt_dmb_col.setOnClickListener(onClickListener);
        bt_dmb_spec.setOnClickListener(onClickListener);
        bt_dmb_search_start.setOnClickListener(onClickListener);
        bt_dmb_search_stop.setOnClickListener(onClickListener);
        //fm
        bt_fm_ch.setOnClickListener(onClickListener);
        bt_fm_search.setOnClickListener(onClickListener);
        bt_fm_start.setOnClickListener(onClickListener);
        bt_fm_col.setOnClickListener(onClickListener);
        bt_fm_spec.setOnClickListener(onClickListener);
        bt_fm_search_start.setOnClickListener(onClickListener);
        bt_fm_search_stop.setOnClickListener(onClickListener);
        for( Button bt : bt_dmb_ch_list){ bt.setOnClickListener(onClickListener); }
        for( Button bt : bt_fm_ch_list) { bt.setOnClickListener(onClickListener); }
        //initialize bool
        for( boolean bool : bool_dmb_ch_list){ bool = false;}
        for( boolean bool : bool_fm_ch_list){ bool = false;}
        //initialize progress bar
        progressBar_dmb = (ProgressBar)findViewById(R.id.dmb_progressBar);
        progressBar_fm = (ProgressBar)findViewById(R.id.fm_progressBar);

        //Initialize layout -- DMB
        layout_dmb = (LinearLayout)findViewById(R.id.layout_dmb);
        layout_device = (LinearLayout)findViewById(R.id.layout_device);
        layout_display = (LinearLayout)findViewById(R.id.layout_display);
        layout_signal = (LinearLayout)findViewById(R.id.layout_signal);
        layout_service = (LinearLayout)findViewById(R.id.layout_service);
        layout_search=(LinearLayout)findViewById(R.id.layout_search);

        //Initialize layout -- FM
        layout_fm = (LinearLayout)findViewById(R.id.layout_fm);
        layout_fm_device = (LinearLayout)findViewById(R.id.layout_fm_device);
        layout_fm_display = (LinearLayout)findViewById(R.id.layout_fm_display);
        layout_fm_signal = (LinearLayout)findViewById(R.id.layout_fm_signal);

        layout_fm_search=(LinearLayout)findViewById(R.id.layout_fm_search);

        //Initialize graphview
        //dmb
        graphView_dmb_spec = (GraphView) findViewById(R.id.dmb_spec_graph);
        graphView_dmb_con = (GraphView) findViewById(R.id.dmb_con_graph);
        //fm
        graphView_fm_spec = (GraphView) findViewById(R.id.fm_spec_graph);
        graphView_fm_con = (GraphView) findViewById(R.id.fm_con_graph);
        //Initialize imageview
        //dmb
        imageView_dmb_connect = (ImageView)findViewById(R.id.imageView_dmb_connect);
        imageView_dmb_signal = (ImageView)findViewById(R.id.imageView_dmb_signal);
        //fm
        imageView_fm_connect = (ImageView)findViewById(R.id.imageView_fm_connect);
        imageView_fm_signal = (ImageView)findViewById(R.id.imageView_fm_signal);

        //Initialize Textview
        //dmb
        txt_dmb_strength = (TextView)findViewById(R.id.txt_dmb_strength);
        txt_dmb_quality = (TextView)findViewById(R.id.txt_dmb_quality);
        //fm
        txt_fm_strength = (TextView)findViewById(R.id.txt_fm_strength);
        txt_fm_quality = (TextView)findViewById(R.id.txt_fm_quality);
        //Initialize spinner
        //dmb
        spinner_dmb_list = (Spinner)findViewById(R.id.spin_dmb_ch);
        String[] str = getResources().getStringArray(R.array.spinner_list_channel);
        ArrayAdapter<String> adapter= new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, str);
        adapter.setDropDownViewResource(R.layout.spinner_check_list);
        //dmb 주파수 리스트  spinner list
        spinner_dmb_list.setAdapter(adapter);
        //주파수 선택시
        /*
         *  dmb_frequency 선택된 주파수 저장
         *  현재까지 저장되었던 constallation 값 초기화
         */
        spinner_dmb_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dmb_frequency = (int) (Float.parseFloat(spinner_dmb_list.getItemAtPosition(position).toString().substring(5))*1000);
                Log.d("btton","btn");
                bt_dmb_start.setText("시작");
                start_receive_dmb_signal = false;
                dmb_dotCount = 0;
                graphView_dmb_con.destroyDrawingCache();
                graphView_dmb_con.removeAllSeries();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //fm
        spinner_fm_list = (Spinner)findViewById(R.id.spin_fm_ch);
        str = getResources().getStringArray(R.array.spinner_list_fm);
        ArrayAdapter<String> fm_adapter= new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, str);
        fm_adapter.setDropDownViewResource(R.layout.spinner_check_list);
        spinner_fm_list.setAdapter(fm_adapter);
        /*
         *  fm_frequency 선택된 주파수 저장
         *  현재까지 저장되었던 constallation 값 초기화
         */
        spinner_fm_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fm_frequency = (int) (Float.parseFloat(spinner_fm_list.getItemAtPosition(position).toString())*1000);
                start_receive_fm_signal = false;
                Log.d("btton","btn");
                bt_fm_start.setText("시작");
                fm_dotCount = 0;
                graphView_fm_con.destroyDrawingCache();
                graphView_fm_con.removeAllSeries();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txt_service_list = (TextView)findViewById(R.id.txt_service_list);

        /*
         * Graph View 스타일 정의
         */
        graphView_dmb_con.getGridLabelRenderer().setHighlightZeroLines(true);
        //그리드 갯수
        graphView_dmb_con.getGridLabelRenderer().setNumHorizontalLabels(8);
        graphView_dmb_con.getGridLabelRenderer().setNumVerticalLabels(8);
        graphView_dmb_con.getGridLabelRenderer().setTextSize(20);
        //외곽선 생성
        graphView_dmb_con.getViewport().setDrawBorder(true);
        //최대 최소 값
        graphView_dmb_con.getViewport().setXAxisBoundsManual(true);
        graphView_dmb_con.getViewport().setYAxisBoundsManual(true);
        graphView_dmb_con.getViewport().setMaxX(250);
        graphView_dmb_con.getViewport().setMaxY(250);
        graphView_dmb_con.getViewport().setMinX(-250);
        graphView_dmb_con.getViewport().setMinY(-250);

        //최대 최소 값
        graphView_dmb_spec.getViewport().setXAxisBoundsManual(true);
        graphView_dmb_spec.getViewport().setYAxisBoundsManual(true);
        graphView_dmb_spec.getViewport().setMaxX(2);
        graphView_dmb_spec.getViewport().setMaxY(70);
        graphView_dmb_spec.getViewport().setMinX(0);
        graphView_dmb_spec.getViewport().setMinY(20);
        graphView_dmb_spec.getGridLabelRenderer().setNumHorizontalLabels(10);
        graphView_dmb_spec.getGridLabelRenderer().setNumVerticalLabels(10);


        graphView_fm_con.getGridLabelRenderer().setHighlightZeroLines(true);
        //그리드 갯수
        graphView_fm_con.getGridLabelRenderer().setNumHorizontalLabels(8);
        graphView_fm_con.getGridLabelRenderer().setNumVerticalLabels(8);
        graphView_fm_con.getGridLabelRenderer().setTextSize(20);
        //외곽선 생성
        graphView_fm_con.getViewport().setDrawBorder(true);
        //최대 최소 값
        graphView_fm_con.getViewport().setXAxisBoundsManual(true);
        graphView_fm_con.getViewport().setYAxisBoundsManual(true);
        graphView_fm_con.getViewport().setMaxX(250);
        graphView_fm_con.getViewport().setMaxY(250);
        graphView_fm_con.getViewport().setMinX(-250);
        graphView_fm_con.getViewport().setMinY(-250);

        //최대 최소 값
        graphView_fm_spec.getViewport().setXAxisBoundsManual(true);
        graphView_fm_spec.getViewport().setYAxisBoundsManual(true);

        graphView_fm_spec.getViewport().setMaxX(2);
        graphView_fm_spec.getViewport().setMaxY(80);
        graphView_fm_spec.getViewport().setMinX(0);
        graphView_fm_spec.getViewport().setMinY(10);
        graphView_fm_spec.getGridLabelRenderer().setNumHorizontalLabels(10);
        graphView_fm_spec.getGridLabelRenderer().setNumVerticalLabels(8);
        // Creating TabPagerAdapter adapter
        mHandler = new MyHandler(this);

        //Initialize pointer

        errStatus_dab = new SMonErrorDab();
        //error_status
        errStatus_dab.FecLock = 0;

        progressBar_dmb.setMax(21);
        progressBar_fm.setMax(200);


        bt_dmb_col.callOnClick();


    }
   // check handelr event


    Button.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch ( v.getId()){
                case R.id.bt_dmb       :
                    bt_dmb_start.setText("시작");
                    bool_dmb_fm = false;
                    dmb_dotCount = 0;
                    dmb_barCount = 0;
                    start_receive_fm_signal = false;
                    fm_dotCount = 0;
                    graphView_fm_con.destroyDrawingCache();
                    graphView_fm_con.removeAllSeries();
                    break;
                case R.id.bt_fm        :
                    bt_fm_start.setText("시작");
                    bool_dmb_fm = true;
                    fm_dotCount = 0;
                    fm_barCount = 0;
                    start_receive_dmb_signal = false;
                    dmb_dotCount = 0;
                    graphView_dmb_con.destroyDrawingCache();
                    graphView_dmb_con.removeAllSeries();
                    if(bool_dmb_search_start){
                        start_receive_dmb_signal = false;
                        set_dmb_search_disable();
                    }
                    break;

                case R.id.bt_dmb_col  : bool_dmb_col_spec = false;
                    data = "dmbContellationMon";
                    graphView_dmb_spec.removeAllSeries();
                    dmb_barCount = 0;
                    if(usbService != null){ usbService.write(data.getBytes()); }   break;

                case R.id.bt_dmb_spec : bool_dmb_col_spec = true;

                    data = "dmbSpectrumMon";
                    graphView_dmb_con.removeAllSeries();
                    for( int i = 0; i < m_iArrayCnt ; i++){iPtrNilai[i] = i;}
                    dmb_dotCount = 0;

                    if(usbService != null){
                        usbService.write(data.getBytes());
                        for( int i = 0; i < m_iArrayCnt ; i++){iPtrNilai[i] = 0;}
                    }  break;

                case R.id.bt_dmb_ch    : bool_dmb_ch_search = false;     break;
                case R.id.bt_dmb_search: bool_dmb_ch_search = true;     break;
                case R.id.bt_dmb_ch1  :  spinner_dmb_list.setSelection(0);  break;
                case R.id.bt_dmb_ch2  :  spinner_dmb_list.setSelection(1);  break;
                case R.id.bt_dmb_ch3  :  spinner_dmb_list.setSelection(2);  break;
                case R.id.bt_dmb_ch4  :  spinner_dmb_list.setSelection(3);  break;
                case R.id.bt_dmb_ch5  :  spinner_dmb_list.setSelection(4);  break;
                case R.id.bt_dmb_ch6  :  spinner_dmb_list.setSelection(5);  break;
                case R.id.bt_dmb_ch7  :  spinner_dmb_list.setSelection(6);  break;
                case R.id.bt_dmb_ch8  :  spinner_dmb_list.setSelection(7);  break;
                case R.id.bt_dmb_ch9  :  spinner_dmb_list.setSelection(8);  break;
                case R.id.bt_dmb_ch10 :  spinner_dmb_list.setSelection(9);  break;
                case R.id.bt_dmb_ch11 :  spinner_dmb_list.setSelection(10); break;
                case R.id.bt_dmb_ch12 :  spinner_dmb_list.setSelection(11); break;
                case R.id.bt_dmb_ch13 :  spinner_dmb_list.setSelection(12); break;
                case R.id.bt_dmb_ch14 :  spinner_dmb_list.setSelection(13); break;
                case R.id.bt_dmb_ch15 :  spinner_dmb_list.setSelection(14); break;
                case R.id.bt_dmb_ch16 :  spinner_dmb_list.setSelection(15); break;
                case R.id.bt_dmb_ch17 :  spinner_dmb_list.setSelection(16); break;
                case R.id.bt_dmb_ch18 :  spinner_dmb_list.setSelection(17); break;
                case R.id.bt_dmb_ch19 :  spinner_dmb_list.setSelection(18); break;
                case R.id.bt_dmb_ch20 :  spinner_dmb_list.setSelection(19); break;
                case R.id.bt_dmb_ch21 :  spinner_dmb_list.setSelection(20); break;

                case R.id.bt_fm_ch1  :  spinner_fm_list.setSelection(chk_blue_channel_list[0]);  break;
                case R.id.bt_fm_ch2  :  spinner_fm_list.setSelection(chk_blue_channel_list[1]);  break;
                case R.id.bt_fm_ch3  :  spinner_fm_list.setSelection(chk_blue_channel_list[2]);  break;
                case R.id.bt_fm_ch4  :  spinner_fm_list.setSelection(chk_blue_channel_list[3]);  break;
                case R.id.bt_fm_ch5  :  spinner_fm_list.setSelection(chk_blue_channel_list[4]);  break;
                case R.id.bt_fm_ch6  :  spinner_fm_list.setSelection(chk_blue_channel_list[5]);  break;
                case R.id.bt_fm_ch7  :  spinner_fm_list.setSelection(chk_blue_channel_list[6]);  break;
                case R.id.bt_fm_ch8  :  spinner_fm_list.setSelection(chk_blue_channel_list[7]);  break;
                case R.id.bt_fm_ch9  :  spinner_fm_list.setSelection(chk_blue_channel_list[8]);  break;
                case R.id.bt_fm_ch10 :  spinner_fm_list.setSelection(chk_blue_channel_list[9]);  break;
                case R.id.bt_fm_ch11 :  spinner_fm_list.setSelection(chk_blue_channel_list[10]); break;
                case R.id.bt_fm_ch12 :  spinner_fm_list.setSelection(chk_blue_channel_list[11]); break;
                case R.id.bt_fm_ch13 :  spinner_fm_list.setSelection(chk_blue_channel_list[12]); break;
                case R.id.bt_fm_ch14 :  spinner_fm_list.setSelection(chk_blue_channel_list[13]); break;
                case R.id.bt_fm_ch15 :  spinner_fm_list.setSelection(chk_blue_channel_list[14]); break;
                case R.id.bt_fm_ch16 :  spinner_fm_list.setSelection(chk_blue_channel_list[15]); break;
                case R.id.bt_fm_ch17 :  spinner_fm_list.setSelection(chk_blue_channel_list[16]); break;
                case R.id.bt_fm_ch18 :  spinner_fm_list.setSelection(chk_blue_channel_list[17]); break;
                case R.id.bt_fm_ch19 :  spinner_fm_list.setSelection(chk_blue_channel_list[18]); break;
                case R.id.bt_fm_ch20 :  spinner_fm_list.setSelection(chk_blue_channel_list[19]); break;
                case R.id.bt_fm_ch21 :  spinner_fm_list.setSelection(chk_blue_channel_list[20]); break;
                case R.id.bt_fm_ch22 :  spinner_fm_list.setSelection(chk_blue_channel_list[21]); break;
                case R.id.bt_fm_ch23 :  spinner_fm_list.setSelection(chk_blue_channel_list[22]); break;
                case R.id.bt_fm_ch24 :  spinner_fm_list.setSelection(chk_blue_channel_list[23]); break;
                case R.id.bt_fm_ch25 :  spinner_fm_list.setSelection(chk_blue_channel_list[24]); break;
                case R.id.bt_fm_ch26 :  spinner_fm_list.setSelection(chk_blue_channel_list[25]); break;
                case R.id.bt_fm_ch27 :  spinner_fm_list.setSelection(chk_blue_channel_list[26]); break;
                case R.id.bt_fm_ch28 :  spinner_fm_list.setSelection(chk_blue_channel_list[27]); break;
                case R.id.bt_fm_ch29 :  spinner_fm_list.setSelection(chk_blue_channel_list[28]); break;
                case R.id.bt_fm_ch30 :  spinner_fm_list.setSelection(chk_blue_channel_list[29]); break;
                case R.id.bt_fm_ch31 :  spinner_fm_list.setSelection(chk_blue_channel_list[30]); break;
                case R.id.bt_fm_ch32 :  spinner_fm_list.setSelection(chk_blue_channel_list[31]); break;
                case R.id.bt_fm_ch33 :  spinner_fm_list.setSelection(chk_blue_channel_list[32]); break;
                case R.id.bt_fm_ch34 :  spinner_fm_list.setSelection(chk_blue_channel_list[33]); break;
                case R.id.bt_fm_ch35 :  spinner_fm_list.setSelection(chk_blue_channel_list[34]); break;
                case R.id.bt_fm_ch36 :  spinner_fm_list.setSelection(chk_blue_channel_list[35]); break;
                case R.id.bt_fm_ch37 :  spinner_fm_list.setSelection(chk_blue_channel_list[36]); break;
                case R.id.bt_fm_ch38 :  spinner_fm_list.setSelection(chk_blue_channel_list[37]); break;
                case R.id.bt_fm_ch39 :  spinner_fm_list.setSelection(chk_blue_channel_list[38]); break;
                case R.id.bt_fm_ch40 :  spinner_fm_list.setSelection(chk_blue_channel_list[39]); break;
                case R.id.bt_fm_ch41 :  spinner_fm_list.setSelection(chk_blue_channel_list[40]); break;
                case R.id.bt_fm_ch42 :  spinner_fm_list.setSelection(chk_blue_channel_list[41]); break;
                case R.id.bt_fm_ch43 :  spinner_fm_list.setSelection(chk_blue_channel_list[42]); break;
                case R.id.bt_fm_ch44 :  spinner_fm_list.setSelection(chk_blue_channel_list[43]); break;
                case R.id.bt_fm_ch45 :  spinner_fm_list.setSelection(chk_blue_channel_list[44]); break;
                case R.id.bt_fm_ch46 :  spinner_fm_list.setSelection(chk_blue_channel_list[45]); break;
                case R.id.bt_fm_ch47 :  spinner_fm_list.setSelection(chk_blue_channel_list[46]); break;
                case R.id.bt_fm_ch48 :  spinner_fm_list.setSelection(chk_blue_channel_list[47]); break;
                case R.id.bt_fm_ch49 :  spinner_fm_list.setSelection(chk_blue_channel_list[48]); break;
                case R.id.bt_fm_ch50 :  spinner_fm_list.setSelection(chk_blue_channel_list[49]); break;
                case R.id.bt_fm_ch51 :  spinner_fm_list.setSelection(chk_blue_channel_list[50]); break;
                case R.id.bt_fm_ch52 :  spinner_fm_list.setSelection(chk_blue_channel_list[51]); break;
                case R.id.bt_fm_ch53 :  spinner_fm_list.setSelection(chk_blue_channel_list[52]); break;
                case R.id.bt_fm_ch54 :  spinner_fm_list.setSelection(chk_blue_channel_list[53]); break;
                case R.id.bt_fm_ch55 :  spinner_fm_list.setSelection(chk_blue_channel_list[54]); break;
                case R.id.bt_fm_ch56 :  spinner_fm_list.setSelection(chk_blue_channel_list[55]); break;
                case R.id.bt_fm_ch57 :  spinner_fm_list.setSelection(chk_blue_channel_list[56]); break;
                case R.id.bt_fm_ch58 :  spinner_fm_list.setSelection(chk_blue_channel_list[57]); break;
                case R.id.bt_fm_ch59 :  spinner_fm_list.setSelection(chk_blue_channel_list[58]); break;
                case R.id.bt_fm_ch60 :  spinner_fm_list.setSelection(chk_blue_channel_list[59]); break;
                case R.id.bt_dmb_channel_search:
                    start_receive_dmb_signal = true;
                    for( Button bt : bt_dmb_ch_list){ bt.setBackgroundResource(R.drawable.y_w_box_dot_border); bt.setTextColor(Color.WHITE); }
                    bool_start_flag = true;
                    bool_dmb_search_start = true;
                    data = "dmbTune f=" + dmb_frequency;
                    if(usbService != null){ usbService.write(data.getBytes()); }
                    recursivehandler(-1);
                    bool_start_flag = false;
                    break;
                case R.id.bt_dmb_channel_search_stop:
                    start_receive_dmb_signal = false;
                    set_dmb_search_disable();
                    break;
                case R.id.bt_dmb_start: // dmb 시작 버튼 눌렀을 때 이벤트
                        if(start_receive_dmb_signal == false) {
                            start_receive_dmb_signal = true;
                            bt_dmb_start.setText("초기화");
                            data = "dmbTune f=" + dmb_frequency;
                            if (usbService != null) {
                                usbService.write(data.getBytes());
                            }
                        }else{
                            start_receive_dmb_signal = false;
                            Log.d("btton","btn");
                            bt_dmb_start.setText("시작");
                            //rw_dmb_cmd(true,DMB_TS_STOP);
                        }
                        // 현재 CONSTALLATION이 선택 되어 있는지 SPECTRUM이 선택되었는지 여부에 따라 시작 data 변경
                        if(bool_dmb_col_spec){
                            data = "dmbSpectrumMon";
                            if(usbService != null){ usbService.write(data.getBytes()); }
                        }else{
                            data = "dmbContellationMon";
                            if(usbService != null){ usbService.write(data.getBytes()); }
                        }

                    break;


                case R.id.bt_fm_col  : bool_fm_col_spec = false;
                    data = "fmContellationMon";
                    graphView_fm_spec.removeAllSeries();
                    fm_barCount = 0;
                    if(usbService != null){ usbService.write(data.getBytes()); }   break;

                case R.id.bt_fm_spec : bool_fm_col_spec = true;

                    data = "fmSpectrumMon";
                    graphView_fm_con.removeAllSeries();
                    fm_dotCount = 0;
                    if(usbService != null){usbService.write(data.getBytes());}  break;

                case R.id.bt_fm_ch    : bool_fm_ch_search = false;     break;
                case R.id.bt_fm_search: bool_fm_ch_search = true;     break;
                case R.id.bt_fm_channel_search:
                    //fm 검색 작업
                    start_receive_fm_signal = true;
                    for( Button bt : bt_fm_ch_list){ bt.setBackgroundResource(R.drawable.y_w_box_dot_border); bt.setTextColor(Color.WHITE); }
                    for( int i = 0 ; i < chk_channel_list.length ; i++){ chk_channel_list[i] = -1;}
                    for( int i = 0 ; i < chk_blue_channel_list.length;i++){chk_blue_channel_list[i] = -1;}
                    bool_fm_start_flag = true;
                    ch_fm_seq=-1;
                    fm_bt_set_seq = 0;
                    bool_fm_search_start = false;
                    new search_task().execute(0); //검색 작업 호출
                    bool_fm_start_flag = false;
                    break;
                case R.id.bt_fm_channel_search_stop:
                    start_receive_fm_signal = false;
                    set_fm_search_disable();
                    break;
                case R.id.bt_fm_start:  // fm 시작 버튼 눌렀을 때 이벤트
                    if(start_receive_fm_signal == false) {
                        start_receive_fm_signal = true;
                        data = "fmTune f=" + fm_frequency;
                        bt_fm_start.setText("초기화");
                        if (usbService != null) {
                            usbService.write(data.getBytes());
                        }
                        // 현재 CONSTALLATION이 선택 되어 있는지 SPECTRUM이 선택되었는지 여부에 따라 시작 data 변경
                        if(bool_fm_col_spec){
                            data = "fmSpectrumMon";
                            if(usbService != null){ usbService.write(data.getBytes()); }
                        }else{
                            data = "fmContellationMon";
                            if(usbService != null){ usbService.write(data.getBytes()); }
                        }
                        break;
                    }else{
                        start_receive_fm_signal = false;
                        Log.d("btton","btn");
                        bt_fm_start.setText("시작");
                        //rw_dmb_cmd(true,DMB_TS_STOP);
                    }

            }
            /*
             * Layout DMB
             * Layout device
             * Layout signal
             * Layout display
             * Layout search
             * Layout service
            */
            if(bool_dmb_fm){            /////////////////////////////////////////   FM
                bt_fm.setBackgroundResource(R.drawable.new_r_box_w_border);
                bt_dmb.setBackgroundResource(R.drawable.new_b_box_w_border);
                spinner_dmb_list.setVisibility(GONE);
                spinner_fm_list.setVisibility(View.VISIBLE);
                layout_fm.setVisibility(View.VISIBLE);
                layout_dmb.setVisibility(GONE);
                layout_device.setVisibility(GONE);
                layout_signal.setVisibility(GONE);
                layout_display.setVisibility(GONE);
                layout_search.setVisibility(GONE);
                layout_service.setVisibility(GONE);
                imageView_fm_connect.setImageResource(R.drawable.off);
                imageView_fm_signal.setImageResource(R.drawable.off);
                if(bool_fm_ch_search){
                    layout_fm_signal.setVisibility(GONE);
                    layout_fm_display.setVisibility(GONE);
                    layout_fm_device.setVisibility(GONE);
                    layout_fm_search.setVisibility(View.VISIBLE);
                    bt_fm_ch.setBackgroundResource(R.drawable.new_b_box_w_border);
                    bt_fm_search.setBackgroundResource(R.drawable.new_r_box_w_border);
                }else{
                    layout_fm_signal.setVisibility(View.VISIBLE);
                    layout_fm_display.setVisibility(View.VISIBLE);
                    layout_fm_device.setVisibility(View.VISIBLE);
                    layout_fm_search.setVisibility(GONE);
                    bt_fm_ch.setBackgroundResource(R.drawable.new_r_box_w_border);
                    bt_fm_search.setBackgroundResource(R.drawable.new_b_box_w_border);
                }
                if(bool_fm_col_spec){
                    bt_fm_spec.setBackgroundResource(R.drawable.new_r_box_w_border);
                    bt_fm_col.setBackgroundResource(R.drawable.new_b_box_w_border);
                    graphView_fm_spec.setVisibility(View.VISIBLE);
                    graphView_fm_con.setVisibility(View.GONE);
                }else{
                    bt_fm_spec.setBackgroundResource(R.drawable.new_b_box_w_border);
                    bt_fm_col.setBackgroundResource(R.drawable.new_r_box_w_border);
                    graphView_fm_spec.setVisibility(View.GONE);
                    graphView_fm_con.setVisibility(View.VISIBLE);
                }
            }else{            /////////////////////////////////////////   DMB
                bt_fm.setBackgroundResource(R.drawable.new_b_box_w_border);
                bt_dmb.setBackgroundResource(R.drawable.new_r_box_w_border);
                spinner_dmb_list.setVisibility(View.VISIBLE);
                spinner_fm_list.setVisibility(GONE);
                layout_fm.setVisibility(GONE);
                layout_fm_device.setVisibility(GONE);
                layout_fm_signal.setVisibility(GONE);
                layout_fm_display.setVisibility(GONE);
                layout_fm_search.setVisibility(GONE);
                layout_dmb.setVisibility(View.VISIBLE);
                imageView_dmb_connect.setImageResource(R.drawable.off);
                imageView_dmb_signal.setImageResource(R.drawable.off);
                if(bool_dmb_ch_search){
                    layout_signal.setVisibility(GONE);
                    layout_display.setVisibility(GONE);
                    layout_device.setVisibility(GONE);
                    layout_search.setVisibility(View.VISIBLE);
                    layout_service.setVisibility(GONE);
                    bt_dmb_ch.setBackgroundResource(R.drawable.new_b_box_w_border);
                    bt_dmb_search.setBackgroundResource(R.drawable.new_r_box_w_border);
                }else{
                    layout_signal.setVisibility(View.VISIBLE);
                    layout_display.setVisibility(View.VISIBLE);
                    layout_device.setVisibility(View.VISIBLE);
                    layout_search.setVisibility(GONE);
                    layout_service.setVisibility(View.VISIBLE);
                    bt_dmb_ch.setBackgroundResource(R.drawable.new_r_box_w_border);
                    bt_dmb_search.setBackgroundResource(R.drawable.new_b_box_w_border);
                }
                if(bool_dmb_col_spec){
                    bt_dmb_spec.setBackgroundResource(R.drawable.new_r_box_w_border);
                    bt_dmb_col.setBackgroundResource(R.drawable.new_b_box_w_border);
                    graphView_dmb_spec.setVisibility(View.VISIBLE);
                    graphView_dmb_con.setVisibility(View.GONE);

                }else{
                    bt_dmb_spec.setBackgroundResource(R.drawable.new_b_box_w_border);
                    bt_dmb_col.setBackgroundResource(R.drawable.new_r_box_w_border);
                    graphView_dmb_spec.setVisibility(View.GONE);
                    graphView_dmb_con.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.androdi_toolbar_spinner_menu, menu);
        MenuItem item = menu.findItem(R.id.spinner);
        return true;
    }

 /*
  * usb 관련
  */

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UsbService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                    Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_NO_USB: // NO USB CONNECTED
                    Toast.makeText(context, "No USB connected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                    Toast.makeText(context, "USB disconnected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
                    Toast.makeText(context, "USB device not supported", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            usbService = ((UsbService.UsbBinder) arg1).getService();
            usbService.setHandler(mHandler);

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            usbService = null;
        }
    };
    @Override
    public void onResume() {
        super.onResume();
        setFilters();  // Start listening notifications from UsbService
        startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
    }
    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mUsbReceiver);
        unbindService(usbConnection);
    }
    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!UsbService.SERVICE_CONNECTED) {
            Intent startService = new Intent(this, service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            startService(startService);
        }
        Intent bindingIntent = new Intent(this, service);
        bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }
    private void setFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(UsbService.ACTION_NO_USB);
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
        registerReceiver(mUsbReceiver, filter);
    }
    /*
     * This handler will be passed to UsbService. Data received from serial port is displayed through this handler
     */
    private class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        public MyHandler(MainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbService.MESSAGE_FROM_SERIAL_PORT:

                    if(msg.obj == null) Log.d("LONG","null");
                    OnDataReceiving((byte[]) msg.obj);

                    break;
                case UsbService.CTS_CHANGE:
                    Toast.makeText(mActivity.get(), "CTS_CHANGE",Toast.LENGTH_LONG).show();
                    break;
                case UsbService.DSR_CHANGE:
                    Toast.makeText(mActivity.get(), "DSR_CHANGE",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
    /*
     *  OnDataReceivingThread()
     */
    public void OnDataReceiving(byte[] data){
        byte[] receive_data = data;
        byte[] rxData ;//= new byte[64];//.getBytes();//
        byte[] msg = new byte[256];
        int i;
        int j;

        int fm_0x1 = 0;
        rxData = new byte[64];

        if(start_receive_dmb_signal && !bool_dmb_fm){ //dmb
            for( j = 0 ; j <receive_data.length/64;j++ ) {
                System.arraycopy(receive_data,j*64,rxData,0,64);
                for (i = 0; i < 376; i++) tsiDoubleData[i] = 0;
                if (rxData[0] == 'I' && rxData[1] == 'X' && rxData[2] == 'A' && rxData[3] == 'L') {
                    XCD_SetAgcLed(LOCK);
                    switch (rxData[6]) {
                        case (byte) 0xf8:
                            if (rxData[7] == 0) Arrays.fill(msg, (byte) 0);
                            System.arraycopy(rxData, 10, msg, 54 * rxData[7], rxData[9]);
                            if (rxData[7] + 1 == rxData[8]) {
                            }
                            break;
                        case (byte) 0x10:  /// on. off strength quality
                            if (mDrmFirstInit == 1 & gDrmInitFlag != 1) {
                                mDrmFirstInit = 0;
                            }
                            XCD_GetDabLock(rxData[7]);

                            temp3 = (rxData[56] & 0xff);
                            temp2 = (rxData[57] & 0xff);
                            temp1 = (rxData[58] & 0xff);
                            temp0 = (rxData[59] & 0xff);

                            strength = ((temp3) | (temp2 << 8) | (temp1 << 16) | (temp0 << 24));
                            strength = strength + 5;

                            if (dmb_frequency/1000 < 181.0)
                            {
                                if (strength > -30)			strength += 9;
                                else if (strength > -50)		strength += 9;
                                else if (strength > -70)		strength += 7;
                                else if (strength > -94)		strength += 5;
                                else					strength += 4;

                            }
                            else if (dmb_frequency/1000 < 184.0)
                            {
                                if (strength > -30)			strength += 9;
                                else if (strength > -50)		strength += 9;
                                else if (strength > -70)		strength += 7;
                                else if (strength > -94)		strength += 5;
                                else					strength += 2;
                            }
                            else if (dmb_frequency/1000 < 197.0)
                            {
                                if (strength > -30)			strength += 10;
                                else if(strength > -50)		strength += 9;
                                else if (strength > -70)		strength += 7;
                                else if (strength > -94)		strength += 5;
                                else					strength += 4;
                            }
                            else if (dmb_frequency/1000 >= 202.0)
                            {
                                if (strength > -30)			strength += 10;
                                else if (strength > -50)		strength += 9;
                                else if (strength > -70)		strength += 7;
                                else if (strength > -94)		strength += 5;
                                else					strength += 5;
                            }
                            else
                            {
                                if (strength> -30)			strength += 10;
                                else if(strength > -50)		strength += 9;
                                else if (strength > -70)		strength += 7;
                                else if (strength > -94)		strength += 5;
                                else					strength += 4;
                            }


                            temp3 = (rxData[36] & 0xff);
                            temp2 = (rxData[37] & 0xff);
                            temp1 = (rxData[38] & 0xff);
                            temp0 = (rxData[39] & 0xff);

                            byte[] fTmp_byte = new byte[]{rxData[39],rxData[38],rxData[37],rxData[36]};
                            fTmp = ByteBuffer.wrap(fTmp_byte).getFloat();//(float)fTmp_int;

                            temp3 = (rxData[60] & 0xff);
                            temp2 = (rxData[61] & 0xff);
                            temp1 = (rxData[62] & 0xff);
                            temp0 = (rxData[63] & 0xff);
                            quality = ((temp3) | (temp2 << 8) | (temp1 << 16) | (temp0 << 24));

                            //quality = iTMP
                            iTmp = quality;
                            XCD_SnrCalculation(fTmp, iTmp);


                            txt_dmb_strength.setText("  " + strength + "dBm");// -110 ~ 0
                            txt_dmb_quality.setText("  " + snr_sum + "dB");//0 ~ 30

                            break;
                        case (byte) 0x12:  /// dmb col
                            if (!bool_dmb_col_spec && !bool_dmb_fm)   //
                            {
                                int x, y;
                                int ii;
                                for (ii = 0; ii < 7; ii++) {
                                    temp3 = (rxData[8 * ii + 8] & 0xff);
                                    temp2 = (rxData[8 * ii + 9] & 0xff);
                                    temp1 = (rxData[8 * ii + 10] & 0xff);
                                    temp0 = (rxData[8 * ii + 11] & 0xff);
                                    x = ((temp3) | (temp2 << 8) | (temp1 << 16) | (temp0 << 24));
                                    temp3 = (rxData[8 * ii + 8 + 4] & 0xff);
                                    temp2 = (rxData[8 * ii + 9 + 4] & 0xff);
                                    temp1 = (rxData[8 * ii + 10 + 4] & 0xff);
                                    temp0 = (rxData[8 * ii + 11 + 4] & 0xff);
                                    y = ((temp3) | (temp2 << 8) | (temp1 << 16) | (temp0 << 24));
                                    x = x >> 4;
                                    y = y >> 4;
                                    if (Math.abs(x) < 127 && Math.abs(y) < 127) {
                                        drawDot(x, y, bool_dmb_fm);
                                    }
                                }

                            }

                            break;
                        case (byte) 0x13: // dmb sepc
                            if (bool_dmb_col_spec && !bool_dmb_fm) {
                                for (ii = 0; ii < 7; ii++) {
                                    int x, y,tmpy;
                                    int noise = 30;

                                    temp3 = (rxData[8 * ii + 8] & 0xff);
                                    temp2 = (rxData[8 * ii + 9] & 0xff);
                                    temp1 = (rxData[8 * ii + 10] & 0xff);
                                    temp0 = (rxData[8 * ii + 11] & 0xff);
                                    tSpecCnt = ((temp3) | (temp2 << 8) | (temp1 << 16) | (temp0 << 24));
                                    temp3 = (rxData[8 * ii + 8 + 4] & 0xff);
                                    temp2 = (rxData[8 * ii + 9 + 4] & 0xff);
                                    temp1 = (rxData[8 * ii + 10 + 4] & 0xff);
                                    temp0 = (rxData[8 * ii + 11 + 4] & 0xff);
                                    Spower = ((temp3) | (temp2 << 8) | (temp1 << 16) | (temp0 << 24));
                                    if (Spower < 1) Spower = 1;
                                    SpwrLog = (float) Math.log10((float) (Spower));
                                    x = tSpecCnt;
                                    y = (int) ((SpwrLog * 40) - 230);

                                    x = x + 133;// -133 ~ 133
                                    y = y + 70;
                                    y = y - (int)((30 - snr_sum));


                                    //spectrum 노이즈 생성 및 조정
                                    if (y < noise) y = (int) (noise + (Math.random() * 5));
                                    else{
                                        tmpy = y - noise;

                                        if(snr_sum<=5){
                                            y = (int) (tmpy * 0.14) + noise;
                                        }else if(snr_sum<=10){
                                            y = (int) (tmpy * 0.2) + noise;
                                        }else if(snr_sum<=15){
                                            y = (int) (tmpy * 0.26) + noise;
                                        }else if(snr_sum<=20){
                                            y = (int) (tmpy * 0.32) + noise;
                                        }else if(snr_sum<=25){
                                            y = (int) (tmpy * 0.38) + noise;
                                        }else{
                                            y = (int) (tmpy * 0.44) + noise;
                                        }
                                    }

                                    if (x >= 0 && x < 266) {
                                        iPtrNilai[x >> 1] = y;
                                        dmb_barCount++;
                                    }
                                    if (dmb_barCount > 266) {
                                        graphView_dmb_spec.destroyDrawingCache();
                                        graphView_dmb_spec.removeAllSeries();
                                        dmb_barCount = 0;
                                        drawBar(bool_dmb_fm);
                                    }
                                }
                            }
                            break;

                        case (byte) 0x01://TSI data
                            //fm 일때만
                            if (!(bool_dmb_fm && /*!bool_fm_col_spec &&*/ !bool_fm_search_start)) {
                                if (rxData[7] == 0) {
                                    rxTsiFlag[0] = rxTsiFlag[1] = rxTsiFlag[2] = rxTsiFlag[3] = false;
                                }
                                // src, srcPos, dst, dstgPos, length
                                System.arraycopy(rxData, 16, tsiData, 48 * rxData[7], rxData[9]);
                                rxTsiFlag[rxData[7]] = true;
                                if ((rxData[7] + 1) == rxData[8]) {
                                    if (rxTsiFlag[0] && rxTsiFlag[1] && rxTsiFlag[2] && rxTsiFlag[3]) {
                                        if (tsiData[0] == (byte)0x47) {
                                            if (tsiData[1] == (byte)0xFF) {
                                                if (tsiData[2] == (byte)0x81) {
                                                    rxFicFlag[0] = true;
                                                    rxFicFlag[1] = false;
                                                    rxFicFlag[2] = false;
                                                    System.arraycopy(tsiData, 4, ficData, 0, 184);
                                                } else if (tsiData[2] == (byte)0x01) {
                                                    rxFicFlag[1] = true;
                                                    System.arraycopy(tsiData, 4, ficData, 184, 184);
                                                } else if (tsiData[2] == (byte)0x41) {
                                                    rxFicFlag[2] = true;
                                                    System.arraycopy(tsiData, 4, ficData, 368, 16);
                                                    if (rxFicFlag[0] && rxFicFlag[1] && rxFicFlag[2] ) {
                                                        for (i = 0; i < 384; i += 32) {
                                                            firCrcTotCnt++;
                                                            temp0 = (ficData[i]&(byte)0xE0);
                                                            if (temp0 == (byte)0x20){
                                                                temp1 = (ficData[i+1]&(byte)0x07);
                                                                if (temp1 == (byte)0x00){
                                                                    int k;
                                                                    byte[] label_byte = new byte[16];
                                                                    for( k = 0 ; k < 16 ; k++){
                                                                        label_byte[k] = ficData[i+4+k];
                                                                    }
                                                                    String label = new String(label_byte);
                                                                    Log.d("LABEL",""+label.trim());
                                                                    if(snr_sum>8){
                                                                        txt_service_list.setText(label.trim());
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                    }
                }
                else{
                    XCD_SetAgcLed(LOCK);
                }
            }

        }else if(start_receive_fm_signal && bool_dmb_fm) { //fm
            for (j = 0; j < receive_data.length / 64; j++) {
//
                System.arraycopy(receive_data, j * 64, rxData, 0, 64);
                if (rxData[0] == 'I' && rxData[1] == 'X' && rxData[2] == 'A' && rxData[3] == 'L') {
                    imageView_fm_connect.setImageResource(R.drawable.on);  //GREEN
                    switch (rxData[6]) {
                        case (byte) 0xf8:
                            if (rxData[7] == 0) Arrays.fill(msg, (byte) 0);
                            //src dst  L
                            System.arraycopy(rxData, 10, msg, 54 * rxData[7], rxData[9]);
                            if (rxData[7] + 1 == rxData[8]) {
                                // print debug msg
                            }
                            break;
                        case (byte) 0x30: // fm
//                            XCD_GetFmLock(rxData[7]);
                            temp3 = (rxData[56] & 0xff);
                            temp2 = (rxData[57] & 0xff);
                            temp1 = (rxData[58] & 0xff);
                            temp0 = (rxData[59] & 0xff);

                            strength = ((temp3) | (temp2 << 8) | (temp1 << 16) | (temp0 << 24));
                            if (strength > -50) strength += 20;
                            else if (strength > -105) strength += 15;
                            else strength += 18;


                            temp3 = (rxData[60] & 0xff);
                            temp2 = (rxData[61] & 0xff);
                            temp1 = (rxData[62] & 0xff);
                            temp0 = (rxData[63] & 0xff);
                            quality = ((temp3) | (temp2 << 8) | (temp1 << 16) | (temp0 << 24));

                            gFmsnr = quality;
                            if(gFmsnr > 10){
                                imageView_fm_signal.setImageResource(R.drawable.on);
                            }else{
                                imageView_fm_signal.setImageResource(R.drawable.off);
                            }
                            Log.d("ASD", ""+strength);
                            if(strength >= -90){ // search list에서 TUNE 시 일정 RSSI 이하 무시
                                bt_fm_ch_list[blue_box_loc].setText(bt_fm_ch_list[b_box_loc].getText());
                                 bt_fm_ch_list[blue_box_loc].setVisibility(View.VISIBLE);
                                 bt_fm_ch_list[blue_box_loc].setBackgroundResource(R.drawable.y_b_box_dot_border);
                                 bt_fm_ch_list[blue_box_loc].setTextColor(Color.WHITE);
                                 bt_fm_ch_list[blue_box_loc].setEnabled(true);
                                 if(flag_b_box<0) flag_b_box*=-1;
                            }

                            txt_fm_strength.setText("" + strength + "dBm");// -110 ~ 0
                            txt_fm_quality.setText("" + quality + "dB");//0 ~ 30
                            break;
                        case (byte) 0x31:
                            FmSchRes = (rxData[8] << 8) | (rxData[7] & 0xFF);
                            Log.d("FmSchRes", "" + (FmSchRes & 0x01) + ":" + rxData[6]);
                            gFmScanDone = 1;
                            progressBar_fm.setProgress(ch_fm_seq + 1);
                            if (gFmScanDone == 1) {
                                if ((FmSchRes & 0x01) == 1) {
                                    gFmScanDone = 0;
                                    FmSchRes = 0;
                                    if (!bool_fm_search_start) {
                                        Log.d("LOCK", "bt" + fm_bt_set_seq + "ch_" + ch_fm_seq);
                                        if(fm_bt_set_seq<60) {
                                            chk_channel_list[fm_bt_set_seq] = ch_fm_seq; // 0x31 체크 된 주파수 리스트
                                            bt_fm_ch_list[fm_bt_set_seq].setText(spinner_fm_list.getItemAtPosition(ch_fm_seq).toString());
                                            bt_fm_ch_list[fm_bt_set_seq++].setTextColor(Color.WHITE);
                                        }
                                    }
                                } else {
                                    FmSchRes = 0;
                                    gFmScanDone = 0;
                                }
                            }
                            break;

                        case (byte) 0x33: //fm con
                            if (bool_fm_col_spec && bool_dmb_fm) {
                                for (ii = 0; ii < 7; ii++) {
                                    int x, y,tmpy,noise;
                                    temp3 = (rxData[8 * ii + 8] & 0xff);
                                    temp2 = (rxData[8 * ii + 9] & 0xff);
                                    temp1 = (rxData[8 * ii + 10] & 0xff);
                                    temp0 = (rxData[8 * ii + 11] & 0xff);
                                    tSpecCnt = ((temp3) | (temp2 << 8) | (temp1 << 16) | (temp0 << 24));
                                    temp3 = (rxData[8 * ii + 8 + 4] & 0xff);
                                    temp2 = (rxData[8 * ii + 9 + 4] & 0xff);
                                    temp1 = (rxData[8 * ii + 10 + 4] & 0xff);
                                    temp0 = (rxData[8 * ii + 11 + 4] & 0xff);
                                    Spower = ((temp3) | (temp2 << 8) | (temp1 << 16) | (temp0 << 24));
                                    if (Spower < 1) Spower = 1;
                                    SpwrLog = (float) Math.log10((float) (Spower));
                                    x = tSpecCnt;
                                    y = (int) ((SpwrLog * 40) - 230);   //  최초의 Y값 : rc->bottom


                                    x = x + 133;// -133 ~ 133
//                                    y /= 2;
                                    y = y + 60;

                                    y = y - (int)((30 - gFmsnr)*2);
                                    y/=2;
                                    noise = 30;
//                                    if (y < 35) y = (int) (30);
                                    if (y < noise+5) y = (int) (noise + (Math.random() * 5));

                                    if (x >= 0 && x < 266) {
                                        iPtrNilai[x >> 1] = y;
                                        fm_barCount++;
                                    }
                                    if (fm_barCount > 266) {
                                        graphView_fm_spec.destroyDrawingCache();
                                        graphView_fm_spec.removeAllSeries();
                                        fm_barCount = 0;
                                        drawBar(bool_dmb_fm);
                                    }
                                }
                            }
                            break;

                        case (byte) 0x01://TSI data

                            if (bool_dmb_fm && !bool_fm_col_spec && !bool_fm_ch_search) {
                                fm_0x1++;
                                if (rxData[7] == 0) {
                                    rxTsiFlag[0] = rxTsiFlag[1] = rxTsiFlag[2] = rxTsiFlag[3] = false;
                                }
                                // src, srcPos, dst, dstgPos, length
                                System.arraycopy(rxData, 16, tsiData, 48 * rxData[7], rxData[9]);
                                rxTsiFlag[rxData[7]] = true;
                                if ((rxData[7] + 1) == rxData[8]) {
                                    if (rxTsiFlag[0] && rxTsiFlag[1] && rxTsiFlag[2] && rxTsiFlag[3]) {
                                        if (tsiData[0] == 0x47) {
                                            if (tsiData[1] == 0xFF) {
                                                if (tsiData[2] == 0x81) {
                                                    rxFicFlag[0] = true;
                                                    rxFicFlag[1] = false;
                                                    rxFicFlag[2] = false;
                                                    System.arraycopy(tsiData, 4, ficData, 0, 184);
                                                    //memcpy(ficData, &tsiData[4], 184);
                                                } else if (tsiData[2] == 0x01) {
                                                    rxFicFlag[1] = true;
                                                    System.arraycopy(tsiData, 4, ficData, 184, 184);
                                                    //memcpy(&ficData[184], &tsiData[4], 184);
                                                } else if (tsiData[2] == 0x41) {
                                                    rxFicFlag[2] = true;
                                                    System.arraycopy(tsiData, 4, ficData, 368, 16);
                                                    //memcpy(&ficData[184 * 2], &tsiData[4], 16);

                                                    if (rxFicFlag[0] && rxFicFlag[1] && rxFicFlag[2] && gFicDumpEn == true) {

                                                    }
                                                }

                                            }
                                        }
                                    }

                                    int idx;
                                    System.arraycopy(tsiDoubleData, 188, tsiDoubleData, 0, 188);
                                    System.arraycopy(tsiData, 0, tsiDoubleData, 188, 188);
                                    for (idx = 0; idx < 188; idx++) {
                                        if (tsiDoubleData[idx] == (byte) 0x47 && tsiDoubleData[idx + 1] == (byte) 0x47 && tsiDoubleData[idx + 2] == (byte) 0x47) {
                                            System.arraycopy(tsiDoubleData, idx, tsiData, 0, 188);
                                            break;
                                        }
                                    }

                                    if (idx != 0 && idx != 188) {
                                        idx = 0;
                                    }
                                }
                                for (i = 0; i < 40; i += 4) {
                                    temp0 = tsiData[i];
                                    temp1 = temp0 << 8;
                                    temp2 = tsiData[i + 2];
                                    temp3 = temp2 << 8;
                                    temp0 = tsiData[i + 1];
                                    temp2 = tsiData[i + 3];
                                    i_val = (temp1 + temp0);
                                    q_val = (temp3 + temp2);
                                    if ((i_val & 0x00008000) != 0)
                                        i_val = i_val | 0xFFFF8000;
                                    if ((q_val & 0x00008000) != 0)
                                        q_val = q_val | 0xFFFF8000;
                                    int temp_x;
                                    int temp_y;
                                    temp_x = (int) (i_val / 100);
                                    temp_y = (int) (q_val / 100);
                                    if (temp_x < 127 && temp_y < 127 && temp_x > -127 && temp_y > -127) {
                                        if (temp_x != 0 && temp_y != 0)
                                            drawDot(temp_x, temp_y, bool_dmb_fm);
                                    }
                                }
                                Arrays.fill(tsiData, (byte) 0);
                            }
                            break;


                    }
                } else {
                    imageView_fm_connect.setImageResource(R.drawable.off); //RED
                }
            }
        }

        if( receive_data.length >=64) {
            System.arraycopy(receive_data, 0, rxData, 0, 64);
            if (rxData[0] == 'I' && rxData[1] == 'X' && rxData[2] == 'A' && rxData[3] == 'L') {
                imageView_fm_connect.setImageResource(R.drawable.on);  //GREEN
                imageView_dmb_connect.setImageResource(R.drawable.on);
            } else {
                imageView_fm_connect.setImageResource(R.drawable.off);  //GREEN
                imageView_dmb_connect.setImageResource(R.drawable.off);
            }
        }
    }
    public void XCD_GetDabLock(byte lockStatus){


        //****************** Set I2C Lock LED *******************
        XCD_SetI2cLed(LOCK);
        //TabFragment1.txt_chk.setText("GETDABLOCK");
        //***************** Set Lock Status LED ****************

//        if ((lockStatus & 0x01) == 0x01)	// AGC Lock  - 장비연결
//            XCD_SetAgcLed(LOCK);
//        else
//            XCD_SetAgcLed(UNLOCK);

        if ((lockStatus & 0x08) == 0x08)	// OFDM Lock
            XCD_SetOfdmLed(LOCK);
        else
            XCD_SetOfdmLed(UNLOCK);

        if ((lockStatus & 0x40) == 0x40)	// FEC Lock - dmb rock
        {
            XCD_SetFecLed(LOCK);
            errStatus_dab.FecLock = 1;
        }
        else
        {
            XCD_SetFecLed(UNLOCK);
            errStatus_dab.FecLock = 0;
        }
    }
    public void XCD_SetI2cLed(boolean mode){
        if (mode == LOCK)
        {
            //GREEN
        }
        else
        {
            //RED
        }
    }
    public void XCD_SetAgcLed(boolean mode){
        if (mode == LOCK)
        {
            imageView_dmb_connect.setImageResource(R.drawable.on);  //GREEN
        }
        else
        {
            imageView_dmb_connect.setImageResource(R.drawable.off); //RED
        }
    }
    public void XCD_SetOfdmLed(boolean mode) {
        if (mode == LOCK) {
            //GREEN
        } else {
            //RED

        }
    }
    public void XCD_SetFecLed(boolean mode){
        if (mode == LOCK) {
            imageView_dmb_signal.setImageResource(R.drawable.on);  //GREEN
            bt_dmb_ch_list[ch_seq].setBackgroundResource(R.drawable.y_r_box_dot_border);
            bt_dmb_ch_list[ch_seq].setTextColor(Color.WHITE);
            bt_dmb_ch_list[ch_seq].setEnabled(true);
        } else {
            imageView_dmb_signal.setImageResource(R.drawable.off); //RED
        }
    }
    public void XCD_GetFmLock(byte lockStatus){
        XCD_SetI2cLed(LOCK);
//        if ((lockStatus & 0x01) == 0x01)	SetAgcLed(LOCK);		// AGC Lock Flag
//        else	SetAgcLed(UNLOCK);

//        if ((lockStatus & 0x02) == 0x02)	SetOfdmLed(LOCK);		// FM/AM Lock
//        else	SetOfdmLed(UNLOCK);

        if ((lockStatus & 0x08) == 0x08)  	SetTpsLed(LOCK);		// Pilot Lock
        else	SetTpsLed(UNLOCK);
    }
    public void SetTpsLed(boolean mode){
        if (mode == LOCK) {
            imageView_fm_signal.setImageResource(R.drawable.on);  //GREEN
            bt_fm_ch_list[ch_seq].setBackgroundResource(R.drawable.y_r_box_dot_border);
            bt_fm_ch_list[ch_seq].setTextColor(Color.WHITE);
        } else {
            imageView_fm_signal.setImageResource(R.drawable.off); //RED
        }
    }
    public void SetAgcLed(boolean mode){
        if (mode == LOCK)
        {
            imageView_fm_connect.setImageResource(R.drawable.on);  //GREEN


        }
        else
        {
            imageView_fm_connect.setImageResource(R.drawable.off); //RED
        }
    }
    public void SetOfdmLed(boolean mode) {
        if (mode == LOCK) {
            //GREEN
        } else {
            //RED
        }
    }
    public void drawDot(int x, int y, boolean mode) {
        if(mode){
            PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(new DataPoint[]{
                    new DataPoint(x*2, y*2)
            });
            series.setSize(10);
            graphView_fm_con.addSeries(series);
            fm_dotCount++;
            if(fm_dotCount>1000) {
                graphView_fm_con.destroyDrawingCache();
                graphView_fm_con.removeAllSeries();
                fm_dotCount = 0;
            }
        }else{
            PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(new DataPoint[]{
                    new DataPoint(x*2, y*2)
            });
            series.setSize(10);
            //series.setColor(Color.rgb(39,17,88));
            graphView_dmb_con.addSeries(series);
            dmb_dotCount++;
            if(dmb_dotCount >1000) {
                graphView_dmb_con.destroyDrawingCache();
                graphView_dmb_con.removeAllSeries();
                dmb_dotCount = 0;
            }
        }
    }
    public void drawBar(boolean mode){


        if(mode) {
            DataPoint[] spec = new DataPoint[132];
            for( int i =0 ; i < 132; i++){
                spec[i] = new DataPoint((float)i/(float)61,iPtrNilai[i]);
            }
            BarGraphSeries<DataPoint> series = new BarGraphSeries<>(spec);
            series.setDataWidth(0.01) ;
            graphView_fm_spec.addSeries(series);
            fm_barCount++;
        }else{
            DataPoint[] spec = new DataPoint[132];
            for( int i =0 ; i < 132; i++){
                spec[i] = new DataPoint((float)i/(float)61,iPtrNilai[i]);
            }
            BarGraphSeries<DataPoint> series = new BarGraphSeries<>(spec);
            series.setDataWidth(0.01) ;
            graphView_dmb_spec.addSeries(series);
            dmb_barCount++;
        }



    }
    public void XCD_SnrCalculation(float cer, int pwr){
        String str;
        int snr;
        if(errStatus_dab.FecLock ==1){
            if (cer < 0.00005)
            {
                snr = 30;
            }
            else if (cer < 0.0004)
            {
                snr = pwr + 1;
            }
            else
            {
                if (cer < 0.00085)			snr = 13;
                else if (cer < 0.0012)		snr = 12;
                else if (cer < 0.003)		snr = 11;
                else if (cer < 0.007)		snr = 10;
                else if (cer < 0.015)		snr = 9;
                else if (cer < 0.025)		snr = 8;
                else if (cer < 0.038)		snr = 7;
                else if (cer < 0.052)		snr = 6;
                else if (cer < 0.082)		snr = 5;
                else if (cer < 0.200)		snr = 4;
                else						snr = 14;
            }
        }else
            snr = 0;

        snr_sum += snr;
        snr_sum = snr_sum>>1;
        gDmbSnr = snr_sum;
    }
    public void set_dmb_search_disable(){
         bool_dmb_search_start = false;
        bt_dmb_search_start.setEnabled(true);
        bt_dmb_search_start.setBackgroundResource(R.drawable.new_b_box_w_border);
    }
    //클릭 방지
    public void set_fm_search_disable(){
        bool_fm_search_start = true;
        //bt_fm_search_start.setEnabled(true);
        bt_fm_search_start.setBackgroundResource(R.drawable.new_b_box_w_border);
    }

    /// added by chkim 2018-01-07
    public void rw_dmb_cmd(boolean mode, int cmd_usb){
        // USB dongle command
        /// usb command list
        //final static int DMB_TS_START   = 00;
        //final static int DMB_TS_STOP    = 10;
        //final static int FM_TS_START    = 02;
        //final static int FM_TS_STOP     = 20;
        ////
        String wdata;

        if (mode)
        {
            /// write
            switch(cmd_usb)
            {
                case DMB_TS_START:
                    wdata = "regWrite= 06 170 255";
                    if (usbService != null) {
                        usbService.write(wdata.getBytes());
                    }
                    break;
                case DMB_TS_STOP:
                    wdata = "regWrite= 06 170 000";
                    if (usbService != null) {
                        usbService.write(wdata.getBytes());
                    }
                    break;
                case FM_TS_START:
                    wdata = "regWrite= 00 084 000";
                    if (usbService != null) {
                        usbService.write(wdata.getBytes());
                    }
                    break;
                case FM_TS_STOP:
                    wdata = "regWrite= 00 084 128";
                    if (usbService != null) {
                        usbService.write(wdata.getBytes());
                    }
                    break;
                default:
                    break;
            }

        }

        if (usbService != null) {
            usbService.write(data.getBytes());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(mUsbReceiver);
    }

    public void recursivehandler(final int i){
        hander.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(i<0){
                    bt_dmb_search_start.setEnabled(false);
                    bt_dmb_search_start.setBackgroundResource(R.drawable.new_r_box_w_border);
                }
                ch_seq = i;
                ch_seq++;
                progressBar_dmb.setProgress(ch_seq+1);
                data = "dmbTune f=" + dmb_channel_list[ch_seq];
                bt_dmb_ch_list[ch_seq].setBackgroundResource(R.drawable.y_g_box_dot_border);
                bt_dmb_ch_list[ch_seq].setTextColor(Color.BLACK);
                bt_dmb_ch_list[ch_seq].setEnabled(false);
                if (usbService != null) {
                    usbService.write(data.getBytes());
                }
                if(ch_seq< 20) {         Log.d("fm_re",""+ch_seq);
                    if(bool_dmb_search_start) recursivehandler(ch_seq);
                }else{
                    bt_dmb_search_start.setEnabled(true);
                    bt_dmb_search_start.setBackgroundResource(R.drawable.new_b_box_w_border);
                    bool_dmb_search_start = false;
                }
            }
        },1000);
    }
    public void fm_recursivehandler(final int i){
        hander.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(i<0){
                    bt_fm_search_start.setEnabled(false);
                    bt_fm_search_start.setBackgroundResource(R.drawable.new_r_box_w_border);
                }
                ch_fm_seq = i;
                ch_fm_seq++;

                data = "fmTune f=" + fm_channel_list[ch_fm_seq];
//                bt_fm_ch_list[ch_seq].setBackgroundResource(R.drawable.y_g_box_dot_border);
//                bt_fm_ch_list[ch_seq].setTextColor(Color.BLACK);
//                bt_fm_ch_list[ch_seq].setEnabled(false);
                if (usbService != null) {
                    usbService.write(data.getBytes());
                }
                if(ch_fm_seq< fm_channel_list.length) {

                    if(bool_fm_search_start) fm_recursivehandler(ch_fm_seq);
                }else{
                    bt_fm_search_start.setEnabled(true);
                    bt_fm_search_start.setBackgroundResource(R.drawable.new_b_box_w_border);
                    bool_fm_search_start = false;
                }
            }
        },1000);
    }

    public class  search_task extends AsyncTask<Integer,Void,Void>{

        @Override
        protected void onPreExecute() {
            if(ch_fm_seq<=0){
                bt_fm_search_start.setEnabled(false);
                bt_fm_search_start.setBackgroundResource(R.drawable.new_r_box_w_border);
            }
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... params) {
            Log.d("LEN",""+fm_channel_list.length);
            for( ch_fm_seq = 0 ; ch_fm_seq < fm_channel_list.length;ch_fm_seq++) {

                if(!bool_fm_search_start) {
                    data = "fmScan f=" + fm_channel_list[ch_fm_seq]+" hth= -50 Vth= 5000 pwth= 1000000";

                    Log.d("ASD", data);
                    if (usbService != null) {
                        usbService.write(data.getBytes());
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    break;
                }
                if(fm_bt_set_seq>bt_fm_ch_list.length) break;

            }
            if(!bool_fm_search_start) ch_fm_seq = fm_channel_list.length-1;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            Log.d("async",""+ch_fm_seq);
            new  post_search_task().execute();
            super.onPostExecute(aVoid);
        }
    }

    int blue_box_loc = 0;

    public class post_search_task extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            int length = 0;

            blue_box_loc = 0;
            for( b_box_loc = 0; chk_channel_list[b_box_loc] != -1; b_box_loc++){

            }
            length = b_box_loc;

            for( b_box_loc = 0 ; chk_channel_list[b_box_loc] != -1 ;b_box_loc++) {
                flag_b_box = -1;
                progressBar_fm.setProgress((int) (100+((float)b_box_loc/length)*100));
                Log.d("ASD",""+progressBar_fm.getProgress());
                if(!bool_fm_search_start) {
                    data = "fmTune f=" + (int) (Float.parseFloat(spinner_fm_list.getItemAtPosition(chk_channel_list[b_box_loc]).toString())*1000);
                    Log.d("ASD", data);
                    if (usbService != null) {
                        usbService.write(data.getBytes());
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(flag_b_box>0){
                        chk_blue_channel_list[blue_box_loc] = chk_channel_list[b_box_loc];
                        blue_box_loc++;
                    }
                }
                else{
                    break;
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {

            Log.d("async",""+ch_fm_seq);
            bt_fm_ch_list[fm_bt_set_seq].setVisibility(View.VISIBLE);
            bt_fm_search_start.setEnabled(true);
            bt_fm_search_start.setBackgroundResource(R.drawable.new_b_box_w_border);
            bool_fm_search_start = true;

            super.onPostExecute(aVoid);
        }
    }
}
