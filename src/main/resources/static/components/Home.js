new Vue({
    el: '#app',
    data: {
        accessToken: '',
        refreshToken: '',
        nome: '',
        foto: 'null'
    },
    mounted() {
        // Pega os itens da localStorage
        this.displayUserImage();
        this.accessToken = localStorage.getItem('accessToken');
        this.refreshToken = localStorage.getItem('refreshToken');
        this.nome = localStorage.getItem('nome');
    },
    methods: {
        disconnect() {
            // Limpa os itens da localStorage
            localStorage.removeItem('accessToken');
            localStorage.removeItem('refreshToken');
            localStorage.removeItem('login');
            localStorage.removeItem('nome');
            localStorage.removeItem('foto');

            // Limpa os itens do Vue
            this.accessToken = '';
            this.refreshToken = '';
            this.nome = ''; 
            this.foto = 'null';

            // Seta a foto placeholder
            userImg = document.getElementById('display1');
            userImg.src = 'https://ennhri.org/wp-content/uploads/2023/01/Portrait-placeholder.png';
        },
        // Exibe a foto do usuário
        displayUserImage() {
            var userImg = document.getElementById('display1');
            this.foto = localStorage.getItem('foto');
            if (this.foto && this.foto !== 'null') {
                userImg.src = 'data:image/png;base64,' + this.foto.replace(/"/g, '');;
            }
            else {
                userImg.src = 'https://ennhri.org/wp-content/uploads/2023/01/Portrait-placeholder.png';
            }
        }
    }
});