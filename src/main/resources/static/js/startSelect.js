$(document).ready(function () {
    var url = window.location.href;
    console.log(url);
    console.log(url.split('='));
    var gender = url.split('=')[1];
    console.log(gender);

    $('#url').val(gender);
    if(gender === 'boy'){
        console.log("是男生");
        $("#pic1").attr('src','/photos/gongzhu_11.png');
        $("#pic2").attr('src','/photos/gongzhu_12.png');
        $("#pic3").attr('src','/photos/gongzhu_13.png');

        $("#a1").attr('href','/user/resultCartoon?id=11')
        $("#a2").attr('href','/user/resultCartoon?id=12')
        $("#a3").attr('href','/user/resultCartoon?id=13')


    }else if(gender === 'girl'){
        console.log("是女生");
        $("#pic1").attr('src','/photos/gongzhu_1.png');
        $("#pic2").attr('src','/photos/gongzhu_2.png');
        $("#pic3").attr('src','/photos/gongzhu_4.png');

        $("#a1").attr('href','/user/resultCartoon?id=1')
        $("#a2").attr('href','/user/resultCartoon?id=2')
        $("#a3").attr('href','/user/resultCartoon?id=4')
    }




});

