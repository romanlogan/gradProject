import Enemy from "./Enemy.js";
import Boss from "./Boss.js";
import Flying_Pumpkin from "./pumpkin.js";
import Pirate from "./Pirate.js";
import Fying_Cyclops from "./Flying_Cyclops.js";
import Elf_bow from "./elf bow.js";
import undead_elf from "./undead elf.js";
import skeleton_mage from "./skeleton mage.js";
import zombie from "./zombie.js";
import foxy from "./foxy.js";
import cyclop_pirate from "./cyclop pirate.js";
import Skeleton from "./skeleton.js";

export let stage = 1;
let difficulty = 0;

const stageBackgrounds = {
    0: '../texture/background1_shore.png',
    1: '../texture/background2_shore.png',
    2: '../texture/background3_jungle.png',
    3: '../texture/background4_jungle.png',
    5: '../texture/background5_cave.png',
    6: '../texture/background6_exitcave.png',
    8: '../texture/background7_city.png',
    10: '../texture/background8_town.png',
    12: '../texture/background9_entracne.png',
    14: '../texture/background10_final.png'
};

function updateBackground(stage) {
    const backgroundImage = stageBackgrounds[stage] || 'background/background1.png';
    const battleArea = document.getElementById('battle-area');
    if (battleArea) {
        battleArea.style.position = 'fixed'; // Ensure the background is fixed to the screen
        battleArea.style.top = '0';
        battleArea.style.left = '0';
        battleArea.style.width = '100vw'; // Full width of the viewport
        battleArea.style.height = '100vh'; // Full height of the viewport
        battleArea.style.backgroundImage = `url('${backgroundImage}')`;
        battleArea.style.backgroundSize = 'cover';
        battleArea.style.backgroundPosition = 'bottom center';
        battleArea.style.backgroundRepeat = 'no-repeat';
        battleArea.style.zIndex = '-1'; // Send it to the back

    } else {
        console.error('Battle area not found!');
    }
}


// export let idMap=[0,1,2,3,5,6,8,10,12,14]; // boss stage <--> 14 0-4-->enemy, 5-8-->pirate, 9-13-->Flying_Cyclops
export let idMap=[0,2,14];
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


    updateBackground(value);
    console.log("Resetting enemy stage for value:", value);
    if(resetEnemyStage)
    {
        //shore stage 1 & 2
        if(value>=0 && value <=1)
        {
            switch (caseID) {
                case 0:
                    summonPirate();
                    break;
                case 1:
                    cyclopPirate();
                    summonPirate();
                    break;
                case 2:
                    summonPirate();
                    summonPirate();
                    cyclopPirate();
                    break;
                case 3:
                    summonPirate();
                    summonPirate();
                    cyclopPirate();
                    break;
                default:
                    break;
            }
        }
        //jungle
        else if(value>=2 && value <=4)
        {
            switch (caseID) {
                case 0:
                    summonShadow();
                    break;
                case 1:
                    summonElfBow();
                    summonShadow();
                    break;
                case 2:
                    summonElfBow();
                    summonElfBow();
                    summonElfBow();
                    break;
                case 3:
                    summonElfBow();
                    summonElfBow();
                    summonElfBow();
                    summonElfBow();
                    break;
                default:
                    break;
            }
        }
        //in the cave
        else if(value===5)
        {
            {
                switch (caseID) {
                    case 0:
                        summonPumpkin();
                        break;
                    case 1:
                        summonPumpkin();
                        summonPumpkin();
                        break;
                    case 2:
                        summonFlying_Cyclops();
                        summonPumpkin();
                        summonFlying_Cyclops();
                        break;
                    case 3:
                        summonPumpkin();
                        summonFlying_Cyclops();
                        summonFlying_Cyclops();
                        summonFlying_Cyclops();
                        break;
                    default:
                        break;
                }
            }
        }
        //town and city
        else if(value>=6 && value<=11)
        {
            {
                switch (caseID) {
                    case 0:
                        summonFoxy();
                        break;
                    case 1:
                        summonUndeadElf();
                        summonZombie();
                        break;
                    case 2:
                        summonZombie();
                        summonUndeadElf();
                        summonFoxy();
                        break;
                    case 3:
                        summonFoxy();
                        summonZombie();
                        summonUndeadElf();
                        summonZombie();
                        break;
                    default:
                        break;
                }
            }
        }
        //entrance of castle
        else if(value===12)
        {
            {
                switch (caseID) {
                    case 0:
                        summonUndead();
                        summonMage();
                        summonUndead();
                        summonUndead();
                        break;
                    case 1:
                        summonUndead();
                        summonMage();
                        summonUndead();
                        summonUndead();
                        break;
                    case 2:
                        summonUndead();
                        summonMage();
                        summonUndead();
                        summonUndead();
                        break;
                    case 3:
                        summonUndead();
                        summonMage();
                        summonUndead();
                        summonUndead();
                        break;
                    default:
                        break;
                }
            }
        }
        //final boss
        else if(value===14)
        {
            summonBoss();
            return;
        }

    }
    difficulty++;
}

