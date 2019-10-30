package com.a3top.testmakesound;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * 生成口令集
 * 本例为 鼠标位移 定位方式
 * roworcolumn 默认列接常闭端
 * 本案继电器板为济南因诺
 * Created by Administrator on 2017/11/21.
 * 
 * version 1.1 2018/11/29 合规修改
 * 时长560改为562，1690改为1687,结束码改为652单峰
 */

public class Command {
    private boolean mouse_flag;//是否是鼠标位移方式
    private String containerid;
    //private String cardid;
    //private int[] rowOrColumn;//行、列切换，常闭在列
    private int[] reversal ;//正反转切换
    //private int rowdown;//跨过行光电开关
    private int row_eye;//行继电器
    private int[] column_eye;//列继电器(两列对应一个继电器，按顺序来)
    private int[] rowValue;//鼠标检测位移，行值

    public String getContainerid() {
        return containerid;
    }

    public void setContainerid(String containerid) {
        this.containerid = containerid;
    }

    public int[] getReversal() {
        return reversal;
    }

    public void setReversal(int[] reversal) {
        this.reversal = reversal;
    }

    public int getRow_eye() {
        return row_eye;
    }

    public void setRow_eye(int row_eye) {
        this.row_eye = row_eye;
    }

    public int[] getColumn_eye() {
        return column_eye;
    }

    public void setColumn_eye(int[] column_eye) {
        this.column_eye = column_eye;
    }

    public void setRowValue(int[] rowvalue){this.rowValue=rowvalue;}
    public int[] getRowValue(){return  rowValue;}
    //原始继电器指令集
    //本案为红外指令，自锁，一按开，再按关，
    //ray_basic(0)为全关，1～8分别对应1～8继电器
    public static int[] ray_base(int x){
        int[] pattern={9000, 4500, 562};
        switch (x) {
            case 0://全关

                pattern=new int[]{9000, 4500,
                        562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 1687,
                        562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 562,
                        562, 562, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 562, 562, 562, 562, 562,
                        562, 1687, 562, 562, 562, 562, 562, 562, 562, 562, 562, 1687, 562, 1687, 562, 1687,
                        562};
                break;

            case 1:
                pattern = new int[]{9000, 4500,
                        562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 1687,
                        562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 562,
                        562, 562, 562, 1687, 562, 562, 562, 1687, 562, 562, 562, 562, 562, 562, 562, 562,
                        562, 1687, 562, 562, 562, 1687, 562, 562, 562, 1687, 562, 1687, 562, 1687, 562, 1687,
                        562};
                break;
            case 2:
                pattern = new int[]{9000, 4500,
                        562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 1687,
                        562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 562,
                        562, 1687, 562, 1687, 562, 562, 562, 1687, 562, 1687, 562, 562, 562, 562, 562, 562,
                        562, 562, 562, 562, 562, 1687, 562, 562, 562, 562, 562, 1687, 562, 1687, 562, 1687,
                        562};
                break;

            case 3:
                pattern = new int[]{9000, 4500,
                        562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 1687,
                        562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 562,
                        562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 562, 562, 562, 562, 562,
                        562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 1687, 562, 1687, 562, 1687,
                        562};

                break;
            case 4:
                pattern = new int[]{9000, 4500,
                        562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 1687,
                        562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 562,
                        562, 650, 562, 650, 562, 1687, 562, 1687, 562, 650, 562, 562, 562, 562, 562, 562,
                        562, 1687, 562, 1687, 562, 650, 562, 562, 562, 1687, 562, 1687, 562, 1687, 562, 1687,
                        562};
                break;
            case 5:
                pattern = new int[]{9000, 4500,
                        562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 1687,
                        562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 562,
                        562, 1687, 562, 650, 562, 1687, 562, 1687, 562, 650, 562, 562, 562, 562, 562, 562,
                        562, 650, 562, 1687, 562, 650, 562, 562, 562, 1687, 562, 1687, 562, 1687, 562, 1687,
                        562};
                break;
            case 6:
                pattern = new int[]{9000, 4500,
                        562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 1687,
                        562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 562,
                        562, 650, 562, 1687, 562, 1687, 562, 1687, 562, 650, 562, 562, 562, 562, 562, 562,
                        562, 1687, 562, 650, 562, 650, 562, 562, 562, 1687, 562, 1687, 562, 1687, 562, 1687,
                        562};
                break;
            case 7:
                pattern = new int[]{9000, 4500,
                        562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 1687,
                        562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 562,
                        562, 562,562, 562,562, 562,562, 562,562, 562,562, 562,562, 562,562, 562,
                        562, 1687,562, 1687,562, 1687,562, 1687,562, 1687,562, 1687,562, 1687,562, 1687,
                        562};
                break;
            case 8:
                pattern = new int[]{9000, 4500,
                        562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 562, 1687,
                        562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 562,
                        562, 1687, 562, 1687, 562, 1687, 562, 1687, 562, 650, 562, 562, 562, 562, 562, 562,
                        562, 650, 562, 650, 562, 650, 562, 562, 562, 1687, 562, 1687, 562, 1687, 562, 1687,
                        562};
                break;
        }
        return pattern;
    }

