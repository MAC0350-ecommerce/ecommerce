new Vue({
    el: '#app',
    data: {
        login: '',
        password: '',
        errorMessage: ''
    },
    methods: {
        // Login
        // TODO: PRECISA VALIDAR O TOKEN, CASO JA EXISTA NO LOCAL OU COOKIE
        access() {
            this.errorMessage = '';
            // Verifica se os campos estão preenchidos
            if (!this.login || !this.password) {
                this.errorMessage = 'Preencha todos os campos!';
                return;
            }

            // Organiza os dados do usuário
            const data = {
                login: this.login,
                senha: this.password
            };

            // POST request
            axios.post('http://localhost:8080/api/auth/login', data)
                .then(response => {
                    // Salva o token no localStorage
                    localStorage.setItem('accessToken', response.data.accessToken);

                    // Salva o token no cookie
                    document.cookie = "tokenAPP=" + response.data.accessToken + ";"

                    // Define headers
                    const accessToken = response.data.accessToken;
                    const headers = {
                        Authorization: 'Bearer ' + accessToken
                    };

                    // GET request
                    axios.get('http://localhost:8080/api/cadastros/' + this.login, { headers })
                        .then(response => {
                            // Salva informações do usuario no localStorage
                            localStorage.setItem('nome', response.data.nome);
                            localStorage.setItem('foto', response.data.foto);
                            localStorage.setItem('papel', response.data.papel);
                            localStorage.setItem('id', response.data.id);
                            localStorage.setItem('login', response.data.login);

                            // Redireciona pra Home
                            window.location.href = '/';
                        })
                        .catch(error => {
                            this.errorMessage = error;
                        });
                })
                .catch(error => {
                    this.errorMessage = 'Usuário ou Senha incorretos';
                });
        },
    }
});
