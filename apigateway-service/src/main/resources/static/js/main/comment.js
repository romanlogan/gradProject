
function showSuccessMessage(message){
    Swal.fire({
        title: message,
        // text: 'Email or password does not exist or is incorrect',
        icon: 'success',
        background: 'rgb(249, 238, 222)',
        color: 'rgba(95, 56, 39)',
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
        background: 'rgb(249, 238, 222)',
        color: 'rgba(95, 56, 39)',
        confirmButtonText: 'OK'
    });
}

function showLoginRequiredMessage(){

    Swal.fire({
        title: 'Please use after logging in.',
        icon: 'error',
        background: 'rgb(249, 238, 222)',
        color: 'rgba(95, 56, 39)',
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

            if (result.code == '400') {
                if (result.errorMap.content !== undefined) {
                    showErrorMessage(result.errorMap.content.message);
                }
            }

            if(result.status === undefined){
                showSuccessMessage('Comment has been successfully posted.');
            }
        },
        error : function(jqXHR, status, error, result){

            if(jqXHR.status == 503){
                showErrorMessage('We apologize for the inconvenience. Please try again later.');
            }else if(jqXHR.status == 401){
                showLoginRequiredMessage();
            }
            else if(jqXHR.status == 400){
                showErrorMessage(jqXHR.responseText);
            }
            else if(jqXHR.status == 200){
                showSuccessMessage('Comment has been successfully posted.');
            }
            else{
                alert(jqXHR.status+"\n"+jqXHR.responseText+"\n"+error);
                location.href='/game-service/main';
            }
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
                if (result.errorMap.replyId !== undefined) {
                    showErrorMessage(result.errorMap.replyId.message);
                }
            }

            if(result.status === undefined){
                showSuccessMessage('Comment successfully deleted');
            }
        },
        error : function(jqXHR, status, error, result){

            if(jqXHR.status == '200'){
                showSuccessMessage('Comment successfully deleted');
            }else if(jqXHR.status == 401){
                showLoginRequiredMessage();
            } else{
                showErrorMessage('Failed to delete comment, please try again later');
            }
        }
    });

}

function updateComment() {

    const token = localStorage.getItem('token');
    var paramData = {
        commentId : document.getElementById("updateCommentBtn").value,
        content : document.getElementById("updateCommentContentRequest").value
    };
    var param = JSON.stringify(paramData);
    $.ajax({
        url      : "/comment-service/update",
        type     : "PUT",
        contentType : "application/json",
        data     : param,
        beforeSend : function(xhr){
            xhr.setRequestHeader("Authorization", "Bearer" + token);
        },
        dataType : "json",
        cache   : false,
        success  : function(result, status,data){
            if (result.code == '400') {
                if (result.errorMap.content !== undefined) {
                    showErrorMessage(result.errorMap.content.message);
                }
            }

            if(result.status === undefined){
                showSuccessMessage('Comment successfully updated');
            }
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
        }
    });

}
