let searchFrameActive=0;
function searchFrameOnClick(e){
    let search_frame = document.getElementsByClassName('search-frame');
    let slider = document.getElementById('slider');
    //改变滑条的位置和大小
    // slider.style.width = "" + search_frame[0].getBoundingClientRect().width + "px";
    for (let i = 0; i < search_frame.length; i++) {
        search_frame[i].onclick = function (e) {
            var left=addRippleEffect(e,this,"rgba(30, 144, 255, .15)");
            if (searchFrameActive<0||i===searchFrameActive) return;
            let width = this.getBoundingClientRect().width;
            this.style.color = "dodgerblue";
            search_frame[searchFrameActive].style.color='gray';

            //改变滑条的位置和大小
            slider.style.width = width + "px";
            slider.style.left = left-vue.frame.first_left + "px";

            let contactList=document.getElementsByClassName('contact-list');
            let searchResultList=document.getElementsByClassName('search-result-list');
            searchResultList[searchFrameActive].style.width="0";
            searchResultList[searchFrameActive].style.opacity="0";
            searchResultList[i].style.width="100%";
            searchResultList[i].style.opacity="1";

            searchResultList[i].style.display="block";
            searchResultList[searchFrameActive].style.display="none";
            searchFrameActive=i;
        }
    }
}
//拖动frame
function dragFrame(e,name){
    let slider = document.getElementById('slider');
    let list=document.getElementsByClassName(name);
    let width=list[0].getBoundingClientRect().width*3/4;//需要修改
    for(var i=0;i<list.length;i++){
        list[i].onmousedown=function(e){
            for(var j=0;j<list.length;i++){
                if(width===0){
                    width=list[j].getBoundingClientRect().width*3/4;
                }
                else break;
            }
            let listObj=this;
            let disX=e.clientX-this.offsetLeft;
            let move=null;
            document.onmousemove=function(e){
                move=e.clientX-disX;
                if(move>0||Math.abs(move)>width) return;
                listObj.style.left=move+"px";
                let sliderLeft=slider.getBoundingClientRect().left+move;
                slider.style.left=sliderLeft+"px";
            }
            document.onmouseup=function(){
                document.onmousemove=null;
                listObj.onmouseup=null;
                if(flag){
                    // console.log("drag1");
                    autoSlider(slider,'frame',vue.frame.frameActive);
                }
                else{
                    // console.log("drag2");
                    autoSlider(slider,'search-frame',searchFrameActive);
                }
            }
        }
    }
}
let flag=true;
//flag true不查找false查找
//slider自动归位
function autoSlider(slider,name,active){
    // console.log("auto"+active);
    let li_list = document.getElementsByClassName(name);
    let left=li_list[active].getBoundingClientRect().left;
    // console.log(left);
    slider.style.left = "" + left-vue.frame.first_left + "px";
    
}
//left-search
function leftSearchOnClick(){
    let leftSearch=document.getElementById('left-search');
    let settingBtn=document.getElementById('setting-btn');
    let searchCloseBtn=document.getElementById('search-close-btn');
    let contactList=document.getElementsByClassName('contact-list');
    let searchResultList=document.getElementsByClassName('search-result-list');
    leftSearch.onfocus=function(){
        settingBtn.style.height="0";
        searchCloseBtn.style.height="45px";
        settingBtn.style.opacity="0";
        searchCloseBtn.style.opacity="1";
        
        let list=document.getElementsByClassName('list')[0];
        list.classList.add('unactive');
        let searchList=document.getElementsByClassName('search-list')[0];
        searchList.classList.remove('unactive');

        contactList[vue.frame.frameActive].style.width="0";
        contactList[vue.frame.frameActive].style.opacity="0";
        contactList[vue.frame.frameActive].style.display="none";
        searchResultList[searchFrameActive].style.width="100%";
        searchResultList[searchFrameActive].style.opacity="1";
        searchResultList[searchFrameActive].style.display="block";

        let search_frame = document.getElementsByClassName('search-frame');
        let slider = document.getElementById('slider');
        slider.style.width = "" + search_frame[0].getBoundingClientRect().width + "px";
        // console.log("onfous");
        autoSlider(slider,'search-frame',searchFrameActive);
        flag=false;
        
    }
    searchCloseBtn.onclick=function(){
        settingBtn.style.height="45px";
        searchCloseBtn.style.height="0";
        settingBtn.style.opacity="1";
        searchCloseBtn.style.opacity="0";

        let list=document.getElementsByClassName('list')[0];
        list.classList.remove('unactive');
        let searchList=document.getElementsByClassName('search-list')[0];
        searchList.classList.add('unactive');
        
        searchResultList[searchFrameActive].style.width="0";
        searchResultList[searchFrameActive].style.opacity="0";
        searchResultList[searchFrameActive].style.display="none";
        contactList[vue.frame.frameActive].style.width="100%";
        contactList[vue.frame.frameActive].style.opacity="1";
        contactList[vue.frame.frameActive].style.display="block";

        let li_list = document.getElementsByClassName('frame');
        let slider = document.getElementById('slider');
        slider.style.width = "" + li_list[0].getBoundingClientRect().width + "px";
        // console.log("onclick");
        autoSlider(slider,'frame',vue.frame.frameActive);
        flag=true;
    }
}

