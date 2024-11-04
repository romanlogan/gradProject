import Enemy from "./Enemy.js";
import Boss from "./Boss.js";


export let stage = 1;
let difficulty = 0;


export let idMap=[0,1,2,3,5,6,8,10,12,14];
idMap.length=10;

export let stageID=[];
stageID.length=15;
export let caseID=0;

export function setStageID(){
    for(let i=0;i<stageID.length;i++){
        caseID=Math.floor(Math.random() * 4);
        stageID[i]=caseID;
        console.log("stageID[",i,"]:",stageID[i]);
    }
}

export function stageEnemy(value){

    caseID = stageID[value];
    console.log("Generating enemies for caseID:", caseID);

    if(value===14)
    {
        summonBoss();
        return;
    }
    console.log("Resetting enemy stage for value:", value);
    if(resetEnemyStage)
    {
        switch (caseID) {
            case 0:
                summonEnemy1();
                break;
            case 1:
                summonEnemy1();
                summonEnemy2();
                break;
            case 2:
                summonEnemy1();
                summonEnemy2();
                summonEnemy3();
                break;
            case 3:
                summonEnemy1();
                summonEnemy2();
                summonEnemy3();
                summonEnemy4();
                break;
            default:
                break;
        }
    }
    difficulty++;
}

function summonBoss(){
    let boss=new Boss();
    boss.create();
}

function summonEnemy1() {
    let enemy1 = new Enemy();
    enemy1.name='Enemy1';
    enemy1.create();
    enemy1.maxHP = 6 + (2 * difficulty) + Math.floor(Math.random() * 5);
    enemy1.HP = enemy1.maxHP;
}


function summonEnemy2(){
    let enemy2 = new Enemy();
    enemy2.name='Enemy2';
    enemy2.create();
    enemy2.maxHP=6 +difficulty+ Math.floor(Math.random() * 5);
    enemy2.HP=enemy2.maxHP;
}
function summonEnemy3(){
    let enemy3 = new Enemy();
    enemy3.name='Enemy3';
    enemy3.create();
    enemy3.maxHP=5 +difficulty+ Math.floor(Math.random() * 5);
    enemy3.HP=enemy3.maxHP;
}

function summonEnemy4(){
    let enemy4 = new Enemy();
    enemy4.name='Enemy4';
    enemy4.create();
    enemy4.maxHP=5 +difficulty+ Math.floor(Math.random() * 5);
    enemy4.HP=enemy4.maxHP;
}

export function nextStage() {
    console.log("nextStage");
    stage++;
    const currentStageElement = document.getElementById("currentStage");
    if (currentStageElement) {
        currentStageElement.innerText = 'Stage : ' + stage;
    } else {
        console.error("currentStage element not found in the DOM");
        return Promise.reject(new Error("Failed to find currentStage element"));
    }

    return Initialization.Initialization()
        .then(() => {
            Battle.resetRound();
            return Battle.nextRound();
        })
        .catch(error => console.error("Error in nextStage:", error));
}


function resetEnemyStage() {
    // 確保陣列已清空
    window.enemyCharacter.length = 0;

    // 確保 DOM 已清空
    const enemyArea = document.getElementById('enemy-area');
    if (enemyArea) {
        enemyArea.innerHTML = '';
        console.log('Enemy area reset: All characters and DOM elements removed.');
    } else {
        console.error('Error: enemy-area not found in DOM.');
    }
}

export async function getStage() {
    return stage;
}

export function getCurrentStage(){
    return stage;

}