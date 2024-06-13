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
        new_tag: '',
        new_nome: '',
        new_ativado: true
    },
    mounted() {
        // Pega o token
        const accessToken = localStorage.getItem('accessToken');
        const headers = {
            Authorization: 'Bearer ' + accessToken
        };

        // GET request
        waitForElementToExist('#loading').then(loading => {
            axios.get('http://localhost:8080/api/categorias/', { headers })
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

                        // Tag
                        const tagCell = document.createElement('td');
                        tagCell.textContent = row.tag;
                        tr.appendChild(tagCell);

                        // Codigo
                        const nomeCell = document.createElement('td');
                        nomeCell.textContent = row.nome;
                        tr.appendChild(nomeCell);

                        // Data de Cadastro
                        const dataCell = document.createElement('td');
                        dataCell.textContent = row.dataCadastro;
                        tr.appendChild(dataCell);

                        // Ativado
                        const ativadoCell = document.createElement('td');
                        ativadoCell.textContent = row.ativado ? 'Sim' : 'Não';
                        tr.appendChild(ativadoCell)

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
            if (!this.new_tag || !this.new_nome) {
                alert("Preencha todos os campos!");
                return;
            }
            
            // Organiza os dados
            const data = {
                tag: this.new_tag,
                nome: this.new_nome,
                ativado: this.new_ativado
            };
            
            // POST request
            axios.post('http://localhost:8080/api/categorias/', data)
                .then(response => {
                    alert("Item adicionado com sucesso!");
                    window.location.reload();
                })
                .catch(error => {
                    console.error(error);
                    alert(error);
                });
        }
    }
});