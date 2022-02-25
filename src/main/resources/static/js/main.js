
let statisticsActive=-1;
let websocket = null; //websocket连接服务器使用
function sendPost(url, data, callback) {
    axios({
        url: url,
        method: 'post',
        data: data,
        headers: {
            'Content-Type': "application/json;charset=UTF-8",
        },
    }).then(function (msg) {
        callback(msg);
    });
}

function sendGet(url, param, callback) {
    axios({
        url: url,
        method: 'get',
        params: param,
        headers: {
            'Content-Type': "application/json;charset=UTF-8",
        },
    }).then(function (msg) {
        callback(msg);
    });
}

//发送消息
function send(fromUser,toUser,cotent) {
    //获取输入的文本信息进行发送
    const chatMsg = {
        content: cotent,
        dest: toUser,
        origin: fromUser
    };

    websocket.send(JSON.stringify(chatMsg));
}


function formatDate(dateString){
    let time=new Date(dateString);
    return {
        year:time.getFullYear(),
        mouth:time.getMonth()+1,
        day:time.getDate(),
        hour:time.getHours(),
        minute:time.getMinutes()
    }
}

let vue = new Vue({
    el: "#app",
    computed: {
        showTitle() {
            return (this.messagebox.option === 0) ? this.messagebox.folderTitle : this.messagebox.title;
        },
        showMsg() {
            return (this.messagebox.option === 0) ? this.messagebox.folderMsg : this.messagebox.msg;
        },
        userPhone(){
            return this.userInfo.phone.slice(0,3)+' '+this.userInfo.phone.slice(3,7)+' '+this.userInfo.phone.slice(7);
        },
    },
    data: {
        currentChat:{
            contactInfo: {},
        },
        messagebox: {
            //0移除联系人分组 1其他提示信息
            option: 0,
            flag: false,
            title: " ",
            msg: " ",
            folderTitle: "移除联系人分组",
            folderMsg: "你确定要删除这个联系人分组吗？你的联系人分组将被删除",
        },
        userInfo: {
            name: '',
            nickname: '',
            gender: '',
            email: '',
            phone: '',
            id: -1,
            headportrait:'',
        },
        contactClassify: [],
        searchClassify: ['用户', '文件', '图片', '视频', '音频'],
        contact:[],
        searchResult:{
            user:[],
            file:[],
            photo:[],
            video:[],
            music:[]
        },
        searchContent:{
            content:''
        },
        selected:{
            folderName:'',
            selectedPerson:[]
        },
        addOrEdit:true,//true为添加 false为编辑
        frame:{
            frameActive:0,
            first_left:36,
        },
        contactSelect:{
            frameActive:-1,
            contactActive:-1,
        },
        sendInfo:{
            content:'',
            fromUser:'',
            toUser:'',
        },
    },
    methods: {
        changeInfo(e) {
            sendPost('/changeInfo', this.userInfo, (msg) => {
                let response = msg.data;
                let responseInfo = response.obj;
                Vue.set(vue.messagebox, 'option', 1);
                Vue.set(vue.messagebox, 'title', responseInfo.title);
                Vue.set(vue.messagebox, 'msg', responseInfo.msg);
                $('.create-chat-folder').css('display', 'block');
                $('.cancel').css('display', 'none');
                if (response.status === 500) {
                    if (responseInfo.email != null) {
                        $('#UserEmail').val(responseInfo.email);
                    }
                    if (responseInfo.phone != null) {
                        $('#UserPhone').val(responseInfo.phone);
                    }
                }
            });
            e.preventDefault();
        },
        cancelOption: function () {
            //取消提交联系人分类表单
            $('.create-chat-folder').css('display', 'none');
            //清空input内容
            $('#newFolderName').val("");
        },
        okOption: function () {
            $('.create-chat-folder').css('display', 'none');
            if (vue.messagebox.option === 0) {
                //提交联系人分类表单

            }
            else if(vue.messagebox.option===2){
                //提交新增联系人分类表单
                sendPost('/folder/put/'+getCookie('id'),
                    this.selected,
                    (msg)=>{
                        let response=msg.data;
                        if(response.status===200){
                            Vue.set(this.messagebox, 'option', -1);
                            Vue.set(this.messagebox, 'title', "联系人分组");
                            Vue.set(this.messagebox, 'msg', response.obj);
                            $('#folder-operation').css({'width':'0','opacity':'0'});
                            $('.user-setting-hidden:eq(1)').css({'width':'384px','opacity':'1'});
                            this.contactClassify.push(this.selected.folderName);
                            this.contact.push(this.selected.selectedPerson);
                            $('.create-chat-folder').css('display', 'block');
                            $('.cancel').css('display', 'none');
                        }
                        else{//status==500

                        }
                    });
            }
        },
        searchSubmit(){
            if(this.searchContent.content==='') return;
            sendGet('/search/'+getCookie('id'),
                this.searchContent,(msg)=>{
                    let data=msg.data.obj;
                    console.log(data.user);
                    this.searchResult.user=data.user;
                });
        },
        clearSearch(){
            this.searchContent.content="";
        },
        addSelected(){
            this.selected.selectedPerson.length=0;
            for(let person of this.contact[0]){
                if(person.select){
                    this.selected.selectedPerson.push(person);
                }
            }

            $('#folder-operation').css({'width':'384px','opacity':'1'});
            $('#select-mumbers').css({'width':'0','opacity':'0'});
        },
        deleteContactFromFolder(){
            // for(var i=0;i<folderItems.length;i++){
            //     folderItems[i].onclick=function(e){
            //         Vue.set(vue.messagebox, 'option', 0);
            //         // addRippleEffect(e,this,"rgba(120,120,120,.25)");
            //         let name=this.getElementsByTagName('span')[0].innerHTML;
            //         let folderFormInput=folderForm.getElementsByTagName('input')[0];
            //         folderFormInput.value=name;
            //         expandFolder(this);
            //         folderTitle.innerHTML="编辑分类";
            //         folderSetting.innerHTML="<ion-icon name='trash-outline'></ion-icon>";
            //     }
            // }
        },
        folderSubmit(){
            Vue.set(this.messagebox, 'option', -1);
            Vue.set(this.messagebox, 'title', "联系人分组");
            if(this.selected.folderName===""){
                Vue.set(this.messagebox, 'msg', "您的分组名为空");
                $('.create-chat-folder').css('display', 'block');
                $('.cancel').css('display', 'none');
                return;
            }
            for(let name of this.searchClassify){
                if(name===this.selected.folderName){
                    Vue.set(this.messagebox, 'msg', "您已有该分组名");
                    $('.create-chat-folder').css('display', 'block');
                    $('.cancel').css('display', 'none');
                    return;
                }
            }
            if(this.addOrEdit){
                Vue.set(this.messagebox, 'option', 2);
                Vue.set(this.messagebox, 'msg', "您确定添加此联系人分组？");
                $('.create-chat-folder').css('display', 'block');
            }
            else if(!this.addOrEdit){
                $('.create-chat-folder').css('display', 'block');
            }
        },
        frameClick(index,e){
            if(index===this.frame.frameActive||this.frame.frameActive<0) return;

            let left=addRippleEffect(e,e.target,"rgba(30, 144, 255, .15)");
            let width = e.target.getBoundingClientRect().width;
            $('.frame:eq('+index+')').css('color','dodgerblue');
            $('.frame:eq('+this.frame.frameActive+')').css('color','gray');

            //改变滑条的位置和大小
            $('#slider').css({'width':width+'px','left':left-this.frame.first_left+'px'});

            let contactList=document.getElementsByClassName('contact-list');
            contactList[this.frame.frameActive].style.width="0";
            contactList[this.frame.frameActive].style.opacity="0";
            contactList[index].style.width="100%";
            contactList[index].style.opacity="1";

            contactList[index].style.display="block";
            contactList[this.frame.frameActive].style.display="none";
            this.frame.frameActive=index;
        },
        contactClick(index,e){
            if(this.contactSelect.frameActive===this.frame.frameActive&&this.contactSelect.contactActive===index) return;
            if(this.contactSelect.contactActive>=0){
                $('.contact-list:eq('+this.contactSelect.frameActive+') '+'.contact-items:eq('+this.contactSelect.contactActive+')').removeClass('active');
            }
            this.contactSelect.contactActive=index;
            this.contactSelect.frameActive=this.frame.frameActive;
            this.currentChat.contactInfo=this.contact[this.contactSelect.frameActive][index];
            this.sendInfo.toUser=this.currentChat.contactInfo.contactid;
            e.target.classList.add('active');
            //打开信息栏
            $('.right-close:eq(0)').css('transform','');
            $('.ordinary:eq(0)').css({'width':'100%','opacity':'1'});
            $('.right-info:eq(0)').css({'width':'100%','opacity':'1'});
            //隐藏提示词
            $('.reminder:eq(0)').css('display','none');
            //显示聊天主界面
            $('.top:eq(0)').css('height','3.5rem');
            //显示聊天输入框
            $('.input-frame:eq(0)').css('height','4rem');
            onLoadChatMain();
            if(statisticsActive!==-1){
                $('.statistics-close-icon:eq(statisticsActive)').click();
            }
        },
        doNotDisturb(){
            this.currentChat.contactInfo.doNotDisturb=!this.currentChat.contactInfo.doNotDisturb;
            //查找所有修改bell
        },
        sendMsg(){
            send(this.sendInfo.fromUser,this.sendInfo.toUser,this.sendInfo.content);
            this.sendInfo.content="";
        },
        putMessage(message){
            let JsonObject=JSON.parse(message);
            let row=vue.contactSelect.frameActive,col=vue.contactSelect.contactActive;
            JsonObject.time=formatDate(JsonObject.time);
            this.contact[row][col].chatInfo.push(JsonObject);
        },

    },
    components: {
    },
});

