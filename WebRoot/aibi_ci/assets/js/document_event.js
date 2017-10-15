/**
 * User: wangfei8
 * Date: 13-6-27
 * Time: 上午10:39
 */
//防止退格键页面返回
$(document).keydown(function (e) {
    var keyEvent;
    if (e.keyCode == 8) {//判断是否是退格
        var d = e.srcElement || e.target;
        if (d.tagName.toUpperCase() == 'INPUT' || d.tagName.toUpperCase() == 'TEXTAREA') {
            keyEvent = d.readOnly || d.disabled;
        } else {
            keyEvent = true;
        }
    } else {
        keyEvent = false;
    }
    if (keyEvent) {
        e.preventDefault();
    }
});
