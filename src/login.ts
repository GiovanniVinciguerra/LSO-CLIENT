import axios from "axios";

const button = document.getElementById("login");
const info = document.getElementById("info");
const username_input = document.getElementById("username") as HTMLInputElement;
const password_input = document.getElementById("password") as HTMLInputElement;

const trismaster = 'http://trismaster.ddns.net:8080/';

if (button) {
  console.log("Sono qui");
  button.addEventListener("click", async () => {
    if (info)
      info.innerText = 'Tentativo di login in corso...';

    const username = username_input?.value;
    const password = password_input?.value;

    if (!username || !password) {
      if (info) info.innerText = "Inserisci username e password";
      return;
    }

    try {
        const response = await axios.post(
            trismaster + 'login',
            `{
              "username": "${username}",
              "password": "${password}"
            }`,
            {
              headers: {
                'Content-Type': 'application/json'
              }
            }
          );          

      console.log("Login riuscito:", response.data);

      if (info) info.innerText = "Login effettuato con successo!";

      localStorage.setItem('sessionId', response.data.token);
      localStorage.setItem('username', response.data.username);

      window.location.href = '/home.html';

    } catch (error) {
      console.error("Errore login:", error);
      if (info) info.innerText = "Errore durante il login. Controlla le credenziali.";
    }
  });
}
