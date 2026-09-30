// validator Utils
var vac = vac || {};


vac = (function(){
    'use strict';
    let ad_server = "http://127.0.0.1:8888";

    return{
        // 핸들러
        handler: {
            // 길이제한
            maxlength: function(e){
                var e 			= e || window.event
                    ,keyCode 	= (e.which) ? e.which : e.keyCode;

                var maxlength = $(e.target).attr("maxlength");
                var value = e.target.value;

                if(value.length > maxlength) {
                    e.target.value = value.slice(0, maxlength);
                }
            },
            // 숫자
            onlyNumeric: function (e) {
                var e 			= e || window.event
                    ,keyCode 	= (e.which) ? e.which : e.keyCode;

                if (keyCode == 8 || keyCode == 46 || keyCode == 37 || keyCode == 39) {
                    return;
                } else {
                    e.target.value = e.target.value.replace(/[^0-9]/g, '');
                }
            },
            // 영문
            onlyAlpha: function (e) {
                var e 			= e || window.event
                    ,keyCode 	= (e.which) ? e.which : e.keyCode;

                if (keyCode == 8 || keyCode == 46 || keyCode == 37 || keyCode == 39) {
                    return;
                } else {
                    e.target.value = e.target.value.replace(/[^a-zA-Z]/g, '');
                }
            },
            // 숫자 + 영문
            onlyAlphaNumeric:function (e) {
                var e			= e || window.event
                    ,keyCode	= (e.which) ? e.which : e.keyCode;

                if (keyCode == 8 || keyCode == 46 || keyCode == 37 || keyCode == 39) {

                } else {
                    e.target.value = e.target.value.replace(/[^a-zA-Z0-9]/g, '');
                }
            }
        },
        // 문자열
        string: {
            /**
             * 문자가 빈문자열일 경우 null을 반환한다.
             * @param str
             * @returns {null|*}
             */
            nvl : function (str){
                if(_.isEmpty(str)) return null;
                return str;
            },
            /**
             * 문자가 빈문자열일 경우 지정문자로 반환한다.
             * @param str 문자
             * @param str2 지정문자
             * @returns {*}
             */
            nvl2 : function (str, str2){
                if(_.isEmpty(str)) return str2;
                return str;
            },
            /**
             * vac.string.format('{0}입니다.', '문자형') => 문자형입니다.
             * @param format 포멧
             * @param args 파라미터
             * @returns 포멧팅문자열
             */
            format: function (format, ...args){
                return format.replace(/{(\d+)}/g, function(match, num) {
                    num = Number(num);
                    return typeof(args[num]) != undefined ? args[num] : match;
                });
            },
            /**
             * 문자열 이스케이프
             */
            htmlEscape:function(str) {
                return str
                    .replace(/&/g, '&amp;')
                    .replace(/"/g, '&quot;')
                    .replace(/'/g, '&#39;')
                    .replace(/</g, '&lt;')
                    .replace(/\//g, '&#92;')
                    .replace(/>/g, '&gt;');
            },
            /**
             * 문자열 언이스케이프
             */
            htmlUnescape:function(str) {
                return str
                    .replace(/&quot;/g, '"')
                    .replace(/&#39;/g, "'")
                    .replace(/&#x27;/g, "'")
                    .replace(/&lt;/g, '<')
                    .replace(/&gt;/g, '>')
                    .replace(/&#92;/g, '/')
                    .replace(/&#x2F;/g, '/')
                    .replace(/&amp;/g, '&')
                    .replace(/&#034;/g, '"');
            },
            /**
             * Url인코딩된 문자열인지 확인
             */
            isEncoded: function(str) {
                str = str || '';
                return str !== decodeURIComponent(str);
            },
        },
        ajax: {
            // Form 요청
            submit: function (url, el, success, fail) {
                var slef = this;
                var formData = new FormData($(el)[0]);
                this._submit(url, formData, success, fail);
            },
            _submit: function (url, formData, success, fail) {
                var slef = this;

                this.post({
                    url: url,
                    data: formData,
                    cache: false,
                    contentType: false,
                    processData: false,
                    success: function (data) {
                        if (success) success(data);
                    },
                    error: function (xhr, status, error) {

                        if( xhr.status == 401) location.href = "/";

                        var result = JSON.parse(xhr.responseText)
                        if(fail){
                            fail(result)
                        }else{
                            alert(result.message);
                        }
                    }
                })
            },
            // Call
            call: function (opts){

                if(!opts.url) throw new Error("url은 필수값입니다.")
                if(!opts.method) throw new Error("method는 필수값입니다.")

                let successCallback = opts.success;
                let errorCallback = opts.error;

                var defaultOpts = {
                     async: true
                    ,beforeSend: function( xhr ) {
                        let csrf = $("meta[name='csrf']").attr('content');
                        xhr.setRequestHeader("X-CSRF-TOKEN", csrf);
                    }
                    ,success : function (data){
                        if(successCallback){
                            successCallback(data);
                        } else {
                            console.log(data);
                        }
                    }
                    ,error: function(xhr, status, error){

                         if( xhr.status == 401) location.href = "/";

                         if( errorCallback){
                             errorCallback(xhr, status, error);
                         }else{
                             var result = JSON.parse(xhr.responseText)
                             alert(result.message);
                         }
                    }
                }

                delete opts['success'];
                delete opts['error'];

                opts  = _.extend(defaultOpts, opts)
                console.log("vac.ajax.call opts=", opts);

                $.ajax(opts);
            },
            // Get 요청
            get: function (opts) {
                var defaultOpts = {
                    method: "GET"
                }
                if( opts.url && opts.url.indexOf("?") != -1){
                    var u = opts.url.split("?")
                    var b = u[0];
                    var q = u[1];
                    var nq = []
                    if(q){
                        q = q.split("&")
                        _.each(q, function (value) {
                            var t = value.split("=");
                            var k = t[0];
                            var v = vac.string.isEncoded(t[1]) ? t[1] : encodeURIComponent(t[1])
                            nq.push(k + "=" + v)
                        })
                        opts.url = b + "?" + nq.join("&");
                    }
                }
                opts  = _.extend(defaultOpts, opts)
                this.call(opts)
            },
            // Post 요청
            post: function (opts) {
                console.log("opts=",opts)
                var defaultOpts = {
                    method: "POST"
                }
                opts  = _.extend(defaultOpts, opts)
                this.call(opts)
            },
            link: function (opts) {

                let ad_url = ad_server + "/adlog";


                console.log("vac.ajax.link opts=", opts);
                if( opts.url && opts.url.indexOf("?") != -1){
                    var u = opts.url.split("?")
                    var b = u[0];
                    var q = u[1];
                    var nq = []
                    if(q){
                        q = q.split("&")
                        _.each(q, function (value) {
                            var t = value.split("=");
                            var k = t[0];
                            var v = vac.string.isEncoded(t[1]) ? t[1] : encodeURIComponent(t[1])
                            nq.push(k + "=" + v)
                        })
                        opts.params =  "?" + nq.join("&");
                        opts.url = ad_url + opts.params;
                    }

                    opts.refererUrl = b;
                } else {
                    opts.refererUrl = opts.url;
                    opts.url = ad_url; 
                }
                console.log("vac.ajax.link opts.url=", opts.url);
                console.log("vac.ajax.link opts.refererUrl=", opts.refererUrl);

                var defaultOpts = {
                    method: "GET"
                    ,beforeSend: function( xhr ) {
                        let csrf = $("meta[name='csrf']").attr('content');
                        xhr.setRequestHeader("X-CSRF-TOKEN", csrf);
                        xhr.setRequestHeader("Referer-URL", opts.refererUrl);
                    }
                    ,success : function (data){
                        if(data.code=="200"){
                            document.cookie = "referer_token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";//별도 함수 적용예정
                            document.cookie = "token_message=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
                            console.log('hash1 = '+data);
                            document.cookie = "referer_token="+data.refererToken +  "; path=/; max-age=30"; // 30초
                            document.cookie = "token_message="+data.tokenMessage + "; path=/; max-age=30"; // 30초
                            // SetCookie("token_message", data.message, 30); // 30초
                            console.log("cookie=", document.cookie);
                            if(opts.refererUrl){
                                console.log("vac.act.link2 refererUrl=", opts.refererUrl + opts.params);
                                if(opts.params && opts.params.length > 0){
                                    opts.refererUrl += opts.params;
                                }
                                location.href = opts.refererUrl;
                            }
                        } else if (data.code == "403") {
                            alert("접근이 거부되었습니다.");
                        } else {
                            alert("parameter에 문제가 있습니다.");
                        }

                    }
                    ,error: function(xhr, status, error){

                        console.log("vac.ajax.link error=", xhr, status, error);

                    }
                }

                opts  = _.extend(defaultOpts, opts)
                console.log("vac.act.link2 opts=", opts);   
                this.call(opts);

            }
        },
        act: {
            logs: function (data, url) {
                vac.ajax.post({
                    url: vac_url,
                    processData : false,
                    contentType : false,
                    data : data,
                    success : function(result) {
                        console.log('hash1 = '+result);

                        document.cookie = "code="+result;
                        // setCookie("code", result);
                        vasubmit();
                        // fetch(url, {
                        //     method: "POST",
                        //     headers: {"code": result},
                        //     body: data
                        // })
                        // .then(data => console.log(data))
                        // .catch(error => console.log(error));

                        // location.reload();

                        // const xhrHttp = new XMLHttpRequest();
                        // xhrHttp.open("POST", url);
                        // xhrHttp.setRequestHeader("code", result);
                        // xhrHttp.send(data);

                        // vac.ajax.post({
                        //     url: url,
                        //     processData : false,
                        //     contentType : false,
                        //     data : data,
                        //     beforeSend: function( xhr ) {
                        //         xhr.setRequestHeader("code", result);
                        //     },
                        //     success : function(result) {
                        //         console.log('hash2 = '+result);
                        //     },
                        //     error : function (xhr, status, error) {

                        //         alert("에러가 발생하였습니다.");
                        //     }
                        // })
                    },
                    error : function (xhr, status, error) {

                        alert("에러가 발생하였습니다.");
                    }
                });
            },
            link: function (url, data) {

                vac.ajax.get({
                    url: vac_url,
                    async: false,
                    data: data,
                    success : function(result) {
                        console.log('hash1 = '+result);
                        vac.ajax.get({
                            url: url,
                            async: false,
                            beforeSend: function( xhr ) {
                                xhr.setRequestHeader("code", result);
                            },
                            success : function(result) {
                                console.log('hash2 = '+result);
                            },
                            error : function (xhr, status, error) {

                                alert("에러가 발생하였습니다.");
                            }
                        })
                    },
                    error : function (xhr, status, error) {

                        alert("에러가 발생하였습니다.");
                    }
                })

            },
            
        }
    }
}());
function valink(link){
    
    let opts = {
        url: link
    }
    vac.ajax.link(opts);
    // if( url && url.indexOf("?") != -1){
    //     var u = url.split("?");
    //     var b = u[0];
    //     var q = u[1];
    //     vac.act.link(url, q);
    // }else{
    //     location.href = url;
    // }
}