function summonBoss(){
    let boss=new Boss();
    boss.create();
}

//shore level
function cyclopPirate() {
    let cycloppirate = new cyclop_pirate();
    cycloppirate.name='Cyclop';
    cycloppirate.create();
    cycloppirate.maxHP = 6 + (2 * difficulty) + Math.floor(Math.random() * 5);
    cycloppirate.HP = cycloppirate.maxHP;
}

function summonPirate(){
    let pirate1 = new Pirate();
    pirate1.name='Pirate';
    pirate1.create();
    pirate1.maxHP=5 +difficulty+ Math.floor(Math.random() * 5);
    pirate1.HP=pirate1.maxHP;
}

//Jungle level
function summonElfBow(){
    let enemy2 = new Elf_bow();
    enemy2.name='Lev';
    enemy2.create();
    enemy2.maxHP=6 +difficulty+ Math.floor(Math.random() * 5);
    enemy2.HP=enemy2.maxHP;
}
function summonShadow(){
    let shadow = new Enemy();
    shadow.name='Shadow';
    shadow.create();
    shadow.maxHP=5 +difficulty+ Math.floor(Math.random() * 5);
    shadow.HP=shadow.maxHP;
}

//Cave
function summonFlying_Cyclops(){
    let flying_cyclops = new Fying_Cyclops();
    flying_cyclops.name='Fly Bot';
    flying_cyclops.create();
    flying_cyclops.maxHP=5 +difficulty+ Math.floor(Math.random() * 5);
    flying_cyclops.HP=flying_cyclops.maxHP;
}

function summonPumpkin(){
    let pumpkin = new Flying_Pumpkin();
    pumpkin.name='Jack O lantern';
    pumpkin.create();
    pumpkin.maxHP=6 +difficulty+ Math.floor(Math.random() * 5);
    pumpkin.HP=pumpkin.maxHP;
}

//exit, town
function summonZombie(){
    let Zombie = new zombie();
    Zombie.name='Zombie';
    Zombie.create();
    Zombie.maxHP=5 +difficulty+ Math.floor(Math.random() * 5);
    Zombie.HP=Zombie.maxHP;
}

function summonFoxy(){
    let Foxy = new foxy();
    Foxy.name='Foxy';
    Foxy.create();
    Foxy.maxHP = 6 + (2 * difficulty) + Math.floor(Math.random() * 5);
    Foxy.HP=Foxy.maxHP;
}
function summonUndeadElf(){
    let Uelf = new undead_elf();
    Uelf.name='Relf';
    Uelf.create();
    Uelf.maxHP=6 +difficulty+ Math.floor(Math.random() * 5);
    Uelf.HP=Uelf.maxHP;
}

function summonMage(){
    let Mage = new skeleton_mage();
    Mage.name='Lich';
    Mage.create();
    Mage.maxHP = 6 + (2 * difficulty) + Math.floor(Math.random() * 5);
    Mage.HP=Mage.maxHP;
}

function summonUndead(){
    let Undead = new Skeleton();
    Undead.name='Undead';
    Undead.create();
    Undead.maxHP= 6 +difficulty+ Math.floor(Math.random() * 5);
    Undead.HP=Undead.maxHP;
}


function positionEnemies() {
    const enemyArea = document.getElementById('enemy-area');
    const enemies = Array.from(enemyArea.children);

    // Ensure enemy-area has position relative
    enemyArea.style.position = 'relative';

    const enemyCount = enemies.length;
    const centerX = enemyArea.offsetWidth / 2;
    const centerY = enemyArea.offsetHeight / 2;
    const radius = 100; // Adjust radius for spacing

    enemies.forEach((enemy, index) => {
        // Calculate the angle for each enemy's position
        const angle = (2 * Math.PI * index) / enemyCount;
        const x = centerX + radius * Math.cos(angle) - enemy.offsetWidth / 2;
        const y = centerY + radius * Math.sin(angle) - enemy.offsetHeight / 2;

        // Set the position relative to the enemy-area
        enemy.style.position = 'absolute';
        enemy.style.left = `${x}px`;
        enemy.style.top = `${y}px`;
    });
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

export function setStage(savedStage){
    stage = savedStage;
}

