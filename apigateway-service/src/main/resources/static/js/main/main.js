$(document).ready(function() {

    // const textarea = document.getElementById('commentContent');
    // const charCount = document.getElementById('charCount');
    //
    // console.log(textarea)
    //
    // textarea.addEventListener('input', () => {
    //     charCount.textContent = textarea.value.length;
    // });

    // carousel
    const track = document.querySelector('.carousel-track');
    const slides = Array.from(track.children);
    const nextButton = document.querySelector('.carousel-button.right');
    const prevButton = document.querySelector('.carousel-button.left');

    const slideWidth = slides[0].getBoundingClientRect().width;

    // 각 슬라이드를 옆으로 배치
    slides.forEach((slide, index) => {
        slide.style.left = `${slideWidth * index}px`;
    });

    // 슬라이드를 이동하는 함수
    const moveToSlide = (track, currentSlide, targetSlide) => {
        track.style.transform = `translateX(-${targetSlide.style.left})`;
        currentSlide.classList.remove('current-slide');
        targetSlide.classList.add('current-slide');
    };

    // 이전 버튼 클릭 시
    prevButton.addEventListener('click', () => {
        const currentSlide = track.querySelector('.current-slide');
        const prevSlide = currentSlide.previousElementSibling;

        if (prevSlide) {
            moveToSlide(track, currentSlide, prevSlide);
        }
    });

    // 다음 버튼 클릭 시
    nextButton.addEventListener('click', () => {
        const currentSlide = track.querySelector('.current-slide');
        const nextSlide = currentSlide.nextElementSibling;

        if (nextSlide) {
            moveToSlide(track, currentSlide, nextSlide);
        }
    });

    //------------------------------


    var modal = document.getElementById("myModal");

    // Close modal when clicking outside the modal content
    window.onclick = function(event) {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    }


    //
    const token = localStorage.getItem('token');
    if (token) {
        console.log(token);
    }

    const loginBtn = document.getElementById('loginFormBtn');
    const logoutBtn = document.getElementById('logoutBtn');
    const myInfoBtn = document.getElementById('myInfoBtn');
    const registerFormBtn = document.getElementById('registerFormBtn');

    $.ajax({
        url      : "/user-service/validateJwt",
        type     : "GET",
        contentType : "application/json",
        beforeSend: function(xhr) {
            // 헤더에 토큰 추가
            xhr.setRequestHeader("Authorization", "Bearer"+token);
        },
        success  : function(result, status, data){

            //여기서 로그인 된 유저의 UUID 를 받기 ? or email 을 받아서
            // 이것이 존재하면 로그인된 유저
            //존재하지 않으면 로그인 안된 유저

            console.log("token is valid");
            logoutBtn.style.display = 'block';
            myInfoBtn.style.display = 'block';
            loginBtn.style.display = 'none';
            registerFormBtn.style.display = 'none';

            //    Comment 아래에 요소들을 돌면서 응답으로 온 email 과 요소에 이미 바인딩 되어있는 email 이 같으면 수정 , 삭제 버튼을 표시하기
            console.log("userID: " + result.loggedInUserId);

            let commentBox = document.getElementsByClassName('savedCommentBox');

            // add comment to update and delete btn
            for (let i = 0; i < commentBox.length; i++) {

                // result.loggedInUserId 와 savedCommentBox 의 value 를 비교 해서 같으면 버튼 추가
                const commentId = commentBox[i].querySelector('#savedCommentId').value;
                const commentUserEmail = commentBox[i].querySelector('#savedCommentUserEmail').value;

                console.log(commentId);
                console.log(commentUserEmail);

                if(commentUserEmail === result.loggedInUserId){

                    const deleteBtn = document.createElement('button');
                    deleteBtn.className = 'deleteCommentBtn';
                    deleteBtn.innerHTML = `<i class="fa-solid fa-trash-can fa-2xl" style="color: #ffffff;"></i>`;
                    deleteBtn.value = commentId;
                    deleteBtn.onclick = () => deleteComment(commentId);
                    commentBox[i].appendChild(deleteBtn);


                    const updateBtn = document.createElement('button');
                    updateBtn.id = 'updateCommentBtn';
                    updateBtn.innerHTML = `<i class="fa-solid fa-pen-to-square fa-2xl" style="color: #ffffff;"></i>`;
                    updateBtn.setAttribute('data-toggle', 'modal');
                    updateBtn.setAttribute('data-target', '#updateCommentModal');

                    updateBtn.value = commentId;
                    // updateBtn.onclick = () => updateComment(commentId);
                    commentBox[i].appendChild(updateBtn);
                }
            }

            // add reply to update and delete btn

            let replyBox = document.getElementsByClassName('savedReplyBox');

            for (let i = 0; i < replyBox.length; i++) {

                const replyId = replyBox[i].querySelector('#savedReplyId').value;
                const replyUserEmail = replyBox[i].querySelector('#savedReplyEmail').value;

                if(replyUserEmail === result.loggedInUserId){

                    const deleteBtn = document.createElement('button');
                    deleteBtn.className = 'deleteReplyBtn';
                    deleteBtn.innerHTML = `<i class="fa-solid fa-trash-can fa-2xl" style="color: #ffffff;"></i>`;
                    deleteBtn.value = replyId;
                    deleteBtn.onclick = () => deleteReply(replyId);
                    replyBox[i].appendChild(deleteBtn);


                    const updateBtn = document.createElement('button');
                    updateBtn.id = 'updateReplyBtn';
                    updateBtn.innerHTML = `<i class="fa-solid fa-pen-to-square fa-2xl" style="color: #ffffff;"></i>`;
                    updateBtn.setAttribute('data-toggle', 'modal');
                    updateBtn.setAttribute('data-target', '#updateReplyModal');

                    updateBtn.value = replyId;
                    // updateBtn.onclick = () => updateComment(commentId);
                    replyBox[i].appendChild(updateBtn);
                }
            }





        },
        error : function(jqXHR, status, error, result){
            // alert(jqXHR.status+"\n"+jqXHR.responseText+"\n"+error);
            console.log("token is not valid");
            console.log(jqXHR.status+"\n"+jqXHR.responseText+"\n"+error);


            logoutBtn.style.display = 'none';
            myInfoBtn.style.display = 'none';
            loginBtn.style.display = 'block';
            registerFormBtn.style.display = 'block';

        }
    });

});


