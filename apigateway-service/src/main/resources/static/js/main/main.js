$(document).ready(function() {
    const track = document.querySelector('.carousel-track');
    const slides = Array.from(track.children);
    const nextButton = document.querySelector('.carousel-button.right');
    const prevButton = document.querySelector('.carousel-button.left');
    const slideWidth = slides[0].getBoundingClientRect().width;

    // Place each slide sideways
    slides.forEach((slide, index) => {
        slide.style.left = `${slideWidth * index}px`;
    });

    // Function to move the slide
    const moveToSlide = (track, currentSlide, targetSlide) => {
        track.style.transform = `translateX(-${targetSlide.style.left})`;
        currentSlide.classList.remove('current-slide');
        targetSlide.classList.add('current-slide');
    };

    // When clicking the previous button
    prevButton.addEventListener('click', () => {
        const currentSlide = track.querySelector('.current-slide');
        const prevSlide = currentSlide.previousElementSibling;

        if (prevSlide) {
            moveToSlide(track, currentSlide, prevSlide);
        }
    });

    // When clicking the next button
    nextButton.addEventListener('click', () => {
        const currentSlide = track.querySelector('.current-slide');
        const nextSlide = currentSlide.nextElementSibling;
        if (nextSlide) {
            moveToSlide(track, currentSlide, nextSlide);
        }
    });

    var modal = document.getElementById("myModal");

    // Close modal when clicking outside the modal content
    window.onclick = function(event) {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    }

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
            xhr.setRequestHeader("Authorization", "Bearer"+token);
        },
        success  : function(result, status, data){
            //Get the email of the logged in user
            //if validateJwt success, user has logged in

            //display logout btn and myInfo btn for logged in user
            logoutBtn.style.display = 'block';
            myInfoBtn.style.display = 'block';
            loginBtn.style.display = 'none';
            registerFormBtn.style.display = 'none';

            // If the email that came in response is the same as the email already bound to the element while iterating through the elements under the comment and reply, this comment and reply are written by the currently logged in user, so display the edit and delete buttons.
            // If they are different, this comment and reply are not written by the currently logged in user, so do not display the edit and delete buttons.
            let commentBox = document.getElementsByClassName('savedCommentBox');

            // add comment to update and delete btn
            for (let i = 0; i < commentBox.length; i++) {

                // Compare the value of result.loggedInUserId and savedCommentBox and add a button if they are the same
                const commentId = commentBox[i].querySelector('#savedCommentId').value;
                const commentUserEmail = commentBox[i].querySelector('#savedCommentUserEmail').value;

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
                    replyBox[i].appendChild(updateBtn);
                }
            }
        },
        error : function(jqXHR, status, error, result){
            //if validateJwt error, user has not logged in

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

// myInfo feature is temporarily closed
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
    //         xhr.setRequestHeader("Authorization", "Bearer" + token);
    //     },
    //     dataType : "json",
    //     cache   : false,
    //     success  : function(result, status,data){
    //         location.href = "http://127.0.0.1:8000/game-service/main"
    //     },
    //     error : function(jqXHR, status, error, result){
    //         alert(jqXHR.status+"\n"+jqXHR.responseText+"\n"+error);
    //         location.href = "http://127.0.0.1:8000/game-service/main"
    //     }
    // });

}


function playNewGame(){

    const token = localStorage.getItem('token');
    $.ajax({
        url      : "/user-service/validateJwt",
        type     : "GET",
        contentType : "application/json",
        beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", "Bearer"+token);
        },
        success  : function(result, status, data){
            //move to loading page
            location.href = "/game-service/loading?type=new"
        },
        error : function(jqXHR, status, error, result){
            showLoginRequiredMessage();
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
            xhr.setRequestHeader("Authorization", "Bearer"+token);
        },
        success  : function(result, status, data){
            //move to loading page
            location.href = "/game-service/loading?type=continue"

        },
        error : function(jqXHR, status, error, result){
            showLoginRequiredMessage();
        }
    });
}

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

