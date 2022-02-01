(function(){
    let addFolder=document.querySelector('#add-folder');
    let folderOperation=document.querySelector('#folder-operation');
    let folderSettingClose=document.querySelector('#folder-setting-close');
    let userSettingHidden=null;
    let folderItems=document.getElementsByClassName('folder-items');
    let folderTitle=folderOperation.getElementsByTagName('h4')[0];
    let folderSetting=folderOperation.getElementsByClassName('folder-setting')[0];
    let folderForm=document.querySelector('.folder-form');
    let input_method=folderForm.getElementsByTagName('input');
    let folderContactList=document.querySelector('.folder-contact-list');
    let options=document.querySelector('.options');
    let optionsSelect=options.getElementsByTagName('div');
    let formSubmit=document.getElementById('submit-new-folder');
    let selectMumbers=document.getElementById('select-mumbers');
    let folderContactAddBtn=document.getElementById('folder-contact-add-btn');
    let selectList=document.getElementsByClassName('select-list')[0];
    let selectItems=selectList.getElementsByClassName('select-mumbers-items');
    let selectSetting=document.querySelector('.select-setting');
    let selectSettingClose=document.querySelector('.select-setting-close');
    let submitWayPut="put";
    let submitWayDelete="delete";

    //展开分类页面
    function expandFolder(obj){
        userSettingHidden=obj.parentNode.parentNode;
        userSettingHidden.style.width="0";
        userSettingHidden.style.opacity="0";
        folderOperation.style.width="384px";
        folderOperation.style.opacity="1";
    }
    //展开选择成员页面
    function expandSelect(obj){
        let parent=obj.parentNode.parentNode.parentNode.parentNode;
        parent.style.width="0";
        parent.style.opacity="0";
        selectMumbers.style.width="384px";
        selectMumbers.style.opacity="1";
    }
    //关闭选择成员页面
    function shrinkSelect(obj){
        let parent=obj.parentNode.parentNode;
        parent.style.width="0";
        parent.style.opacity="0";
        folderOperation.style.width="384px";
        folderOperation.style.opacity="1";
    }

    //添加成员点击事件
    folderContactAddBtn.onclick=function(){
        expandSelect(this);
        //设置列表高度
        //获得页面高度
        let bodyHeight=(window.innerHeight) ? window.innerHeight : (document.documentElement && document.documentElement.clientHeight) ? document.documentElement.clientHeight : document.body.offsetHeight;
        selectList.style.height=bodyHeight-80+"px";
    }

    //提交成员选择表单
    selectSetting.onclick=function(){
        let selectSubmit=document.getElementById('select-submit');
        selectSubmit.click();
    }

    //关闭成员选择页面
    selectSettingClose.onclick=function(e){
        // console.log(this);
        addRippleEffect(e,this,"rgba(30, 144, 255, 1)");
        shrinkSelect(this);
    }

    //为selectItems设置点击事件改变checkbox状态
    for(var i=0 ;i<selectItems.length;i++){
        selectItems[i].onclick=function(e){
            addRippleEffect(e,this,"rgba(30, 144, 255, 1)");
            let checkbox=this.getElementsByTagName('input')[0];
            if(checkbox.checked){
                checkbox.checked=false;
            }
            else{
                checkbox.checked=true;
            }
        }
    }


    //添加联系人分类
    addFolder.onclick=function(){
        expandFolder(this);
        folderTitle.innerHTML="添加分类";
        folderSetting.innerHTML="<ion-icon name='checkmark-outline'></ion-icon>";
        input_method[0].value=submitWayPut;
        
        let folderContactListParent=folderContactList.parentNode;
        let blankFolderContactList=document.createElement('div');

        if(folderContactList)
            folderContactList.remove();

        blankFolderContactList.className="folder-contact-list";
        folderContactListParent.appendChild(blankFolderContactList);
        folderForm.getElementsByTagName('input')[1].value="";
    }
    //关闭联系人文件夹设置
    folderSettingClose.onclick=function(e){
        addRippleEffect(e,this,"rgba(120,120,120,.25)");
        let folderPage=this.parentNode.parentNode;
        userSettingHidden.style.width="384px";
        userSettingHidden.style.opacity="1";
        folderPage.style.width="0";
        folderPage.style.opacity="0";
    }

    for(var i=0;i<folderItems.length;i++){
        folderItems[i].onclick=function(e){
            // addRippleEffect(e,this,"rgba(120,120,120,.25)");
            let name=this.getElementsByTagName('span')[0].innerHTML;
            let folderFormInput=folderForm.getElementsByTagName('input')[1];
            folderFormInput.value=name;
            expandFolder(this);
            folderTitle.innerHTML="编辑分类";
            folderSetting.innerHTML="<ion-icon name='trash-outline'></ion-icon>";
            input_method[0].value=submitWayDelete;
        }
    }

    function showCreateFolder(){
        let createChatFolder=document.querySelector('.create-chat-folder');
        createChatFolder.style.display="block";
    }
    function displayCreateFolder(){
        let createChatFolder=document.querySelector('.create-chat-folder');
        createChatFolder.style.display="none";
    }

    //为表单提交绑定
    folderSetting.onclick=function(){
        if(folderTitle.innerHTML=="编辑分类"){
            showCreateFolder();
        }
        else{
            formSubmit.click();
        }
    }

    //为options选项绑定点击事件
    for(var i=0;i<optionsSelect.length;i++){
        optionsSelect[i].onclick=function(){
            displayCreateFolder();
            if(this.className=="cancel"){
                return;
            }
            else{
                //确定删除
                formSubmit.click();
            }
        }
    }
    
})();