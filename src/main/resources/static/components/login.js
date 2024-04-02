new Vue({
    el: '#app',
    data: {
        username: '',
        password: '',
        errorMessage: ''
    },
    methods: {
        login() {
            if (this.username === 'admin' && this.password === 'admin') {
                alert('Login efetuado com sucesso');
                window.location.href = 'Home.html';
            } else {
                this.errorMessage = 'Login ou Senha inválidos!';
            }
        }
    }
});
