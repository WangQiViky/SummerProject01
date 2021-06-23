function startCamera(){
    console.log("调用人脸拟合的方法")
    getRequest(
        '/photo',
        function (res) {
            var data = res.content||[];
            console.log("后端返回的东西在这里："+res)
            console.log("后端返回的数据在这里"+data)
            getImage(data);
        },
        function (error) {
            alert(JSON.stringify(error));
        }
    );
}
function getImage(data){
    console.log("进入插字符串的方法："+data)
    var url = window.location.href;
    var array = url.split('/');
    var gender = array[array.length-1];
    window.location.replace("/user/showPhoto/?data="+data+"&gender="+gender);
}
function picturePs(){
    var url = window.location.href;
    var array = url.split('/');
    var gender = array[array.length-1];
    window.location.replace("/user/selectWhichPs/?gender="+gender);
}
function pictureFix(){
    var url = window.location.href;
    var array = url.split('/');
    var gender = array[array.length-1];
    window.location.replace("/user/selectWhichFix/?gender"+gender);
}


function chooseCartoon() {
    var url = window.location.href;
    var array = url.split('/');
    var gender = array[array.length-1];
    window.location.replace("/user/selectWhichCartoon/?gender="+gender);
}