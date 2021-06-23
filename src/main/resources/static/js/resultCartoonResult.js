let id;

$(document).ready(function () {
    id=(window.location.href.split('?')[1].split('=')[1]);
    console.log("id="+id)
    var url="http://qv1uxhk9p.hn-bkt.clouddn.com/"+id
    console.log(url)
    $('img').attr("src",url)
    $('img').attr("height","400")
})

function getImage(data){
    console.log("进入插字符串的方法："+data)
    var url="http://qv1uxhk9p.hn-bkt.clouddn.com/"+id
    console.log(url)
    $('img').attr("src",url)
}
function endChoose(){
    window.location.replace("/user/startSelect");
}