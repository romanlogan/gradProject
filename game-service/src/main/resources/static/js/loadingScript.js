document.addEventListener("DOMContentLoaded", () => {
  const progressBar = document.getElementById("progress");
  const character = document.getElementById("character");
  const loadingText = document.getElementById("loading-text");
  const loadingBarContainer = document.querySelector(".loading-bar");
  let progress = 0;

  // Calculate maximum distance the character can travel within the loading bar
  const maxCharacterPosition = loadingBarContainer.offsetWidth - character.offsetWidth + 30;

  function updateLoading() {
    if (progress < 100) {
      progress += 1;
      progressBar.style.width = `${progress}%`;
      loadingText.textContent = `Loading... ${progress}%`;

      // Set character's position based on the progress percentage
      character.style.left = `${(progress / 100) * maxCharacterPosition}px`;
    } else {
      // Ensure character is positioned exactly at the end when loading is complete
      loadingText.textContent = "Loaded!";
      progressBar.style.width = "100%";
      character.style.left = `${maxCharacterPosition}px`;
      clearInterval(loadingInterval);

      // game service 로 요청을 보내서 게임으로 보내기
      const token = localStorage.getItem('token');
      let playType =  document.getElementById("playType").value;

      location.href = "/game-service/cardGame?token=" + token + "&playType="+playType;

    }
  }

  // Update loading progress every 30 milliseconds
  const loadingInterval = setInterval(updateLoading, 30);
});
