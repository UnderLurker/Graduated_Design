/** @type {HTMLCanvasElement} */
openCamera = function () {
    let res = true;
    let video = document.getElementById('video');
    navigator.getUserMedia = (navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia)
    navigator.getUserMedia({
        video: true,
        audio: false
    }, function (stream) {
        mediaStreamTrack = typeof stream.stop === 'function' ? stream : stream.getTracks()[0];
        video.srcObject = stream;
        video.play();
    }, function (e) {
        console.log("获取摄像头失败！！");
        res = false;
    });
    return res;
}

uploadImg = function (url) {
    let video = document.getElementById('video');
    let canvas = document.getElementById('canvas');
    let context = canvas.getContext("2d");
    context.drawImage(video, 0, 0, canvas.width, canvas.height);
    let userImgSrc = canvas.toDataURL("img/png");

    sendData({
        'faceBase': userImgSrc,
        'name': $("#UserNameFace").val()
    },url);
}