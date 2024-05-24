new Vue({
    el: '#app',
    data: {
        xxx: "a",
        new_pid: "",
        new_codigo: ""
    },
    mounted() {
        // Pega o token
        const accessToken = localStorage.getItem('accessToken');
        const headers = {
            Authorization: 'Bearer ' + accessToken
        };
        
        this.xxx = accessToken;
        // GET request
        axios.get('http://localhost:8080/api/itens/', { headers })
            .catch(error => {
                console.error(error);
                alert("Não foi possível carregar os dados :(");
            })
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

                    // ID do Produto
                    const pidCell = document.createElement('td');
                    pidCell.textContent = row.produto_id;
                    tr.appendChild(pidCell);

                    // Codigo
                    const codigoCell = document.createElement('td');
                    codigoCell.textContent = row.codigo;
                    tr.appendChild(codigoCell);

                    // Data de Cadastro
                    const dataCell = document.createElement('td');
                    dataCell.textContent = row.dataCadastro;
                    tr.appendChild(dataCell);

                    tableBody.appendChild(tr);
                });
                loading.classList.add('d-none'); // Esconde o texto de carregamento
                table.classList.remove('d-none'); // Mostra a tabela
            });
    },
    methods: {
        addNewItem() {
            if (!this.new_pid || !this.new_codigo) {
                alert("Preencha todos os campos!");
                return;
            }
            
            // Organiza os dados
            const data = {
                produto_id: this.new_pid,
                codigo: this.new_codigo
            };
            
            // POST request
            axios.post('http://localhost:8080/api/itens/', data)
                .catch(error => {
                    console.error(error);
                    alert("Não foi possível adicionar o item :(");
                })
                .then(response => {
                    alert("Item adicionado com sucesso!");
                    window.location.reload();
                });
        }
    }
});
