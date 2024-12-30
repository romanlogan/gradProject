
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
            displayMessage("你的手牌已達上限");
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

                displayMessage("所有卡片已重新放回並洗牌！");
            } else {
                displayMessage("没有卡片可以抽取！");
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

        let hoverTimeout; // 用于记录 setTimeout 的变量
        cardElement.addEventListener('mouseover', () => {
            hoverTimeout = setTimeout(() => {
                showCardImage(card.imageUrl, card.DefaultValue); // 显示卡牌图片和 DefaultValue
            }, 250); // 0.25秒延迟
        });

        cardElement.addEventListener('mouseleave', () => {
            clearTimeout(hoverTimeout); // 清除延迟
            hideCardImage(); // 隐藏卡牌图片
        });

        cardElement.appendChild(img);
        playerArea.appendChild(cardElement);
    }

    // 重新設置卡牌選擇功能
    makeCardsSelectable();
}


function showCardImage(imageUrl, defaultValue) {
    const imageContainer = document.getElementById('hover-image');

    // 更新 HTML 結構，包括圖片和文字欄
    imageContainer.innerHTML = `
    <div style="text-align:center">
        <img src="${imageUrl}" alt="Card Image" style="max-width: 50vw; max-height: 50vh; display: block; margin: 0 auto;" />
        <p style="background-color: rgba(0, 0, 0, 0.6); margin-top: -50px; font-size: 1.3em; color: #ffffff; padding: 2px 10px; border-radius: 5px; display: inline-block;">Default Value: ${defaultValue}</p>
    </div>
`;

    imageContainer.style.display = 'block';
}


function hideCardImage() {
    const imageContainer = document.getElementById('hover-image');
    imageContainer.style.display = 'none';
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
        displayMessage("Please place the card on a valid target!");
        return; // 如果目標無效則退出
    }

    // 提取 img 標籤中的 alt 屬性
    let targetImg = targetElement.querySelector('img');
    let targetDescription = null;
    if (targetImg) {
        targetDescription = targetImg.alt; // 取得 alt 屬性
        console.log("targetDescription:", targetDescription);
    } else {
        displayMessage("No valid image found in the target object!");
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
        displayMessage("Your cost is not enough for you to use this card.");
        return;
    }

    if (foundCharacter) {
        console.log("Match character:", target);
        window.selectedCharacter = foundCharacter;

        // 在這裡寫對該角色進行的操作
        if (window.selectedCard.CardID === 2) {
            console.log("Healing Effect for CardID 3 triggered.");
            showPowerUpAnimation(targetElement);
        }

        if (window.selectedCard.CardID === 3) {
            console.log("Healing Effect for CardID 3 triggered.");
            showHealingEffect(targetElement);
        }

        // Execute card effect logic
        cardEmploy(window.selectedCard, window.selectedCharacter);

        if (window.selectedCard.CardID === 1 || window.selectedCard.Description.toLowerCase().includes("damage")) {
            showSlashAnimation(targetElement);
            vibrateCharacter(targetElement);
        }

        if (window.selectedCard.CardID === 5) {
            showAttackEffect(targetElement);
            vibrateCharacter(targetElement);
        }

    } else {
        displayMessage("Target character not found!");

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

function displayMessage(message) {
    const logContainer = document.getElementById("log-container");
    const messageBox = document.createElement("div");
    messageBox.className = "message-box";
    messageBox.textContent = message;
    logContainer.appendChild(messageBox);
    setTimeout(() => {
        messageBox.remove();
    }, 5000); // Remove the message after 5 seconds
}

function showAttackEffect(targetElement) {
    if (!targetElement) {
        console.error("Target element for attack effect not found.");
        return;
    }

    const attackEffect = document.createElement('div');
    attackEffect.className = 'attack-animation';

    // Position the effect in the center of the target element
    attackEffect.style.top = `${targetElement.offsetHeight / 2}px`;
    attackEffect.style.left = `${targetElement.offsetWidth / 2}px`;

    targetElement.appendChild(attackEffect);

    // Remove the effect after the animation finishes
    setTimeout(() => {
        attackEffect.remove();
    }, 500); // Match the animation duration
}


function showHealingEffect(targetElement) {
    if (!targetElement) {
        console.error("No target element for healing effect.");
        return;
    }

    // Find the image inside the target element
    const targetImage = targetElement.querySelector('img');
    if (!targetImage) {
        console.error("No image found inside the target element.");
        return;
    }

    // Get the image's position relative to its parent
    const imageRect = targetImage.getBoundingClientRect();

    // Create the healing icon
    const healingIcon = document.createElement('div');
    healingIcon.className = 'healing-icon';

    // Position the healing icon relative to the image
    healingIcon.style.position = 'absolute';
    healingIcon.style.top = `${targetImage.offsetTop - 10}px`; // Adjust for vertical alignment
    healingIcon.style.left = `${targetImage.offsetLeft + targetImage.offsetWidth / 2 - 10}px`; // Center horizontally

    // Append to the targetElement (not the image, so it's layered correctly)
    targetElement.appendChild(healingIcon);

    // Remove after animation

    setTimeout(() => {
        healingIcon.remove();
    }, 1000); // Match animation duration

}





//POWER UP ANIMATION
function showPowerUpAnimation(targetElement) {
    if (!targetElement) {
        console.error("Target element for power-up animation is not defined.");
        return;
    }

    console.log("Applying circular glow effect to:", targetElement);

    // Ensure we're targeting the image inside the container
    const imageElement = targetElement.querySelector('img');
    if (imageElement) {
        imageElement.classList.add('glow-effect');

        // Remove the effect after the animation completes
        setTimeout(() => {
            imageElement.classList.remove('glow-effect');
        }, 1000); // Match the CSS animation duration
    } else {
        console.error("No image found in the target element.");
    }
}




function showSlashAnimation(targetElement) {
    if (!targetElement) {
        console.error("Target element not found for slash animation.");
        return;
    }

    // Get the position and size of the target element relative to the enemy-area
    const targetRect = targetElement.getBoundingClientRect();
    const enemyArea = document.getElementById('enemy-area');
    const enemyAreaRect = enemyArea.getBoundingClientRect();

    // Calculate the slash animation's position relative to the enemy-area
    const left = targetRect.left - enemyAreaRect.left;
    const top = targetRect.top - enemyAreaRect.top;

    // Create the slash animation element
    const slash = document.createElement('div');
    slash.className = 'slash-animation';

    // Set the position and size of the animation
    slash.style.position = 'absolute';
    slash.style.left = `${left}px`; // Position relative to enemy-area
    slash.style.top = `${top}px`; // Position relative to enemy-area
    slash.style.width = `${targetRect.width}px`;
    slash.style.height = `${targetRect.height}px`;

    // Append the slash animation to the enemy-area
    enemyArea.appendChild(slash);

    // Remove the animation after it finishes
    setTimeout(() => {
        slash.remove();
    }, 500); // Match the duration of the animation
}

function vibrateCharacter(targetElement) {
    if (!targetElement) {
        console.error("Target element for vibration is not defined.");
        return;
    }
    console.log("Applying vibration effect to:", targetElement);

    const image = targetElement.querySelector('img'); // Target only the image
    if (image) {
        // Add vibrate and red-tint classes to the image
        image.classList.add('vibrate', 'red-tint');

        // Remove both classes after the duration
        setTimeout(() => {
            image.classList.remove('vibrate', 'red-tint');
        }, 500); // Matches the animation duration
    } else {
        console.error("No image found inside the target element.");
    }
}