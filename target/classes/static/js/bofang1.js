let backgroundName;
let alreadyRes=[];

$(document).ready(function () {
    backgroundName = (window.location.href.split('?')[1].split('=')[1]);

    getRequest(
        'data/getAllImageName',
        function (res) {
            console.log(res);
            alreadyRes=paixu(res);
            console.log(alreadyRes);
            strToWav(alreadyRes);
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


function paixu(data){
    var C=new Array(data.length);
    for(let i=0;i<data.length;i++){
        let temp=data[i].page_name;
        let num=parseInt(temp.substring(4,temp.length));
        console.log("图像的第"+i+"个元素将被放到第"+num)
        C[num-1]=data[i];

    }
    return C;
}
function strToWav(data){
    let url="http://qv1uxhk9p.hn-bkt.clouddn.com/";
    let str="";
    var D=new Array(data.length);
    for(let i=0;i<data.length;i++){
        str=str+data[i].wav_name;
        // str=str+data[i];
        if(i===0){
            D[i]=(data[i].wav_name).length*300;
        }else{
            D[i]=D[i-1]+(data[i].wav_name).length*300;
        }

    }
    console.log(str);
    console.log(D);
    getRequest(
        '/talking/toWav?wavStr='+str,
        function (res) {
            console.log(res);
            let str1='<audio src="'+url+res+'" autoplay="autoplay"></audio>';
            $('#wav-container').empty;
            $('#wav-container').html(str1);

            for(let i=0;i<D.length;i++){
                if(i===0){
                    insertImage(data[i].hash_name);
                }else{
                    setTimeout(function(){
                        console.log("延迟执行"+i)
                        let url="http://qv1uxhk9p.hn-bkt.clouddn.com/";
                        let str="";
                        str=str+'<a href="#"><img src="'+url+data[i].hash_name+'" alt="#"></a>';

                        $('#image-container').empty;
                        $('#image-container').html(str);
                    },D[i-1]);
                }
            }

        },
        function (error) {
            alert(JSON.stringify(error));
        }
    );
}
function insertImage(data){
    let url="http://qv1uxhk9p.hn-bkt.clouddn.com/";
    let str="";
    str=str+'<a href="#"><img src="'+url+data+'" alt="#"></a>';

    $('#image-container').empty;
    $('#image-container').html(str);
}