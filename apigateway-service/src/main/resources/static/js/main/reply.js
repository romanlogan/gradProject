
function showSuccessMessage(message){
    Swal.fire({
        title: message,
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


function openReplyModalBox(commentId){

    var modal = document.getElementById("myModal");
    var replyModalBox = document.getElementById("replyModal-content");

    // Check if addReplyBtn already exists and remove it if so
    var existingBtn = replyModalBox.querySelector('.sendBtn');
    if (existingBtn) {
        replyModalBox.removeChild(existingBtn);
    }

    const addReplyBtn = document.createElement('button');
    addReplyBtn.className = 'sendBtn';
    addReplyBtn.innerHTML = `<i class="fa-solid fa-paper-plane fa-2xl" style="color: #ffffff;"></i>`;
    addReplyBtn.value = commentId;
    addReplyBtn.onclick = () => addReply(commentId);
    replyModalBox.appendChild(addReplyBtn);

    modal.style.display = "block";
}

function addReply(commentId) {

    const token = localStorage.getItem('token');

    var paramData = {
        commentId : commentId,
        content : document.getElementById("replyContent").value
    };

    var param = JSON.stringify(paramData);

    $.ajax({
        url      : "/reply-service/save",
        type     : "POST",
        contentType : "application/json",
        data     : param,
        beforeSend : function(xhr){
            xhr.setRequestHeader("Authorization", "Bearer" + token);
        },
        dataType : "json",
        cache   : false,
        success  : function(result, status,data){
            showSuccessMessage('Reply has been successfully posted.');
        },
        error : function(jqXHR, status, error, result){

            if(jqXHR.status == 503){
                showErrorMessage('We apologize for the inconvenience. Please try again later.')
            }
            else if(jqXHR.status == 401){
                showLoginRequiredMessage();
            }
            else{
                alert(jqXHR.status+"\n"+jqXHR.responseText+"\n"+error);
            }
        }
    });

}

function closeModal() {
    var modal = document.getElementById("myModal");
    modal.style.display = "none";
}

function deleteReply(replyId) {
    const token = localStorage.getItem('token');
    var paramData = {
        replyId : replyId
    };
    var param = JSON.stringify(paramData);
    $.ajax({
        url      : "/reply-service/delete",
        type     : "DELETE",
        contentType : "application/json",
        data     : param,
        beforeSend : function(xhr){
            xhr.setRequestHeader("Authorization", "Bearer" + token);
        },
        dataType : "json",
        cache   : false,
        success  : function(result, status,data){
            showSuccessMessage('Successfully deleted the reply');

            if (result.code == '400') {
                if (result.errorMap.replyId !== undefined) {
                    showErrorMessage(result.errorMap.replyId.message);
                }
            }

            if(result.status === undefined) {
                showSuccessMessage('Reply successfully deleted');
            }
        },
        error : function(jqXHR, status, error, result){

            if(jqXHR.status == '200'){
                showSuccessMessage('reply successfully deleted');
            }
            else if(jqXHR.status == '401'){
                showLoginRequiredMessage();
            }
            else{
                showErrorMessage('Failed to deleted reply, please try again later');
            }

        }
    });
}

function updateReply() {

    const token = localStorage.getItem('token');
    var paramData = {
        replyId : document.getElementById("updateReplyBtn").value,
        content : document.getElementById("updateReplyContentRequest").value,
    };

    var param = JSON.stringify(paramData);

    $.ajax({
        url      : "/reply-service/update",
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
                showSuccessMessage('Reply has been successfully updated');
            }
        },
        error : function(jqXHR, status, error, result){
            if(jqXHR.status == '200'){
                showSuccessMessage('reply successfully updated');
            }
            else if(jqXHR.status == '401'){
                showLoginRequiredMessage();
            }
            else{
                showErrorMessage('Failed to update reply, please try again later');
            }
        }
    });
}