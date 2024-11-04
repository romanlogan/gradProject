import Hero from "./Hero.js";
import Enemy from "./Enemy.js";
import * as Deck_Default from "./Deck_Default.js";

let count=0;

function initializeCharacters() {
    if(count===0){
        let hero  = new Hero();
        hero.create();
    }
}

function initializeStage(){
    Stage.setStageID();
}

async function initializeCardSystem() {
    await resetCardSystem();
    await initializeDeck();
    CardSystem.shuffle(window.drawCards); // 洗牌
    CardSystem.updateDrawCards(); // 更新顯示卡牌
    CardSystem.updateDropCards(); // 傳遞 drawCards 作為參數
    CardSystem.makeCardsSelectable();
}

async function initializeDeck(){
    await Deck_Default.initializeDeck(); //初始化Deck
    window.drawCards = Deck_Default.getDeck(); //Deck 放到drawCards裡
    console.log('All cards after initialization:', window.drawCards); // 應有內容
}

export async function Initialization(){
    await initializeCharacters();
    await initializeCardSystem();
    if(count===0)   await initializeStage();
    else{
        resetEnemyStatusBars();
        resetBattleArea();
    }
    await stageSummonEnemy();
    console.log("initialization.stageSummonEnemy");
    await initializeStatus();

    count++;
}

export async function stageSummonEnemy(){
    Stage.stageEnemy(Stage.idMap[Stage.stage-1]); //
    console.log("Stage.idMap: ",Stage.idMap);
    console.log("Stage.stage: ",Stage.stage);
    console.log("Stage.idMap[Stage.stage-1]: ",Stage.idMap[Stage.stage-1]);
}

async function initializeStatus(){
    // 角色的狀態
    for (let characterGroup of window.characters) {
        // 遍歷子陣列中的每一個角色
        for (let character of characterGroup) {
            await Battle.updateCharacterStatus(character);
        }
    }
}

async function resetCardSystem() {
    window.drawCards.length = 0;
    window.dropCards.length = 0;
    window.playerOne.length = 0;
}

export function resetEnemyStatusBars() {
    const enemyStatusBar = document.getElementById('enemy-status-bar');

    // 重置 enemy-status-bar
    if (enemyStatusBar) {
        enemyStatusBar.innerHTML = ''; // 清空內容
        for (let i = 1; i <= 4; i++) {
            const statusContent = document.createElement('div');
            statusContent.className = 'status-content';
            statusContent.id = `enemy-status-${i}`;
            enemyStatusBar.appendChild(statusContent);
        }
        console.log('enemy-status-bar reset.');
    } else {
        console.error('enemy-status-bar element not found');
    }
}

export function resetBattleArea() {
    // 重置 enemy-area
    const enemyArea = document.getElementById('enemy-area');
    if (enemyArea) {
        enemyArea.innerHTML = ''; // 清空內容
        for (let i = 1; i <= 4; i++) {
            const slot = document.createElement('div');
            slot.className = 'ea';
            slot.id = `ea-${i}`;
            enemyArea.appendChild(slot);
        }
        console.log('enemy-area reset.');
    } else {
        console.error('enemy-area element not found');
    }
}




