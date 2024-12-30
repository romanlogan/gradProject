//Card.js

export default class Card {

    constructor(cardID, cardName, cost, imageUrl, description, defaultValue,index) {
        this.CardID = cardID; // 卡牌 ID
        this.CardName = cardName; // 卡牌名称
        this.Cost = cost; // 成本
        this.imageUrl = imageUrl; // 图片 URL
        this.Description = description; // 描述
        this.DefaultValue = defaultValue; // 默认值
    }

    // 將 loadFromLib44rary 修改為靜態方法
    static async loadFromLibrary(ID) {
        try {
            const response = await fetch('/cardLibrary.json');

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            const cards = await response.json();
            const cardData = cards.find(card => card.CardID === ID);

            if (cardData) {
                return new Card(
                    cardData.CardID,
                    cardData.CardName,
                    cardData.Cost,
                    cardData.imageUrl,
                    cardData.Description,
                    cardData.DefaultValue
                );
            } else {
                console.warn(`No card found with ID: ${ID}`);
                return null;
            }
        } catch (error) {
            console.error('Failed to load card library:', error);
        }
    }
}

// 打出卡牌
export async function playCard(card, target) {

    window.cost -= window.selectedCard.Cost;
    CostSystem.upDateCost();
    console.log("扣除Cost:",window.selectedCard.Cost);
    let character =window.friendlyCharacter[0];
    console.log("catch character:",character);

    switch (card.CardID) {
        case 1:
            await target.takeDamage(card.DefaultValue,character);
            console.log("playCard--case1");
            break;
        case 2:
            await target.increaseTempDefense(card.DefaultValue);
            console.log("playCard--case2");
            break;
        case 3:
            await target.setHP(card.DefaultValue);
            console.log("playCard--case3");
            break;
        case 4:
            await target.takeAOE(card.DefaultValue,target.team,character);
            console.log("playCard--case4");
            break;
        case 5:
            console.log(target.name,target.temporaryDEF);
            if(target.temporaryDEF>0)
            {
                await target.takeDamage(card.DefaultValue+10,character);
                console.log("playCard--case5 with bonus");
            }
            else
            {
                await target.takeDamage(card.DefaultValue+0,character);
                console.log("playCard--case5 without bonus");
            }
            break;

        case 6:

            const randomIndex = Math.floor(Math.random() * window.enemyCharacter.length);
            target = window.enemyCharacter[randomIndex];
            await target.takeDamage(card.DefaultValue,character);
            console.log("playCard--case6");
            break;

        case 7:
            await character.increaseTempAttack(card.DefaultValue);
            console.log("playCard--case7");
            break;

        case 8:
            await character.increaseAttack(card.DefaultValue);
            console.log("playCard--case8");
            break;

        default:
            console.log('This card has no defined effect.');
            break;
    }
    await Battle.checkVictoryOrDefeat();
}