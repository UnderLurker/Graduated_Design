function Contact(contactId, headPortrait, nickname, doNotDisturb, phone, index, chatInfo, misTiming,relative) {
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
    this.relative=relative;
}
/**
 * 联系人的坐标
 * @param {number} row classify
 * @param {number} col 该列第几个
 */
function Coordinate(row,col){
    this.row=row;
    this.col=col;
}

/**
 * 每个联系人的所有位置
 * @param {number} contactId id
 * @param {number} row classify
 * @param {number} col 该列第几个
 */
function Index(contactId,row,col){
    this.contactId=contactId,
    this.coordinate=new Array();
    this.coordinate.push(new Coordinate(row,col));
    this.addCoordinate=function (row,col){
        this.coordinate.push(new Coordinate(row,col));
    }
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
        contact.misTiming,0));
    table.push(new Index(contact.contactid,0,vue.contact[0].length-1));
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
    for(let person of table){
        if(person.contactId===response.id){
            table.splice(table.indexOf(person),1);
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
    $('#resetName').val(vue.currentChat.contactInfo.nickname);
    $('#reset-contact-name').css('display','block');
    $('#resetName').keydown(function(e){
        if(e.keyCode===13){
            $('#submitResetName').click();
        }
    })
});

$('#cancelResetName').click(function(){
    vue.cancelOption(); 
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
//修改联系人关系
changeRelative=function(relative){
    for(let item of table){
        if(item.contactId===vue.currentChat.contactInfo.contactid){
            for(let obj of item.coordinate){
                let row=obj.row,col=obj.col;
                vue.contact[row][col].relative=relative;
            }
            break;
        }
    }
    
}

blackContact = function(nickname){
    Vue.set(vue.messagebox, 'option', -1);
    Vue.set(vue.messagebox, 'title', "拉黑联系人");
    Vue.set(vue.messagebox, 'msg', "被 " + nickname + " 拉黑!!!");
    $('.create-chat-folder').css('display', 'block');
    $('.cancel').css('display', 'none');
}
//还原被选中删除的人
reductionSelectedPerson=function(){
    //将tempSelectedPerson里的数据重新添加回vue.selected.selectedPerson中
    // {
    //     local:location,
    //     person:person
    // }
    for(let i=tempSelectedPerson.length-1;i>=0;i--){
        vue.selected.selectedPerson.splice(tempSelectedPerson[i].local,0,tempSelectedPerson[i].person);
    }
    tempSelectedPerson=[];
}