function getCookie(name) {
    let strcookie = document.cookie;//获取cookie字符串
    let arrcookie = strcookie.split("; ");//分割
    //遍历匹配
    for (let i = 0; i < arrcookie.length; i++) {
        let arr = arrcookie[i].split("=");
        if (arr[0] === name) {
            return arr[1];
        }
    }
    return "";
}

function Contact(contactId, headPortrait,nickname,doNotDisturb,phone,index,chatInfo) {
    this.contactid=contactId;
    this.headportrait=headPortrait;
    this.nickname=nickname;
    this.doNotDisturb=doNotDisturb;
    this.phone=phone;
    if(index===0){
        this.select=false;
    }
    this.chatInfo=[];
    if(chatInfo!==null&&chatInfo!==undefined){
        for(let item of chatInfo){
            item.time=formatDate(item.time);
            this.chatInfo.push(item);
        }
    }
}
//向服务器请求用户信息（除密码）
(function () {
    axios({
        url: '/prepare/' + getCookie('id'),
        method: 'get',
    }).then(function (msg) {
        let userData = msg.data.obj.user;
        vue.userInfo.name = userData.name;
        vue.userInfo.nickname = userData.nickname;
        vue.userInfo.gender = userData.gender;
        vue.userInfo.email = userData.email;
        vue.userInfo.phone = userData.phone;
        vue.userInfo.id = userData.id;
        vue.userInfo.headportrait=(msg.data.obj.headportrait===null)?'./image/1.jpeg':'/headportrait/'+msg.data.obj.headportrait;

        vue.contactClassify = msg.data.obj.folder;

        for(let folder in vue.contactClassify){
            vue.contact.push([]);
        }

        //遍历服务器发送回的数据
        //将数据分类存储
        for(let p in msg.data.obj.contact){
            let person=msg.data.obj.contact[p];
            let index=0;
            for(let name of vue.contactClassify){
                if(person.folder===name)
                    break;
                index++;
            }
            let img=null;
            if(person.headportrait==null){
                img='./image/1.jpeg';
            }
            else{
                img='/headportrait/'+person.headportrait;
            }
            vue.contact[index].push(new Contact(
                person.contactid,
                img,
                person.nickname,
                person.doNotDisturb,
                person.phone,index,
                person.chatInfoList));
        }

        //改变滑条的位置和大小
        $('#slider').css('width','77.5px');

        vue.sendInfo.fromUser=getCookie('id');
    });
})();
