import * as Deck from "./Deck_Default.js";
import * as Stage from "./stage.js";

export let round = 0;
let result = "";

let selectedReward = null;

// 設置 rewardModal，防止點擊外部或按 ESC 關閉
const rewardModal = new bootstrap.Modal(document.getElementById('rewardSelectionModal'), {
    backdrop: 'static', // 禁用點擊外部關閉
    keyboard: false     // 禁用 ESC 關閉
});

export function resetRound(){
    round =0;
}

export async function nextRound() {
    CardSystem.dropAll();

    for (let character of window.friendlyCharacter) {
        if(character.name ==="Hero")await character.replyTemporary();
    }

    if(round!==0)
    {
        // 呼叫每個角色的動作
        for (let characterGroup of window.characters) {
            // 遍歷子陣列中的每一個角色
            for (let character of characterGroup) {
                if(character.name !=="Hero")
                {
                    await character.recordTemp(); //紀錄
                    if (character.action) {
                        character.action(); // 呼叫角色的 action 方法
                    } else {
                        console.log("這個角色:",character.name,"沒有 action 方法",character);
                    }
                    await character.replyTemporary(); //回扣
                    //這裡要補上全體血量更新,不然角色被攻擊不會更新角色狀態
                    await updateCharacterStatus(character);
                    await checkVictoryOrDefeat();
                    if(result==="Defeat")break; //避免不停Defeat的情況
                }
            }
        }
    }
    await nextTurn();
}

export async function nextTurn() {
    round++;
    await showNextRoundEffect();
    CardSystem.draw(5);
    CostSystem.resetCost();
    CostSystem.upDateCost();
    for (let character of window.friendlyCharacter) {
        if(character.name ==="Hero")await character.recordTemp();
    }

}

async function showNextRoundEffect() {
    const nextTurnElement = document.getElementById('next-round');

    if (!nextTurnElement) {
        console.error("Element with id 'next-turn' not found");
        return;
    }

    // 顯示特效並加上閃爍效果
    nextTurnElement.style.opacity = '1';
    nextTurnElement.style.transform = 'translate(-50%, -50%) scale(1.2)';
    nextTurnElement.style.pointerEvents = 'auto';
    nextTurnElement.classList.add('flash'); // 加入閃爍動畫

    // 2秒後隱藏特效
    setTimeout(() => {
        nextTurnElement.style.opacity = '0';
        nextTurnElement.style.transform = 'translate(-50%, -50%) scale(1)';
        nextTurnElement.style.pointerEvents = 'none';
        nextTurnElement.classList.remove('flash'); // 移除閃爍動畫
    }, 2000);
}

export async function checkVictoryOrDefeat() {
    console.log("function checkVictoryOrDefeat");

    if(result ==="Congratulation"){
        await Congratulation();
        return;
    }

    // 檢查友方角色是否全部死亡
    if (!friendlyCharacter || friendlyCharacter.length === 0) {
        await Defeat();
        return;
    }
    // 檢查敵方角色是否全部死亡
    if (!enemyCharacter || enemyCharacter.length === 0) {
        await Victory(); // 呼叫 Victory 函數
        return;
    }
    console.log("Game continues...");
}
export async function Congratulation() {

    console.log("Congratulations! You have completed the game!");
    await createCongratulationInterface();
}

export async function Defeat() {
    result = "Defeat";
    console.log("Defeat");
    await createDefeatInterface();
}

export async function Victory() {
    result = "Victory";
    console.log("Victory");

    try {
        await showRewardSelection(); // Wait for user action
        await saveData("auto");            // Execute after reward selection
        await Stage.nextStage();     // Proceed to the next stage
    } catch (error) {
        console.error("An error occurred:", error);
    }
}

