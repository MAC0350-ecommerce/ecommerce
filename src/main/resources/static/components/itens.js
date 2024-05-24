new Vue({
    el: '#app',
    mounted() {
        // Pega o token
        const accessToken = localStorage.getItem('accessToken');
        const headers = {
            Authorization: 'Bearer ' + accessToken
        };
        // GET request
        axios.get('http://localhost:8080/api/itens/', { headers })
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

                    // Foto
                    const fotoCell = document.createElement('td');
                    fotoCell.textContent = row.foto === "null" ? "Sim" : "Não"; 
                    tr.appendChild(fotoCell);
                    
                    tableBody.appendChild(tr);
                });
                loading.classList.add('d-none'); // Esconde o texto de carregamento
                table.classList.remove('d-none'); // Mostra a tabela
            })
            .catch(error => {
                console.error(error);
                alert('Erro ao carregar dados');
            });
    }
});
