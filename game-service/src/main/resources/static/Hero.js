// Hero.js
import Character from './Character.js'; // 引入 Character 類別

export default class Hero extends Character {
    constructor(name, maxHP, HP, defense, imageSrc,healthBarId,team,type,statusHP,ATK,temporaryATK,temporaryDEF) {
        super(name, maxHP, HP, defense, imageSrc,healthBarId,team,type,statusHP,ATK,temporaryATK,temporaryDEF);
        this.name='Hero';
        this.maxHP=80;
        this.HP=80;
        this.defense=0;
        this.imageSrc='../texture/mainHero.png';
        this.healthBarId='hero-health-bar';
        this.team='friendly-area';
        this.type='friendly-object';
        this.statusHP='hero-hp';
        this.ATK=0;
        this.temporaryATK=0;
        this.temporaryDEF=0;
    }
    action(){
    }

    getHp(){
        return this.HP
    }

}

