(function($) {
    // time
    $.blockUI.defaults.fadeIn = 200;
    $.blockUI.defaults.fadeOut = 200;
    // loading
    $.blockUI.defaults.message = "<h1 style='margin: 5px;'>正在执行，请耐心等候...</h1>";
    // css
    $.blockUI.defaults.css.border = "none";
    $.blockUI.defaults.css.baseZ = 1003;
    $.blockUI.defaults.css["-webkit-border-radius"] = "10px";
    $.blockUI.defaults.css["-moz-border-radius"] = "10px";
    $.blockUI.defaults.css["-o-border-radius"] = "10px";
    $.blockUI.defaults.css["-ms-border-radius"] = "10px";
    $.blockUI.defaults.css["border-radius"] = "10px";
    $.blockUI.defaults.css.padding = "15px";
    $.blockUI.defaults.css.opacity = 0.6;
    $.blockUI.defaults.css.color = "#FFFFFF";
    $.blockUI.defaults.css.backgroundColor = "#000000";
    $.blockUI.defaults.css.cursor = 'wait'; // 把鼠标置为等待状态
    // overlay css
    $.blockUI.defaults.overlayCSS.backgroundColor = "#000000"; // "#FFFFFF"
    $.blockUI.defaults.overlayCSS.opacity = 0.2; // 0.8
    $.blockUI.defaults.overlayCSS.cursor = 'wait'; 
    $.blockUI.defaults.cursorReset = 'default'; // 把鼠标置为默认状态
})(jQuery);
