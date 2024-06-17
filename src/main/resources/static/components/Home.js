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
        nome: null,
        papel: null,
        itens_carrinho: []
    },
    mounted() {
        this.displayUserImage();
        this.nome = localStorage.getItem('nome');
        this.papel = localStorage.getItem('papel'); 

        this.itens_carrinho = localStorage.getItem('itens_carrinho'); 
        this.itens_carrinho = (this.itens_carrinho === null) ? [] : this.itens_carrinho.split(','); 

        waitForElementToExist('#loading').then(loading => {
            // pega os produtos ativados
            axios.get('http://localhost:8080/api/produtos/ativados')
                .then(response => {
                    const produtos = response.data;
                    const carouselContainer = document.getElementById('productCarouselContainer');

                    // catalogo de produtos
                    produtos.forEach(produto => {
                        const productCard = this.createProductCard(produto.id, produto.fotos, produto.nome, produto.preco);
                        const colDiv = document.createElement('div');
                        colDiv.className = 'col-md-3 product-card';
                        colDiv.appendChild(productCard);
                        carouselContainer.appendChild(colDiv);
                    });
                    loading.classList.add('d-none'); // Esconde o texto de carregamento
                })
                .catch(error => {
                    console.error(error);
                    alert(error);
                });
        });
    },
    methods: {
        disconnect() {
            // Limpa os itens da localStorage
            localStorage.removeItem('nome');
            localStorage.removeItem('foto');
            localStorage.removeItem('papel');
            localStorage.removeItem('accessToken');
            localStorage.removeItem('itens_carrinho');
            localStorage.removeItem('id');

            // Limpa os cookies
            document.cookie = "";

            // refresh
            window.location.href = '/';
        },

        // Exibe a foto do usuário
        displayUserImage() {
            var userImg = document.getElementById('display1');
            const foto = localStorage.getItem('foto');
            if (foto !== null) {
                userImg.src = 'data:image/png;base64,' + foto.replace(/"/g, '');
            }
            else {
                userImg.src = '../img/usuario_padrao.png';
            }
        },

        createProductCard(id, photos, name, price) {
            // Cria o card
            const cardDiv = document.createElement('div');
            cardDiv.className = 'card';
        
            // Adiciona o carousel
            const carousel = this.createCarousel(id, photos);
            cardDiv.appendChild(carousel);
        
            // Adiciona o corpo do card
            const cardBody = document.createElement('div');
            cardBody.className = 'product-info';
        
            // Adiciona o nome
            const cardTitle = document.createElement('h5');
            cardTitle.className = 'card-title';
            cardTitle.textContent = name;
            cardBody.appendChild(cardTitle);
        
            // Adiciona o preço
            const cardPrice = document.createElement('p');
            cardPrice.className = 'card-text';
            const formattedPrice = parseFloat(price).toFixed(2);
            cardPrice.innerHTML = `<strong>R$${formattedPrice}</strong>`;
            cardBody.appendChild(cardPrice);
            
        
            // Adiciona o botão de comprar
            const addToCartButton = document.createElement('button');
            addToCartButton.textContent = 'Comprar';
            addToCartButton.className = 'btn btn-primary';
            addToCartButton.style.backgroundColor = 'white';
            addToCartButton.style.color = 'black';
            addToCartButton.style.border = '1px solid rgba(0, 0, 0, 0.5)';
            addToCartButton.style.borderRadius = '50px'; 
            
            // Adiciona efeito de hover
            addToCartButton.style.transition = 'background-color 0.3s ease'; 
            addToCartButton.addEventListener('mouseenter', function() {
                addToCartButton.style.backgroundColor = 'lightblue';
            });
            addToCartButton.addEventListener('mouseleave', function() {
                addToCartButton.style.backgroundColor = 'white';
            });

            // Adiciona o produto ao carrinho
            addToCartButton.addEventListener('click', () => {
                if (this.nome !== null) {
                    this.itens_carrinho.push(id);
                    localStorage.setItem('itens_carrinho', this.itens_carrinho);
                    alert('Produto adicionado ao carrinho!');
                }
                else {
                    window.location.href = '/login';
                }
            });

            cardBody.appendChild(addToCartButton);
            
            cardDiv.appendChild(cardBody);
            return cardDiv;
        },

        // seta os parametros do carousel
        createCarouselItem(src, isActive) {
            const div = document.createElement('div');
            div.className = `carousel-item ${isActive ? 'active' : ''}`;
            const img = document.createElement('img');
            img.src = src;
            img.className = 'd-block w-100';
            div.appendChild(img);
            return div;
        },
        
        // cria o carousel
        createCarousel(id, photos) {
            const carouselId = `carousel-${id}`;
            const div = document.createElement('div');
            div.id = carouselId;
            div.className = 'carousel slide';
            div.setAttribute('data-bs-ride', 'carousel');
        
            const innerDiv = document.createElement('div');
            innerDiv.className = 'carousel-inner';
        
            // adiciona as fotos
            for (let i = 0; i < photos.length; i++) {
                const src = 'data:image/png;base64,' + photos[i].foto.replace(/"/g, '');
                const isActive = i === 0; // seta o primeiro como o padrao
                const carouselItem = this.createCarouselItem(src, isActive);
                innerDiv.appendChild(carouselItem);
            }
            div.appendChild(innerDiv);
        
            // botoes de controle do carousel
            if (photos.length > 1) {
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
    },
});