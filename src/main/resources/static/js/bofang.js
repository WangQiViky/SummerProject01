let backgroundName;

$(document).ready(function () {
    backgroundName = (window.location.href.split('?')[1].split('=')[1]);

    getRequest(
        'data/getAllImageName',
        function (res) {
            console.log(res);
            paixu1(res);
            paixu(res);
        },
        function (error) {
            alert(JSON.stringify(error));
        }
    );

});
function endChoose(){
    console.log("返回上一页")
    window.location.replace("/finish?place="+backgroundName);
}
function insertImage(data){
    let url="http://qv1uxhk9p.hn-bkt.clouddn.com/";
    let str="";
    for(let i=0;i<data.length;i++){
        // let temp=data[i].hash_name;
        let temp=data[i];
        console.log(temp);
        str=str+'<a href="#" data-shutter-title="'+temp+'"><img src="'+url+temp+'" alt="#"></a>';
    }

    $('#image-container').empty;
    $('#image-container').html(str);
    $(function () {
        $('.shutter').shutter({
            shutterW: 500, // 容器宽度
            shutterH: 358, // 容器高度
            isAutoPlay: true, // 是否自动播放
            playInterval: 6000, // 自动播放时间
            curDisplay: 3, // 当前显示页
            fullPage: false // 是否全屏展示
        });
    });
}
function paixu(data){
    var C=new Array(data.length);
    for(let i=0;i<data.length;i++){
        let temp=data[i].page_name;
        let num=parseInt(temp.substring(4,temp.length));
        console.log("图像的第"+i+"个元素将被放到第"+num)
        if(num==1){
            C[data.length-1]=data[i].hash_name;
        }else{
            C[num-2]=data[i].hash_name;
        }

    }
    insertImage(C);
}
function paixu1(data){
    var C=new Array(data.length);
    for(let i=0;i<data.length;i++){
        let temp=data[i].page_name;
        let num=parseInt(temp.substring(4,temp.length));
        console.log("音乐的第"+i+"个元素将被放到第"+num)
        C[num-1]=data[i].wav_name;
    }
    strToWav(C);
}
function strToWav(data){
    let url="http://qv1uxhk9p.hn-bkt.clouddn.com/";
    let str="";
    for(let i=0;i<data.length;i++){
        // str=str+data[i].wav_name;
        str=str+data[i];
    }
    console.log(str);
    getRequest(
        '/talking/toWav?wavStr='+str,
        function (res) {
            console.log(res);
            let str1='<audio src="'+url+res+'" autoplay="autoplay"></audio>';
            $('#wav-container').empty;
            $('#wav-container').html(str1);

        },
        function (error) {
            alert(JSON.stringify(error));
        }
    );
}
