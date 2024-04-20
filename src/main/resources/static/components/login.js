new Vue({
    el: '#app',
    data: {
        login: '',
        password: '',
        errorMessage: ''
    },
    methods: {
        // Login
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
            .catch(error => {
                this.errorMessage = 'Usuário ou Senha incorretos';
            })
            .then(response => {
                // Salva os tokens no localStorage
                localStorage.setItem('accessToken', response.data.accessToken);
                localStorage.setItem('refreshToken', response.data.refreshToken);     
                
                // Define headers
                const accessToken = response.data.accessToken;
                const headers = {
                    Authorization: 'Bearer ' + accessToken 
                };
                
                // GET request
                axios.get('http://localhost:8080/api/usuarios/' + this.login, { headers })
                .then(response => {
                    // Salva informações do usuario no localStorage
                    localStorage.setItem('login', this.login);
                    localStorage.setItem('nome', response.data.nome);
                    localStorage.setItem('foto', response.data.foto);

                    // Redireciona pra Home
                    window.location.href = '/Home.html';
                })
                .catch(error => {
                    this.errorMessage = error; 
                });
            });
        }
    }
});

