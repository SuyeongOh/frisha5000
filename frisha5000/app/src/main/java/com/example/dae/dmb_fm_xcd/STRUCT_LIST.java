package com.example.dae.dmb_fm_xcd;

/**
 * Created by chk on 2018-01-29.
 */

public class STRUCT_LIST {
    final static int XCD_OK = 0;
    final static int XCD_NOTHING = 2;
    final static int XCD_FAIL = 	(-1);
    final static int MSC_STREAM_AUDIO = 0x00;
    final static int MSC_STREAM_DATA = 0x01;
    final static int FIDC = 0x02;
    final static int MSC_PACKET_DATA = 0x03;
    final static int LABEL_NUM = 17;    // Ensemble, service, service component label number
    final static int MAX_SERV_COMP = 64;    // maximum service component number in Ensemble channel
    final static int MAX_SUB_CH_NUM = 12;    // maximum service component number in one service
    final static int MAX_SERVICE_NUM = 20;    // maximum service number in one ensemble
    final static int USER_APP_NUM = 6;     // maximum number of user applications
    final static int ALL_FLAG_MATCH = 0x11;  // get FIC Time information complete
    final static int TIME_FLAG = 0x01;  // get FIC type0 extension10 complete
    final static int LTO_FLAG = 0x10;  // get FIC type0 extension9 complete
    final static int XCD_FIC_GOING = 0;
    final static int XCD_FIC_LABEL = 0x01;
    final static int XCD_FIC_APP_TYPE = 0x10;
    final static int XCD_FIC_DONE = 0x11;
    final static int Unspec_DATA = 0;
    final static int TMC_Type = 1;
    final static int EWS_Type = 2;
    final static int ITTS_Type = 3;
    final static int Paging_Type = 4;
    final static int TDC_Type = 5;
    final static int KDMB_Type = 24;
    final static int Em_IP_Type = 59;
    final static int MOT_Type = 60;
    final static int PS_noDSCTy = 61;

    public class DATE_TIME_INFO{
        long				MJD;
        char				LSI;
        char				conf_ind;
        char				utc_flag;
        char				apm_flag;
        char				time_flag;
        char				get_flag;
        short				years;
        char				months_dec;
        char[]				months_ste = new char[4];
        char[]				weeks = new char[4];
        char				days;
        char				hours;
        char				minutes;
        char				seconds;
        short				milliseconds;
        long				LTO;          ///Local Time Offset
    }

    public class SVR_COM_DESP{
        ///MCI Information
        long BIT_RATE;   ///T0/1 Service Component BitRate(kbit/s)
        long P_L;        ///TO/1 Service Component Protection Level
        long START_Addr; ///TO/1 Service Component CU Start Address
        long SUB_CH_Size;///TO/1 Service Component CU Size
        char  TMID;       ///T0/2 Transport Mechanism Identifier. 00:stream audio, 01:stream data, 10:FIDC, 11:packet data
        char  ASCTy;      ///T0/2 Audio Service Component type 00:foreground, 01:background, 10:multi-channel
        char  SubChid;    ///T0/2 Sub-channel Identifier
        char  P_S;        ///T0/2 1:primary, 0:secondary
        char  CA_flag;    ///T0/2 flag shall indicate whether access control 0:no access, 1:access
        char  DSCTy;      ///T0/2, T0/3 Data Service Component Type
        char  FIDCid;     ///T0/2 Fast Information Data Channel
        char  TCid;       ///T0/2 FIDC Type Component Identifier
        char  Ext;        ///T0/2 FIDC Extension
        short SCid;       ///T0/2 Service Component Identifier
        char  DG_flag;    ///T0/3 Data group flag
        short Packet_add; ///T0/3 The address of packet in which the service component is carried
        short CA_Org;     ///T0/3 Conditional Access Organization
        char  FEC_scheme; ///T0/14 FEC Frame
        //SI Information
        char	language;   ///T0/5 Service Component Language. TS 101 576, table 9 and 10
        char  charset;    ///T1/4 Service component label identify a character set
        char[] Label = new char[LABEL_NUM]; ///T1/4 Service component label
        //Service componet structure
        long Sid;        ///T0/2 Service Identifier for service component linkage
        char  SCidS;      ///T0/8 Service Component Identifier within the service
        //User application information
        char  Num_User_App;                       ///T0/13 Number of User Application
        short[] User_APP_Type = new short[USER_APP_NUM];	    ///T0/13 User Application Type
        char[]  User_APP_data_length = new char[USER_APP_NUM]; ///T0/13 User Application data length
        char[]  User_APP_data = new char[24];                  ///T0/13 User Application data
    }

    public class SERVICE_DESC{
        //MCI Information
        long Sid;            ///T0/2 Service Identifier
        char  Country_id;     ///T0/2 TS 101 756[23], tables 3 to 7
        long Service_ref;    ///T0/2 Indicate the number of the service
        char  ECC;            ///T0/2 Extended country code
        char  Local_flag;     ///T0/2 0:whole ensemble 1:partial ensemble
        char  CAID;           ///T0/2 Conditional Access Id
        char  Num_ser_comp;   ///T0/2 Max 12 for 16-bits SIds, Max 11 for 32-bits SIds
        char  P_D;            ///Type0 Programme(16-bit SId)/Data(32-bit SId) service
        //SI Information
        char  label_flag;     ///T1/1 Character flag field
        char  charset;        ///T1/1, T1/5 identify a character set
        char[]  Label = new char[LABEL_NUM]; ///T1/1, T1/5 Service label
        //Program type
        char	int_code;       ///T0/17 basic Programme Type category. TS 101 576, International table
        char[]  ser_comp_num = new char[MAX_SUB_CH_NUM]; /// Service Component Index number in a Service
    }

    public class FIDC_DESC{
        char  EWS_current_segmemt;
        char  EWS_total_segmemt;
        char  EWS_Message_ID;
        char[]  EWS_category = new char[4];
        char  EWS_priority;
        long EWS_time_MJD;
        char  EWS_time_Hours;
        char  EWS_time_Minutes;
        char  EWS_region_form;
        char  EWS_region_num;
        char  EWS_Rev;
        FIDC_EWS_Region[]  EWS_Region = new FIDC_EWS_Region[15];
        char[]  EWS_short_sentence = new char[409];
    }

    public class FIDC_EWS_Region
    {
        char[]  Sub_Region = new char[11];
    };


    public class ENSEMBLE_DESC {
        long svr_num;        ///Service number in a Ensemble
        long svr_comp_num;   ///Service Component number in a Ensemble
        long label_num;      ///Service Label number in a Ensemble
        //MCI Information
        int id;             ///T0/0 Ensemble Identifier
        short TII_main_id;
        short TII_sub_id_cnt;
        short[] TII_Subid = new short[4];
        short change_flag;    ///T0/0 Indicator to be change in the sub-channel or service organization
        short Alarm_flag;     ///T0/0 Ensemble Alarm message
        //SI Information
        short charset;        ///T1/0 Character flag field
        char[] Label = new char[LABEL_NUM]; ///T1/0 Ensemble label
        long freq;           ///Ensemble Frequency
        short label_flag;     ///Ensemble label Flag

        DATE_TIME_INFO date_time_info;
        SERVICE_DESC[] svr_desc = new SERVICE_DESC[MAX_SERVICE_NUM];
        SVR_COM_DESP[] svr_comp = new SVR_COM_DESP[MAX_SERV_COMP];
        FIDC_DESC fidc_desc;
    }
}
