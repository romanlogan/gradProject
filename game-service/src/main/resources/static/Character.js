//Character.js
let tempA=0;
let tempD =0;

export default class Character {
    constructor(name, maxHP, HP, defense, imageSrc,healthBarId,team,type,statusHP,ATK,temporaryATK,temporaryDEF) {
        this.name = name;           // Character name
        this.maxHP = maxHP;         // Maximum health
        this.HP = HP;               // Current health
        this.defense = defense;     // Defense value
        this.imageSrc = imageSrc;   // Image source
        this.healthBarId = healthBarId;
        this.team = team;
        this.type = type;
        this.statusHP=statusHP;
        this.ATK=ATK;
        this.temporaryATK=temporaryATK;
        this.temporaryDEF=temporaryDEF;
    }

    // Display message function to replace alert
    displayMessage(message) {
        const logContainer = document.getElementById("log-container");
        const messageBox = document.createElement("div");
        messageBox.className = "message-box";
        messageBox.textContent = message;
        logContainer.appendChild(messageBox);
        setTimeout(() => {
            messageBox.remove();
        }, 5000); // Remove the message after 5 seconds
    }

    async takeDamage(damage, character) {
        let effectiveDamage = Math.max(
            (damage + character.ATK + character.temporaryATK) -
            (this.defense + this.temporaryDEF),
            0
        ); // 計算實際傷害

        this.HP -= effectiveDamage;

        if (this.HP < 0) this.HP = 0;
        if (this.HP > this.maxHP) this.HP = this.maxHP;

        this.displayMessage(`${this.name} takes ${effectiveDamage} damage. Remaining HP: ${this.HP}`);

        // 修正這裡：透過 ID 抓取對應的元素，並更新文字
        const hpElement = document.getElementById(this.statusHP);
        if (hpElement) {
            hpElement.textContent = `${this.HP}/${this.maxHP}`;
        }
        await Battle.updateCharacterStatus(this);
    }

    async takeAOE(damage,team,character){
        const targets =
            team === "friendly-area"
                ? [...window.friendlyCharacter]
                : [...window.enemyCharacter]; // 複製陣列

        for (let target of targets) {
            await target.takeDamage(damage, character);
        }
    }
    async increaseAttack(ATK){
        this.ATK+=ATK;
        this.displayMessage(`${this.name} increasing ATK. Current ATK:${this.ATK}+${this.temporaryATK}`);
    }
    async increaseDefense(DEF){
        this.defense+=DEF;
        this.displayMessage(`${this.name} increasing DEF. Current DEF:${this.defense}+${this.temporaryDEF}`);
    }

    async increaseTempAttack(ATK) {
        this.temporaryATK+=ATK;
        this.displayMessage(`${this.name} increasing TempATK. Current ATK:${this.ATK}+${this.temporaryATK}`);
    }

    async increaseTempDefense(DEF) {
        this.temporaryDEF+=DEF;
        this.displayMessage(`${this.name} increasing TempDEF. Current DEF:${this.defense}+${this.temporaryDEF}`);
    }

    async recordTemp(){
        tempA=this.temporaryATK;
        tempD=this.temporaryDEF;
    }

    async replyTemporary(){
        this.temporaryATK-=tempA;
        this.temporaryDEF-=tempD;
    }

    async setHP(HP){
        this.displayMessage(`${this.name} is healing, value:${HP}`);
        if(HP+this.HP<=this.maxHP){
            this.HP+=HP;
        }
        else{
            this.HP=this.maxHP;
        }
    }

    createElement() {
        const Container = document.createElement('div');
        Container.className = this.type;

        const healthBarWrapper = document.createElement('div');
        healthBarWrapper.className = 'health-bar-wrapper';

        const healthBar = document.createElement('div');
        healthBar.className = 'health-bar';
        healthBar.id = this.healthBarId;

        healthBarWrapper.appendChild(healthBar);
        Container.appendChild(healthBarWrapper);

        const objectImg = document.createElement('img');
        objectImg.alt = this.name;
        objectImg.src = this.imageSrc;
        objectImg.setAttribute('draggable', 'false');

        const objectDescription = document.createElement('p');
        objectDescription.className = 'target-description';
        objectDescription.textContent = this.name;

        Container.appendChild(objectImg);
        Container.appendChild(objectDescription);

        objectImg.addEventListener('mouseover', (event) => {
            const hoverTimeout = setTimeout(() => {
                showCharacterInfo(this, event); // 傳遞事件以計算座標
            }, 250); // 延遲 0.25 秒顯示
            objectImg.addEventListener('mouseleave', () => {
                clearTimeout(hoverTimeout);
                hideCharacterInfo();
            }, { once: true });
        });

        return Container; // 確保返回的是 DOM 節點
    }