async function showRewardSelection() {
    console.log("showRewardSelection started");

    const rewardOptions = document.getElementById('rewardOptions');
    if (!rewardOptions) {
        console.error("rewardOptions element not found");
        return Promise.reject("rewardOptions element not found");
    }

    console.log("rewardOptions done");

    // Clear previous content
    rewardOptions.innerHTML = '';

    // Await the random card IDs to ensure an array is returned
    const randomCardIDs = await getRandomCardIDs(3);  // Await here

    console.log("randomRewardCardID done");

    // Load cards with timeout
    const cardLoadPromises = randomCardIDs.map(id => loadCardWithTimeout(id));

    console.log("cardLoadPromises done");

    try {
        const loadedCards = await Promise.allSettled(cardLoadPromises);
        console.log("loadedCards done");
        const fragment = document.createDocumentFragment();
        console.log("fragment done");

        loadedCards.forEach(result => {
            if (result.status === "fulfilled") {
                const card = result.value;
                const cardElement = document.createElement('div');
                cardElement.className = 'reward-card';

                const cardImage = document.createElement('img');
                cardImage.src = card.imageUrl;
                cardImage.alt = card.CardName;

                const cardName = document.createElement('div');
                cardName.textContent = card.CardName;

                cardElement.appendChild(cardImage);
                cardElement.appendChild(cardName);

                cardElement.onclick = () => selectReward(card);
                fragment.appendChild(cardElement);
            } else {
                console.error(result.reason);
            }
        });

        rewardOptions.appendChild(fragment);
        rewardModal.show();

        // Return a promise that resolves when a button is clicked
        return new Promise((resolve) => {
            // Confirm button handling
            document.getElementById('confirmReward').onclick = () => {
                if (selectedReward) {
                    console.log(`Confirmed Reward: ${selectedReward.CardName}`);
                    displayMessage(`You have chosen: ${selectedReward.CardName}`);
                    Deck.getReward(selectedReward);
                    rewardModal.hide();
                    resolve(); // Resolve the promise on confirmation
                } else {
                    displayMessage('Please select a reward before confirming.');
                }
            };

            // Cancel button handling
            document.getElementById('cancelReward').onclick = () => {
                console.log("cancelReward button clicked");
                displayMessage("You have chosen to skip the reward.");
                rewardModal.hide();
                resolve(); // Resolve the promise on cancellation
            };
        });
    } catch (error) {
        console.error("Error loading reward cards:", error);
        throw error; // Pass the error up the chain
    }
}

function loadCardWithTimeout(id, timeout = 5000) {
    return new Promise((resolve, reject) => {
        Card.default.loadFromLibrary(id)
            .then(card => {
                if (card) resolve(card);
                else reject(`Card with ID ${id} could not be loaded.`);
            })
            .catch(error => reject(`Failed to load card with ID ${id}: ${error}`));

        setTimeout(() => reject(`Loading card with ID ${id} timed out.`), timeout);
    });
}

async function getRandomCardIDs(count) {
    try {
        // Load the card library
        const response = await fetch('./cardLibrary.json');
        if (!response.ok) {
            throw new Error('Failed to load card library');
        }
        const cardLibrary = await response.json();

        // Ensure there are enough cards in the library
        const maxIndex = cardLibrary.length;
        if (maxIndex < count) {
            console.warn("Not enough cards in the library; adjusting count to available cards.");
            count = maxIndex;
        }

        const randomIDs = [];
        while (randomIDs.length < count) {
            const randomIndex = Math.floor(Math.random() * maxIndex);
            const cardID = cardLibrary[randomIndex].CardID;

            // Add the CardID if it's not already in the array
            if (!randomIDs.includes(cardID)) {
                randomIDs.push(cardID);
            }
        }

        console.log("Randomly selected CardIDs:", randomIDs);
        return randomIDs;
    } catch (error) {
        console.error("Error in getRandomCardIDs:", error);
        return [];
    }
}

function selectReward(card) {
    selectedReward = card;
    console.log(`Selected Reward: ${card.CardName}`);
}

document.getElementById('confirmReward').onclick = () => {
    if (selectedReward) {
        console.log(`Confirmed Reward: ${selectedReward.CardName}`);
        displayMessage(`You have chosen: ${selectedReward.CardName}`);
        let cardIds = Deck.getReward(selectedReward);
        rewardModal.hide();
        displayMessage("轉場事件待完成");
        //缺轉場
    } else {
        displayMessage('Please select a reward before confirming.');
    }
}

