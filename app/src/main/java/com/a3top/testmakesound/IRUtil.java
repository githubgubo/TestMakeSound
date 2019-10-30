package com.a3top.testmakesound;

import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

/**
 * 红外遥控编码声音波形生成工具
 * Created by Administrator on 2018/11/12.
 */

public class IRUtil {
    final private int sampleRate = 44100;//采样频率
    final private int freqOfTone=19000;//发声频率
    private int buffSize;
    private byte[] genSignal;
    private byte[] genSpace;

    public IRUtil(){
        buffSize = AudioTrack.getMinBufferSize(sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT) * 4;

        genSignal = new byte[buffSize];
        genSpace = new byte[buffSize];
        int[] pattern = new int[]{9000, 4500,
                560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 560, 1690,
                560, 1690, 560, 1690, 560, 1690, 560, 1690, 560, 1690, 560, 1690, 560, 1690, 560, 560,
                560, 650, 560, 650, 560, 1690, 560, 1690, 560, 650, 560, 560, 560, 560, 560, 560,
                560, 1690, 560, 1690, 560, 650, 560, 560, 560, 1690, 560, 1690, 560, 1690, 560, 1690,
                9000, 2250, 2250, 94000, 9000, 2250, 2250, 94000};
        for (int j = 0; j < buffSize; ) {
            double dVal = Math.sin(2 * Math.PI * ((double) j) / 4.0
                    / (((double) sampleRate) / ((double) freqOfTone)));
            //final short val = (short) ((dVal * 32767));//上下双侧振幅，在红外中无效
            //final short val_minus = (short) -val;//上下双侧振幅，反相数据

            final short val = (short) (16383 +(dVal * 16383));//左声道数据，单侧振幅
            final short val_minus = (short) (32766-val);//反相数据
            Log.d("main2","j="+j);
            Log.d("main2","val:"+val);
            Log.d("main2","val_minus:"+val_minus);
            // in 16 bit wav PCM, first byte is the low order byte
            genSpace[j] = 0;
            genSignal[j++] = (byte) (val & 0x00ff);
            genSpace[j] = 0;
            genSignal[j++] = (byte) ((val & 0xff00) >>> 8);
            genSpace[j] = 0;
            //genSignal[j++] = (byte) (val_minus & 0x00ff);//右声道反相
            genSignal[j++] = (byte) (val & 0x00ff);//左右声道相同
            genSpace[j] = 0;
            //genSignal[j++] = (byte) ((val_minus & 0xff00) >>> 8);//右声道反相
            genSignal[j++] = (byte) ((val & 0xff00) >>> 8);//左右声道相同
        }
    }

