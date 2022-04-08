
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
