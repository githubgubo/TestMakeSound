package com.a3top.testmakesound;

import android.app.Application;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.SoundPool;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {
Context mContext;
    int count;
String file;
SoundPool sp;
    int outLine=0;
    boolean stop = true;
    int nowSp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;
        count=1;

        sp = initSoundPool();
        /*
        //载入完成时触发，载入与此最好隔开一段时间，否则该属性尚未起作用载入已经完成了
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                Log.d("soundPoll","play"+i+".wav");
                sp.play(i,1,1,0,0,1);
            }
        });
        */
        //sp.load(mContext,R.raw.stero_0,0);
        sp.load(mContext,R.raw.a1h,1);
        sp.load(mContext,R.raw.a2h,2);
        sp.load(mContext,R.raw.a3h,3);
        sp.load(mContext,R.raw.a4h,4);
        sp.load(mContext,R.raw.a5h,5);
        sp.load(mContext,R.raw.a6h,6);
        sp.load(mContext,R.raw.a7h,7);
        sp.load(mContext,R.raw.a8h,8);
        sp.load(mContext,R.raw.a0h,9);
        //载入完成时触发播放

        //irUtil.SignalProcessor(pattern0, Environment.getExternalStorageDirectory() + "/" +"shabi.pcm");
        //irUtil.SignalProcessor(pattern0,"");
//生成
findViewById(R.id.create).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        //生成音频pcm
        IRUtil irUtil = new IRUtil();
        for (int i=0;i<9;i++){

            irUtil.SignalProcessor(Command.ray_base(i), Environment.getExternalStorageDirectory() + "/" +i+".pcm");
            Log.d("creat","saved:"+Environment.getExternalStorageDirectory() + "/" +i+".pcm");

        }
        //pcm转wav
        Pcm2WavUtil p2w = new Pcm2WavUtil(44100,AudioFormat.CHANNEL_IN_STEREO,AudioFormat.ENCODING_PCM_16BIT);
        String filePathName = Environment.getExternalStorageDirectory() + "/";
        String inPath;
        String outPath;
        for(int j=0;j<9;j++){
            inPath = filePathName + j +".pcm";
            outPath = filePathName + j +".wav";
            Log.d("pcm2wavUtil","convert "+j+".pcm to "+ j +".wav");
            p2w.pcmToWav(inPath,outPath);
        }

    }
});
//响
findViewById(R.id.sound).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        try {
            if(stop) {
                while (stop){
                    nowSp=playPool(5);
                    Log.d("nowPlay:",String.valueOf(nowSp));
                    Thread.sleep(1000);
                    stop=false;
                }
            }else{
                sp.stop(nowSp);
                Log.d("nowPause:",String.valueOf(nowSp));
                stop=true;
            }
        }catch (Exception e){
            Log.e("play soundPool",e.toString());
        }

    }
    });
//响生成的wav
findViewById(R.id.sound2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("soundPoll","load 5.wav");
                sp.load(Environment.getExternalStorageDirectory() + "/5.wav",9);

            }
        });
