let statisticsActive = -1;
let websocket = null; //websocket连接服务器使用
let emojiActive = 0;
let activeLeft;
let sendByJson= { 'Content-Type': "application/json;charset=UTF-8"};
let sendByForm={ 'Content-Type': 'multipart/form-data' };

function sendPost(url, data, headers, callback) {
    axios({
        url: url,
        method: 'post',
        data: data,
        headers: headers,
    }).then(function (msg) {
        callback(msg);
    });
}
/**
 * 
 * @param {string} url 
 * @param {object} param 
 * @param {object} headers 
 * @param {func} callback 
 */
function sendGet(url, param, headers, callback) {
    axios({
        url: url,
        method: 'get',
        params: param,
        headers: headers,
    }).then(function (msg) {
        callback(msg);
    });
}

//发送消息
function send(fromUser, toUser, content, file) {
    //获取输入的文本信息进行发送
    const chatMsg = {
        content: content,
        dest: toUser,
        origin: fromUser,
        file: file
    };

    websocket.send(JSON.stringify(chatMsg));
}

function CSTToGMT(strDate, isDatabase) {
    let dateStr = strDate.split(" ");
    let strGMT = dateStr[0] + " " + dateStr[1] + " " + dateStr[2] + " " + dateStr[5] + " " + dateStr[3] + " GMT+0800";
    let date = new Date(Date.parse(strGMT));
    return date;
}

