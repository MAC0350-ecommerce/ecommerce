new Vue({
    el: '#app',
    data: {
        new_nome: '',
        new_preco: '',
        new_descricao: '',
        new_ativado: '',
        new_fotos: [],
        imageBytes: null
    },
    mounted() {
        // Pega o token
        const accessToken = localStorage.getItem('accessToken');
        const headers = {
            Authorization: 'Bearer ' + accessToken
        };
        
        // GET request
        axios.get('http://localhost:8080/api/produtos/', { headers })
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
                    const nomeCell = document.createElement('td');
                    nomeCell.textContent = row.nome;
                    tr.appendChild(nomeCell);

                    // Preco
                    const precoCell = document.createElement('td');
                    precoCell.textContent = row.preco;
                    tr.appendChild(precoCell);

                    // Data de Cadastro
                    const dataCell = document.createElement('td');
                    dataCell.textContent = row.dataCadastro;
                    tr.appendChild(dataCell);

                    // Descricao
                    const descricaoCell = document.createElement('td');
                    descricaoCell.textContent = row.descricao;
                    tr.appendChild(descricaoCell);

                    // Ativado
                    const ativadoCell = document.createElement('td');
                    ativadoCell.textContent = row.ativado ? 'Sim' : 'Não';
                    tr.appendChild(ativadoCell)

                    // Fotos Carousel
                    const fotosCell = document.createElement('td');
                    const carouselDiv = document.createElement('div');
                    carouselDiv.id = 'carouselExampleControls' + row.id; 
                    carouselDiv.className = 'carousel slide';
                    carouselDiv.setAttribute('data-ride', 'carousel');
                    const innerDiv = document.createElement('div');
                    innerDiv.className = 'carousel-inner';

                    // Adiciona as fotos no carousel
                    for (let i = 0; i < row.fotos.length; i++) {
                        const foto = row.fotos[i].foto;
                        const carouselItem = document.createElement('div');
                        carouselItem.className = 'carousel-item' + (i === 0 ? ' active' : ''); // Set first item as active
                        const img = document.createElement('img');
                        img.src = 'data:image/png;base64,' + foto.replace(/"/g, '');
                        img.style.border = '1px solid #000';
                        img.style.width = '80px';
                        img.style.height = '80px';

                        carouselItem.appendChild(img);
                        innerDiv.appendChild(carouselItem);
                    }

                    if (row.fotos.length > 1) {
                        // Previous button
                        const prevControl = document.createElement('a');
                        prevControl.className = 'carousel-control-prev';
                        prevControl.href = '#carouselExampleControls' + row.id;
                        prevControl.setAttribute('role', 'button');
                        prevControl.setAttribute('data-slide', 'prev');
                        prevControl.innerHTML = '<span class="carousel-control-prev-icon" aria-hidden="true"></span><span class="sr-only"> < </span>';
                        prevControl.style.color = 'black';
                        prevControl.style.textDecoration = 'none';
                        prevControl.style.fontSize = '1.5rem';
                        prevControl.style.opacity = '1';
                        
                        // Next button
                        const nextControl = document.createElement('a');
                        nextControl.className = 'carousel-control-next';
                        nextControl.href = '#carouselExampleControls' + row.id;
                        nextControl.setAttribute('role', 'button');
                        nextControl.setAttribute('data-slide', 'next');
                        nextControl.innerHTML = '<span class="carousel-control-next-icon" aria-hidden="true"></span><span class="sr-only"> > </span>';
                        nextControl.style.color = 'black';
                        nextControl.style.textDecoration = 'none';
                        nextControl.style.fontSize = '1.5rem';
                        nextControl.style.opacity = '1';

                        // Adiciona os elementos do carousel na pagina
                        carouselDiv.appendChild(prevControl);
                        carouselDiv.appendChild(nextControl);
                    }
                    carouselDiv.appendChild(innerDiv);
                    fotosCell.appendChild(carouselDiv);
                    tr.appendChild(fotosCell);

                    tableBody.appendChild(tr);
                });
                loading.classList.add('d-none'); // Esconde o texto de carregamento
                table.classList.remove('d-none'); // Mostra a tabela
            })
            .catch(error => {
                console.error(error);
                alert(error);
            });
    },
    methods: {
        addNewItem() {
            if (!this.new_nome || !this.new_preco || !this.new_descricao || !this.new_ativado){
                alert("Preencha todos os campos!");
                return;
            }
            
            // Formata o campo 'preco'
            this.new_preco = parseFloat(this.new_preco);

            // Formata o campo 'ativado'
            const ativadoLowercase = this.new_ativado.toLowerCase();
            if (ativadoLowercase === 'sim' || ativadoLowercase === '1'){
                this.new_ativado = "true";
            }
            else if (ativadoLowercase === 'não' || ativadoLowercase === 'nao' || ativadoLowercase === '0'){
                this.new_ativado = "false";
            }
            else if (ativadoLowercase !== 'true' && ativadoLowercase !== 'false'){
                alert("O campo 'Ativado' deve ser 'Sim' ou 'Não'");
                return;
            }


            // Organiza os dados
            const data = {
                nome: this.new_nome,
                preco: this.new_preco,
                descricao: this.new_descricao,
                ativado: this.new_ativado,
                fotos: this.new_fotos
            };

            // POST request
            axios.post('http://localhost:8080/api/produtos/', data)
                .then(response => {
                    alert("Produto adicionado com sucesso!");
                    window.location.reload();
                })
                .catch(error => {
                    console.error(error);
                    alert("Não foi possível adicionar o item :(");
                });
        },
        // upload das foto
        handleFileUpload(event) {
            const files = event.target.files;

            for (let i = 0; i < files.length; i++) {
                const file = files[i]
                this.imageName = file.name;

                // converte parra array de bytes
                if (file && file.type.startsWith('image/')) {
                    const reader = new FileReader();
                    reader.onload = () => {
                        const arrayBuffer = reader.result;
                        this.imageBytes = new Uint8Array(arrayBuffer);
                        this.imageBytes = Array.from(this.imageBytes);
                        this.new_fotos.push(this.imageBytes);
                    };
                    reader.readAsArrayBuffer(file);
                }
            }
        }
    }
});
