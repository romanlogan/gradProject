
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


function openReplyModalBox(commentId){

    var modal = document.getElementById("myModal");
    var replyModalBox = document.getElementById("replyModal-content");

    // 이미 추가된 addReplyBtn이 있는지 확인하고 제거
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

    // const replyContentBox = document.getElementById('replyContentBox');
    //
    // if(replyContentBox.style.display === 'none'){
    //     replyContentBox.style.display = 'block';
    // }else{
    //     replyContentBox.style.display = 'none';
    // }
}

function addReply(commentId) {

    console.log('add reply');

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
            /* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
            xhr.setRequestHeader("Authorization", "Bearer" + token);
        },
        dataType : "json",
        cache   : false,
        success  : function(result, status,data){

            showSuccessMessage('Reply has been successfully posted.');

            // location.href = "http://127.0.0.1:8000/game-service/main"
        },
        error : function(jqXHR, status, error, result){

            if(jqXHR.status == 503){

                showErrorMessage('We apologize for the inconvenience. Please try again later.')

                // location.href = "http://127.0.0.1:8000/game-service/main"
            }
            else if(jqXHR.status == 401){

                showLoginRequiredMessage();
            }
            else{

                alert(jqXHR.status+"\n"+jqXHR.responseText+"\n"+error);
                // location.href = "http://127.0.0.1:8000/game-service/main"
            }


        }
    });

}

function closeModal() {

    var modal = document.getElementById("myModal");
    modal.style.display = "none";

}


function deleteReply(replyId) {

    console.log('try delete reply');

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
            /* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
            xhr.setRequestHeader("Authorization", "Bearer" + token);
        },
        dataType : "json",
        cache   : false,
        success  : function(result, status,data){

            showSuccessMessage('Successfully deleted the reply');

            if (result.code == '400') {
                console.log('---------400--------')

                if (result.errorMap.replyId !== undefined) {
                    showErrorMessage(result.errorMap.replyId.message);
                    console.log("result.errorMap.content.message: "+result.errorMap.replyId.message);
                }
            }

            if(result.status === undefined) {
                showSuccessMessage('Reply successfully deleted');
            }

            // location.href = "http://127.0.0.1:8000/game-service/main"
        },
        error : function(jqXHR, status, error, result){

            // alert(jqXHR.status+"\n"+jqXHR.responseText+"\n"+error);

            if(jqXHR.status == '200'){
                showSuccessMessage('reply successfully deleted');
            }
            else if(jqXHR.status == '401'){
                showLoginRequiredMessage();
            }
            else{
                showErrorMessage('Failed to deleted reply, please try again later');
            }

            // alert(jqXHR.status+"\n"+jqXHR.responseText+"\n"+error);

            // location.href = "http://127.0.0.1:8000/game-service/main"
        }
    });

}

function updateReply() {

    const token = localStorage.getItem('token');

    const replyId = document.getElementById("updateReplyBtn").value;
    const content = document.getElementById("updateReplyContentRequest").value;

    var paramData = {
        replyId : replyId,
        content : content,
    };

    var param = JSON.stringify(paramData);

    $.ajax({
        url      : "/reply-service/update",
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
                showSuccessMessage('Reply has been successfully updated');
            }

            // location.href = "http://127.0.0.1:8000/game-service/main"
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

            // showErrorMessage()

            // alert(jqXHR.status+"\n"+jqXHR.responseText+"\n"+error);

            // location.href = "http://127.0.0.1:8000/game-service/main"
        }
    });
}