function formatDate(dateString, isDatabase) {
    let time = null;
    if (isDatabase) {
        time = new Date(dateString);
    } else {
        time = CSTToGMT(dateString);
    }
    let min = time.getMinutes();
    if (min < 10) min = '0' + min;
    return {
        year: time.getFullYear(),
        mouth: time.getMonth() + 1,
        day: time.getDate(),
        hour: time.getHours(),
        minute: min
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
        userPhone() {
            return phoneStyle(this.userInfo.phone);
        },
        showOutLineTime(){
            let time=this.currentChat.contactInfo.misTiming;
            if(time>21){
                return '已经很久没有上线了';
            }
            else if(time>7){
                return '已经有'+time%7+'周多没有上线了';
            }
            else if(time>0){
                return '最近几天刚刚上线过';
            }
            else if(time===0){
                return '刚刚上线';
            }
        }
    },
    data: {
        currentChat: {
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
            headportrait: '',
            emojilist: [],
        },
        contactClassify: [],
        searchClassify: ['用户', '文件', '图片', '视频', '音频'],
        contact: [],
        searchResult: {
            user: [],
            file: [],
            photo: [],
            video: [],
            music: []
        },
        searchContent: {
            content: '',
        },
        selected: {
            folderName: '',
            selectedPerson: []
        },
        addOrEdit: true, //true为添加 false为编辑
        frame: {
            frameActive: 0,
            first_left: 36,
        },
        contactSelect: {
            frameActive: 0,
            contactActive: -1,
        },
        sendInfo: {
            fromUser: '',
            toUser: '',
        },
        emojiArr: emojiArr,
        media: {
            audio: false,
        }
    },
    methods: {
        changeInfo(e) {
            sendPost('/changeInfo', this.userInfo, sendByJson,(msg) => {
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
            $('#reset-contact-name').css('display', 'none');
            //清空input内容
            $('#newFolderName').val("");
            $('#media').val('');
            $('#resetName').val('')
        },
        okOption: function () {
            $('.create-chat-folder').css('display', 'none');
            if (vue.messagebox.option === 0) {
                //提交联系人分类表单

            } else if (vue.messagebox.option === 2) {
                //提交新增联系人分类表单
                sendPost('/folder/put/' + getCookie('id'),
                    this.selected, sendByJson, (msg) => {
                        let response = msg.data;
                        if (response.status === 200) {
                            Vue.set(this.messagebox, 'option', -1);
                            Vue.set(this.messagebox, 'title', "联系人分组");
                            Vue.set(this.messagebox, 'msg', response.obj);
                            $('#folder-operation').css({
                                'width': '0',
                                'opacity': '0'
                            });
                            $('.user-setting-hidden:eq(1)').css({
                                'width': '384px',
                                'opacity': '1'
                            });
                            this.contactClassify.push(this.selected.folderName);
                            this.contact.push(this.selected.selectedPerson);
                            $('.create-chat-folder').css('display', 'block');
                            $('.cancel').css('display', 'none');
                        } else { //status==500

                        }
                    });
            } else if (vue.messagebox.option === 3) {
                //发送多个文件
                let formData = new FormData();
                let files = $('#media').prop('files');
                for (let file of files) {
                    formData.append('files', file);
                }
                sendPost('/uploadMultipleFiles/' + getCookie('id') + '/' + this.currentChat.contactInfo.contactid, formData, sendByForm, (msg) => {
                    let response = msg.data.obj;
                    for (let item of response) {
                        this.putMessage(item);
                    }
                });
                $('#media').val('');
            } else if(vue.messagebox.option === 4){
                //删除联系人
                let id=this.currentChat.contactInfo.contactid;
                sendPost('/contact/delete/'+getCookie('id'),{
                    'id':id
                }, sendByJson,(msg)=>{
                    let response=msg.data;
                    if(response.status===200){
                        vueDeleteContact(response.obj);
                    }
                });
            } else if(vue.messagebox.option === 5){
                //删除聊天记录
                // sendPost('/chatInfo/delete/'+getCookie('id'),)
            }
        },
        searchSubmit() {
            if (this.searchContent.content === '') return;
            sendGet('/search/' + getCookie('id'),
                this.searchContent,null, (msg) => {
                    let data = msg.data.obj;
                    this.searchResult.user = data.user;
                    formatDateList(data.file);
                    formatDateList(data.photo);
                    formatDateList(data.video);
                    formatDateList(data.music);
                    this.searchResult.file=data.file;
                    this.searchResult.photo=data.photo;
                    this.searchResult.video=data.video;
                    this.searchResult.music=data.music;
                });
        },
        clearSearch() {
            this.searchContent.content = "";
        },
        changFlag() {
            this.addOrEdit = true;
            this.selected.folderName = '';
            this.selected.selectedPerson = [];
        },
        addSelected() {
            this.selected.selectedPerson.length = 0;
            for (let person of this.contact[0]) {
                if (person.select) {
                    this.selected.selectedPerson.push(person);
                }
            }

            $('#folder-operation').css({
                'width': '384px',
                'opacity': '1'
            });
            $('#select-mumbers').css({
                'width': '0',
                'opacity': '0'
            });
        },
        deleteContactFromFolder(index, e) {
            this.addOrEdit = false;
            this.selected.folderName = this.contactClassify[index];
            this.selected.selectedPerson = this.contact[index];
            $('#folder-operation').css({
                'width': '384px',
                'opacity': '1'
            });
            $('.user-setting-hidden:eq(1)').css({
                'width': '0',
                'opacity': '0'
            });
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
        folderSubmit() {
            Vue.set(this.messagebox, 'option', -1);
            Vue.set(this.messagebox, 'title', "联系人分组");
            if (this.selected.folderName === "") {
                Vue.set(this.messagebox, 'msg', "您的分组名为空");
                $('.create-chat-folder').css('display', 'block');
                $('.cancel').css('display', 'none');
                return;
            }
            for (let name of this.searchClassify) {
                if (name === this.selected.folderName) {
                    Vue.set(this.messagebox, 'msg', "您已有该分组名");
                    $('.create-chat-folder').css('display', 'block');
                    $('.cancel').css('display', 'none');
                    return;
                }
            }
            if (this.addOrEdit) {
                Vue.set(this.messagebox, 'option', 2);
                Vue.set(this.messagebox, 'msg', "您确定添加此联系人分组？");
                $('.create-chat-folder').css('display', 'block');
            } else if (!this.addOrEdit) {
                $('.create-chat-folder').css('display', 'block');
            }
        },
        frameClick(index, e) {
            if (index === this.frame.frameActive || this.frame.frameActive < 0) return;

            let left = addRippleEffect(e, e.target, "rgba(30, 144, 255, .15)");
            let width = e.target.getBoundingClientRect().width;
            $('.frame:eq(' + index + ')').css('color', 'dodgerblue');
            $('.frame:eq(' + this.frame.frameActive + ')').css('color', 'gray');

            //改变滑条的位置和大小
            $('#slider').css({
                'width': width + 'px',
                'left': left - this.frame.first_left + 'px'
            });

            let contactList = document.getElementsByClassName('contact-list');
            contactList[this.frame.frameActive].style.width = "0";
            contactList[this.frame.frameActive].style.opacity = "0";
            contactList[index].style.width = "100%";
            contactList[index].style.opacity = "1";

            contactList[index].style.display = "block";
            contactList[this.frame.frameActive].style.display = "none";
            this.frame.frameActive = index;
        },
        contactClick(index, e) {
            if (this.frame.frameActive===(this.contact.length-1)) return;
            if (this.contactSelect.frameActive === this.frame.frameActive && this.contactSelect.contactActive === index) return;
            if (this.contactSelect.contactActive >= 0) {
                $('.contact-list:eq(' + this.contactSelect.frameActive + ') ' + '.contact-items:eq(' + this.contactSelect.contactActive + ')').removeClass('active');
            }
            this.contactSelect.contactActive = index;
            this.contactSelect.frameActive = this.frame.frameActive;
            this.currentChat.contactInfo = this.contact[this.contactSelect.frameActive][index];
            this.sendInfo.toUser = this.currentChat.contactInfo.contactid;
            e.target.classList.add('active');
            //打开信息栏
            $('.right-close:eq(0)').css('transform', '');
            $('.ordinary:eq(0)').css({
                'width': '100%',
                'opacity': '1'
            });
            $('.right-info:eq(0)').css({
                'width': '100%',
                'opacity': '1'
            });
            //隐藏提示词
            $('.reminder:eq(0)').css('display', 'none');
            //显示聊天主界面
            $('.top:eq(0)').css('height', '3.5rem');
            //显示聊天输入框
            $('.input-frame:eq(0)').css('height', '4rem');
            onLoadChatMain();
            if (statisticsActive !== -1) {
                $('.statistics-close-icon:eq(statisticsActive)').click();
            }
        },
        doNotDisturb() {
            this.currentChat.contactInfo.doNotDisturb = !this.currentChat.contactInfo.doNotDisturb;
            //查找所有修改bell
        },
        sendMsg(file) {
            send(this.sendInfo.fromUser, this.sendInfo.toUser, $('#editor').val(), file);
            $('#editor').val('');
        },
        putMessage(message) {
            let row = vue.contactSelect.frameActive,
                col = vue.contactSelect.contactActive;
            if (typeof message === 'string') {
                let JsonObject = JSON.parse(message);
                if(addFriendInfo(JsonObject)) return;
                if(JsonObject.deleteContact){
                    vueDeleteContact(JsonObject);
                    return;
                }
                JsonObject.time = formatDate(JsonObject.time, false);
                this.contact[row][col].chatInfo.push(JsonObject);
            } else {
                if(addFriendInfo(message)) return;
                if(message.deleteContact){
                    vueDeleteContact(message);
                    return;
                }
                message.time = formatDate(message.time, true);
                this.contact[row][col].chatInfo.push(message);
            }
        },
        submitHeadPortrait() {
            let formData = new FormData();
            let file = $('#image-input-frame').prop('files')[0];
            formData.append('headportrait', file, file.name);
            formData.append('scale', $('#scale').val());
            formData.append('x', $('#x').val());
            formData.append('y', $('#y').val());
            formData.append('w', $('#w').val());
            formData.append('h', $('#h').val());
            formData.append('originWidth', originWidth);
            formData.append('originHeight', originHeight);
            sendPost('/' + this.userInfo.id + '/headportrait', formData, {
                'Content-Type': 'multipart/form-data',
            }, (msg) => {
                this.userInfo.headportrait = msg.data.obj;
            });
        },
        emojiClick() {
            console.log(1);
        },
        emojiSwitch(index, e) {
            if (index === emojiActive) return;
            let currentLeft = e.target.getBoundingClientRect().left;
            let currentWidth = e.target.getBoundingClientRect().width;
            $('.emoji-container').css({
                'width': '0',
                'color': 'black'
            });
            $('.emoji-container:eq(' + index + ')').css({
                'width': '100%',
                'color': 'dodgerblue'
            });
            let left = (activeLeft === originLeft) ? (currentLeft - activeLeft) : currentLeft;
            $('#emoji-list-active').css({
                'width': currentWidth + 'px',
                'left': left + 'px'
            });
            emojiActive = index;
        },
        upLoadMedia() {
            $('#media').click();
        },
        download(no,filename) {
            handleExport('/file/' + no, filename);
        },
        audioPreview(fileStorageNo, filename) {
            // showMedia();
            this.media.audio = true;
            setTimeout(() => {
                initAudioEvent(fileStorageNo, filename);
            }, 500);
        },
        videoPreview(fileStorageNo, photo) {
            let player = $('.video-player').get('0');
            player.style.display = 'block';
            $('#video-player').attr({
                'poster': photo,
                'src': '/media/preview/' + fileStorageNo
            });
        },
        photoPreview(fileStorageNo, filename) {
            let player = $('.photo-player').get('0');
            player.style.display = 'block';
            $('#photo-player').attr({
                'src': '/photo/' + fileStorageNo
            });
            document.getElementById('photo-download').onclick = function () {
                handleExport('/file/' + fileStorageNo, filename);
            }
            //放大缩小每次变化为10%
            document.getElementById('photo-expand').onclick = function () {
                if (radio > 2) return;
                radio += 0.1;
                reSetSize();
            }
            document.getElementById('photo-shrink').onclick = function () {
                if (radio < 0.3) return;
                radio -= 0.1;
                reSetSize();
            }
            var photoPlayer = document.getElementById('photo-player');
            photoPlayer.onmousedown = function (e) {
                photoPlayer.style.cursor='move';
                e.preventDefault();
                var oLeft = e.clientX - this.offsetLeft;
                var oTop = e.clientY - this.offsetTop;
                document.onmousemove = function (e) {
                    let left=e.clientX-oLeft;
                    let top=e.clientY-oTop;
                    //图片移动出界判断
                    let l=left<photoWidth*radio*(0.1-1); //左
                    let r=left>photoWidth*(1-radio*0.1); //右
                    let u=top<photoHeight*radio*(0.1-1); //上
                    let d=top>photoHeight*(1-radio*0.1); //下
                    if(l||u||r||d){
                        if(l&&left<0||r&&left>0){
                            if((u&&top<0)||(d&&top>0)) return;
                            photoPlayer.style.top = top + 'px';
                        }
                        else if((u&&top<0)||(d&&top>0)){
                            if((l&&left<0)||(r&&left>0)) return;
                            photoPlayer.style.left = left + 'px';
                        }
                        return;
                    }
                    photoPlayer.style.left = left + 'px';
                    photoPlayer.style.top = top + 'px';
                }
                document.onmouseup = function () {
                    document.onmousemove = null;
                    photoPlayer.onmouseup = null;
                    photoPlayer.style.cursor='pointer';
                }
            }
        },
        addContact(contact){
            let headportrait=contact.headportrait.split('/')[contact.headportrait.split('/').length-1];
            sendPost('/addContact/'+getCookie('id'),{
                'id':contact.id,
                'headportrait':headportrait,
                'nickname':contact.nickname,
            },sendByJson,(msg)=>{
                if(msg.data.status===200){
                    Vue.set(vue.messagebox, 'option', -1);
                    Vue.set(vue.messagebox, 'title', "发送好友请求");
                    Vue.set(vue.messagebox, 'msg', contact.nickname+" 好友请求成功");
                    $('.create-chat-folder').css('display', 'block');
                    $('.cancel').css('display', 'none');
                }
            });
        },
        userAddContact(person){
            let userheadportrait=this.userInfo.headportrait.split('/')[this.userInfo.headportrait.split('/').length-1];
            let contactheadportrait=person.headportrait.split('/')[person.headportrait.split('/').length-1];
            //用户确认添加联系人
            sendPost('/'+getCookie('id')+'/userAddContact',{
                'contactId':person.contactid,
                'contactHeadportrait':contactheadportrait,
                'userHeadportrait':userheadportrait,
            },sendByJson,(msg)=>{
                if(msg.data.status===200){
                    this.contact[this.contact.length-1].splice(this.contact[this.contact.length-1].indexOf(person),1);
                    let response=msg.data.obj;
                    vueAddContact(response);
                }
            });
        },
        deleteContact(){
            let nickname=this.currentChat.contactInfo.nickname;
            Vue.set(this.messagebox, 'option', 4);
            Vue.set(this.messagebox, 'title', "删除联系人");
            Vue.set(this.messagebox, 'msg', "您确定删除 "+nickname+" 吗？");
            $('.create-chat-folder').css('display', 'block');
            $('.cancel').css('display', 'block');
        },
        deleteChatInfo(){
            let nickname=this.currentChat.contactInfo.nickname;
            Vue.set(this.messagebox, 'option', 5);
            Vue.set(this.messagebox, 'title', "删除聊天记录");
            Vue.set(this.messagebox, 'msg', "您确定删除与 "+nickname+" 的聊天记录吗？");
            $('.create-chat-folder').css('display', 'block');
            $('.cancel').css('display', 'block');
        },
    },
    components: {},
});

function getCookie(name) {
    let strcookie = document.cookie; //获取cookie字符串
    let arrcookie = strcookie.split("; "); //分割
    //遍历匹配
    for (let i = 0; i < arrcookie.length; i++) {
        let arr = arrcookie[i].split("=");
        if (arr[0] === name) {
            return arr[1];
        }
    }
    return "";
}

/**
 * 电话号码格式转换
 * @param {string} phone 
 * @returns 
 */
function phoneStyle(phone){
    return '+86 ' + phone.slice(0, 3) + ' ' + phone.slice(3, 7) + ' ' + phone.slice(7);
}
/**
 * 新添加好友的提示
 * @param {object} msg 
 * @returns msg是否是添加好友信息
 */
function addFriendInfo(msg){
    if(msg.friend){
        $('#addfriend-frame img').attr('src','/headportrait/'+msg.headportrait);
        $('#hint-info').text(msg.nickname+' 想要添加您为好友');
        $('#addfriend-info').removeClass('add-hint-unactive').addClass('add-hint-active');
        setTimeout(()=>{
            $('#addfriend-info').removeClass('add-hint-active').addClass('add-hint-unactive');
            vue.contact[vue.contact.length-1].push(new Contact(msg.id,'/headportrait/'+msg.headportrait,msg.nickname,false,msg.phone,1,null,null));
        },6000);
        return true;
    }
    else if(msg.sure){
        vueAddContact(msg);
    }
    return false;
}
//格式化数组时间
function formatDateList(obj){
    for(let item of obj){
        item.time=formatDate(item.time,true);
    }
}
function vueAddContact(contact){
    let img = null;
    if (contact.headportrait !== '1.jpeg') {
        img = '/headportrait/' + contact.headportrait;
    }
    else{
        img = './image/1.jpeg';
    }
    vue.contact[0].push(new Contact(contact.contactid,
        img,
        contact.nickname,
        false,contact.phone,0,null,
        contact.misTiming));
}
function vueDeleteContact(response){
    for(let i=0;i<vue.contact.length-1;i++){
        for(let j=0;j<vue.contact[i].length;j++){
            let person=vue.contact[i][j];
            if(person.contactid===response.id){
                vue.contact[i].splice(vue.contact[i].indexOf(person),1);
            }
        }
    }
    $('.left-nav-expand').click();
    vue.currentChat.contactInfo={};
    $('.ordinary:eq(0)').css({
        'width': '0',
        'opacity': '0'
    });
    $('.right-info:eq(0)').css({
        'width': '0',
        'opacity': '0'
    });
    //隐藏提示词
    $('.reminder:eq(0)').css('display', 'block');
    //显示聊天主界面
    $('.top:eq(0)').css('height', '0');
    //显示聊天输入框
    $('.input-frame:eq(0)').css('height', '0');
    Vue.set(vue.messagebox, 'option', 1);
    Vue.set(vue.messagebox, 'title', "删除联系人");
    Vue.set(vue.messagebox, 'msg', response.deleteContact===true?"已被 "+response.nickname+" 删除":"已成功删除 "+response.nickname);
    $('.create-chat-folder').css('display', 'block');
    $('.cancel').css('display', 'none');
}
function Contact(contactId, headPortrait, nickname, doNotDisturb, phone, index, chatInfo, misTiming) {
    this.contactid = contactId;
    this.headportrait = headPortrait;
    this.nickname = nickname;
    this.doNotDisturb = doNotDisturb;
    this.phone = phone;
    if (index === 0) {
        this.select = false;
    }
    this.chatInfo = [];
    if (chatInfo !== null && chatInfo !== undefined) {
        for (let item of chatInfo) {
            item.time = formatDate(item.time, true);
            //item.content=item.content.replace(/(<svg class="coolapk-emotion" aria-hidden="true">[^]*?<\/svg>)/g,'"</p>$1<p>"');
            if (item.file) {
                item.suffix = item.content.split('.').slice(-1)[0];
            }
            this.chatInfo.push(item);
        }
    }
    this.misTiming=misTiming;
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
        vue.userInfo.headportrait = (msg.data.obj.headportrait === null) ? './image/1.jpeg' : '/headportrait/' + msg.data.obj.headportrait;
        vue.userInfo.emojilist = msg.data.obj.emojiList;

        vue.contactClassify = msg.data.obj.folder;

        for (let folder in vue.contactClassify) {
            vue.contact.push(new Array());
        }

        //遍历服务器发送回的数据
        //将数据分类存储
        for (let person of msg.data.obj.contact) {
            let index = 0;
            for (let name of vue.contactClassify) {
                if (person.folder === name)
                    break;
                index++;
            }
            if(person.folder===null) index--;
            let img = null;
            if (person.headportrait == null) {
                img = '/image/1.jpeg';
            } else {
                img = '/headportrait/' + person.headportrait;
            }
            vue.contact[index].push(new Contact(
                person.contactid,
                img,
                person.name,
                person.doNotDisturb,
                phoneStyle(person.phone), index,
                person.chatInfoList,
                person.misTiming));
        }

        //改变滑条的位置和大小
        $('#slider').css('width', '77.5px');

        vue.sendInfo.fromUser = getCookie('id');
    });
})();