// rightInfo变化
function rightInfoChange(rightInfo){
    //协助联系人点击
    let rightInfoShrink=true;
    //right-info伸缩功能实现
    let rightClose=document.getElementsByClassName('right-close')[0];
    rightClose.onclick=function(){
        if(rightInfoShrink){
            rightClose.style.transform="rotate(180deg)";
            rightInfo.style.width="0";
            rightInfo.style.opacity="0";
            rightInfoShrink=false;
            // transform: rotate(180deg);
        }
        else{
            rightClose.style.transform="";
            rightInfo.style.width="100%";
            rightInfo.style.opacity="1";
            rightInfoShrink=true;
        }
    }
}

//统一添加涟漪动画
function allRiff(){
    let riffAnimation=document.getElementsByClassName('riff');
    for(var i=0;i<riffAnimation.length;i++){
        riffAnimation[i].onclick=function(e){
            addRippleEffect(e,this,"rgba(120,120,120,.25)");
        }
    }
}

//左侧收缩
function leftNavShrink(e){
    let searchRightShrink=true;
    let searchRight=document.getElementsByClassName('search-right')[0];
    let leftNavExpand=document.getElementsByClassName('left-nav-expand')[0];
    searchRight.onclick=function(e){
        if(vue.contactSelect.contactActive===-1) return;
        let leftNav=document.getElementsByClassName('left-nav')[0];
        addRippleEffect(e,this,"rgba(30, 144, 255, .15)");
        if(searchRightShrink){
            leftNav.style.width="0";
            searchRightShrink=false;
            leftNavExpand.style.opacity="1";
        }
    }
    leftNavExpand.onclick=function(e){
        if(vue.contactSelect.contactActive===-1) return;
        let leftNav=document.getElementsByClassName('left-nav')[0];
        addRippleEffect(e,this,"rgba(30, 144, 255, .15)");
        if(!searchRightShrink){
            leftNav.style.width="384px";
            searchRightShrink=true;
            leftNavExpand.style.opacity="0";
        }
    }
}

