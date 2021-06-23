package com.example.cinema.blImpl.talking;

import com.example.cinema.bl.talking.TtsTool;
import com.iflytek.cloud.speech.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;


public class XunFeiTtsTool implements TtsTool {

    private static Logger LOGGER = LoggerFactory.getLogger(XunFeiTtsTool.class);

    private Object lock = new Object();

    // 语音合成对象
    private SpeechSynthesizer mTts;

    private String ttsPcmDir;

    public String fileName="TextMusic";


    public String convertWord(String word) throws IOException {
        /**一个集合的代码，把image里的textMusic转成wav格式的，并上传，返回上传成功的哈希值*/
        XunFeiTtsTool xunFeiTtsTool =new XunFeiTtsTool("604595a5","./image");
        xunFeiTtsTool.textToVoice(word);
        PcmToWav pcmToWav=new PcmToWav();
        pcmToWav.convert();
        //上传文件
        UploadImageImlp uploadImageImlp=new UploadImageImlp();
        String re=uploadImageImlp.uploadFromService("TextMusic.wav");
        return re;
    }

    public XunFeiTtsTool(String appId, String ttsPcmDir) {
        LOGGER.info("------Speech Utility init tts------");
        this.ttsPcmDir = ttsPcmDir;
        SpeechUtility.createUtility(SpeechConstant.APPID + "=" + appId);
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer();
        if (mTts != null) {
            // 设置发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
            mTts.setParameter(SpeechConstant.SPEED, "20");
            mTts.setParameter(SpeechConstant.PITCH, "55");
            mTts.setParameter(SpeechConstant.SAMPLE_RATE, "15000");
        } else {
            LOGGER.error("tts handler init fail");
        }
    }

    public String textToVoice(String text) {
        try {
            String pcmPath = ttsPcmDir + File.separator + fileName + ".pcm";

            // 设置合成音频保存位置（可自定义保存位置），默认不保存
            mTts.synthesizeToUri(text, pcmPath, mSynListener);
            synchronized (lock) {
                lock.wait();
            }

            return pcmPath;
        } catch (Exception e) {
            LOGGER.error("textToVoice get exception:" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 合成监听器
     */
    SynthesizeToUriListener mSynListener = new SynthesizeToUriListener() {

        public void onBufferProgress(int progress) {
            LOGGER.info("*************合成进度*************" + progress);

        }

        public void onSynthesizeCompleted(String uri, SpeechError error) {
            if (error == null) {
                LOGGER.info("*************合成成功*************");
                LOGGER.info("合成音频生成路径：" + uri);
            } else {
                LOGGER.info("******合成失败*******" + error.getErrorCode()
                        + "*************");
            }
            synchronized (lock) {
                LOGGER.info("通知合成成功");
                lock.notify();
            }

        }


        @Override
        public void onEvent(int eventType, int arg1, int arg2, int arg3, Object obj1, Object obj2) {}

    };

}
