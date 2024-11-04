
// 初始化一個空的 deck 陣列
let deck = [];

let cardIDs = [1,1,1,1,1,2,2,2,2,2,3,3,4,4,5,5]; // 應該要從後端拿

// 使用 loadFromLibrary 方法載入卡片並填充 deck
export async function initializeDeck() {

    for (let id of cardIDs) {
        const card = await Card.default.loadFromLibrary(id); // 直接通過類名調用靜態方法
        if (card instanceof Card.default) { // 確保是 Card 實例
            deck.push(card); // 將 Card 實例推入 deck
        } else {
            console.error(`Card with ID ${id} could not be loaded.`); // 錯誤訊息
        }
    }

    // 為每個卡牌分配唯一的 index 值
    for (let i = 0; i < deck.length; i++) {
        deck[i].index = i + 1;
    }

    console.log("deck",deck); // 在此輸出 deck，確認是否填充成功
}

// 將 deck 陣列匯出，讓其他模組能夠使用
export function getDeck() {
    return deck;
}

export function getCards() {
    return cardIDs;
}

export function getReward(card){
    cardIDs.push(card.CardID);
    console.log(`getReward: ${card.CardName}`);
    console.log("cardIDs[]:",cardIDs);

    return cardIDs;
}
