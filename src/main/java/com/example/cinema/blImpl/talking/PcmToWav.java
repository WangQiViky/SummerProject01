package com.example.cinema.blImpl.talking;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PcmToWav {
    public static void main(String[] args) throws IOException {
        PcmToWav pcmToWav=new PcmToWav();
        pcmToWav.convert();
    }

    public void convert() throws IOException{
        String src = "./image/TextMusic.pcm";
        FileInputStream fis = new FileInputStream(src);
        FileOutputStream fos = new FileOutputStream("./image/TextMusic.wav");

        //计算长度
        int PCMSize = 0;
        byte[] buf = new byte[1024 * 4];
        int size = fis.read(buf);

        while (size != -1) {
            PCMSize += size;
            size = fis.read(buf);
        }
        fis.close();

        //填入参数，比特率等等。这里用的是16位单声道 8000 hz
        WaveHeader header = new WaveHeader();
        //长度字段 = 内容的大小（PCMSize) + 头部字段的大小(不包括前面4字节的标识符RIFF以及fileLength本身的4字节)
        header.fileLength = PCMSize + (44 - 8);
        header.FmtHdrLeth = 16;
        header.BitsPerSample = 16;
        header.Channels = 1;
        header.FormatTag = 0x0001;
        header.SamplesPerSec = 8000;
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
}
