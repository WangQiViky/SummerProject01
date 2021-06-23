let backgroundName;

$(document).ready(function () {
    backgroundName = (window.location.href.split('?')[1].split('=')[1]);
    console.log(backgroundName);

    getRequest(
        'data/getAllImageName',
        function (res) {
            console.log(res);
            paixu(res);
        },
        function (error) {
            alert(JSON.stringify(error));
        }
    );
});
function insertImage(data){
    console.log("插入图片的顺序是："+data)
    let url="http://qv1uxhk9p.hn-bkt.clouddn.com/";
    let str="";
    for(let i=0;i<data.length;i++){
        // let temp=data[i].hash_name;
        let temp=data[i];
        console.log(temp);
        let numm=i+1;
        str=str+'<li><a href="'+'/user/startTalking?place='+backgroundName+'&page='+'page'+numm+'" >' +
            '     <img src="'+url+temp+'" class="mini" width="280" height="180" />' +
            // '<img src="'+url+temp+'" class="pic"  />' +
            '</a></li>';
    }
    $('#image-container').html(str);
}
function paixu(data){
    var C=new Array(data.length);

    for(let i=0;i<data.length;i++){
        let temp=data[i].page_name;
        let num=parseInt(temp.substring(4,temp.length));
        console.log("第"+i+"个元素将被放到第"+num)
        C[num-1]=data[i].hash_name;
    }
    insertImage(C);
}
function finishAll(){
    //todo:可以把张亦池那个计数也归零
    getRequest(
        'data/deleteImageAll',
        function (res) {
            console.log(res);
            window.location.replace("/index");
        },
        function (error) {
            alert(JSON.stringify(error));
        }
    );
}
function bofang(){
    console.log("进入放字符串的方法："+backgroundName);
    window.location.replace("/bofang?place="+backgroundName);
}