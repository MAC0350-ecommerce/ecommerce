new Vue({
    el: '#app',
    data: {
        name: '',
        login: '',
        password: '',
        confirmPassword: '',
        errorMessage: ''
    },
    methods: {
        signup() {
            this.errorMessage = '';

            // Verifica se os campos estão preenchidos
            if (!this.name || !this.login || !this.password || !this.confirmPassword) {
                this.errorMessage = 'Preencha todos os campos!';
                return;
            }
            if (this.password !== this.confirmPassword) {
                this.errorMessage = 'As senhas não conferem!';
                return;
            }

            // Organiza os dados do usuário 
            const userData = {
                nome: this.name,
                login: this.login,
                senha: this.password
            };

            // POST request 
            axios.post('http://localhost:8080/api/cadastros', userData)
                .then(response => {
                    alert('Conta criada com sucesso!');
                    window.location.href = '/views/login.html';
                })
                .catch(error => {
                    this.errorMessage = "Criação de conta falhou";
                    // console.error(error);
                });
        }
    }
});
