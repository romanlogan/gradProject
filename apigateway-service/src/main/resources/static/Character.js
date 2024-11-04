//Character.js

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

    async takeDamage(damage, character) {
        let effectiveDamage = Math.max(
            (damage + character.ATK + character.temporaryATK) -
            (this.defense + this.temporaryDEF),
            0
        ); // 計算實際傷害

        this.HP -= effectiveDamage;

        if (this.HP < 0) this.HP = 0;
        if (this.HP > this.maxHP) this.HP = this.maxHP;

        alert(`${this.name} takes ${effectiveDamage} damage. Remaining HP: ${this.HP}`);

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

    async increaseAttack(ATK) {
        this.temporaryATK+=ATK;
        alert(`${this.name} increasing ATK. Current temporaryATK:${this.temporaryATK}`);
    }

    async increaseDefense(DEF) {
        this.temporaryDEF+=DEF;
        alert(`${this.name} increasing DEF. Current temporaryDEF:${this.temporaryDEF}`);
    }

    async resetTemporary(property){
        console.log("resetTemporary",this.name);
        console.log("resetProperty",property);
        if(property==="ATK")this.temporaryATK=0;
        if(property==="DEF")this.temporaryDEF=0;
    }

    async setHP(HP){
        alert(`${this.name} is healing, value:${HP}`);
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

}



