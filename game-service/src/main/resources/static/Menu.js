import * as Battle from "./BattleSystem.js";

export function menu() {
    const menuBox = document.getElementById("menuBox");
    const computedStyle = window.getComputedStyle(menuBox);

    console.log("Menu display style before Function:", computedStyle.display);

    if (computedStyle.display === "none") {
        menuBox.style.display = "block";
        document.getElementById("menu").style.display = "flex"; // 顯示 menu
        console.log("OpenMenuFunction");
    } else {
        menuBox.style.display = "none";
        document.getElementById("menu").style.display = "none"; // 隱藏 menu
        console.log("CloseMenuFunction");
    }

    console.log("Menu display style after Function:", menuBox.style.display);
}

export function saveAndExit(){

    Battle.saveData("menu");

    location.href="/game-service/main";
}