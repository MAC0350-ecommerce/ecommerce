new Vue({
    el: '#app',
    data: {
        name: '',
        login: '',
        password: '',
        confirmPassword: '',
        errorMessage: '',
        imageName: '',
        imageBytes: []
    },
    methods: {
        // criar conta
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
                senha: this.password,
                foto: this.imageBytes
            };

            // POST request 
            axios.post('http://localhost:8080/api/usuarios', userData)
                .then(response => {
                    alert('Conta criada com sucesso!');
                    window.location.href = '/views/login.html';
                })
                .catch(error => {
                    this.errorMessage = error; 
                });
        },
        // upload da foto
        handleFileUpload(event) {
            const file = event.target.files[0];
            this.imageName = file.name;

            // converte parra array de bytes
            if (file && file.type.startsWith('image/')) {
                const reader = new FileReader();
                reader.onload = () => {
                    const arrayBuffer = reader.result;
                    this.imageBytes = new Uint8Array(arrayBuffer);
                    this.imageBytes = Array.from(this.imageBytes);
                };
                reader.readAsArrayBuffer(file);
            }
        }            
    }
});