//展开统计栏
function statisticsExpand(e){
    let statisticsItems=document.getElementsByClassName('statistics-items');
    let ordinary=document.getElementsByClassName('ordinary')[0];
    let statisticsInfo=document.getElementsByClassName('statistics-info');
    let statisticsClose=document.getElementsByClassName('statistics-close-icon');
    for(var i=0;i<statisticsItems.length;i++){
        statisticsItems[i].onclick=function () {
            for(var j=0;j<statisticsItems.length;j++){
                if(this==statisticsItems[j]) break;
            }
            // addRippleEffect(e,this,"rgba(30, 144, 255, .15)");
            ordinary.style.width="0";
            ordinary.style.opacity="0";
            statisticsInfo[j].style.width="100%";
            statisticsInfo[j].style.opacity="1";
            statisticsActive=j;
        }
    }
    for(var i=0;i<statisticsClose.length;i++){
        statisticsClose[i].onclick=function(){
            this.parentElement.parentElement.style.width="0";
            this.parentElement.parentElement.style.opacity="0";
            ordinary.style.width="100%";
            ordinary.style.opacity="1"
            statisticsActive=-1;
        }
    }
}
let isHidden=true;
function onLoadHidden(){
    let settingBtn=document.getElementById('setting-btn');
    let hidden=document.getElementById('hidden');
    let height=35.2*document.getElementsByClassName('hidden-items').length+16;
    settingBtn.onclick=function(){
        if(isHidden){
            settingBtn.style.transform="rotate(180deg)";
            settingBtn.style.opacity="0";
            hidden.style.height=height+"px";
            settingBtn.innerHTML="<ion-icon name='close-outline'></ion-icon>";
            settingBtn.style.opacity="1";
            isHidden=false;
        }
        else{
            settingBtn.style.transform="rotate(0deg)";
            settingBtn.innerHTML="<ion-icon name='ellipsis-vertical-outline'></ion-icon>";
            hidden.style.height="0";
            isHidden=true;
        }
    }
}
function userSetting(){
    let hiddenItems=document.getElementsByClassName('hidden-items');
    let hidden=document.getElementById('hidden');
    let leftNavMain=document.getElementById('left-nav-main');
    let userSetting=document.getElementById('user-setting');
    for(var i=1;i<hiddenItems.length;i++){
        hiddenItems[i].onclick=function(){
            leftNavMain.style.width="0";
            leftNavMain.style.opacity="0";
            hidden.style.height="0";
            isHidden=true;
            userSetting.style.width="384px";
            userSetting.style.opacity="1";

        }
    }
    let hiddenHeadClose=document.getElementsByClassName('hidden-head-close');
    for(var i=0;i<hiddenHeadClose.length;i++){
        hiddenHeadClose[i].onclick=function () {
            leftNavMain.style.width="384px";
            leftNavMain.style.opacity="1";
            userSetting.style.width="0";
            userSetting.style.opacity="0";
            
            let settingBtn=document.getElementById('setting-btn');
            let hidden=document.getElementById('hidden');
            settingBtn.style.transform="rotate(0deg)";
            settingBtn.innerHTML="<ion-icon name='ellipsis-vertical-outline'></ion-icon>";
            hidden.style.height="0";
            isHidden=true;
        }
    }
}
//编辑用户资料
function editUserInfo(){
    let userSettingInfoItems=document.getElementsByClassName('user-setting-info-items');
    let userSettingHidden=document.getElementsByClassName('user-setting-hidden');
    let userSetting=document.getElementById('user-setting');
    for(let i=0;i<userSettingInfoItems.length-1;i++){
        userSettingInfoItems[i].onclick=function () {
            let j;
            for(j=0;j<userSettingInfoItems.length-1;j++){
                if(this===userSettingInfoItems[j]) break;
            }
            userSettingHidden[j].style.width="384px";
            userSettingHidden[j].style.opacity="1";
            userSetting.style.width="0";
            userSetting.style.opacity="0";
        }
    }
    let userSettingClose=document.getElementsByClassName('user-setting-close');
    for(let i=0;i<userSettingClose.length;i++){
        userSettingClose[i].onclick=function(){
            let j;
            for(j=0;j<userSettingClose.length;j++){
                if(this===userSettingClose[j]) break;
            }
            userSetting.style.width="384px";
            userSetting.style.opacity="1";
            userSettingHidden[j].style.width="0";
            userSettingHidden[j].style.opacity="0";
        }
    }
}

window.onload = function (e) {
    let rightInfo=document.getElementsByClassName('right-info')[0];

    searchFrameOnClick(e);
    allRiff();
    rightInfoChange(rightInfo);
    leftNavShrink(e);
    statisticsExpand(e);
    onLoadHidden();
    userSetting();
    editUserInfo();
    dragFrame(e,'list');
    leftSearchOnClick();
    darkModel();
}

function scrollToBottom(){
    $('#chat-main')[0].scrollTop=$('#chat-main')[0].scrollHeight;
}

let emojiShow=false;
document.getElementById('emotion').onclick=function (e){
    addRippleEffect(e,this,"rgba(30, 144, 255, .15)");
    if(emojiShow){
        $('#emoji').css('height','0rem');
    }
    else{
        $('#emoji').css('height','10rem');
    }
    emojiShow=!emojiShow;
    onLoadChatMain();
}
//加载主聊天框
function onLoadChatMain(){
    if(vue.contactSelect.contactActive===-1) return;
    let chatMain=document.getElementById('chat-main');
    let bodyHeight=(window.innerHeight) ? window.innerHeight : (document.documentElement && document.documentElement.clientHeight) ? document.documentElement.clientHeight : document.body.offsetHeight;
    let emotionHeight=(emojiShow)?16*5+64:0;
    chatMain.style.height=(bodyHeight-136-emotionHeight)+"px";
    chatMain.style.width="100%";
    scrollToBottom();
}
window.onresize=onLoadChatMain;

function darkModel(){
    let darkModel=document.getElementById('darkModel');
    let flag=true;
    darkModel.onclick=function(){
        let dark=document.getElementById('dark');
        if(flag){
            dark.style.left="20px";
            flag=false;
        }
        else{
            dark.style.left="-2px";
            flag=true;
        }
    }
}
