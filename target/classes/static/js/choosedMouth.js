let id;
let data;

$(document).ready(function () {
    id=(window.location.href.split('?')[1].split('=')[1]);
    console.log("id="+id)
    adjustMouth();
})
function adjustMouth(){
    getRequest(
        '/adjust/mouth',
        function (res) {
             data = res.content||[];
            console.log("后端返回的东西在这里："+res)
            console.log("后端返回的数据在这里"+data)
            console.log("mouth")
            if(data=="false"){
                window.location.replace("/abandon?data="+id);
            }else{
                 getImage(data);
            }
        },
        function (error) {
            alert(JSON.stringify(error));
        }
    );
}
function getImage(data){
    console.log("进入插字符串的方法："+data)
    var url="http://qv1uxhk9p.hn-bkt.clouddn.com/"+data
    console.log(url)
    $('img').attr("src",url)
    $('img').attr("height","400")
}

function jinru(){
    window.location.replace("/user/resultCartoonAdjust?data="+data);
}

function choosedEye(){
    window.location.replace("/user/mouthBeforeEye?data="+data);
}

function choosedMouth(){
    window.location.replace("/user/choosedMouthAndEye?data="+data);
}