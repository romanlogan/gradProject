//CardSystem.js

// 洗牌功能
export function shuffle(array) {
    for (let i = array.length - 1; i > 0; i--) { // 修正為 i > 0
        let j = Math.floor(Math.random() * (i + 1)); // 修正為 i + 1
        [array[i], array[j]] = [array[j], array[i]]; // 交換元素
    }
}

export function draw(num) {
    let drawnCards = 0; // 記錄已抽取的卡數

    while (drawnCards < num) { // 使用 while 迴圈以便根據狀態決定是否繼續
        if (window.playerOne.length >= 10) {
            alert("你的手牌已達上限");
            break; // 如果手牌達上限，停止抽卡
        } else {
            if (window.drawCards.length > 0) {
                // 抽取第一张牌
                let card = window.drawCards[0];

                // 將該卡牌完整資訊加入 playerOne 中
                window.playerOne.push({ ...card });

                // 從 drawCards 中移除該卡牌
                window.drawCards.splice(0, 1);

                // 更新畫面上的卡牌顯示
                updatePlayerCards();
                updateDrawCards();

                drawnCards++; // 每成功抽取一張卡就加一
            } else if (window.dropCards.length > 0) {
                // 把 drawCards 裡的卡片移回 drawCards
                window.drawCards = window.drawCards.concat(window.dropCards);
                window.dropCards = [];

                shuffle(window.drawCards);
                updateDrawCards();
                updateDropCards();

                alert("所有卡片已重新放回並洗牌！");
            } else {
                alert("没有卡片可以抽取！");
                break; // 如果沒有卡片可以抽取，停止抽卡
            }
        }
    }
}







export function dropAll() {
    // 把 playerOne 的卡牌加入 drawCards
    window.dropCards = window.dropCards.concat(window.playerOne);
    window.playerOne = [];

    updatePlayerCards();
    updateDropCards();
}


// 更新手牌显示
export function updatePlayerCards() {
    console.log("window.playerOne:",window.playerOne); // 檢查 playerOne 中的卡牌是否正確
    const playerArea = document.getElementById('card');
    playerArea.innerHTML = ''; // 清空当前卡片

    for (const card of window.playerOne) {
        const cardElement = document.createElement('div');
        cardElement.className = 'card';

        // 使用 card.imageUrl 获取图片路径
        const img = document.createElement('img');
        img.src = card.imageUrl; // 更新图片路径
        img.alt = card.CardName; // 设置合适的 alt 文本

        cardElement.appendChild(img);
        playerArea.appendChild(cardElement);
    }

    // 重新設置卡牌選擇功能
    makeCardsSelectable();
}



// 更新 drawCards 显示
export function updateDrawCards() {
    const drawCardsContainer = document.getElementById('all-cards');
    drawCardsContainer.innerHTML = ''; // 清空容器

    window.drawCards.forEach(card => {
        const cardElement = document.createElement('div');
        cardElement.className = 'card'; // 添加样式类

        cardElement.innerHTML = `
            <img src="${card.imageUrl}" alt="${card.CardName}" draggable="false" />  <!-- 确保不可拖动 -->
        `;
        drawCardsContainer.appendChild(cardElement); // 添加到容器中
    });

}

// 更新弃牌区
export function updateDropCards() {
    let drawCardsDiv = document.getElementById("drop-cards");
    let cardImages = "";

    for (let i = 0; i < window.dropCards.length; i++) {
        const card = window.dropCards[i];
        cardImages += `<img src='${card.imageUrl}' alt='${card.CardName}' draggable="false">`; // 直接使用卡片資訊
    }

    drawCardsDiv.innerHTML = cardImages; // 更新顯示
}

// 抽牌区按钮
export function toggleDrawCards() {
    let drawCardsDiv = document.getElementById("all-cards");
    drawCardsDiv.style.display = (drawCardsDiv.style.display === "none") ? "grid" : "none"; // 切换显示状态
}

// 弃牌区按钮
export function toggleDropCards() {
    let dropCardsDiv = document.getElementById("drop-cards");
    dropCardsDiv.style.display = (dropCardsDiv.style.display === "none") ? "grid" : "none"; // 切换显示状态
}

// 状态(是否可以拖拉卡牌)
export function allowDrop(event) {
    event.preventDefault(); // 允许拖放行为
}

