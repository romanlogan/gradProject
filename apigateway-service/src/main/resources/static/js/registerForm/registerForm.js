function sendRegistration(){

    // var token = $("meta[name='_csrf']").attr("content");
    // var header = $("meta[name='_csrf_header']").attr("content");

    var paramData = {
        email : document.getElementById("email").value,
        name : document.getElementById("name").value,
        pwd : document.getElementById("password").value
    };

    var param = JSON.stringify(paramData);

    $.ajax({
        url      : "/user-service/users",
        type     : "POST",
        contentType : "application/json",
        data     : param,
        // beforeSend : function(xhr){
        //     /* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
        //     xhr.setRequestHeader(header, token);
        // },
        dataType : "json",
        cache   : false,
        success  : function(result, status, data){

            console.log("data : "+data);
            console.log("result :  " +result);
            console.log("result.code : "+result.code);
            console.log("result.status : "+result.status);
            // console.log("result.errorMap.name.rejectValue: "+result.errorMap.name.rejectValue);

            console.log('status :' + status);

            if (result.code == '400') {
                console.log('---------400--------')

                if (result.errorMap.name !== undefined) {

                    showErrorMessage(result.errorMap.name.message);

                    // console.log("result.errorMap.name.message: "+result.errorMap.name.message);
                    // alert(result.errorMap.name.message);
                }

                if (result.errorMap.email !== undefined) {
                    showErrorMessage(result.errorMap.email.message);
                    // console.log("result.errorMap.email.message: "+result.errorMap.email.message);
                    // alert(result.errorMap.email.message);
                }

                if (result.errorMap.pwd !== undefined) {
                    showErrorMessage(result.errorMap.pwd.message);
                    // console.log("result.errorMap.pwd.message: "+result.errorMap.pwd.message);
                    // alert(result.errorMap.pwd.message);
                }


                // 2~3 개 필드가 동시에 에러가 나면 0번째를 가져오면 안됨
                // 그러면 message 를 list 가 아니라 map 으로 가져와야 하나 ?
                // var commentError = document.getElementById("commentError");
                // commentError.innerText = result.messageList[0];
            }


            if(result.status === undefined){

                Swal.fire({
                    title: 'Your registration has been successful.',
                    // text: 'Email or password does not exist or is incorrect',
                    icon: 'success',
                    background: 'rgb(249, 238, 222)', // 배경색
                    color: 'rgba(95, 56, 39)',       // 글자색
                    confirmButtonText: 'OK'
                });


                // alert("가입이 성공되었습니다.");
                setTimeout(() => {
                    location.href='/game-service/main';
                }, 700);

            }

        },
        error : function(jqXHR, status, error, request){

            //에러를 반환하면 여기로 옴
            // console.log(jqXHR.status+"\n"+jqXHR.responseText+"\n"+error)
            console.log(jqXHR.responseText);


            // alert(jqXHR.status+"\n"+jqXHR.responseText+"\n"+error);
            alert(jqXHR.responseText);


        }
    });
}

function showErrorMessage(message){
    Swal.fire({
        title: message,
        // text: 'Email or password does not exist or is incorrect',
        icon: 'error',
        background: 'rgb(249, 238, 222)', // 배경색
        color: 'rgba(95, 56, 39)',       // 글자색
        confirmButtonText: 'OK'
    });
}

function getLoginForm(){

    location.href="/user-service/loginForm"
}