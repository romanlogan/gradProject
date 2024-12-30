import Character from './Character.js';

export default class Cyclop_pirate extends Character {
    constructor(name, maxHP, HP, defense, imageSrc,healthBarId,team,type,statusHP,ATK,temporaryATK,temporaryDEF) {
        super(name, maxHP, HP, defense, imageSrc,healthBarId,team,type,statusHP,ATK,temporaryATK,temporaryDEF);
        this.name = 'Cyclop Pirate';
        this.maxHP = 7;
        this.HP = 7;
        this.defense = 0;
        this.imageSrc = '../texture/Enemies/cyclop_pirate.png';
        this.healthBarId = 'enemy-health-bar';
        this.team = 'enemy-area';
        this.type = 'enemy-object';
        this.statusHP = 'enemy-hp';
        this.ATK = 0;
        this.temporaryATK = 0;
        this.temporaryDEF = 0;
    }
    async action(){

        let actionID = Math.floor(Math.random()*3); //0,1,2
        let target = null;

        // 隨機選擇一個友方角色作為目標
        if (window.friendlyCharacter.length > 0) {
            const randomIndex = Math.floor(Math.random() * window.friendlyCharacter.length);
            target = window.friendlyCharacter[randomIndex];
        }
        else
        {
            console.log('No friendly characters available to attack.');
            return;
        }
        console.log(`${this.name} action to ${target.name}`);
        console.log(this.name," actionID:", actionID);

        if(Battle.round%5===0)
        {
            await this.increaseAttack(1);
        }

        switch(actionID){
            case 0:
                //
                await target.takeDamage(5, this);
                break;
            case 1:
                await this.increaseTempDefense(8);
                break;
            case 2:
                await target.takeDamage(3, this);
                await this.increaseTempDefense(3);
                break;
            default:
                console.log('No valid action selected');
                break;
        }
        await Battle.checkVictoryOrDefeat();
    }
}
