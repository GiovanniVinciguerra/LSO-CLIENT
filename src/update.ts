import axios from "axios";

const trismaster = 'http://trismaster.ddns.net:8080/';

async function updateSession() {
  const sessionId = localStorage.getItem("sessionId");
  const username = localStorage.getItem("username");

  if (!sessionId || !username) {
    console.warn("sessionId o username non presenti nel localStorage");
    return;
  }

  try {
    const response = await axios.post(trismaster + "update", {
      sessionId: sessionId,
      username: username
    });

    console.log("Update avvenuto con successo:", response.data);
  } catch (error) {
    console.error("Errore durante la chiamata a /update:", error);
  }
}

// Esegui automaticamente quando la pagina è pronta
document.addEventListener("DOMContentLoaded", () => {
  updateSession();
});
