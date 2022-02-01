let frameActive=0;
let first_left=null;
function frameOnClick(e){
    let li_list = document.getElementsByClassName('frame');
    let slider = document.getElementById('slider');
    first_left=li_list[0].getBoundingClientRect().left;
    //改变滑条的位置和大小
    slider.style.width = "" + li_list[0].getBoundingClientRect().width + "px";
    for (let i = 0; i < li_list.length; i++) {
        li_list[i].onclick = function (e) {
            var left=addRippleEffect(e,this,"rgba(30, 144, 255, .15)");
            if (frameActive<0||i==frameActive) return;
            let width = this.getBoundingClientRect().width;
            this.style.color = "dodgerblue";
            li_list[frameActive].style.color='gray';

            //改变滑条的位置和大小
            slider.style.width = "" + width + "px";
            // slider.style.left=this.getBoundingClientRect().left+list.getBoundingClientRect().left;
            slider.style.left = "" + left-first_left + "px";

            let contactList=document.getElementsByClassName('contact-list');
            contactList[frameActive].style.width="0";
            contactList[frameActive].style.opacity="0";
            contactList[i].style.width="100%";
            contactList[i].style.opacity="1";

            contactList[i].style.display="block";
            contactList[frameActive].style.display="none";
            frameActive=i;
        }
    }
}
let searchFrameActive=0;
function searchFrameOnClick(e){
    let search_frame = document.getElementsByClassName('search-frame');
    let slider = document.getElementById('slider');
    //改变滑条的位置和大小
    // slider.style.width = "" + search_frame[0].getBoundingClientRect().width + "px";
    for (let i = 0; i < search_frame.length; i++) {
        search_frame[i].onclick = function (e) {
            var left=addRippleEffect(e,this,"rgba(30, 144, 255, .15)");
            if (searchFrameActive<0||i==searchFrameActive) return;
            let width = this.getBoundingClientRect().width;
            this.style.color = "dodgerblue";
            search_frame[searchFrameActive].style.color='gray';

            //改变滑条的位置和大小
            slider.style.width = width + "px";
            slider.style.left = left-first_left + "px";

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
                if(width==0){
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
                    autoSlider(slider,'frame',frameActive);
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
    slider.style.left = "" + left-first_left + "px";
    
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

        contactList[frameActive].style.width="0";
        contactList[frameActive].style.opacity="0";
        contactList[frameActive].style.display="none";
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
        contactList[frameActive].style.width="100%";
        contactList[frameActive].style.opacity="1";
        contactList[frameActive].style.display="block";

        let li_list = document.getElementsByClassName('frame');
        let slider = document.getElementById('slider');
        slider.style.width = "" + li_list[0].getBoundingClientRect().width + "px";
        // console.log("onclick");
        autoSlider(slider,'frame',frameActive);
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
let contactActive=-1;
let statisticsClose=document.getElementsByClassName('statistics-close-icon');
function contactOnClick(e,rightInfo){
    let contact_list=document.getElementsByClassName("contact-items");
    for(let i = 0; i < contact_list.length; i++){
        contact_list[i].onclick=function(e){
            if(contactActive==i) return;
            let unread=document.getElementsByClassName('unread-num')[i];
            addRippleEffect(e,this,"rgba(30, 144, 255, 1)");
            unread.style.display="none";
            if(contactActive>=0){
                contact_list[contactActive].classList.remove('active');
            }
            contactActive=i;
            this.classList.add('active');
            //打开信息栏
            document.getElementsByClassName('right-close')[0].style.transform="";
            let ordinary=document.getElementsByClassName('ordinary')[0];
            ordinary.style.width="100%";
            ordinary.style.opacity="1";
            rightInfo.style.width="100%";
            rightInfo.style.opacity="1";
            //隐藏提示词
            let reminder=document.getElementsByClassName('reminder')[0];
            reminder.style.display="none";
            //显示聊天主界面
            let top=document.getElementsByClassName('top')[0];
            top.style.height="3.5rem";
            //显示聊天输入框
            let inputFrame=document.getElementsByClassName('input-frame')[0];
            inputFrame.style.height="4rem";
            onLoadChatMain();
            if(statisticsActive!=-1){
                statisticsClose[statisticsActive].click();
            }
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
        if(contactActive==-1) return;
        let leftNav=document.getElementsByClassName('left-nav')[0];
        addRippleEffect(e,this,"rgba(30, 144, 255, .15)");
        if(searchRightShrink){
            leftNav.style.width="0";
            searchRightShrink=false;
            leftNavExpand.style.opacity="1";
        }
    }
    leftNavExpand.onclick=function(e){
        if(contactActive==-1) return;
        let leftNav=document.getElementsByClassName('left-nav')[0];
        addRippleEffect(e,this,"rgba(30, 144, 255, .15)");
        if(!searchRightShrink){
            leftNav.style.width="384px";
            searchRightShrink=true;
            leftNavExpand.style.opacity="0";
        }
    }
}
//添加点击事件
function bellOnClick(bell1,bell2){
    bell1.onclick=function(e){
        if(this.style.height=="0"){
            this.style.height="2rem";
            bell2.style.height="0";
        }
        else{
            this.style.height="0";
            bell2.style.height="2rem";
        }
    }
}
//bell动画
function bellAnimation(){
    let bellList=document.getElementsByClassName('bell');
    bellOnClick(bellList[0],bellList[1]);
    bellOnClick(bellList[1],bellList[0]);
}

//展开统计栏
let statisticsActive=-1;
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
    for(var i=0;i<userSettingInfoItems.length-1;i++){
        userSettingInfoItems[i].onclick=function () {
            for(var j=0;j<userSettingInfoItems.length-1;j++){
                if(this==userSettingInfoItems[j]) break;
            }
            userSettingHidden[j].style.width="384px";
            userSettingHidden[j].style.opacity="1";
            userSetting.style.width="0";
            userSetting.style.opacity="0";
        }
    }
    let userSettingClose=document.getElementsByClassName('user-setting-close');
    for(var i=0;i<userSettingClose.length;i++){
        userSettingClose[i].onclick=function(){
            for(var j=0;j<userSettingClose.length;j++){
                if(this==userSettingClose[j]) break;
            }
            userSetting.style.width="384px";
            userSetting.style.opacity="1";
            userSettingHidden[j].style.width="0";
            userSettingHidden[j].style.opacity="0";
        }
    }
}

//清空搜索框
function deleteSearchInput(){
    let searchMain=document.querySelector('.search-main');
    let searchClose=searchMain.getElementsByClassName('close')[0];
    searchClose.onclick=function(){
        let input=searchMain.getElementsByTagName('input')[0];
        input.value="";
    }
}

window.onload = function (e) {
    let rightInfo=document.getElementsByClassName('right-info')[0];

    frameOnClick(e);
    searchFrameOnClick(e);
    contactOnClick(e,rightInfo);
    allRiff();
    rightInfoChange(rightInfo);
    leftNavShrink(e);
    bellAnimation(e);
    statisticsExpand(e);
    onLoadHidden();
    userSetting();
    headImgUpLoad();
    editUserInfo();
    deleteSearchInput();
    dragFrame(e,'list');
    leftSearchOnClick();
    darkModel();
}

//加载主聊天框
function onLoadChatMain(){
    if(contactActive==-1) return;
    let chatMain=document.getElementById('chat-main');
    let bodyHeight=(window.innerHeight) ? window.innerHeight : (document.documentElement && document.documentElement.clientHeight) ? document.documentElement.clientHeight : document.body.offsetHeight;
    chatMain.style.height=""+(bodyHeight-136)+"px";
    chatMain.style.width="100%";
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
            flag=true
        }
    }
}