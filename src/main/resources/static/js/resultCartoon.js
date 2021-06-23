let id;

$(document).ready(function () {
    id=(window.location.href.split('?')[1].split('=')[1]);
    console.log("id="+id)
    getCartoon();
})
function getCartoon(){
    getRequest(
        '/cartoon/get/'+id,
        function (res) {
            var data = res.content||[];
            console.log("后端返回的东西在这里："+res)
            console.log("后端返回的数据在这里"+data)
            if(data==="false"){
                window.location.replace("/false");
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

function endChoose(){
    window.location.replace("/user/startSelect");
}