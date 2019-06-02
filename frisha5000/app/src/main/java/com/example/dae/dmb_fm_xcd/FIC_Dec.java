//package com.example.dae.dmb_fm_xcd;
//
//import static com.example.dae.dmb_fm_xcd.STRUCT_LIST.*;
//
///**
// * Created by chk on 2018-01-29.
// */
//
//public class FIC_Dec {
//
//    final static int XCD_FIB_END_MARKER = 0xFF;
//
//    public class FIG_DATA{
//        char type;
//        char length;
//        char[] data;
//        char byte_cnt;
//        char bit_cnt;
//    }
//
//    public class FIG_TYPE0{
//        char C_N;	///current or the next version of the multiplex configuration. 0:current, 1:next
//        char OE;	///Other Ensemble. 0:this ensemble, 1:other ensemble
//        char P_D;	///Porgramme/data service Id. 0:16-bit SId, 1:32-bit SId
//        char Ext;	///Extension
//    }
//
//    public class FIG_TYPE0_Ext0{
//        short Eid;              ///Ensemble Id
//        char  Country_ID;       ///Country Id
//        long Ensemble_Ref;     ///Ensemble reference
//        char  Change_flag;      ///Change flag. 00:no, 01:sub-channel, 10:service, 11:sub-channel & service
//        char  AI_flag;          ///Alarm announcement
//        char  CIF_Count0;       ///High part module-20 counter
//        char  CIF_Count1;       ///Low part module-250 counter
//        char  Occurence_Change; ///Indicate the value of the lower part of the CIF counter from which the new configuration
//    }
//
//    public class FIG_TYPE0_Ext1{
//        char  SubChid;         ///Sub-channel Id
//        long StartAdd;        ///Address the first CU of the sub-channel
//        char  S_L_form;        ///Indicate whether the short or the long form. 0:short, 1:long
//        long Size_Protection;
//        char  Table_sw;        ///Table 7 of UEP
//        char  Table_index;
//        char  Option;          ///000: data rate 8n kbit/s. 001: data rate 32n kbit/s
//        char  Protection_Level;
//        long Sub_ch_size;
//    }
//
//    public class FIG_TYPE0_Ext2_ser_comp_des{
//        char  TMID;      ///Transport mechanism Id
//        char  ASCTy;     ///Audio Service component type
//        char  SubChid;   ///Sub-channel Id
//        char  P_S;       ///Primary/Secondary
//        char  CA_flag;   ///CAS flag
//        char  DSCTy;     ///Data Service Component type
//        char  FIDCid;    ///FIDC Id
//        char  TCid;      ///FIDC Type component Id
//        char  Ext;       ///FIDC Extension
//        short SCid;      ///Service component Id
//    }
//
//    public class FIG_TYPE0_Ext2{
//        long Sid;         ///Service Id
//        char  Country_id;  ///Country Id
//        long Service_ref; ///Service reference
//        char  ECC;         ///Extended country code
//        char  Local_flag;  ///Local flag
//        char  CAID;        ///Conditional Access Id
//        char  Num_ser_comp;///Number of service components
//        FIG_TYPE0_Ext2_ser_comp_des[] svr_comp_des = new FIG_TYPE0_Ext2_ser_comp_des[MAX_SERV_COMP];
//    }
//
//    public class FIG_TYPE0_Ext3{
//        short SCid;        ///Service Component Id
//        char  CA_Org_flag; ///Conditional Access flag
//        char  DG_flag;     ///Data Group flag
//        char  DSCTy;       ///Data Service Component type
//        char  SubChid;     ///Sub-channel Id
//        short Packet_add;  ///Packet address
//        short CA_Org;      ///Conditional Access Organization
//    }
//
//    public class FIG_TYPE0_Ext4{
//        char  M_F;         ///0:MSC and SubChId, 1:FIC and FIDCId
//        char  SubChid;
//        char  FIDCid;
//        short CA_Org;
//    }
//
//    public class FIG_TYPE0_Ext5{
//        char  L_S_flag;     ///0:short form, 1:long form
//        char  MSC_FIC_flag; ///0:MSC in stream mode, 1:FIC
//        char  SubChid;
//        char  FIDCid;
//        short SCid;
//        char  Language;     ///Audio/data service component language (Ex. Korean = 0x65)
//    }
//
//    public class FIG_TYPE0_Ext6{
//        char  id_list_flag;
//        char  LA;
//        char  S_H;
//        char  ILS;
//        long LSN;
//        char  id_list_usage;
//        char  idLQ;
//        char  Shd;
//        char  Num_ids;
//        short[] id = new short[12];
//        char[] ECC = new char[12];
//        long[] Sid = new long[12];
//    }
//
//    public class FIG_TYPE0_Ext8{
//        long Sid;
//        char  Ext_flag;
//        char  SCidS;
//        char  L_S_flag;
//        char  MSC_FIC_flag;
//        char  SubChid;
//        char  FIDCid;
//        long SCid;
//    }
//
//    public class FIG_TYPE0_Ext9{
//        char  Ext_flag;
//        char  LTO_unique;
//        char  Ensemble_LTO;   ///Ensemble Local Time Offset
//        char  Ensemble_ECC;   ///Ensemble Extended Country Code
//        char  Inter_Table_ID; ///International Table ID
//        char  Num_Ser;        ///Number of Service
//        char  LTO;            ///Local Time Offset
//        char  ECC;            ///Extended Country Code
//        long[] Sid = new long[11];
//    }
//
//    public class FIG_TYPE0_Ext10{
//        long MJD;           ///Modified Julian Date
//        char  LSI;           ///Leap Second Indicator
//        char  Conf_ind;      ///Confidence Indicator
//        char  UTC_flag;
//        long UTC;           ///Co-ordinated Universal Time
//        char  Hours;
//        char  Minutes;
//        char  Seconds;
//        short Milliseconds;
//    }
//
//    public class FIG_TYPE0_Ext11{
//        char  GATy;
//        char  G_E_flag;
//        char  Upper_part;
//        char  Lower_part;
//        char  length_TII_list;
//        char[]  Mainid = new char[12];
//        char  Length_Subid_list;
//        char[]  Subid = new char[36];
//        long Latitude_Coarse;
//        long Longitude_coarse;
//        long Extent_Latitude;
//        long Extent_Longitude;
//    }
//
//    public class FIG_TYPE0_Ext13{
//        long Sid;
//        char  SCidS;
//        char  Num_User_App;
//        short[] User_APP_Type = new short[6];
//        char[]  User_APP_data_length = new char[6];
//        char  CA_flag;
//        char  CA_Org_flag;
//        char  X_PAD_App_Ty;
//        char  DG_flag;
//        char  DSCTy;
//        short CA_Org;
//        char[]  User_APP_data = new char[24];
//    }
//
//    public class FIG_TYPE0_Ext14{
//        char  SubChid;     ///Sub-channel Id
//        char  FEC_scheme;
//    }
//
//    public class FIG_TYPE0_Ext16{
//        short Sid;
//        short PNum;
//        char  Continuation_flag;
//        char  Update_flag;
//        short New_Sid;
//        short New_PNum;
//    }
//
//    public class FIG_TYPE0_Ext17{
//        short Sid;        ///Service Id
//        char  S_D;        ///Static or Dynamic
//        char  P_S;        ///Primary or Secondary
//        char  L_flag;     ///Language Flag
//        char  CC_flag;    ///Complimentary Code Flag
//        char  Language;   ///language of Audio
//        char  Int_code;   ///Basic Program Type
//        char  Comp_code;  ///Specific Program Type
//    }
//
//    public class FIG_TYPE0_Ext18{
//        short Sid;
//        short ASU_flags;
//        char  Num_clusters;
//        char[]  Cluster_ID = new char[23];
//    }
//
//    public class FIG_TYPE0_Ext19{
//        char  Cluster_ID;
//        short ASW_flags;
//        char  New_flag;
//        char  Region_flag;
//        char  SubChid;
//        char  Regionid_Lower_Part;
//    }
//
//    public class FIG_TYPE0_Ext21{
//        short ResionID;
//        char  Length_of_FI_list;
//        short id_field;
//        char  R_M;
//        char  Continuity_flag;
//        char  Length_Freq_list;
//        char[]  Control_field = new char[5];
//        char[]  id_field2 = new char[4];
//        long[] Freq_a = new long[5];
//        char[]  Freq_b = new char[17];
//        short[] Freq_c = new short[8];
//        short[] Freq_d = new short[7];
//    }
//
//    public class FIG_TYPE0_Ext22{
//        char  M_S;
//        char  Mainid;
//        long Latitude_coarse;
//        long Longitude_coarse;
//        char  Latitude_fine;
//        char  Longitude_fine;
//        char  Num_Subid_fields;
//        char[]  Subid = new char[4];
//        short[] TD = new short[4];
//        short[] Latitude_offset = new short[4];
//        short[] Longitude_offset = new short[4];
//    }
//
//    public class FIG_TYPE0_Ext24{
//        long Sid;
//        char  CAid;
//        char  Number_Eids;
//        short[] Eid = new short[12];
//    }
//
//    public class FIG_TYPE0_Ext25{
//        long Sid;
//        long ASU_flag;
//        char  Number_Eids;
//        char[]  Eid = new char[12];
//    }
//
//    public class FIG_TYPE0_Ext26{
//        char  Cluster_id_Current_Ensemble;
//        long Asw_flags;
//        char  New_flag;
//        char  Region_flag;
//        char  Region_id_current_Ensemble;
//        long Eid_Other_Ensemble;
//        char  Cluster_id_other_Ensemble;
//        char  Region_id_Oter_Ensemble;
//    }
//
//    public class FIG_TYPE0_Ext27{
//        long Sid;
//        char  Number_PI_Code;
//        long[] PI = new long[12];
//    }
//
//    public class FIG_TYPE0_Ext28{
//        char  Cluster_id_Current_Ensemble;
//        char  New_flag;
//        char  Region_id_Current_Ensemble;
//        long PI;
//    }
//
//    public class FIG_TYPE0_Ext31{
//        long FIG0_flag_field;
//        char  FIG1_flag_field;
//        char  FIG2_flag_field;
//    }
//
//    public class FIG_TYPE5{
//        char D1;
//        char D2;
//        char TCid;
//        char Ext;
//    }
//
//    public class FIG_TYPE5_Ext0{
//        char  SubChid;
//        short Packet_add;
//        char  F1;
//        char  F2;
//        short LFN;
//        char  F3;
//        short Time;
//        char  CAid;
//        short CA_Org;
//        long Paging_user_group;
//    }
//
//    public class FIG_TYPE5_Ext1{
//        long[] TMC_User_Message = new long[30];
//        short[] TMC_System_Message = new short[30];
//    }
//
//    public class FIG_EWS_Region{
//        char[] Sub_Region = new char[11];
//    }
//
//    public class FIG_TYPE5_Ext2{
//        char  current_segmemt;
//        char  total_segmemt;
//        char  Message_ID;
//        char[]  category = new char [4];
//        char  priority;
//        long time_MJD;
//        char  time_Hours;
//        char  time_Minutes;
//        char  region_form;
//        char  region_num;
//        char  Rev;
//        FIG_EWS_Region[] Region = new FIG_EWS_Region[15];
//        char[]  data = new char[409];
//    }
//
//    public class FIG_TYPE6{
//        char  C_N;
//        char  OE;
//        char  P_D;
//        char  LEF;
//        char  ShortCASysId;
//        long Sid;
//        short CASysId;
//        short CAIntChar;
//    }
//
//    FIG_DATA fig_data;
//    char[] FIC_BIT_MASK = {0x0,0x80,0xC0,0xE0,0xFF,0xF8,0xFC,0xFE};
//
//    public long GetBytes(char cnt, char[] Res){
//
//        int i;
//        for(i = 0 ; i < cnt;i++){
//            Res[i] = fig_data.data[fig_data.byte_cnt];
//            fig_data.byte_cnt++;
//        }
//        return STRUCT_LIST.XCD_OK;
//    }
//
//    public long Get_Bits(char cnt, char[] Res){
//        Res[0] = (char) (fig_data.data[fig_data.byte_cnt]&(FIC_BIT_MASK[cnt]>>fig_data.bit_cnt));
//        Res[0] = (char) ((Res[0])>>(8-cnt-fig_data.bit_cnt));
//        fig_data.bit_cnt += cnt;
//        if(fig_data.bit_cnt == 8){
//            fig_data.byte_cnt++;
//            fig_data.bit_cnt = 0;
//        }
//        return STRUCT_LIST.XCD_OK;
//    }
//
//    // void* Get_FIG_ EXT LIST ~ FIG_PARSER
//
//    public long Get_FIG_Init(char[] data){
//        fig_data.data = data;
//        fig_data.byte_cnt = 0;
//        fig_data.bit_cnt = 0;
//        return STRUCT_LIST.XCD_OK;
//    }
//
//
//
//    public long Get_FIG_Header(FIG_DATA[] fig_data){
//
//
//        char[] fig_data_length = ("" + fig_data[0].length).toCharArray();
//
//        GetBytes((char) 1,fig_data_length);//괜찮은지 확인
//        if(fig_data.length == XCD_FIB_END_MARKER)
//            return XCD_FIB_END_MARKER;
//        fig_data[0].type = (char) (fig_data[0].length>>5);//확인 해봐야함
//        fig_data[0].length = (char) (fig_data[0].length & 0x1f);
//        return STRUCT_LIST.XCD_OK;
//    }
//
//    public long FIB_INIT_DEC(char[] fib_ptr){
//        char fib_cnt = 0;
//        char fib_cmd = 1;
//
//        while(fib_cnt < 30){
//            Get_FIG_Init(fib_ptr[fib_cnt]);
//
//            if(Get_FIG_Header(fig_data) == XCD_FIB_END_MARKER)
//                return STRUCT_LIST.XCD_OK;
//            if(fig_data.length == 0)
//                return STRUCT_LIST.XCD_FAIL;
//
//            switch (fig_data.type){
//                case 0:
//                    break;
//                case 1:
//                    break;
//                case 2:
//                    break;
//                case 3:
//                    break;
//                case 4:
//                    break;
//                case 5:
//                    break;
//                case 6:
//                    break;
//                case 7:
//                    break;
//
//            }
//
//        }
//    }
//
//
//}