export async function updateCharacterStatus(character) {
    console.log('Updating status for:', character.name);

    // 更新角色血條和 HP 顯示
    const healthBar = document.getElementById(character.healthBarId);
    const hpElement = document.getElementById(character.statusHP);

    const maxHealthBarWidth = 10;
    const healthWidthVH = Math.max(
        0,
        Math.min((character.HP / character.maxHP) * maxHealthBarWidth, maxHealthBarWidth)
    );

    if (healthBar) healthBar.style.width = healthWidthVH + 'vh';
    if (hpElement) hpElement.textContent = `${character.HP}/${character.maxHP}`;

    // 當角色 HP 為 0 時進行徹底清理
    if (character.HP <= 0) {
        console.log(`${character.name} has 0 HP and will be removed.`);

        // 從隊伍陣列中移除角色
        const teamArray = character.team === 'friendly-area' ? window.friendlyCharacter : window.enemyCharacter;
        const teamIndex = teamArray.indexOf(character);

        if(character.name==="Minotaur"){
            result ="Congratulation";
            await checkVictoryOrDefeat();
            return;
        }

        if (teamIndex !== -1) {
            teamArray.splice(teamIndex, 1);
            console.log(`${character.name} removed from team array.`);
        }

        // 從全域 characters 陣列中移除角色
        window.characters.forEach(group => {
            const groupIndex = group.indexOf(character);
            if (groupIndex !== -1) {
                group.splice(groupIndex, 1);
                console.log(`${character.name} removed from global character groups.`);
            }
        });

        // 徹底移除角色的 DOM 元素
        removeCharacterElement(character);

        // 刪除角色的所有屬性，防止記憶體洩漏
        for (let key in character) {
            if (character.hasOwnProperty(key)) {
                delete character[key];
            }
        }

        console.log(`${character.name} fully removed from memory and DOM.`);
    }

    // 更新狀態欄顯示
    await updateStatusBarsVisibility();
}

function removeCharacterElement(character) {
    // 移除角色的 status-content 內容
    const statusBarId = character.team === 'friendly-area' ? 'friendly-status-bar' : 'enemy-status-bar';
    const statusContents = document.querySelectorAll(`#${statusBarId} .status-content`);

    statusContents.forEach(status => {
        const statusText = status.querySelector(`#${character.statusHP}`);
        if (statusText) {
            console.log(`Removing status content for: ${character.name}`);
            status.remove(); // 完整移除該狀態內容
        }
    });

    // 確保移除角色在對應 battle-area 中的元素
    const areaId = character.team === 'friendly-area' ? 'friendly-area' : 'enemy-area';
    const area = document.getElementById(areaId);

    if (area) {
        const characterElement = Array.from(area.children).find(child =>
            child.querySelector(`#${character.healthBarId}`)
        );
        if (characterElement) {
            characterElement.remove();
            console.log(`Character element removed from ${areaId}: ${character.name}`);
        } else {
            console.warn(`Character element not found in ${areaId} for ${character.name}`);
        }
    }
}

async function updateStatusBarsVisibility() {
    const friendlyStatusBar = document.querySelectorAll('#friendly-status-bar .status-content');
    const enemyStatusBar = document.querySelectorAll('#enemy-status-bar .status-content');

    friendlyStatusBar.forEach(status => {
        status.classList.toggle('hidden', status.innerHTML.trim() === '');
    });

    enemyStatusBar.forEach(status => {
        status.classList.toggle('hidden', status.innerHTML.trim() === '');
    });
}

async function createCongratulationInterface() {
    const congratsContainer = document.createElement('div');
    congratsContainer.id = 'congratulations-container';
    congratsContainer.style.position = 'fixed';
    congratsContainer.style.top = '50%';
    congratsContainer.style.left = '50%';
    congratsContainer.style.transform = 'translate(-50%, -50%)';
    congratsContainer.style.zIndex = '1000';
    congratsContainer.style.backgroundColor = 'rgba(0, 0, 0, 0.8)';
    congratsContainer.style.padding = '50px';
    congratsContainer.style.borderRadius = '10px';
    congratsContainer.style.textAlign = 'center';
    congratsContainer.style.color = 'white';
    congratsContainer.style.fontSize = '2em';
    congratsContainer.style.opacity = '0'; // 開始時隱藏

    congratsContainer.innerHTML = `
        <h1>Congratulations!</h1>
        <p>You have defeated the final boss and completed the game!</p>
        <button id="return-menu" style="padding: 10px 20px; font-size: 1em;" onclick="Battle.returnToMenu()">Return to Menu</button>
    `;

    document.body.appendChild(congratsContainer);

    // 動畫淡入效果
    congratsContainer.animate([{ opacity: 0 }, { opacity: 1 }], { duration: 2000, fill: 'forwards' });

    //play end video
    openEndModal();
}

