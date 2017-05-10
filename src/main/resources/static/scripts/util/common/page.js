define(function (require, exports, module) {

    var $ = require('jquery');
    var laypage = require('laypage');

    function page(pageDom, url, param, callback) {

        param.page = param.page || 0;
        param.size = param.size || 0;

        $.ajax({
            url: url,
            data: param,
            success: function (data) {
                if (data.status == 'SUCCESS') {
                    if (data.content.content.length > 0) {
                        laypage({
                            cont: pageDom,
                            pages: data.content.totalPages,
                            curr: 0,
                            jump: function (e, first) {
                                if (!first) {
                                    param.page = e.curr - 1;
                                    $.ajax({
                                        url: url, data: param, success: function (res) {
                                            e.pages = e.last = res.content.totalPages;
                                            callback(res);
                                        }
                                    });
                                } else {
                                    callback(data);
                                }

                            }
                        });
                    }else{
                        callback(data);
                        $("#"+pageDom).html("");
                    }
                }
            }
        });
    }

    module.exports = page;

});