document.getElementById('photo-close').onclick = function () {
    let player = $('.photo-player').get('0');
    player.style.display = 'none';
    var photoPlayer = document.getElementById('photo-player');
    photoPlayer.style.left = '0px';
    photoPlayer.style.top = '0px';
    photoPlayer.style.width=photoWidth+'px';
    photoPlayer.style.height=photoHeight+'px';
}
//设1rem=16px
const rem = 16;
autoSize = function (e) {
    let imageTag = $(e.target).parent().get('0');
    let naturalWidth = $(e.target).get('0').naturalWidth;
    let naturalHeight = $(e.target).get('0').naturalHeight;
    let rate = naturalWidth / naturalHeight;
    if (imageTag.classList[0] !== 'photo-frame') {
        let normalSize = 7 * rem; //高度固定，宽度随比例变化
        let other = normalSize * rate;

        imageTag.style.width = other + 'px';
        imageTag.style.height = normalSize + 'px';
    } else {
        //photo-frame
        let container = $(imageTag).parent().parent().get('0');
        let maxHeight = container.getBoundingClientRect().height * 0.95;
        let width = imageTag.getBoundingClientRect().width;
        let height = imageTag.getBoundingClientRect().height;
        let calcWidth = width;
        let calcHeight = height;

        //以底边为最长，计算高
        let tempHeight = width / rate;
        if (tempHeight <= maxHeight) {
            calcHeight = tempHeight;
        } else {
            calcWidth = height * rate;
        }
        imageTag.style.width = calcWidth + 'px';
        imageTag.style.height = calcHeight + 'px';
        radio = 1;
        photoWidth = calcWidth;
        photoHeight = calcHeight;
    }
}
let radio;
let photoWidth, photoHeight;

reSetSize = function () {
    $('#photo-player').width(photoWidth * radio);
    $('#photo-player').height(photoHeight * radio);
}