async function createDefeatInterface() {
    const defeatContainer = document.createElement('div');
    defeatContainer.id = 'defeat-container';
    defeatContainer.style.position = 'fixed';
    defeatContainer.style.top = '50%';
    defeatContainer.style.left = '50%';
    defeatContainer.style.transform = 'translate(-50%, -50%)';
    defeatContainer.style.zIndex = '1000';
    defeatContainer.style.backgroundColor = 'rgba(0, 0, 0, 0.8)';
    defeatContainer.style.padding = '50px';
    defeatContainer.style.borderRadius = '10px';
    defeatContainer.style.textAlign = 'center';
    defeatContainer.style.color = 'white';
    defeatContainer.style.fontSize = '2em';
    defeatContainer.style.opacity = '0'; // 開始時隱藏

    defeatContainer.innerHTML = `
        <h1>Defeat</h1>
        <p>You have been defeated in battle.</p>
        <button id="return-menu" style="padding: 10px 20px; font-size: 1em;" onclick="Battle.returnToMenu()">Return to Menu</button>
    `;

    document.body.appendChild(defeatContainer);

    // 動畫淡入效果
    defeatContainer.animate([{ opacity: 0 }, { opacity: 1 }], { duration: 2000, fill: 'forwards' });
}

export function returnToMenu() {
    console.log("Returning to the main menu...");
    window.location.replace('/game-service/main');
}

export async function saveData(saveType) {

    console.log("Starting saveData ----- async saveData() function in game-service");

    // get stage

    let stage;
    const currentStage = Number(Stage.getCurrentStage());
    if(saveType === "auto"){

        stage = currentStage + 1;
    }else if(saveType === "menu"){
        stage = currentStage;
    }
    console.log("stage : " + stage);

    // get heroHp
    let heroHp;
    for (let character of window.friendlyCharacter) {
        if(character.name ==="Hero"){
            heroHp = character.getHp();
        }
    }
    console.log("hero hp : "+heroHp);

    //get Cards
    let cards = Deck.getCards();
    let stringCardIds = cards.join(',');
    console.log("stringCardIds : " + stringCardIds);

    //get user
    let userEmail = document.getElementById('savedUserEmail').value;


    try {
        // 模擬 API 請求並返回 Promise
        const result = await new Promise((resolve, reject) => {

            $.ajax({
                url: "/game-service/save",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify({
                    gameId: 1,
                    playerEmail: userEmail,
                    hp: heroHp,
                    cards: stringCardIds,
                    route: stage
                }),
                success: function (response) {
                    console.log("Data saved successfully", response);
                    resolve(response); // 請求成功時 resolve
                },
                error: function (error) {
                    console.error("Failed to save data", error);
                    reject(error); // 請求失敗時 reject
                }
            });
        });

        console.log("SaveData success:", result);
        return result; // 返回成功的結果

    } catch (error) {
        console.error("An error occurred in saveData:", error);
        displayMessage("Failed to save data. Please try again later.");
    }
}


//  defeat boss then start end video
const endModal = document.getElementById('endVideoModal');
const endModalVideo = document.getElementById('endVideoModalVideo');
const closeEndModal = document.getElementById('endVideoCloseModal');
// const openModalBtn = document.getElementById('openEndVideo');

// 동영상 끝날 때 모달 닫기
endModalVideo.addEventListener('ended', closeModalHandler);

// 모달 닫기 버튼 클릭 시
closeEndModal.addEventListener('click', closeModalHandler);



// 모달 열기 함수
function openEndModal() {
    endModal.style.display = 'flex';
    endModalVideo.play(); // 동영상 재생
}

// 모달 닫기 함수
function closeModalHandler() {
    endModal.style.display = 'none';
    endModalVideo.pause(); // 동영상 정지
    endModalVideo.currentTime = 0; // 동영상 시간 초기화
}

// Display message function to replace alert
function displayMessage(message) {
    const logContainer = document.getElementById("log-container");
    const messageBox = document.createElement("div");
    messageBox.className = "message-box";
    messageBox.textContent = message;
    logContainer.appendChild(messageBox);
    setTimeout(() => {
        messageBox.remove();
    }, 5000); // Remove the message after 5 seconds
}
