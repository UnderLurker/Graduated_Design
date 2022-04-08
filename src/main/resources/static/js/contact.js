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


function vueAddContact(contact){
    let img = null;
    if (contact.headportrait !== null) {
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
    if(emojiShow)
        document.getElementById('emotion').click();
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

$('#editContactNickName').click(function(){
    $('#reset-contact-name').css('display','block');
    $('#resetName').keydown(function(e){
        if(e.keyCode===13){
            $('#submitResetName').click();
        }
    })
});
$('#submitResetName').click(function(){
    let resetName=$('#resetName').val().trim();
    if(resetName==='') return;
    sendPost('/resetName/'+getCookie('id'),{
        name:resetName,
        contactId:vue.currentChat.contactInfo.contactid
    },sendByJson,(msg)=>{
        let response=msg.data;
        if(response.status===200){
            vue.cancelOption();
            Vue.set(vue.messagebox, 'option', -1);
            Vue.set(vue.messagebox, 'title', "更改联系人昵称");
            Vue.set(vue.messagebox, 'msg', "成功修改联系人昵称");
            $('.create-chat-folder').css('display', 'block');
            $('.cancel').css('display', 'none');
            Vue.set(vue.currentChat.contactInfo,'nickname',resetName);
            for(let col of vue.contact){
                for(let person of col){
                    if(person.contactid===vue.currentChat.contactInfo.contactid){
                        person.nickname=resetName;
                    }
                }
            }
        }
    });
});
$('#blackContact').click(function(){
    sendPost('/black/'+getCookie('id'),{
        contactId:vue.currentChat.contactInfo.contactid
    },sendByJson,(msg)=>{
        Vue.set(vue.messagebox, 'option', -1);
        Vue.set(vue.messagebox, 'title', "拉黑联系人");
        $('.create-chat-folder').css('display', 'block');
        $('.cancel').css('display', 'none');
        if(msg.data.status==200){
            Vue.set(vue.messagebox, 'msg', "成功拉黑 "+vue.currentChat.contactInfo.nickname+" !!!");
        }
        else{
            Vue.set(vue.messagebox, 'msg', "拉黑 "+vue.currentChat.contactInfo.nickname+" 失败!!! 请稍后重试。");
        }
    });
});

blackContact = function(nickname){
    Vue.set(vue.messagebox, 'option', -1);
    Vue.set(vue.messagebox, 'title', "拉黑联系人");
    Vue.set(vue.messagebox, 'msg', "被 " + nickname + " 拉黑!!!");
    $('.create-chat-folder').css('display', 'block');
    $('.cancel').css('display', 'none');
}