//切换通道
findViewById(R.id.set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outLine ++;
                Sound sound = new Sound();
                switch (outLine){
                    case 0:
                        sound.changeToSpeaker();
                        break;
                    case 1:
                        sound.changeToHeadset();
                        break;
                    case 2:
                        sound.changeToReceiver();
                        outLine=-1;
                        break;
                }
            }
        });

        //各数值按键
        findViewById(R.id.R1).setOnClickListener(listener);
        findViewById(R.id.R2).setOnClickListener(listener);
        findViewById(R.id.R3).setOnClickListener(listener);
        findViewById(R.id.R4).setOnClickListener(listener);
        findViewById(R.id.R5).setOnClickListener(listener);
        findViewById(R.id.R6).setOnClickListener(listener);
        findViewById(R.id.R7).setOnClickListener(listener);
        findViewById(R.id.R8).setOnClickListener(listener);
        findViewById(R.id.R9).setOnClickListener(listener);
    }
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.R1:
                    //sp.play(0,1,1,0,0,1);
                    playPool(1);
                    break;
                case R.id.R2:
                    //sp.play(2,1,1,0,0,1);
                    playPool(2);
                    break;
                case R.id.R3:
                    //sp.play(3,1,1,0,0,1);
                    playPool(3);
                    break;
                case R.id.R4:
                    //sp.play(4,1,1,0,0,1);
                    playPool(4);
                    break;
                case R.id.R5:
                    //sp.play(5,1,1,0,0,1);
                    playPool(5);
                    break;
                case R.id.R6:
                    //sp.play(6,1,1,0,0,1);
                    playPool(6);
                    break;
                case R.id.R7:
                    //sp.play(7,1,1,0,0,1);
                    playPool(7);
                    break;
                case R.id.R8:
                    //sp.play(8,1,1,0,0,1);
                    playPool(8);
                    break;
                case R.id.R9:
                    sp.play(9,1,1,0,0,1);
                    break;
            }
        }
    };

    private class Sound{

        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
/**
 * 切换到外放
 */
        public void changeToSpeaker(){

            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(true);
        }

/**
 * 切换到耳机模式
 */
        public void changeToHeadset(){
            audioManager.setSpeakerphoneOn(false);
        }

/**
 * 切换到听筒
 */
        public void changeToReceiver(){
            audioManager.setSpeakerphoneOn(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            } else {
                audioManager.setMode(AudioManager.MODE_IN_CALL);
            }
        }
    }


private  void  playPcm(){
    file=Environment.getExternalStorageDirectory() + "/" +3+".pcm";
    try{
        PlayRecord();
    }catch (Exception e){
        Log.d("play",e.toString());
    }
}
private int playPool(int m){
    Log.d("soundPoll","play soundPool "+m);
    return sp.play(m,1,1,0,0,1);
    }
//播放文件
    public void PlayRecord() throws IOException {
        if(file == null){
            return;
        }
//读取文件
        int musicLength = (int) (file.length() / 2);
        short[] music = new short[musicLength];
        //try {
            InputStream is = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);
            int i = 0;
            while (dis.available() > 0) {
                music[i] = dis.readShort();
                i++;
            }
            dis.close();
            AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                    44100, AudioFormat.CHANNEL_OUT_STEREO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    musicLength * 2,
                    AudioTrack.MODE_STREAM);
            audioTrack.play();
            audioTrack.write(music, 0, musicLength);
            audioTrack.stop();
        //} catch (Throwable t) {
         //   Log.e("play", "播放失败"+t.toString());
        //}
    }

    private SoundPool initSoundPool(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //   The content of the comments need API-23
            SoundPool.Builder builder=new SoundPool.Builder();
            builder.setMaxStreams(12);

            AudioAttributes.Builder audioBuilder=new AudioAttributes.Builder();
            audioBuilder.setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN);
            audioBuilder.setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED);

            audioBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            audioBuilder.setUsage(AudioAttributes.USAGE_MEDIA);
            AudioAttributes attributes = audioBuilder.build();

            builder.setAudioAttributes(attributes);
            SoundPool soundPool = builder.build();
            return soundPool;
        }else{
            //      return new SoundPool(10, AudioManager.STREAM_NOTIFICATION, 5);
            SoundPool spd = new SoundPool(12,AudioManager.STREAM_MUSIC,0);
            return  spd;
        }
    }

    private float setratio(){
        // 获取音频服务然后强转成一个音频管理器,后面方便用来控制音量大小用
        AudioManager am = (AudioManager)MainActivity.this.getSystemService(MainActivity.this.AUDIO_SERVICE);
// 设定调整音量为媒体音量,当暂停播放的时候调整音量就不会再默认调整铃声音量了，
        MainActivity.this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
// 获取最大音量值
        float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
// 不断获取当前的音量值
        float audioCurrentVolumn = am.getStreamVolume(AudioManager.STREAM_MUSIC);
//最终影响音量
        float ratio = audioCurrentVolumn/audioMaxVolumn;
        return ratio;
    } //音量控制

@Override
    protected void  onDestroy(){
    super.onDestroy();

    sp.release();
}
}
