package com.a3top.testmakesound;
/**
 * 依照时间ms数组生成遥控波形文件
 */

import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

public class Main2Activity extends AppCompatActivity {
    final int sampleRate = 44100;//采样频率
    final int freqOfTone=19000;//发声频率
    // 一种交替的载波序列模式，通过毫秒测量
    int[] pattern0 = { 1901, 4453, 625, 1614, 625, 1588, 625, 1614, 625,
            442, 625, 442, 625, 468, 625, 442, 625, 494, 572, 1614,
            625, 1588, 625, 1614, 625, 494, 572, 442, 651, 442, 625,
            442, 625, 442, 625, 1614, 625, 1588, 651, 1588, 625, 442,
            625, 494, 598, 442, 625, 442, 625, 520, 572, 442, 625, 442,
            625, 442, 651, 1588, 625, 1614, 625, 1588, 625, 1614, 625,
            1588, 625, 48958 };

    final String filePathName = Environment.getExternalStorageDirectory() + "/" +
            DateFormat.format("yyyy-MM-dd_kk-mm-ss", new Date().getTime());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        findViewById(R.id.standardTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //用本地的pcm2Wav
                    //pcm2wav(filePathName+".pcm",filePathName+".wav");

                    //用pcm2wavUtil
                    Pcm2WavUtil p2w = new Pcm2WavUtil(44100,AudioFormat.CHANNEL_IN_STEREO,AudioFormat.ENCODING_PCM_16BIT);

                    String inPath = filePathName + ".pcm";
                    String outPath = filePathName + ".wav";

                    p2w.pcmToWav(inPath,outPath);
                }catch (Exception e){
                    Log.d("standard test",e.toString());
                }

            }
        });
        SignalProcessor();
    }



    public void SignalProcessor() {

        int buffSize = AudioTrack.getMinBufferSize(sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT) * 4;

        byte[] genSignal = new byte[buffSize];
        byte[] genSpace = new byte[buffSize];

        for (int j = 0; j < buffSize; ) {
            double dVal = Math.sin(2 * Math.PI * ((double) j) / 4.0
                    / (((double) sampleRate) / ((double) freqOfTone)));
            final short val = (short) ((dVal * 32767));
            final short val_minus = (short) -val;
            Log.d("main2","j="+j);
            Log.d("main2","val:"+val);
            Log.d("main2","val_minus:"+val_minus);
            // in 16 bit wav PCM, first byte is the low order byte
            genSpace[j] = 0;
            genSignal[j++] = (byte) (val & 0x00ff);
            genSpace[j] = 0;
            genSignal[j++] = (byte) ((val & 0xff00) >>> 8);
            genSpace[j] = 0;
            genSignal[j++] = (byte) (val_minus & 0x00ff);
            genSpace[j] = 0;
            genSignal[j++] = (byte) ((val_minus & 0xff00) >>> 8);
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

        try{
            FileOutputStream fileOutputStream = new FileOutputStream(fl,true);
            boolean signal = true;

            for (Integer d : pattern0) {
                final int stop = (int) (((double) (d * sampleRate)) / 1000000.0) * 4;
                if (signal) {
                    Log.d("pattern", "signal" + d);
                    for (int i = 0; i < stop; ) {
                        if (stop - i < buffSize){
                            fileOutputStream.write(Arrays.copyOfRange(genSignal, 0, stop - i));
                        i=stop;
                        }else{
                            fileOutputStream.write(Arrays.copyOfRange(genSignal, 0, buffSize));
                        i+=buffSize;}
                    }
                } else {
                    Log.d("pattern", "space" + d);
                    for (int i = 0; i < stop; ) {
                        if (stop - i < buffSize) {
                            fileOutputStream.write(Arrays.copyOfRange(genSpace, 0, stop - i));
                            i=stop;
                        }else{
                            fileOutputStream.write(Arrays.copyOfRange(genSpace, 0, buffSize));
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

    }

        private void pcm2wav(String src, String target) throws Exception {
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
            header.Channels = 2;
            header.FormatTag = 0x0001;
            header.SamplesPerSec = sampleRate;
            header.BlockAlign = (short)(header.Channels * header.BitsPerSample / 8);
            header.AvgBytesPerSec = header.BlockAlign * header.SamplesPerSec;
            header.DataHdrLeth = PCMSize;

            byte[] h = header.getHeader();

            assert h.length == 44; //WAV标准，头部应该是44字节
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
        }

        /*
        WavHeader辅助类。用于生成头部信息。
         */

        public class WaveHeader {
            public final char fileID[] = {'R', 'I', 'F', 'F'};
            public int fileLength;
            public char wavTag[] = {'W', 'A', 'V', 'E'};;
            public char FmtHdrID[] = {'f', 'm', 't', ' '};
            public int FmtHdrLeth;
            public short FormatTag;
            public short Channels;
            public int SamplesPerSec;
            public int AvgBytesPerSec;
            public short BlockAlign;
            public short BitsPerSample;
            public char DataHdrID[] = {'d','a','t','a'};
            public int DataHdrLeth;

            public byte[] getHeader() throws IOException {
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



