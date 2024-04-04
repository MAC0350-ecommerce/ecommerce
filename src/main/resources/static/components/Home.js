new Vue({
    el: '#app',
    data: {
        accessToken: '',
        refreshToken: '',
        nome: ''
    },
    mounted() {
        // Retrieve items from sessionStorage
        this.accessToken = sessionStorage.getItem('accessToken');
        this.refreshToken = sessionStorage.getItem('refreshToken');
        this.nome = sessionStorage.getItem('login');
    },
    methods: {
        disconnect() {
            sessionStorage.removeItem('accessToken');
            sessionStorage.removeItem('refreshToken');
            sessionStorage.removeItem('login');
            this.accessToken = '';
            this.refreshToken = '';
            this.nome = '';
        }
    }
});