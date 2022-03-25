var video = document.querySelector("video");
var playBtn = document.getElementById("playBtn");

function videoPlay(){
    video.play();
    playBtn.innerHTML="<ion-icon name='pause-outline'></ion-icon>";
}
function videoPause(){
    video.pause();
    playBtn.innerHTML="<ion-icon name='caret-forward-outline'></ion-icon>";
}

playBtn.onclick = function () {
    if (video.paused) {
        videoPlay();
    } else {
        videoPause();
    }
}
var full = document.getElementById("full");
var container = document.getElementById("container");
var i = 1; //定义一个变量，用于记录是否为全屏状态
function screenChange() {
    i++;
    //对2取余，为0则全屏，否则关闭全屏
    if (i % 2 == 0) {
        //点击开启全屏
        if (container.requestFullScreen) {
            container.requestFullScreen()
        } else if (container.webkitRequestFullScreen) {
            container.webkitRequestFullScreen() //谷歌
        } else if (container.mozRequestFullScreen) {
            container.mozRequestFullScreen() //火狐
        } else if (div01.msRequestFullscreen) {
            container.msRequestFullscreen() //ie
        }
        $('#video-player').css({'width':'100%','height':'100%','max-height':'100vh','padding':'0'});
        $('.pos').css({'bottom':'0','position':'absolute','width':'100%','bottom':'-3rem'});
        full.innerHTML='<ion-icon name="contract-outline"></ion-icon>';
    } else {
        //关闭全屏
        if (document.cancelFullscreen) {
            document.cancelFullscreen();
        } else if (document.webkitCancelFullScreen) {
            document.webkitCancelFullScreen();
        } else if (document.mozCancelFullScreen) {
            document.mozCancelFullScreen();
        } else if (document.msExitFullscreen) {
            document.msExitFullscreen();
        }
        for(let time of timeId){
            clearTimeout(time);
        }
        $('#video-player').css({'width':'60%','height':'auto','max-height':'85vh','padding':'1%'});
        $('.pos').css({'position':'relative','width':'60%','bottom':'0'});
        full.innerHTML='<ion-icon name="expand-outline"></ion-icon>';
    }

}
full.onclick = screenChange;

function number1(s) {
    if (s < 10) {
        s = "0" + s
    } else {
        s = "" + s
    }
    return s;
}
var time1 = document.getElementById("time1");
var time2 = document.getElementById("time2");
var t1;
//视频获取时间单位为秒，要对它进行一些数据的处理
video.oncanplay = function () {
    //获取视频总时长
    t1 = video.duration;
    var h = parseInt(t1 / 3600);
    var m = parseInt(t1 % 3600 / 60);
    var s = parseInt(t1 % 60);
    //值显示在span标签中
    time2.innerHTML = number1(h) + ":" + number1(m) + ":" + number1(s);
}

video.ontimeupdate = function () {
    //获取当前视频播放的时间
    var t2 = video.currentTime;
    var h = parseInt(t2 / 3600);
    var m = parseInt(t2 % 3600 / 60);
    var s = parseInt(t2 % 60);
    time1.innerHTML = number1(h) + ":" + number1(m) + ":" + number1(s);
    progress.max = t2;
    progress.value = t2 / t1 * progress.max;
}
video.onended = function () {
    progress.value = 0;
    //结束时清0；
    time1.innerHTML = "00" + ":" + "00" + ":" + "00";
    playBtn.innerHTML="<ion-icon name='caret-forward-outline'></ion-icon>";
}

progress.onclick = function (e) {
    //获取当前位置距离父级最左边的距离
    //left = progress.offsetLeft;
    //获取鼠标距离屏幕最左边的距离
    //e.clientX
    //获取鼠标距离当前元素最左边的距离
    var left = e.offsetX;
    console.log(left);
    console.log(t1);
    video.currentTime = left / 320 * t1;
}
//关闭video-player
document.getElementById('video-close').onclick=function(){
    let player=$('.video-player').get('0');
    player.style.display='none';
    videoPause();
}
let timeId=[];
document.getElementById('video-player').onmousemove=function(){
    if(i%2===1) return;
    $('.pos').css({'bottom':'0rem'});
    let time=setTimeout(()=>{
        $('.pos').css({'bottom':'-3rem'});
    },2000);
    timeId.push(time);
}
document.getElementById('video-player').onmouseover=function(){
    for(let time of timeId){
        clearTimeout(time);
    }
    $('.pos').css({'bottom':'0rem'});
}

function videoSpaceKeyDown(event){
    let player=$('.video-player').get('0');
    if(player.style.display==='none') return;
    if(event.keyCode===32){
        if (video.paused) {
            videoPlay();
        } else {
            videoPause();
        }
    }
}
document.onkeydown=videoSpaceKeyDown;