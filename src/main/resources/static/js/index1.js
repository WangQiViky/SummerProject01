let leftDataList=[];
let rightDataList=[];
let backgroundName;
let page;
let num=0;
let blobSto;
let mediaStream1;
let mediaNode;
let jsNode;

$(document).ready(function () {
    backgroundName = (window.location.href.split('?')[1].split('&')[0].split('=')[1]);
    page=(window.location.href.split('?')[1].split('&')[1].split('=')[1]);
    console.log(backgroundName);
    console.log(page);

    if(page=="page1"){
        if(backgroundName=="tree"){
            var url="/photos/tree.png"
            let str="<img src='"+url+"'>"
            // let str='<img src="/images/result11.png">'
            $('#image-container').empty();
            $('#image-container').html(str);
            // $('img').attr("src",url)
            console.log("wwwwwwww")
        }
        else if(backgroundName=="ocean"){
            var url="/photos/ocean.png"
            $('img').attr("src",url)
        }
        else{
            var url="/photos/house.png"
            $('img').attr("src",url)
        }
    }else{
        getBeforePic()
    }


    var ui =document.getElementById("button3");
    ui.style.display="none";
    var uii =document.getElementById("button4");
    uii.style.display="none";



})



function getBeforePic() {
    getRequest(
        '/data/getAllImageName',
        function (res) {
            console.log(res);
            for(let i=0;i<res.length;i++){
                console.log(i);
                console.log(res[i].page_name);
                let pageBeforeNum=page.substring(page.length-1,page.length)-1;
                let pageBefore="page"+pageBeforeNum;
                console.log(pageBefore)
                if(res[i].page_name==pageBefore){
                    let pageName=res[i].hash_name;
                    var url="http://qv1uxhk9p.hn-bkt.clouddn.com/"+pageName
                    let str="<img src='"+url+"'>"
                    $('#image-container').empty();
                    $('#image-container').html(str);
                    console.log(url)
                    // $('img').attr("src",url)
                }
            }
            console.log("wq");
        },
        function (error) {
            alert(JSON.stringify(error));
        }
    );
}

function playMusic(){
    if(!this.value){
        return;
    }
    let fileReader=new FileReader();
    let file=this.files[0];
    fileReader.onload=function(){
        let arrayBuffer=this.result;
        console.log(arrayBuffer);
        let blob=new Blob([new Int8Array(this.result)]);
        let blobUrl=URL.createObjectURL(blob);
        console.log(blobUrl);
        document.querySelector('.audio-node').src=blobUrl;
    }
    fileReader.readAsArrayBuffer(this.files[0]);
}
function record() {
    let startButton=document.getElementById("start-button");
    // ????????????INPUT??????
    let endButton=document.getElementById("end-button");
    startButton.setAttribute("disabled","disabled")
    endButton.removeAttribute("disabled")

    window.navigator.mediaDevices.getUserMedia({
        audio:true
    }).then(mediaStream =>{
        console.log(mediaStream);//???????????????????????????
        mediaStream1=mediaStream;
        console.log("???????????????");
        console.log(mediaStream1);
        beginRecord(mediaStream1);
    }).catch(err=>{
        console.log(err);
    });
}
function createJSNode(audioContext){
    const BUFFER_SIZE=4096;
    const INPUT_CHANNEL_COUNT=2;
    const OUTPUT_CHANNEL_COUNT=2;
    let creator=audioContext.createScriptProcessor || audioContext.createJavaScriptNode;
    creator=creator.bind(audioContext);
    return creator(BUFFER_SIZE,INPUT_CHANNEL_COUNT,OUTPUT_CHANNEL_COUNT);
}
function onAudioProcess(event){
    // console.log(event.inputBuffer);
    let audioBuffer=event.inputBuffer;
    let leftChannelData=audioBuffer.getChannelData(0);
    let rightChannelData=audioBuffer.getChannelData(1);
    // console.log(leftChannelData,rightChannelDate);
    leftDataList.push(leftChannelData.slice(0));
    rightDataList.push(rightChannelData.slice(0));
}
function beginRecord(mediaStream){
    let audioContext=new (window.AudioContext||window.webkitAudioContext);
    mediaNode=audioContext.createMediaStreamSource(mediaStream);
    jsNode=createJSNode(audioContext);
    jsNode.connect(audioContext.destination);
    jsNode.onaudioprocess=onAudioProcess;
    mediaNode.connect(jsNode);
}
function mergeArray(list){
    let length=list.length*list[0].length;
    let data=new Float32Array(length);
    let offset=0;
    for(let i=0;i<list.length;i++){
        data.set(list[i],offset);
        offset+=list[i].length;
    }
    return data;
}
function stopRecord(){

    //??????????????????????????????????????????????????????
    mediaStream1.getAudioTracks()[0].stop();
    mediaNode.disconnect();
    jsNode.disconnect();

    console.log(mediaStream1);
    console.log(mediaNode);
    console.log(jsNode);
    console.log("??????????????????")
    // console.log(leftDataList, rightDataList);


    num++;

    let leftData=mergeArray(leftDataList);
    let rightData=mergeArray(rightDataList);
    let allData=interleaveLeftAndRight(leftData,rightData);
    let waveBuffer=createWavFile(allData);
    console.log(waveBuffer);
    playRecord(waveBuffer);

    // console.log(leftData,rightData);
    let startButton=document.getElementById("start-button");                 // ????????????INPUT??????
    let endButton=document.getElementById("end-button");
    endButton.setAttribute("disabled","disabled")
    startButton.removeAttribute("disabled")

    // startButton.setAttribute("onclick","record2()");
    endButton.setAttribute("onclick","stopRecord2()");

    leftDataList=[];
    rightDataList=[];

}
function record2(){
    console.log("????????????record2??????");
    let startButton=document.getElementById("start-button");                 // ????????????INPUT??????
    let endButton=document.getElementById("end-button");
    startButton.setAttribute("disabled","disabled")
    endButton.removeAttribute("disabled")
}