// 設置卡牌可選擇
export function makeCardsSelectable() {
    const playerCards = document.querySelectorAll('#card img');
    playerCards.forEach((card, index) => {
        card.setAttribute('data-index', index); // 為每個卡片設置唯一的 data-index 屬性
        card.setAttribute('draggable', 'false'); // 初始設置為不可拖動

        // 添加 click 事件監聽器
        card.addEventListener('click', (event) => {
            // 檢查是否已經選擇了此卡牌
            if (window.selectedCard && window.selectedCard.index === index) {
                console.log("取消選擇卡牌");
                window.selectedCard = null; // 取消選擇
                card.setAttribute('draggable', 'false'); // 不可拖動
            } else {
                console.log(`正在選擇卡片，索引為: ${index}`);
                select(index); // 調用選擇卡片的函數

                window.selectedCard = {
                    index: index,
                    imageUrl: card.src,
                    CardName: card.alt
                };
                card.setAttribute('draggable', 'true'); // 可拖動
            }
        });
    });

    const drawCards = document.querySelectorAll('#all-cards img, #drop-cards img');
    drawCards.forEach(card => {
        card.setAttribute('draggable', 'false');
    });

    const targets = document.querySelectorAll('.friendly-object, .enemy-object');
    targets.forEach(target => {
        target.addEventListener('dragover', allowDrop);
        target.addEventListener('drop', triggerCard);
    });
}


// 選擇卡牌的函數
function select(cardIndex) {
    console.log(`選擇的卡牌索引: ${cardIndex}`);

    const playerCards = document.querySelectorAll('#card img');
    playerCards.forEach((card, index) => {
        if (index === cardIndex) {
            card.classList.add('selected'); // 添加高亮樣式
            card.setAttribute('draggable', 'true'); // 設置可拖動

            // 添加拖動事件監聽器
            card.addEventListener('dragstart', (event) => {
                event.dataTransfer.setData('text/plain', cardIndex); // 設置拖動的索引
                console.log(`正在拖動卡片，索引為: ${cardIndex}`);

                window.selectedCard=window.playerOne[cardIndex];
                console.log("window.selectedCard:",window.selectedCard);
            });

            // 添加拖放事件監聽器
            card.addEventListener('dragend', (event) => {
                // 在這裡可以清除選擇的狀態
                card.classList.remove('selected'); // 移除選中樣式
            });
        } else {
            card.classList.remove('selected'); // 移除其他卡牌的高亮樣式
            card.setAttribute('draggable', 'false'); // 設置不可拖動
        }
    });
}



// 觸發卡牌使用
export function triggerCard(event) {
    event.preventDefault();

    // 獲取拖放目標
    let targetElement = event.target.closest('.friendly-object, .enemy-object');
    console.log("targetElement:", targetElement);
    if (!targetElement) {
        alert("请将卡牌放置在有效目标上！");
        return; // 如果目標無效則退出
    }

    // 提取 img 標籤中的 alt 屬性
    let targetImg = targetElement.querySelector('img');
    let targetDescription = null;
    if (targetImg) {
        targetDescription = targetImg.alt; // 取得 alt 屬性
        console.log("targetDescription:", targetDescription);
    } else {
        alert("目标对象中未找到有效的图片！");
        return;
    }

    let target=targetDescription;

    // 使用一些迴圈查找目標角色
    let foundCharacter = null;
    for (let characterGroup of window.characters) {
        foundCharacter = characterGroup.find(character => character.name === target);
        if (foundCharacter) {
            break;  // 如果找到，停止迴圈
        }
    }

    //cost Check
    let cardCost = window.selectedCard.Cost;
    let havenCost =window.cost;
    if(cardCost > havenCost)
    {
        alert("Cost不足以使用卡牌");
        return;
    }

    if (foundCharacter) {
        console.log("找到相符的角色:", target);
        window.selectedCharacter = foundCharacter;

        // 在這裡寫對該角色進行的操作
        cardEmploy(window.selectedCard,window.selectedCharacter); // 假設此函數會使用選中的角色
    } else {
        alert("未找到對應的角色！");
        return;
    }

    removeCardFromHand(window.selectedCard);

    updatePlayerCards();
}

// 移除手牌中的指定卡牌
function removeCardFromHand(selectedCard) {
    // 找到要移除的卡牌在 playerOne 中的索引
    const cardIndex = window.playerOne.findIndex(card => card.index === selectedCard.index);

    // 如果找到了有效的索引
    if (cardIndex !== -1) {

        console.log("Cost足夠");

        let removedCard = window.playerOne.splice(cardIndex, 1)[0]; // 移除指定 index 的卡牌並獲取移除的卡牌
        console.log(`已移除手牌中的卡牌，索引: ${cardIndex}, 卡牌名: ${removedCard.CardName}`);

        // 將移除的卡片放入 dropCards
        window.dropCards.push(removedCard);

        // 更新 drawCards 的顯示
        updateDropCards();
    } else {
        console.error("無法移除卡牌，提供的卡牌無效或不存在。");
    }
}