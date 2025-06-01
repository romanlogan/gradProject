
function showSuccessMessage(message){
    Swal.fire({
        title: message,
        // text: 'Email or password does not exist or is incorrect',
        icon: 'success',
        background: 'rgb(249, 238, 222)', // 배경색
        color: 'rgba(95, 56, 39)',       // 글자색
        confirmButtonText: 'OK'
    });

    setTimeout(() => {
        location.href='/game-service/main';
    }, 700);
}

function showErrorMessage(message)
{
    Swal.fire({
        title: message,
        icon: 'error',
        background: 'rgb(249, 238, 222)', // 배경색
        color: 'rgba(95, 56, 39)',       // 글자색
        confirmButtonText: 'OK'
    });
}

function showLoginRequiredMessage(){

    Swal.fire({
        title: 'Please use after logging in.',
        icon: 'error',
        background: 'rgb(249, 238, 222)', // 배경색
        color: 'rgba(95, 56, 39)',       // 글자색
        confirmButtonText: 'OK'
    });

    setTimeout(() => {
        location.href='/user-service/loginForm';
    }, 700);
}


function saveComment(){

    const token = localStorage.getItem('token');

    var paramData = {
        gameId : 1,
        content : $("#commentContent").val()
    };

    var param = JSON.stringify(paramData);

    $.ajax({
        url      : "/comment-service/save",
        type     : "POST",
        contentType : "application/json",
        data     : param,
        dataType : "json",
        beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", "Bearer"+token);
        },
        cache   : false,
        success  : function(result, status,data){

            console.log("result.code : "+result.code);
            console.log("result.status : "+result.status);

            console.log('status :' + status);

            if (result.code == '400') {
                console.log('---------400--------')

                if (result.errorMap.content !== undefined) {

                    showErrorMessage(result.errorMap.content.message);


                    console.log("result.errorMap.content.message: "+result.errorMap.content.message);
                }

                // 2~3 개 필드가 동시에 에러가 나면 0번째를 가져오면 안됨
                // 그러면 message 를 list 가 아니라 map 으로 가져와야 하나 ?
                // var commentError = document.getElementById("commentError");
                // commentError.innerText = result.messageList[0];
            }


            if(result.status === undefined){

                showSuccessMessage('Comment has been successfully posted.');

            }
            // location.href = "http://127.0.0.1:8000/game-service/main"

        },
        error : function(jqXHR, status, error, result){

            if(jqXHR.status == 503){

                showErrorMessage('We apologize for the inconvenience. Please try again later.');

                // location.href = "http://127.0.0.1:8000/game-service/main"
            }else if(jqXHR.status == 401){
                showLoginRequiredMessage();

                // location.href = "http://127.0.0.1:8000/user-service/loginForm"
            }
            else if(jqXHR.status == 400){

                showErrorMessage(jqXHR.responseText);


                // alert(jqXHR.responseText);
                // location.href = "http://127.0.0.1:8000/game-service/main"
            }
            else if(jqXHR.status == 200){

                showSuccessMessage('Comment has been successfully posted.');


                // alert(jqXHR.responseText);
                // location.href = "http://127.0.0.1:8000/game-service/main"
            }
            else{

                alert(jqXHR.status+"\n"+jqXHR.responseText+"\n"+error);
                //this route will make problem because gateway host is not 127.0.0.1
                // location.href = "http://127.0.0.1:8000/game-service/main"
                location.href='/game-service/main';
            }

            // location.href = "http://127.0.0.1:8000/user-service/loginForm"
            // location.href = "http://127.0.0.1:8000/game-service/main"

        }
    });
}



function deleteComment(commentId) {

    const token = localStorage.getItem('token');

    var paramData = {
        commentId : commentId

    };

    var param = JSON.stringify(paramData);

    $.ajax({
        url      : "/comment-service/delete",
        type     : "DELETE",
        contentType : "application/json",
        data     : param,
        beforeSend : function(xhr){
            xhr.setRequestHeader("Authorization", "Bearer" + token);
        },
        dataType : "json",
        cache   : false,
        success  : function(result, status,data){

            if (result.code == '400') {
                console.log('---------400--------')

                if (result.errorMap.replyId !== undefined) {

                    showErrorMessage(result.errorMap.replyId.message);


                    console.log("result.errorMap.content.message: "+result.errorMap.replyId.message);
                }

                // 2~3 개 필드가 동시에 에러가 나면 0번째를 가져오면 안됨
                // 그러면 message 를 list 가 아니라 map 으로 가져와야 하나 ?
                // var commentError = document.getElementById("commentError");
                // commentError.innerText = result.messageList[0];
            }


            if(result.status === undefined){

                // showSuccessMessage('Comment has been successfully posted.');
                showSuccessMessage('Comment successfully deleted');
            }

            // location.href = "http://127.0.0.1:8000/game-service/main"
        },
        error : function(jqXHR, status, error, result){

            if(jqXHR.status == '200'){
                showSuccessMessage('Comment successfully deleted');
            }else if(jqXHR.status == 401){
                showLoginRequiredMessage();

            } else{
                showErrorMessage('Failed to delete comment, please try again later');
            }

            console.log(jqXHR.status+"\n"+jqXHR.responseText+"\n"+error);

            // location.href = "http://127.0.0.1:8000/game-service/main"
        }
    });

}

function updateComment() {

    const token = localStorage.getItem('token');

    const commentId = document.getElementById("updateCommentBtn").value;
    const content = document.getElementById("updateCommentContentRequest").value;

    var paramData = {
        commentId : commentId,
        content : content
    };

    var param = JSON.stringify(paramData);

    $.ajax({
        url      : "/comment-service/update",
        type     : "PUT",
        contentType : "application/json",
        data     : param,
        beforeSend : function(xhr){
            /* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
            xhr.setRequestHeader("Authorization", "Bearer" + token);
        },
        dataType : "json",
        cache   : false,
        success  : function(result, status,data){

            if (result.code == '400') {
                console.log('---------400--------')

                if (result.errorMap.content !== undefined) {
                    showErrorMessage(result.errorMap.content.message);
                    console.log("result.errorMap.content.message: "+result.errorMap.content.message);
                }
            }

            if(result.status === undefined){
                showSuccessMessage('Comment successfully updated');
            }
            // location.href = "http://127.0.0.1:8000/game-service/main"
        },
        error : function(jqXHR, status, error, result){

            if(jqXHR.status == '200'){
                showSuccessMessage('Comment successfully updated');
            }
            else if(jqXHR.status == 401){
                showLoginRequiredMessage();

            }
            else{
                showErrorMessage('Failed to update comment, please try again later');
            }



            console.log(jqXHR.status+"\n"+jqXHR.responseText+"\n"+error);

            // location.href = "http://127.0.0.1:8000/game-service/main"
        }
    });

}
