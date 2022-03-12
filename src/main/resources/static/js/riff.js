//点击扩散效果
function addRippleEffect(e,object,color){
    let rect = object.getBoundingClientRect();
    let ripple = object.querySelector('.ripple');
    if (!ripple) {
        ripple = document.createElement('span');
        ripple.className = 'ripple';
        ripple.style.height = ripple.style.width = Math.max(rect.width, rect.height) + 'px';
        ripple.style.backgroundColor=''+color;
        object.appendChild(ripple);
    }
    let top = e.pageY - rect.top - ripple.offsetHeight / 2 - document.body.scrollTop;
    let left = e.pageX - rect.left - ripple.offsetWidth / 2 - document.body.scrollLeft;
    ripple.style.top = top + 'px';
    ripple.style.left = left + 'px';
    ripple.classList.add('show');
    setInterval(null,750);
    ripple.classList.remove('show');

    return rect.left;
}
