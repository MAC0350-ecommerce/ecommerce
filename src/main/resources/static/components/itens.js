function waitForElementToExist(selector) {
    return new Promise(resolve => {
        if (document.querySelector(selector)) {
            return resolve(document.querySelector(selector));
        }

        const observer = new MutationObserver(() => {
            if (document.querySelector(selector)) {
                resolve(document.querySelector(selector));
                observer.disconnect();
            }
        });

        observer.observe(document.body, {
            subtree: true,
            childList: true,
        });
    });
}

new Vue({
    el: '#app',
    data: {
        new_pid: '',
        new_codigo: ''
    },
    mounted() {
        // Pega o token
        const accessToken = localStorage.getItem('accessToken');
        const headers = {
            Authorization: 'Bearer ' + accessToken
        };

        // GET request
        waitForElementToExist('#loading').then(loading => {
            axios.get('http://localhost:8080/api/itens/', { headers })
                .then(response => {
                    const data = response.data;
                    const table = document.querySelector('#data-table');
                    const tableBody = document.querySelector('#data-table tbody');

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

                    // Hide loading text and show the table
                    loading.classList.add('d-none');
                    table.classList.remove('d-none');
                });
            })
            .catch(error => {
                console.error(error);
                alert(error);
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
                .then(response => {
                    alert("Item adicionado com sucesso!");
                    window.location.reload();
                })
                .catch(error => {
                    console.error(error);
                    alert("Não foi possível adicionar o item :(");
                });
        }
    }
});