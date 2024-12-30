let cost =0;
let costMax = 3;

window.cost=cost;
window.costMax=costMax;

export function resetCost(){

    window.cost=window.costMax;
}

export function upDateCost() {
    let currentCost=window.cost;
    let maxCost=window.costMax;
    const costDisplay = document.getElementById('costDisplay');
    costDisplay.textContent = `${currentCost}/${maxCost}`; // 更新顯示的成本
}