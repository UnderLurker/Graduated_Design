$('#share-list-close').click(function(){
    $('#share-list').css('display','none');
});
$('#share-contact-info').click(function(){
    if(vue.contact[0].length<=1) return;
    $('#share-list').css('display','block');
});