let leftDataList=[];
let rightDataList=[];
// $(document).ready(function () {
//     backgroundName = (window.location.href.split('?')[1].split('&')[0].split('=')[1]);
//     page=(window.location.href.split('?')[1].split('&')[1].split('=')[1]);
//     console.log(backgroundName);
//     console.log(page);
//
// })

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
    let startButton=document.getElementById("start-button");                 // 先导航到INPUT标签
    let endButton=document.getElementById("end-button");
    startButton.setAttribute("disabled","disabled")
    endButton.removeAttribute("disabled")

    window.navigator.mediaDevices.getUserMedia({
        audio:true
    }).then(mediaStream =>{
        console.log(mediaStream);//录音抽象出来的文件
        beginRecord(mediaStream);
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
    let mediaNode=audioContext.createMediaStreamSource(mediaStream);
    let jsNode=createJSNode(audioContext);
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
    let leftData=mergeArray(leftDataList);
    let rightData=mergeArray(rightDataList);
    let allData=interleaveLeftAndRight(leftData,rightData);
    let waveBuffer=createWavFile(allData);
    console.log(waveBuffer);
    playRecord(waveBuffer);
    // console.log(leftData,rightData);
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
        // 需要用一个view来操控buffer
        view = new DataView(buffer);
    // 写入wav头部信息
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
    // console.log("开始保存");
    saveContent(blob,"music.wav");
    // console.log('完成保存');
    // let blobUrl = URL.createObjectURL(blob);//就是这里的错
    // document.querySelector('.audio-node').src = blobUrl;
}

//像后端传递音频文件并返回生成的哈希值文件名和简要说明，自此以上都是录音的方法
function saveContent (content, fileName) {
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
    formData.append("multipartFile",blob);
    formData.append("background","")
    formData.append("pageName","")

    console.log(formData);
    console.log(formData.get("multipartFile"));
    console.log(formData.get("background"));
    console.log(formData.get("pageName"));

    $.ajax({
        type: 'POST',
        url: "/adjust",
        data: formData,
        contentType: false,
        processData: false,
        cache: false,
        success: function (res) {
            var data = res.content||[];
            console.log("后端返回的东西在这里："+res)
            console.log("后端返回的数据在这里"+data)

            if(data=="false"){
                window.location.replace("/false");
            }else{
                viewImage(data);
            }
        },
        error: function (error) {
            alert("请刷新一遍重新录制，试一试说短一点~~");

        }
    });


}
//是将文件和说明显示在网页中的方法
function viewImage(fileName){
    let url="http://qqd3in7iz.hn-bkt.clouddn.com/"
    let str="<img src='"+url+fileName+"'>"
    $('#image-container').html(str);
}

function finishPicture(){
    window.location.replace("/user/startSelect");
}