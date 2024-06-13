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

// seta os parametros do carousel
function createCarouselItem(src, isActive) {
    const div = document.createElement('div');
    div.className = `carousel-item ${isActive ? 'active' : ''}`;
    const img = document.createElement('img');
    img.src = src;
    img.className = 'd-block w-100';
    div.appendChild(img);
    return div;
}

// cria o carousel
function createCarousel(id, photos) {
    const carouselId = `carousel-${id}`;
    const div = document.createElement('div');
    div.id = carouselId;
    div.className = 'carousel slide';
    div.setAttribute('data-bs-ride', 'carousel');

    const innerDiv = document.createElement('div');
    innerDiv.className = 'carousel-inner';

    for (let i = 0; i < photos.length; i++) {
        const src = 'data:image/png;base64,' + photos[i].foto.replace(/"/g, '');
        const isActive = i === 0; // seta o primeiro como o padrao
        const carouselItem = createCarouselItem(src, isActive);
        innerDiv.appendChild(carouselItem);
    }

    div.appendChild(innerDiv);

    if (photos.length > 1){
        // botao voltar
        const prevButton = document.createElement('button');
        prevButton.className = 'carousel-control-prev';
        prevButton.type = 'button';
        prevButton.setAttribute('data-bs-target', `#${carouselId}`);
        prevButton.setAttribute('data-bs-slide', 'prev');
        prevButton.innerHTML = '<span class="carousel-control-prev-icon" aria-hidden="true"></span><span class="visually-hidden">Previous</span>';
        div.appendChild(prevButton);

        // botao proximo
        const nextButton = document.createElement('button');
        nextButton.className = 'carousel-control-next';
        nextButton.type = 'button';
        nextButton.setAttribute('data-bs-target', `#${carouselId}`);
        nextButton.setAttribute('data-bs-slide', 'next');
        nextButton.innerHTML = '<span class="carousel-control-next-icon" aria-hidden="true"></span><span class="visually-hidden">Next</span>';
        div.appendChild(nextButton);
    }

    return div;
}


new Vue({
    el: '#app',
    data: {
        new_nome: '',
        new_preco: '',
        new_descricao: '',
        new_categoria: '',
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
        
        waitForElementToExist('#loading').then(loading => {
            // GET request
            axios.get('http://localhost:8080/api/produtos/', { headers })
                .then(response => {
                    const data = response.data;
                    const tableBody = document.querySelector('#data-table tbody');
                    const table = document.querySelector('#data-table');
        
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
    
                        // Categoria
                        const categoriaCell = document.createElement('td');
                        categoriaCell.textContent = row.categoria;
                        tr.appendChild(categoriaCell);
    
                        // Descricao
                        const descricaoCell = document.createElement('td');
                        descricaoCell.textContent = row.descricao;
                        tr.appendChild(descricaoCell);
    
                        // Ativado
                        const ativadoCell = document.createElement('td');
                        ativadoCell.textContent = row.ativado ? 'Sim' : 'Não';
                        tr.appendChild(ativadoCell);
    
                        // Fotos
                        const fotosCell = document.createElement('td');
                        if (row.fotos && row.fotos.length > 0) {
                            const carousel = createCarousel(row.id, row.fotos);
                            fotosCell.appendChild(carousel);
                        }
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
        });
    },
    methods: {
        addNewItem() {
            if (!this.new_nome || !this.new_preco || !new_categoria){
                alert("Preencha todos os campos!");
                return;
            }
            
            // Formata o campo 'preco'
            this.new_preco = parseFloat(this.new_preco)

            // Organiza os dados
            const data = {
                nome: this.new_nome,
                categoria: this.new_categoria,
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