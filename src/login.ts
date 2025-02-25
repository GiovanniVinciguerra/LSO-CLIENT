import axios from "axios"

const button = document.getElementById("login")
const info = document.getElementById("info")
const username_input = document.getElementById("username") as HTMLInputElement
const password_input = document.getElementById("password") as HTMLInputElement

const trismaster = 'http://trismaster.ddns.net:8080/'

if(button) {
    console.log("Sono qui")
    button.addEventListener("click", async () => {
        if(info)
            info.innerText = 'Qui saranno visualizzati i messaggi rivolti agli utenti'
    })
}