    /**
     * 生成声音波形
     * @param pattern0  毫秒载波序列
     * @param target    生成的声音文件路径和名称（后缀名必须为.pcm或.wav），可以为空，默认以时间为名，存在sd卡根目录
     */
    public void SignalProcessor(int[] pattern0,String target) {

        String filePathName;
        if(target == null || "".equals(target)){
            filePathName = Environment.getExternalStorageDirectory() + "/" +
                DateFormat.format("yyyy-MM-dd_kk-mm-ss", new Date().getTime());
        }else {
            if(target.lastIndexOf('.')>0) {
                filePathName = target.substring(0, target.lastIndexOf('.'));
            }else{
                filePathName = target;
            }
        }


/*
        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT, buffSize,
                AudioTrack.MODE_STREAM);


        boolean signal = true;
        int count = 0;
        for (Integer d : pattern0) {
            final int stop = (int) (((double) (d * sampleRate)) / 1000000.0) * 4;
            if (signal){
                Log.d("pattern","signal"+d);
                for (int i = 0; i < stop; ) {
                    if (stop - i < buffSize)
                        count = audioTrack.write(genSignal, 0, stop - i);
                    else
                        count = audioTrack.write(genSignal, 0, buffSize);
                    if (count > 0)
                        i += count;
                }}
            else{
                Log.d("pattern","space"+d);
                for (int i = 0; i < stop; ) {
                    if (stop - i < buffSize)
                        count = audioTrack.write(genSpace, 0, stop - i);
                    else
                        count = audioTrack.write(genSpace, 0, buffSize);
                    if (count > 0)
                        i += count;
                }}

            signal = !signal;//高电平与0间隔而出

        }
         audioTrack.play();
        audioTrack.stop();
        audioTrack.release();
        */
        File fl = new File(filePathName + ".pcm");
        if (fl.exists()) {
            Log.d("file",filePathName + ".pcm deleted");
            fl.delete();
        }

        try{
            FileOutputStream fileOutputStream = new FileOutputStream(fl,true);
            boolean signal = true;

            for (int d : pattern0) {
                //d*sampleRate 940000*44100 超出int范围了
                final int stop = (int) (((float)d * sampleRate) / 1000000.0) * 4;
                //final int stop = (int) (((double) (d * sampleRate)) / 1000000.0)*4 ;
                Log.d("IRUtil","bufferSize = "+buffSize);
                Log.w("IRUtil","d in Pattern "+d+" : stop: "+stop);
                if (signal) {
                    Log.d("pattern", "signal" + d);
                    for (int i = 0; i < stop; ) {
                        if (stop - i < buffSize){
                            Log.d("signal", "stop - i < buffSize; i=" + i);
                            fileOutputStream.write(Arrays.copyOfRange(genSignal, 0, stop - i));
                            i=stop;
                        }else{
                            fileOutputStream.write(Arrays.copyOfRange(genSignal, 0, buffSize));
                            Log.d("signal", "stop - i > buffSize; i=" + i);
                            i+=buffSize;}
                    }
                } else {
                    Log.d("pattern", "space" + d);
                    for (int i = 0; i < stop; ) {
                        if (stop - i < buffSize) {
                            Log.d("space", "stop - i < buffSize; i=" + i);
                            fileOutputStream.write(Arrays.copyOfRange(genSpace, 0, stop - i));
                            i=stop;
                        }else{
                            fileOutputStream.write(Arrays.copyOfRange(genSpace, 0, buffSize));
                            Log.d("space", "stop - i > buffSize; i=" + i);
                            i+=buffSize;
                        }
                    }
                }
                signal = !signal;//高电平与0间隔而出
            }
            fileOutputStream.close();

        }catch (Exception e){
            Log.d("save",e.toString());
        }

        if(!(target==null || "".equals(target))){

            String suf = target.substring(target.lastIndexOf('.'));
            if(suf.equals(".wav")){
                pcm2wav(filePathName+".pcm",filePathName+".wav");
                fl.delete();
            }
        }
    }
    /**
     * pcm文件转wav文件
     * @param src pcm文件地址及名称
     * @param target wav文件地址及名称
     *
     */
    private void pcm2wav(String src, String target) {
        try {
            FileInputStream fis = new FileInputStream(src);
            File flo = new File(target);
            FileOutputStream fos = new FileOutputStream(flo);

            //计算长度
            byte[] buf = new byte[1024 * 4];
            int size = fis.read(buf);
            int PCMSize = 0;
            while (size != -1) {
                PCMSize += size;
                size = fis.read(buf);
            }
            fis.close();


            //填入参数，比特率等等。这里用的是16位双声道 44100hz
            WaveHeader header = new WaveHeader();
            //长度字段 = 内容的大小（PCMSize) + 头部字段的大小(不包括前面4字节的标识符RIFF以及fileLength本身的4字节)
            header.fileLength = PCMSize + (44 - 8);
            header.FmtHdrLeth = 16;
            header.BitsPerSample = 16;
            header.Channels = 2;//声道
            header.FormatTag = 0x0001;//PCM/uncompressed 未压缩
            header.SamplesPerSec = 44100;//采样频率
            header.BlockAlign = (short) (header.Channels * header.BitsPerSample / 8);
            header.AvgBytesPerSec = header.BlockAlign * header.SamplesPerSec;
            header.DataHdrLeth = PCMSize;

            byte[] h = header.getHeader();

            //assert h.length == 44; //WAV标准，头部应该是44字节
            if (BuildConfig.DEBUG && h.length != 44) {
                throw new Exception("wave head isn't 44 bits");
            }
            //write header
            fos.write(h, 0, h.length);
            //write data stream
            fis = new FileInputStream(src);
            size = fis.read(buf);
            while (size != -1) {
                fos.write(buf, 0, size);
                size = fis.read(buf);
            }
            fis.close();
            fos.close();
            System.out.println("Convert OK!");
        }catch (Exception e){
            Log.d("pcm2wav error:",e.toString());
        }
    }

        /*
        WavHeader辅助类。用于生成头部信息。
         */

    private class WaveHeader {
        private final char fileID[] = {'R', 'I', 'F', 'F'};
        private int fileLength;
        private char wavTag[] = {'W', 'A', 'V', 'E'};
        private char FmtHdrID[] = {'f', 'm', 't', ' '};
        private int FmtHdrLeth;
        private short FormatTag;
        private short Channels;
        private int SamplesPerSec;
        private int AvgBytesPerSec;
        private short BlockAlign;
        private short BitsPerSample;
        private char DataHdrID[] = {'d','a','t','a'};
        private int DataHdrLeth;

        private byte[] getHeader() throws IOException {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            WriteChar(bos, fileID);
            WriteInt(bos, fileLength);
            WriteChar(bos, wavTag);
            WriteChar(bos, FmtHdrID);
            WriteInt(bos,FmtHdrLeth);
            WriteShort(bos,FormatTag);
            WriteShort(bos,Channels);
            WriteInt(bos,SamplesPerSec);
            WriteInt(bos,AvgBytesPerSec);
            WriteShort(bos,BlockAlign);
            WriteShort(bos,BitsPerSample);
            WriteChar(bos,DataHdrID);
            WriteInt(bos,DataHdrLeth);
            bos.flush();
            byte[] r = bos.toByteArray();
            bos.close();
            return r;
        }

        private void WriteShort(ByteArrayOutputStream bos, int s) throws IOException {
            byte[] mybyte = new byte[2];
            mybyte[1] =(byte)( (s << 16) >> 24 );
            mybyte[0] =(byte)( (s << 24) >> 24 );
            bos.write(mybyte);
        }


        private void WriteInt(ByteArrayOutputStream bos, int n) throws IOException {
            byte[] buf = new byte[4];
            buf[3] =(byte)( n >> 24 );
            buf[2] =(byte)( (n << 8) >> 24 );
            buf[1] =(byte)( (n << 16) >> 24 );
            buf[0] =(byte)( (n << 24) >> 24 );
            bos.write(buf);
        }

        private void WriteChar(ByteArrayOutputStream bos, char[] id) {
            for (int i=0; i<id.length; i++) {
                char c = id[i];
                bos.write(c);
            }
        }
    }
}
