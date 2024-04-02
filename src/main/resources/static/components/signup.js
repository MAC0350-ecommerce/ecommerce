new Vue({
    el: '#app',
    data: {
        username: '',
        password: '',
        confirmPassword: '',
        errorMessage: ''
    },
    methods: {
        signup() {
            if (this.password !== this.confirmPassword) {
                this.errorMessage = 'As senhas não conferem!';
                return;
            }
            alert('Conta criada com sucesso!');
            window.location.href = '/views/login.html';
        }
    }
});