function interleaveLeftAndRight(left,right){
    let totalLength=left.length+right.length;
    let data=new Float32Array(totalLength);
    for(let i=0;i<left.length;i++){
        let k=i*2;
        data[k]=left[i];
        data[k+1]=right[i];
    }
    return data;
}
function createWavFile (audioData) {
    const WAV_HEAD_SIZE = 44;
    let buffer = new ArrayBuffer(audioData.length * 2 + WAV_HEAD_SIZE),
        // ???????????????view?????????buffer
        view = new DataView(buffer);
    // ??????wav????????????
    // RIFF chunk descriptor/identifier
    writeUTFBytes(view, 0, 'RIFF');
    // RIFF chunk length
    view.setUint32(4, 44 + audioData.length * 2, true);
    // RIFF type
    writeUTFBytes(view, 8, 'WAVE');
    // format chunk identifier
    // FMT sub-chunk
    writeUTFBytes(view, 12, 'fmt ');
    // format chunk length
    view.setUint32(16, 16, true);
    // sample format (raw)
    view.setUint16(20, 1, true);
    // stereo (2 channels)
    view.setUint16(22, 2, true);
    // sample rate
    view.setUint32(24, 44100, true);
    // byte rate (sample rate * block align)
    view.setUint32(28, 44100 * 2, true);
    // block align (channel count * bytes per sample)
    view.setUint16(32, 2 * 2, true);
    // bits per sample
    view.setUint16(34, 16, true);
    // data sub-chunk
    // data chunk identifier
    writeUTFBytes(view, 36, 'data');
    // data chunk length
    view.setUint32(40, audioData.length * 2, true);
    let length = audioData.length;
    let index = 44;
    let volume = 1;
    for (let i = 0; i < length; i++) {
        view.setInt16(index, audioData[i] * (0x7FFF * volume), true);
        index += 2;
    }
    return buffer;
}
function writeUTFBytes (view, offset, string) {
    var lng = string.length;
    for (var i = 0; i < lng; i++) {
        view.setUint8(offset + i, string.charCodeAt(i));
    }
}
function playRecord (arrayBuffer) {
    let blob = new Blob([new Uint8Array(arrayBuffer)]);
    // console.log("????????????");
    saveContent(blob,"music.wav");
    // console.log('????????????');
    let blobUrl = URL.createObjectURL(blob);
    document.querySelector('.audio-node').src = blobUrl;
}

//??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
function saveContent (content, fileName) {
    // viewLoad();
    console.log(content.toString())
    // let aTag = document.createElement('a');
    // aTag.setAttribute('download',fileName);
    let blob = new Blob([content],{type:""});
    // aTag.setAttribute('href',URL.createObjectURL(blob));
    // document.body.appendChild(aTag);
    // aTag.click();
    // document.body.removeChild(aTag);
    // console.log(aTag)

    var formData = new FormData();
    blobSto=blob;
    formData.append("multipartFile",blob);
    formData.append("background",backgroundName)
    formData.append("pageName",page)

    console.log(formData);
    console.log(formData.get("multipartFile"));
    console.log(formData.get("background"));
    console.log(formData.get("pageName"));

    $.ajax({
        type: 'POST',
        url: "/talking/upload",
        data: formData,
        contentType: false,
        processData: false,
        cache: false,
        success: function (data) {
            if(data.num%2===0){
                console.log("??????num????????????????????????")
                // $('#loading-container').empty();
                storageName(data.hashName,data.originWord);
                viewImage(data.hashName);
                viewWav(data.wavName);
                if(data.talking==1){
                    console.log("???????????????")
                    viewTalking(data.people,data.sens);
                }else{
                    viewWord(data.originWord);
                }
            }else{
                console.log("??????num????????????????????????")
                console.log(data);
                viewWav(data.wavName);
            }


        },
        error: function (error) {
            alert("???????????????????????????????????????????????????~~");

        }
    });


}

