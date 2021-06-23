let id;

$(document).ready(function () {
    id=(window.location.href.split('?')[1].split('=')[1]);
    console.log("id="+id)
    // getPs();
})




function GetRequestParameters() {

    var url = location.search; //获取url中"?"符后的字串

    var theRequest = new Object();

    if (url.indexOf("?") != -1) {

        var str = url.substr(1);

        var strs = str.split("&"); //将所有参数拆分放入数组

        for (var i = 0; i < strs.length; i++) {  //遍历参数数组

            theRequest[strs[i].split("=")[0]] = decodeURIComponent(strs[i].split("=")[1]);

        }

    }

    return Parameters;

}