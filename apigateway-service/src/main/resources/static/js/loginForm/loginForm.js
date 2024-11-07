
function registerForm(){
    location.href = "/user-service/registration";
}

function sendLoginForm(){

    // var token = $("meta[name='_csrf']").attr("content");
    // var header = $("meta[name='_csrf_header']").attr("content");

    var paramData = {
        email : document.getElementById("email").value,
        password : document.getElementById("password").value
    };

    var param = JSON.stringify(paramData);

    $.ajax({
        url      : "/user-service/login",
        type     : "POST",
        contentType : "application/json",
        data     : param,
        // beforeSend : function(xhr){
        //     /* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
        //     xhr.setRequestHeader(header, token);
        // },
        dataType : "json",
        cache   : false,
        success  : function(result, status){
            alert("로그인이 성공되었습니다.");
            location.href='/user-service/main';

        },
        error : function(jqXHR, status, error, request){

            // alert(jqXHR.status+"\n"+jqXHR.responseText+"\n"+error);

            // console.log(jqXHR.status+"\n"+jqXHR.responseText+"\n"+error);

            if(jqXHR.status == '200'){
                var token = jqXHR.getResponseHeader('token');

                Swal.fire({
                    title: 'Login successful',
                    // text: 'Email or password does not exist or is incorrect',
                    icon: 'success',
                    background: 'rgb(249, 238, 222)', // 배경색
                    color: 'rgba(95, 56, 39)',       // 글자색
                    confirmButtonText: 'OK'
                });

                localStorage.setItem('token', token);

                setTimeout(() => {
                    location.href='/game-service/main';
                }, 700);

            }
            else if(jqXHR.status == '500'){

                Swal.fire({
                    title: 'Oops...',
                    text: 'Email or password does not exist or is incorrect',
                    icon: 'error',
                    background: 'rgb(249, 238, 222)', // 배경색
                    color: 'rgba(95, 56, 39)',       // 글자색
                    confirmButtonText: 'OK'
                });
            }
            else{
                alert(jqXHR.status+"\n"+jqXHR.responseText+"\n"+error);
            }




            // 여기를 token 없이 보내면 main 에서 다시 validatejwt 를 검사하므로 expired 검사 로직을 vlidatejwt 로 옮기면 되지 않을
            // location.href='/game-service/main/'+token;
        }
    });
}