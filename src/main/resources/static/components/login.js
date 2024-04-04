new Vue({
    el: '#app',
    data: {
        login: '',
        password: '',
        errorMessage: ''
    },
    methods: {
        access() {
            this.errorMessage = '';
            // Verifica se os campos estão preenchidos
            if (!this.login || !this.password) {
                this.errorMessage = 'Preencha todos os campos!';
                return;
            }

            // Organiza os dados do usuário 
            const userData = {
                login: this.login,
                senha: this.password
            };

            // POST request
            axios.post('http://localhost:8080/api/auth/login', userData)
                .then(response => {
                    // salva tokens
                    sessionStorage.setItem('accessToken', response.data.accessToken);
                    sessionStorage.setItem('refreshToken', response.data.refreshToken);
                    sessionStorage.setItem('login', this.login);

                    window.location.href = '/Home.html';
                })
                .catch(error => {
                    this.errorMessage = 'Usuário ou Senha incorretos';
                    // console.error(error);
                });
        }
    }
});


// if (this.login === 'admin' && this.password === 'admin') {
//     alert('Login efetuado com sucesso');
//     window.location.href = '/Home.html';
// }