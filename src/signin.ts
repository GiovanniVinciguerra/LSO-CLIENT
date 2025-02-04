import axios from "axios"

const button = document.getElementById("submit")
const name_input = document.getElementById("name") as HTMLInputElement
const surname_input = document.getElementById("surname") as HTMLInputElement
const username_input = document.getElementById("username") as HTMLInputElement
const email_input = document.getElementById("email") as HTMLInputElement
const password_input = document.getElementById("password") as HTMLInputElement

if(button) {
    button.addEventListener("click", async () => {
        if(name_input.value != null && surname_input.value != null && username_input.value != null && email_input.value != null && password_input.value != null) {
            try {
                const response = await axios.post('http://urlserver/signin', {
                    nome: name_input.value,
                    surname: surname_input.value,
                    username: username_input.value,
                    email: email_input.value,
                    password: password_input.value
                });
                console.log('Response:', response.data);
            } catch (error) {
                console.error('Error:', error);
            }
        }
    })
}