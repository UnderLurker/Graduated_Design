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

let vue = new Vue({
    el: "#app",
    computed: {
        showTitle() {
            return (this.messagebox.option == 0) ? this.messagebox.folderTitle : this.messagebox.title;
        },
        showMsg() {
            return (this.messagebox.option == 0) ? this.messagebox.folderMsg : this.messagebox.msg;
        },
        // getClassify(folder){
        //     return this.contact.filter((con)=>{
        //         return con.folder==folder;
        //     });
        // }
    },
    data: {
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
        },
        contactClassify: [],
        searchClassify: ['用户', '文件', '图片', '视频', '音频'],
        contact:[],
        searchResult:[[],[],[],[],[]],
        searchContent:{
            content:''
        }
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
                if (response.status == 500) {
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
            if (vue.messagebox.option == 0) {
                //提交联系人分类表单

            }
        },
        searchSubmit(){
            if(this.searchContent.content=='') return;
            sendGet('/search/'+getCookie('id'),
                this.searchContent,
                (msg)=>{
                let data=msg.data.obj;
                this.searchResult[0]=data.user;
            });
        },
        clearSearch(){
            this.searchContent.content="";
        }
    },
    components: {
        list: {
            props: ['msg'],
            template: `<div class="list">
                        <template v-for="(classify,index) in msg" :key="index">
                            <div class="frame">
                    <div class="title">
                        {{classify}}
                    </div>
                    <div class="unread">
                        <ion-icon name="alert-outline"></ion-icon>
                    </div>
                </div>
                        </template>
                    </div>`,
        },

    }
});

function getCookie(name) {
    let strcookie = document.cookie;//获取cookie字符串
    let arrcookie = strcookie.split("; ");//分割
    //遍历匹配
    for (let i = 0; i < arrcookie.length; i++) {
        let arr = arrcookie[i].split("=");
        if (arr[0] == name) {
            return arr[1];
        }
    }
    return "";
}

function Contact(contactId, headPortrait, unRead,nickname,doNotDisturb) {
    this.contactid=contactId;
    this.headportrait=headPortrait;
    this.unread=unRead;
    this.nickname=nickname;
    this.doNotDisturb=doNotDisturb;
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
                if(person.folder==name)
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
                person.unread,
                person.nickname,
                person.doNotDisturb));
        }
    });
})();