    //行列口令

    private int[] row_on(){
        return new int[]{row_eye};
    }
    private int[] row_off(){
        return new int[]{row_eye};
    }


    private int[] revrow_on(){
           return new int[]{reversal[0],reversal[1],row_eye};
    }
    private int[] revrow_off(){
        return new int[]{row_eye,reversal[0],reversal[1]};
    }



    private int[] column_on(int cc){
           return new int[]{column_eye[cc]};
    }
    private int[] column_off(int cc){
        return new int[]{column_eye[cc]};
    }

    private int[] revcolumn_on(int cc){
        return new int[]{reversal[0],reversal[1],column_eye[cc]};

    }
    private int[] revcolumn_off(int cc){
        return new int[]{column_eye[cc],reversal[0],reversal[1]};
    }

    //获得box口令,鼠标位移定位方式
    //boxid 为1～60的int
    public String getcommand(){
        //先检验必要参数，不能为空，换向继电器必须2个
        if (row_eye==0 || rowValue==null|| column_eye == null || reversal == null ) return "error";
        if (reversal.length!=2) return "error";


        int rows=rowValue.length;
        int cols=column_eye.length*2;//(一个继电器控制两列)

        JSONArray jsonArray = new JSONArray();
        for (int i=1;i<rows*cols;i++){
            int cc = i%6;
            switch(cc){
                case 0:case 1:
                    cc=0;
                    break;
                case 2:case 3:
                    cc=1;
                    break;
                case 4:case 5:
                    cc=2;
                    break;
                default:
                    Log.d("support/command","cc=i%6 error");
            }
            try{
                JSONObject json = new JSONObject();
                json.put("boxid",i);
                JSONObject json_cmd = new JSONObject();

                json_cmd.put("rowon",row_on());
                json_cmd.put("rowoff",row_off());
                json_cmd.put("colon",column_on(cc));
                json_cmd.put("coloff",column_off(cc));

                json_cmd.put("revcolon",revcolumn_on(cc));
                json_cmd.put("revcoloff",revcolumn_off(cc));
                json_cmd.put("revrowon",revrow_on());
                json_cmd.put("revrowoff",revrow_off());

                json_cmd.put("rowvalue",rowValue[i]);
                json.put("command",json_cmd.toString());

                jsonArray.put(json);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return jsonArray.toString();
    }


    static  public void DelayTime(int dwTimeMS) {
        //Thread.yield();
        long StartTime, CheckTime;

        if(0==dwTimeMS) {
            Thread.yield();
            return;
        }
        //Returns milliseconds running in the current thread
        StartTime = System.currentTimeMillis();
        do {
            CheckTime=System.currentTimeMillis();
            Thread.yield();
        } while( (CheckTime-StartTime)<=dwTimeMS);
    }
}
