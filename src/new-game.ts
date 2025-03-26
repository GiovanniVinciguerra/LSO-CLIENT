import axios from "axios";

const trismaster = 'http://trismaster.ddns.net:8080/';

async function getMatchInfo() {
  const sessionId = localStorage.getItem("sessionId");
  const username = localStorage.getItem("username");

  if (!sessionId || !username) {
    console.warn("sessionId o username mancanti");
    return;
  }

  try {
    const response = await axios.post(trismaster + "matchinfo", {
      sessionId,
      username
    });

    const match = response.data;

    // Debug output
    console.log("Match info ricevute:");
    console.log("Match ID:", match.match_id);
    console.log("Player 1:", match.player_1);
    console.log("Player 2:", match.player_2);
    console.log("Status:", match.status);
    console.log("Step:", match.step);

    // Qui puoi aggiornare il DOM con queste info
    // oppure salvarle in una variabile globale o localStorage
  } catch (error) {
    console.error("Errore nel recupero info partita:", error);
  }
}

document.addEventListener("DOMContentLoaded", () => {
  getMatchInfo();
});