function logout() {

    localStorage.removeItem('token');
    location.href = "/game-service/main";
}


function loginForm(){
    location.href = "/user-service/loginForm";
}

function registerForm(){
    location.href = "/user-service/registration";
}

function getMyInfo(){

    const token = localStorage.getItem('token');
    location.href = "/user-service/myInfo/" + token;

    // var paramData = {
    //     commentId : commentId
    //
    // };
    //
    // var param = JSON.stringify(paramData);
    //
    // $.ajax({
    //     url      : "/user-service/myInfo",
    //     type     : "GET",
    //     contentType : "application/json",
    //     data     : param,
    //     beforeSend : function(xhr){
    //         /* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
    //         xhr.setRequestHeader("Authorization", "Bearer" + token);
    //     },
    //     dataType : "json",
    //     cache   : false,
    //     success  : function(result, status,data){
    //         alert("확인되었습니다.");
    //         location.href = "http://127.0.0.1:8000/game-service/main"
    //     },
    //     error : function(jqXHR, status, error, result){
    //         alert(jqXHR.status+"\n"+jqXHR.responseText+"\n"+error);
    //
    //         location.href = "http://127.0.0.1:8000/game-service/main"
    //     }
    // });

}

// $("#addCommentBtn").on("click", function () {


function playNewGame(){

    const token = localStorage.getItem('token');

    $.ajax({
        url      : "/user-service/validateJwt",
        type     : "GET",
        contentType : "application/json",
        beforeSend: function(xhr) {
            // 헤더에 토큰 추가
            xhr.setRequestHeader("Authorization", "Bearer"+token);
        },
        success  : function(result, status, data){

            console.log("token is valid");
            location.href = "/game-service/cardGame?token=" + token + "&playType=new";

        },
        error : function(jqXHR, status, error, result){

            showLoginRequiredMessage();
            // alert('로그인 해주시기 바랍니다.');
            console.log("token is not valid");
            // console.log(jqXHR.status+"\n"+jqXHR.responseText+"\n"+error);
        }
    });
}

function playContinue(){

    const token = localStorage.getItem('token');

    $.ajax({
        url      : "/user-service/validateJwt",
        type     : "GET",
        contentType : "application/json",
        beforeSend: function(xhr) {
            // 헤더에 토큰 추가
            xhr.setRequestHeader("Authorization", "Bearer"+token);
        },
        success  : function(result, status, data){


            console.log("token is valid");
            location.href = "/game-service/cardGame?token=" + token + "&playType=continue";

        },
        error : function(jqXHR, status, error, result){

            showLoginRequiredMessage();
            // alert('로그인 해주시기 바랍니다.');
            console.log("token is not valid");
            // console.log(jqXHR.status+"\n"+jqXHR.responseText+"\n"+error);
        }
    });
}

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