    create() {
        const availableSlot = this.getAvailableSlot();
        if (availableSlot) {
            this.addToTeam(this, this.team);

            // 根據 slot ID 動態生成 healthBarId 和 statusHP
            const slotId = availableSlot.id.toUpperCase(); // 例如：fa-1 -> FA-1
            this.healthBarId = `${slotId}-HP-ID`; // FA-1-HP-ID
            this.statusHP = `${slotId}-ST-HP-ID`; // FA-1-ST-HP-ID
            // 直接使用初始化時的 healthBarId 和 statusHP
            availableSlot.appendChild(this.createElement());

            const statusBarId = this.team === 'friendly-area' ? 'friendly-status-bar' : 'enemy-status-bar';
            const statusContents = document.querySelectorAll(`#${statusBarId} .status-content`);

            if (!statusContents || statusContents.length === 0) {
                console.error(`No status content found in ${statusBarId}`);
                return;
            }

            const areaId = this.team === 'friendly-area' ? 'friendly-area' : 'enemy-area';
            this.adjustCharacterSize(areaId);
            this.adjustCharacterPosition(availableSlot);

            // 在第一個空的 status-content 中添加角色資訊
            for (let status of statusContents) {
                if (status.innerHTML.trim() === '') {
                    const characterInfo = document.createElement('span');
                    characterInfo.className = 'character-info';
                    characterInfo.innerHTML = `${this.name} HP: <span id="${this.statusHP}">${this.HP}/${this.maxHP}</span>`;
                    status.appendChild(characterInfo);
                    break;
                }
            }
        } else {
            console.log('No available slots!');
        }
    }


    adjustCharacterSize(areaId) {
        const area = document.getElementById(areaId);
        const characters = area.querySelectorAll('.friendly-object, .enemy-object');

        const characterCount = characters.length;
        const newHeight = Math.max(8, Math.floor(60 / characterCount)) + 'vh'; // 根據角色數量調整高度

        characters.forEach(character => {
            character.style.height = newHeight;
        });
    }
    adjustCharacterPosition(slot) {
        const children = slot.children;
        Array.from(children).forEach(child => {
            child.style.margin = '0 auto'; // 確保水平居中
            child.style.position = 'relative'; // 避免絕對定位影響對齊
        });
    }

    getAvailableSlot() {
        const areaClass = this.team === 'friendly-area' ? 'fa' : 'ea'; // Select appropriate class
        const slots = document.querySelectorAll(`#${this.team} .${areaClass}`);

        for (let slot of slots) {
            if (slot.children.length === 0) {
                return slot; // Return the first empty slot found
            }
        }
        return null; // No empty slot available
    }

    addToTeam(character,team){
        if(team==="friendly-area")
        {
            window.friendlyCharacter.push(character);
            console.log("character:",character.name,"friendly-area");
        }
        else if(team==="enemy-area")
        {
            window.enemyCharacter.push(character);
            console.log("character:",character.name,"enemy-area");
        }
        else console.log("wrong team");
    }

    //顯示下次的動作
    showAction(value){

    }

    getHp(){

        return this.HP;
    }

    setHp(savedHp){

        this.HP = savedHp;
    }

}

function showCharacterInfo(character, mouseEvent) {
    const infoContainer = document.getElementById('character-info');
    if (!infoContainer) {
        console.error('Character info container not found!');
        return;
    }

    // 設置信息內容
    infoContainer.innerHTML = `
        <p>ATK: ${character.ATK + character.temporaryATK}</p>
        <p>DEF: ${character.defense + character.temporaryDEF}</p>
    `;

    // 獲取滑鼠位置
    const mouseX = mouseEvent.pageX;
    const mouseY = mouseEvent.pageY;

    // 設置資訊框位置
    infoContainer.style.left = `${mouseX}px`; // X軸座標
    infoContainer.style.top = `${mouseY + 10}px`; // Y軸下方 10px
    infoContainer.style.display = 'block';
}

function hideCharacterInfo() {
    const infoContainer = document.getElementById('character-info');
    if (infoContainer) {
        infoContainer.style.display = 'none';
    }
}