function stopRecord2(){
    // viewLoad();
    console.log("????????????stoprecord2??????");
    let startButton=document.getElementById("start-button");                 // ????????????INPUT??????
    let endButton=document.getElementById("end-button");
    // endButton.setAttribute("disabled","disabled")
    startButton.style.display="none";
    endButton.style.display="none";

    var ui =document.getElementById("button3");
    ui.style.display="";
    var uii =document.getElementById("button4");
    uii.style.display="";

    mediaStream1.getAudioTracks()[0].stop();
    mediaNode.disconnect();
    jsNode.disconnect();

    let leftData=mergeArray(leftDataList);
    let rightData=mergeArray(rightDataList);
    let allData=interleaveLeftAndRight(leftData,rightData);
    let waveBuffer=createWavFile(allData);
    console.log(waveBuffer);
    playRecord(waveBuffer);

    // var formData = new FormData();
    // formData.append("multipartFile",blobSto);
    // formData.append("background",backgroundName)
    // formData.append("pageName",page)
    //
    // console.log(formData);
    // console.log(formData.get("multipartFile"));
    // console.log(formData.get("background"));
    // console.log(formData.get("pageName"));
    //
    // $.ajax({
    //     type: 'POST',
    //     url: "/talking/upload",
    //     data: formData,
    //     contentType: false,
    //     processData: false,
    //     cache: false,
    //     success: function (data) {
    //         //?????????????????????????????????????????????
    //         // $('#loading-container').empty();
    //         console.log(data);
    //         viewWav(data.wavName);
    //
    //     },
    //     error: function (error) {
    //         alert("???????????????????????????????????????????????????~~");
    //
    //     }
    // });
}
function viewLoad(){
    let str='<ul id="progress">\n' +
        '    <li><div id="layer1" class="ball"></div><div id="layer7" class="pulse"></div></li>\n' +
        '    <li><div id="layer2" class="ball"></div><div id="layer8" class="pulse"></div></li>\n' +
        '    <li><div id="layer3" class="ball"></div><div id="layer9" class="pulse"></div></li>\n' +
        '    <li><div id="layer4" class="ball"></div><div id="layer10" class="pulse"></div></li>\n' +
        '    <li><div id="layer5" class="ball"></div><div id="layer11" class="pulse"></div></li>\n' +
        '    </ul>';

    $('#loading-container').empty();
    $('#loading-container').html(str);

    console.log("????????????????????????????????????");
    // $('#progress').removeClass('running').delay(10).queue(function(next){
    //     $(this).addClass('running');
    //     next();
    // });

}
function viewTalking(people,sens){
    let str="";
    let list=["example-right","example-obtuse"]
    for(let i=0;i<people.length;i++){
        str=str+'<blockquote class="'+list[i%2]+'">\n' +
                '        <p>'+sens[i]+'</p>\n' +
                '    </blockquote>\n' +
                '    <p>'+people[i]+'</p>';

    }

    $('#talking-container').empty();
    $('#talking-container').html(str);
}
function viewWav(wavName){
    console.log("??????????????????"+wavName);
    let url="http://qv1uxhk9p.hn-bkt.clouddn.com/"
    let str='<audio src="'+url+wavName+'" autoplay="autoplay"></audio>';
    // let str='<img src="/images/result11.png">'

    $('#wav-container').empty();
    $('#wav-container').html(str);
}
//????????????????????????????????????????????????
function viewWord(word){
    console.log(word);
    console.log("??????????????????????????????"+word);
    let str="<p class= \"bg-info\">"+word+"</p>"
    $('#word-container').html(str);
    // viewImage();
    // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
}
function viewImage(fileName){
    let url="http://qv1uxhk9p.hn-bkt.clouddn.com/"
    let str="<img src='"+url+fileName+"'>"
    // let str='<img src="/images/result11.png">'
    $('#image-container').empty();
    $('#image-container').html(str);
}

// ???????????????????????????????????????????????????
function storageName(fileName,wav){
    console.log("??????????????????????????????????????????:"+wav)
    getRequest(
        '/data/storageImage?page_name='+page+'&hash_name='+fileName+'&wav='+wav,
        function (res) {
            var data = res.content||[];

        },
        function (error) {
            alert(JSON.stringify(error));
        }
    );
}

function finishCreate(){
    window.location.replace("/finish?place="+backgroundName);
}
function finishCreate2(){
    let num=parseInt(page.substring(4,page.length));
    console.log("num="+num);
    num++;
    let newPage="page"+num;
    console.log("newPage"+newPage);
    window.location.replace("/user/startTalking?place="+backgroundName+"&page="+newPage);
}