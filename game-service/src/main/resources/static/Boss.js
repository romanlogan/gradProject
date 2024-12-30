import Character from "./Character.js";

let actionID = 0;
let target = null;
let coolDown=false;

export default class Boss extends Character{
    constructor(name, maxHP, HP, defense, imageSrc,healthBarId,team,type,statusHP,ATK,temporaryATK,temporaryDEF) {
        super(name, maxHP, HP, defense, imageSrc,healthBarId,team,type,statusHP,ATK,temporaryATK,temporaryDEF);
        this.name="Minotaur";
        this.maxHP=20;
        this.HP=20;
        this.defense=1;
        this.imageSrc='../texture/Enemies/minotaur.png';
        this.healthBarId='boss-health-bar';
        this.team='enemy-area';
        this.type='enemy-object boss-character';
        this.statusHP='boss-hp';
        this.ATK=0;
        this.temporaryATK=0;
        this.temporaryDEF=0;
    }

    async action() {
        actionID = Math.floor(Math.random() * 5); //0,1,2,3,4

        // 隨機選擇一個友方角色作為目標
        if (window.friendlyCharacter.length > 0) {
            const randomIndex = Math.floor(Math.random() * window.friendlyCharacter.length);
            target = window.friendlyCharacter[randomIndex];
        } else {
            console.log('No friendly characters available to attack.');
            return;
        }
        console.log(`${this.name} action to ${target.name}`);
        console.log(this.name, " actionID:", actionID);

        if(Battle.round%5===0)
        {
            await this.increaseAttack(1);
        }

        if(coolDown===true){
            coolDown= !coolDown;
            this.displayMessage(`${this.name} is rest`);
            return;
        }

        switch(actionID){
            case 0:
                //
                await target.takeDamage(10, this);
                break;
            case 1:
                await this.increaseTempDefense(12);

                break;
            case 2:
                await target.takeDamage(8, this);
                await this.increaseTempDefense(8);

                break;
            case 3:
                await this.increaseTempAttack(5);

                break;
            case 4:
                await target.takeDamage(20,this);
                this.displayMessage(`${this.name} take a rest until next turn.`);
                coolDown=true;
                break;
            case 5:
                await target.takeAOE(8,target.team,this);
                break;
            case 6:
                await this.setHP(20);
                break;
            default:
                console.log('No valid action selected');
                break;
        }
        await Battle.checkVictoryOrDefeat();
    }
}