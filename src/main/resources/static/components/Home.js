new Vue({
    el: '#app',
    data: {
        accessToken: '',
        refreshToken: '',
        nome: '',
        papel: '',
        foto: 'null'
    },
    mounted() {
        // Pega os itens da localStorage
        this.displayUserImage();
        this.accessToken = localStorage.getItem('accessToken');
        this.refreshToken = localStorage.getItem('refreshToken');
        this.nome = localStorage.getItem('nome');
        this.papel = localStorage.getItem('papel');
        this.foto = localStorage.getItem('foto')
    },
    methods: {
        disconnect() {
            // Limpa os itens da localStorage
            localStorage.removeItem('accessToken');
            localStorage.removeItem('refreshToken');
            localStorage.removeItem('login');
            localStorage.removeItem('nome');
            localStorage.removeItem('foto');
            localStorage.removeItem('papel');

            // Limpa os cookies
            document.cookie = ""

            // Limpa os itens do Vue
            this.accessToken = '';
            this.refreshToken = '';
            this.nome = ''; 
            this.papel = '';
            this.foto = 'null';

            // Seta a foto placeholder
            userImg = document.getElementById('display1');
            userImg.src = '../img/usuario_padrao.png';

            // Refresh na pagina
            location.reload();
        },

        // Exibe a foto do usuário
        displayUserImage() {
            var userImg = document.getElementById('display1');
            this.foto = localStorage.getItem('foto');
            if (this.foto && this.foto !== 'null') {
                userImg.src = 'data:image/png;base64,' + this.foto.replace(/"/g, '');
            }
            else {
                userImg.src = '../img/usuario_padrao.png';
            }
        }
    },
});