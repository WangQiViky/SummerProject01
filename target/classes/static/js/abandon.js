let id;
let data;

$(document).ready(function () {
    id=(window.location.href.split('?')[1].split('=')[1]);
    console.log("id="+id);
})

function tiaozheng(){
    window.location.replace("/index");
}


function toIndex(){
    window.location.replace("/index");
}


function toStartStory() {
    window.location.replace("/user/resultCartoonAdjust?data="+id);
}