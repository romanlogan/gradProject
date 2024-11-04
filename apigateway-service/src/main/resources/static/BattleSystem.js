import * as Deck from "./Deck_Default.js";
import * as Stage from "./stage.js";

export let round = 0;
let result = "";

export function resetRound(){
    round =0;
}

export async function nextRound() {
    CardSystem.dropAll();

    for (let character of window.friendlyCharacter) {
        if(character.name ==="Hero")await character.resetTemporary("ATK");
    }

    if(round!==0)
    {
        // 呼叫每個角色的動作
        for (let characterGroup of window.characters) {
            // 遍歷子陣列中的每一個角色
            for (let character of characterGroup) {
                if(character.name !=="Hero")
                {
                    await character.resetTemporary("DEF");
                    if (character.action) {
                        character.action(); // 呼叫角色的 action 方法
                    } else {
                        console.log("這個角色:",character.name,"沒有 action 方法",character);
                    }
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

    // 遍歷 window.characters 的每一個子陣列
    for (let characterGroup of window.characters) {
        // 遍歷子陣列中的每一個角色
        for (let character of characterGroup) {
            if(character.name !=="Hero")await character.resetTemporary("ATK"); //!=Hero時
            else await character.resetTemporary("DEF"); //Hero
        }
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

    createCongratulationInterface();
}


export async function Defeat(){
    result="Defeat";
    alert("Defeat");
    console.log("Defeat");
    //action after Defeat (check the Achievements about this game (your deck,your path, money) --> return to Menu)
}

export function Victory() {
    result = "Victory";
    console.log("Victory");

    // 顯示獎勵選擇，並在獎勵選擇後保存資料並進入下一關卡
    try {
        return showRewardSelection()
            .then(() => saveData())
            .then(() => Stage.nextStage())
            .catch((error) => console.error("An error occurred:", error));
    } catch (error) {
        console.error("An error occurred:", error);
    }

}

// 設置 rewardModal，防止點擊外部或按 ESC 關閉
const rewardModal = new bootstrap.Modal(document.getElementById('rewardSelectionModal'), {
    backdrop: 'static', // 禁用點擊外部關閉
    keyboard: false     // 禁用 ESC 關閉
});

function showRewardSelection() {
    console.log("showRewardSelection started");

    return new Promise((resolve, reject) => {
        const rewardOptions = document.getElementById('rewardOptions');
        if (!rewardOptions) {
            console.error("rewardOptions element not found");
            reject("rewardOptions element not found");
            return;
        }

        rewardOptions.innerHTML = ''; // 清空之前的內容

        const randomCardIDs = getRandomCardIDs(3); // 隨機選擇 3 張卡牌

        // 定義一個卡片加載的超時函數
        function loadCardWithTimeout(id, timeout = 5000) {
            return new Promise((resolveCard, rejectCard) => {
                Card.default.loadFromLibrary(id)
                    .then(card => {
                        if (card) {
                            resolveCard(card);
                        } else {
                            rejectCard(`Card with ID ${id} could not be loaded.`);
                        }
                    })
                    .catch(error => {
                        rejectCard(`Failed to load card with ID ${id}: ${error}`);
                    });

                // 添加超時處理
                setTimeout(() => {
                    rejectCard(`Loading card with ID ${id} timed out.`);
                }, timeout);
            });
        }

        // 使用超時機制加載所有卡片
        const cardLoadPromises = randomCardIDs.map(id =>
            loadCardWithTimeout(id)
                .then(card => {
                    const cardElement = document.createElement('div');
                    cardElement.className = 'reward-card';

                    const cardImage = document.createElement('img');
                    cardImage.src = card.imageUrl;
                    cardImage.alt = card.CardName;

                    const cardName = document.createElement('div');
                    cardName.textContent = card.CardName;

                    cardElement.appendChild(cardImage);
                    cardElement.appendChild(cardName);

                    cardElement.onclick = () => {
                        selectReward(card);
                        console.log(`Selected card: ${card.CardName}`);
                    };
                    rewardOptions.appendChild(cardElement);
                })
                .catch(error => {
                    console.error(error);
                })
        );

        // 使用 Promise.all 等待所有卡片加載完成
        Promise.allSettled(cardLoadPromises)
            .then(results => {
                console.log("All cards loaded or failed, attempting to show reward modal");

                if (rewardModal) {
                    rewardModal.show();
                    console.log("rewardModal shown successfully");
                } else {
                    console.error("rewardModal not initialized");
                    reject("rewardModal not initialized");
                }
            })
            .catch(error => {
                console.error("Error in loading cards:", error);
                reject("Error in loading cards");
            });

        // 確認按鈕的點擊事件處理
        const confirmButton = document.getElementById('confirmReward');
        if (!confirmButton) {
            console.error("confirmReward button not found");
            reject("confirmReward button not found");
            return;
        }

        confirmButton.onclick = () => {
            console.log("confirmReward button clicked");

            if (selectedReward) {
                console.log(`Confirmed Reward: ${selectedReward.CardName}`);
                alert(`You have chosen: ${selectedReward.CardName}`);
                Deck.getReward(selectedReward);
                rewardModal.hide();
                resolve(); // 成功解決 Promise，允許後續流程
            } else {
                alert('Please select a reward before confirming.');
            }
        };

        // 新增取消按鈕的點擊事件處理
        const cancelButton = document.getElementById('cancelReward');
        if (!cancelButton) {
            console.error("cancelReward button not found");
            reject("cancelReward button not found");
            return;
        }

        cancelButton.onclick = () => {
            console.log("cancelReward button clicked");
            alert("You have chosen to skip the reward.");
            rewardModal.hide();
            resolve(); // 解決 Promise，允許進入下一階段
        };
    });
}

function getRandomCardIDs(count) {
    const deck = window.drawCards;
    const randomIDs = [];

    while (randomIDs.length < count) {
        const randomIndex = Math.floor(Math.random() * deck.length);
        const cardID = deck[randomIndex].CardID;
        if (!randomIDs.includes(cardID)) {
            randomIDs.push(cardID);
        }
    }

    return randomIDs;
}

let selectedReward = null;

function selectReward(card) {
    selectedReward = card;
    console.log(`Selected Reward: ${card.CardName}`);
}

document.getElementById('confirmReward').onclick = () => {
    if (selectedReward) {
        console.log(`Confirmed Reward: ${selectedReward.CardName}`);
        alert(`You have chosen: ${selectedReward.CardName}`);
        let cardIds = Deck.getReward(selectedReward);
        rewardModal.hide();
        alert("轉場事件待完成");
        //缺轉場

        // current hero hp
        let heroHp;
        for (let character of window.friendlyCharacter) {
            if(character.name ==="Hero"){
                heroHp = character.getHp();
            }
        }
        console.log("hero hp : "+heroHp);

    
        // playerEmail 
        // let email = [[${useremail}]]

        // cardIds array to string 
        let stringCardIds = cardIds.join(',');
        console.log(result);

        //moved save history logic to here
        console.log("run function:saveData");
        let paramData = {

            gameId : 1,
            playerEmail: "asdf@asdf.com",
            hp: 50,
            cards: stringCardIds,
            route: "1,2,3,5,7,8.9"      //일단 1개 루트만 존재하므로 stage 번호를 그대로 넣기  
        };

        let param = JSON.stringify(paramData);

        //여기서 ajax 호출해서 다음 단계 저장
        $.ajax({
            url      : "http://localhost:8000/history-service/save",
            // url      : "/history-service/save",
            type     : "POST",
            contentType : "application/json",
            data : param,
            success  : function(result, status, data){

                console.log("history save success");

            },
            error : function(jqXHR, status, error, result){

                console.log("history save fail");
            }
        });
        console.log("function saveData done");

    } else {
        alert('Please select a reward before confirming.');
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
    updateStatusBarsVisibility();
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

function updateStatusBarsVisibility() {
    const friendlyStatusBar = document.querySelectorAll('#friendly-status-bar .status-content');
    const enemyStatusBar = document.querySelectorAll('#enemy-status-bar .status-content');

    friendlyStatusBar.forEach(status => {
        status.classList.toggle('hidden', status.innerHTML.trim() === '');
    });

    enemyStatusBar.forEach(status => {
        status.classList.toggle('hidden', status.innerHTML.trim() === '');
    });
}

function createCongratulationInterface(){
    // 建立一個通關動畫的容器
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
    congratsContainer.style.opacity = '0'; // 開始時為隱藏

    congratsContainer.innerHTML = `
        <h1>Congratulations!</h1>
        <p>You have defeated the final boss and completed the game!</p>
        <button id="return-menu" style="padding: 10px 20px; font-size: 1em;" onclick="">Return to Menu</button>
    `;

    document.body.appendChild(congratsContainer);

    // 使用 CSS 動畫淡入效果
    congratsContainer.animate([{ opacity: 0 }, { opacity: 1 }], { duration: 2000, fill: 'forwards' });

    // 綁定返回主選單的按鈕事件
    document.getElementById('return-menu').onclick = () => {
        congratsContainer.animate([{ opacity: 1 }, { opacity: 0 }], { duration: 1000, fill: 'forwards' })
            .onfinish = () => {
            congratsContainer.remove(); // 移除通關容器
            alert('Returning to the main menu...');
            window.location.href = '/main-menu.html'; // 導向主選單（假設有一個主選單頁面）
        };
    };
}

// async function saveData(){

    // console.log("run function:saveData");
    // let paramData = {
    //     gameId : 1,
    //     playerEmail: "asdf@asdf.com",
    //     hp: 50,
    //     cards: "30,33,34,45",
    //     route: "1,2,3,5,7,8.9"
    // };

    // let param = JSON.stringify(paramData);

    // //여기서 ajax 호출해서 다음 단계 저장
    // $.ajax({
    //     url      : "http://localhost:8000/history-service/save",
    //     // url      : "/history-service/save",
    //     type     : "POST",
    //     contentType : "application/json",
    //     data : param,
    //     success  : function(result, status, data){

    //         console.log("history save success");

    //     },
    //     error : function(jqXHR, status, error, result){

    //         console.log("history save fail");
    //     }
    // });
    // console.log("function saveData done");
// }

async function saveData() {

    console.log("Starting saveData");

    // get stage
    let stage = Stage.getCurrentStage();
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



    try {
        // 模擬 API 請求並返回 Promise
        const result = await new Promise((resolve, reject) => {

            $.ajax({
                url: "http://localhost:8000/history-service/save",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify({
                    gameId: 1,
                    playerEmail: "asdf@asdf.com",
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
        alert("Failed to save data. Please try again later.");
    }
}


