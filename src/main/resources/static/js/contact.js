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