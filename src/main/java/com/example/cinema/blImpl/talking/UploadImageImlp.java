package com.example.cinema.blImpl.talking;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;

/**
 * 2021.3.22
 * 这个函数的功能就是将项目根目录下的名为fileName的文件传到七牛云上去，对应的名字就是fileName
 * 如果已经有图片则删除*/
public class UploadImageImlp {

    //...生成上传凭证，然后准备上传
    String accessKey = "pr4fRmyVt8_CodLnHGy099U5_vSffrrxY1RlT3c-";
    String secretKey = "BEubvQCKwcpqtMwYywPWeR1PtBZTT4KeQCDLf2wX";
    String bucket = "betterproject01";


    public static void main(String[] args){
        UploadImageImlp uploadImageImlp=new UploadImageImlp();
        System.out.println(uploadImageImlp.uploadFromService("TextMusic.wav"));
    }

    public String uploadFromService(String fileName){

        String qianzhui="./image/";
        //构造一个带指定 Region 对象的配置类

        Configuration cfg = new Configuration(Region.region2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(accessKey, secretKey);

        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = qianzhui+fileName;
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;

        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
            return putRet.hash;
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }

        return "error";
    }

    public boolean isExists(String fileName){
        Configuration cfg = new Configuration(Region.region2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        //文件名前缀
        String prefix = "";
        //每次迭代的长度限制，最大1000，推荐值 1000
        int limit = 1000;
        //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
        String delimiter = "";
        //列举空间文件列表
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucket, prefix, limit, delimiter);
        while (fileListIterator.hasNext()) {
            //处理获取的file list结果
            FileInfo[] items = fileListIterator.next();
            for (FileInfo item : items) {
                if(fileName.equals(item.key)){
                    return true;
                }
                System.out.println(item.key);
                System.out.println(item.hash);
                System.out.println(item.fsize);
                System.out.println(item.mimeType);
                System.out.println(item.putTime);
                System.out.println(item.endUser);
            }
        }
        return false;
    }
    public void delete(String fileName){
        Configuration cfg = new Configuration(Region.region2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(accessKey, secretKey);
        if(isExists(fileName)){
            String key = fileName;
            BucketManager bucketManager = new BucketManager(auth, cfg);
            try {
                bucketManager.delete(bucket, key);
            } catch (QiniuException ex) {
                //如果遇到异常，说明删除失败
                System.err.println(ex.code());
                System.err.println(ex.response.toString());
            }
        }
    }
}
