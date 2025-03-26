import axios from "axios";

const trismaster = 'http://trismaster.ddns.net:8080/';
const messageContainer = document.getElementById("floatingMessages");

async function loadMessages() {
  if (!messageContainer) return;

  try {
    const response = await axios.get(trismaster + 'messages');

    if (Array.isArray(response.data)) {
      messageContainer.innerHTML = ""; 

      response.data.forEach((msg: string) => {
        const card = document.createElement("div");
        card.className = "card";
        card.textContent = msg;
        messageContainer.appendChild(card);
      });
    }
  } catch (error) {
    console.error("Errore nel recupero dei messaggi:", error);
  }
}

// Esegui al load + ogni 30 secondi
document.addEventListener("DOMContentLoaded", () => {
  loadMessages();
  setInterval(loadMessages, 30000);
});
