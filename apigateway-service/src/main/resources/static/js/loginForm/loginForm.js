
function registerForm(){
    location.href = "/user-service/registration";
}

function sendLoginForm(){

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
        dataType : "json",
        cache   : false,
        success  : function(result, status){
            alert("Login was successful.");
            location.href='/user-service/main';

        },
        error : function(jqXHR, status, error, request){

            if(jqXHR.status == '200'){
                var token = jqXHR.getResponseHeader('token');

                Swal.fire({
                    title: 'Login successful',
                    // text: 'Email or password does not exist or is incorrect',
                    icon: 'success',
                    background: 'rgb(249, 238, 222)',
                    color: 'rgba(95, 56, 39)',
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
                    background: 'rgb(249, 238, 222)',
                    color: 'rgba(95, 56, 39)',
                    confirmButtonText: 'OK'
                });
            }
            else{
                alert(jqXHR.status+"\n"+jqXHR.responseText+"\n"+error);
            }
        }
    });
}