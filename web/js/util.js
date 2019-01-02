
function showUploadMsg() {
    var index = layer.open({
        type: 1,
        title: false,
        skin: "layui-layer-lan",
        closeBtn: 0,
        anin: 2,
        scrollbar: false,
        shadeClose: false,
        content: '<div style="padding: 50px; line-height: 22px; background-color: #393D49; color: #fff; font-weight: 300;"><div class="layui-layer-loading"></div>正在上传，请稍等</div>'
    });
    return index;
}

/*
 * 获取工程的路径
 */
function getRootPath() {
    var pathName = window.location.pathname.substring(1);
    var webName = pathName == '' ? '' : pathName.substring(0, pathName
            .indexOf('/'));
    var url = window.location.protocol + '//' + window.location.host + '/'
        + webName + '/';
    return url;
}