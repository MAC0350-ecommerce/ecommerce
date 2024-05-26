new Vue({
    el: '#app',
    data: {
        new_nome: '',
        new_login: '',
        new_senha: '',
        imageBytes: null,
        xxx: ''
    },
    mounted() {
        // Pega o token
        const accessToken = localStorage.getItem('accessToken');
        const headers = {
            Authorization: 'Bearer ' + accessToken
        };
        this.xxx = accessToken;
        // GET request
        axios.get('http://localhost:8080/api/cadastros/', { headers })
            .then(response => {
                const data = response.data;
                const tableBody = document.querySelector('#data-table tbody');
                const table = document.querySelector('#data-table');
                const loading = document.querySelector('#loading');

                // Adiciona as linhas na tabela
                data.forEach(row => {
                    const tr = document.createElement('tr');

                    // ID
                    const idCell = document.createElement('td');
                    idCell.textContent = row.id;
                    tr.appendChild(idCell);

                    // Nome
                    const nameCell = document.createElement('td');
                    nameCell.textContent = row.nome;
                    tr.appendChild(nameCell);

                    // Login
                    const loginCell = document.createElement('td');
                    loginCell.textContent = row.login;
                    tr.appendChild(loginCell);

                    // Papel
                    const papelCell = document.createElement('td');
                    papelCell.textContent = row.papel;
                    tr.appendChild(papelCell);
                    
                    // Foto
                    const fotoCell = document.createElement('td');
                    const fotoImg = document.createElement('img');
                    fotoImg.src = 'data:image/png;base64,' + row.foto.replace(/"/g, '');
                    fotoImg.style.width = '40px'; 
                    fotoImg.style.height = '40px';
                    fotoImg.style.border = '1px solid #000';
                    fotoCell.appendChild(fotoImg);
                    tr.appendChild(fotoCell);

                    tableBody.appendChild(tr);
                });
                loading.classList.add('d-none'); // Esconde o texto de carregamento
                table.classList.remove('d-none'); // Mostra a tabela
            })
            .catch(error => {
                console.error(error);
                alert("Não foi possível carregar os dados :(");
            });
    },
    methods: {
        addNewItem() {
            if (!this.new_nome || !this.new_login || !this.new_senha){
                alert("Preencha todos os campos!");
                return;
            }

            // Organiza os dados
            const data = {
                nome: this.new_nome,
                preco: this.new_login,
                descricao: this.new_senha,
                fotos: this.imageBytes
            };

            // POST request
            axios.post('http://localhost:8080/api/cadastros/', data)
                .then(response => {
                    alert("Usuário adicionado com sucesso!");
                    window.location.reload();
                })
                .catch(error => {
                    console.error(error);
                    alert("Não foi possível adicionar o item :(");
                });
        },
        // upload das foto
        handleFileUpload(event) {
            const file = event.target.files[0];

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
