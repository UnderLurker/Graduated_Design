(function(){
    let fontSizeRange=document.getElementById('font-size-range');
    
    //预设
    fontSizeRange.value=16;
    document.querySelector('.progress-line').style.width=4*100/8+"%";
    //拖动input：range
    fontSizeRange.oninput=function(){
        let introduce=document.querySelector('.introduce');
        let fontSize=introduce.getElementsByTagName('span')[0];
        let minSize=12,maxSize=20;
        let percentage=100/(maxSize-minSize);
        fontSize.innerHTML=this.value;
        document.querySelector('.progress-line').style.width=(this.value-minSize)*percentage+"%";

        //改变聊天字体大小
        let context=document.getElementsByClassName('chat-info-main');
        for(var i=0;i<context.length;i++){
            context[i].style.fontSize=this.value+"px";
        }
    }

})();