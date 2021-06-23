var id;

$(document).ready(function () {
    id=(window.location.href.split('?')[1].split('=')[1]);
    id = id.split("&")[0]
    console.log("id="+id)
    var url="http://qv1uxhk9p.hn-bkt.clouddn.com/"+id
    console.log(url)
    $('img').attr("src",url)

    // let url="http://qv1uxhk9p.hn-bkt.clouddn.com/"
    // let str="<img class=\"img\" src=\"/photos/photo.png\" width=\"400\" hight=\"400\" alt=\"照片\">"
    // let str="<div class=\"border\" >" +
    //     "<div class='frame' >" +
    //     "<div class='image' style='background-image: url("+url+id+")'></div></div></div>"
    // $('#image-container').html(str);

})



function picturePs(){
    var url = window.location.href;
    var array = url.split('=');
    var gender = array[array.length-1];
    window.location.replace("/user/selectWhichPs?gender="+gender);
}
function pictureFix(){
    var url = window.location.href;
    var array = url.split('=');
    var gender = array[array.length-1];
    window.location.replace("/user/selectWhichFix?gender="+gender);
}