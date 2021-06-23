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
    window.location.replace("/user/showPhoto/?data="+data);
}
function picturePs(){
    window.location.replace("/user/selectWhichPs");
}
function pictureFix(){
    window.location.replace("/user/selectWhichFix");
}


function chooseCartoon() {
    window.location.replace("/user/selectWhichCartoon");
}