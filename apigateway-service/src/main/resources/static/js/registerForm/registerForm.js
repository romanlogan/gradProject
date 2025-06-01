function sendRegistration(){

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
        dataType : "json",
        cache   : false,
        success  : function(result, status, data){

            if (result.code == '400') {
                if (result.errorMap.name !== undefined) {
                    showErrorMessage(result.errorMap.name.message);
                }

                if (result.errorMap.email !== undefined) {
                    showErrorMessage(result.errorMap.email.message);
                }

                if (result.errorMap.pwd !== undefined) {
                    showErrorMessage(result.errorMap.pwd.message);
                }
            }

            if(result.status === undefined){

                Swal.fire({
                    title: 'Your registration has been successful.',
                    icon: 'success',
                    background: 'rgb(249, 238, 222)',
                    color: 'rgba(95, 56, 39)',
                    confirmButtonText: 'OK'
                });

                setTimeout(() => {
                    location.href='/game-service/main';
                }, 700);

            }

        },
        error : function(jqXHR, status, error, request){
            alert(jqXHR.responseText);
        }
    });
}

function showErrorMessage(message){
    Swal.fire({
        title: message,
        icon: 'error',
        background: 'rgb(249, 238, 222)',
        color: 'rgba(95, 56, 39)',
        confirmButtonText: 'OK'
    });
}

function getLoginForm(){

    location.href="/user-service/loginForm"
}