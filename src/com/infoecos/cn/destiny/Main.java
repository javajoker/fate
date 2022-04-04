package com.infoecos.cn.destiny;

import com.infoecos.cn.destiny.lib.TimezoneLocation;
import com.infoecos.cn.destiny.lib.预测.Report;
import com.infoecos.cn.destiny.lib.预测.utils.DestinyUtil;
import com.infoecos.cn.destiny.lib.预测.八字排盘;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws Exception {
        args = "6-15-1953 4:12 y".split(" ");
        if (args.length > 0 && args.length != 3 && args.length != 4) {
            help();
            return;
        }
        Date born = null;
        boolean isMale = true;
        TimezoneLocation location = new TimezoneLocation("");
        try {
            String sBorn = "", gender = "", timezone = "";
            if (args.length > 0) {
                sBorn = String.format("%s %s", args[0], args[1]);
                gender = args[2].toLowerCase();
                if (args.length == 4) timezone = args[3].toUpperCase();
            } else {
                BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("阳历生日（MM-dd-yyyy HH:mm）：");
                sBorn = buffer.readLine();
                System.out.print("男？（y/n）：");
                gender = buffer.readLine().trim().toLowerCase();
                System.out.println("时区（NZDT, IDLE, NZST, NZT, AESST, CST(ACSST), CADT, SADT, EST(EAST), GST, LIGT, CAST, SAT(SAST), WDT(AWSST), JST, KST, MT, WST(AWST), CCT, JT, IT, BT, EETDST, CETDST, EET, FWT, IST, MEST, METDST, SST, BST, CET, DNT, FST, MET, MEWT, MEZ, NOR, SET, SWT, WETDST, GMT, WET, WAT, NDT, ADT, NFT, NST, AST, EDT, CDT, EST, CST, MDT, MST, PDT, PST, YDT, HDT, YST, AHST, CAT, NT, IDLW, CCDT. 默认东八区）：");
                timezone = buffer.readLine().trim().toUpperCase();
            }
            DateFormat dfm = new SimpleDateFormat("MM-dd-yyyy HH:mm");
            born = dfm.parse(sBorn);
            isMale = "male".equals(gender) || "y".equals(gender);
            location = new TimezoneLocation(timezone);
        } catch (Exception e) {
            help();
        }
        八字排盘 destiny = new 八字排盘(born, isMale, location);
        destiny.行运排盘(false);

        System.out.println("=======================================");
        System.out.println(destiny.盘());

        for (Report r : Report.values()) {
            System.out.println(r.toString(destiny));
        }

//        DestinyUtil._VERBOSE(destiny);
    }

    private static void help() {
        System.out.println("fate <MM-dd-yyyy HH:mm> <male|female> [timezone(default:GMT+8)]");
    }
}
