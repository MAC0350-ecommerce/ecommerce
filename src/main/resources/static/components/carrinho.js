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
        nome: null,
        papel: null,
        itens_carrinho: [],
        quantidade: {}
    },
    mounted() {
        this.displayUserImage();
        this.nome = localStorage.getItem('nome');
        this.papel = localStorage.getItem('papel'); 

        this.itens_carrinho = localStorage.getItem('itens_carrinho'); 
        this.itens_carrinho = (this.itens_carrinho === null) ? [] : this.itens_carrinho.split(','); 

        // Pega o token
        const accessToken = localStorage.getItem('accessToken');
        const headers = {
            Authorization: 'Bearer ' + accessToken
        };


        waitForElementToExist('#loading').then(loading => {
            // pega a quantidade de cada item
            this.itens_carrinho.forEach(item => {
                if (item in this.quantidade) {
                    this.quantidade[item]++;
                } else {
                    this.quantidade[item] = 1;
                }
            });

            loading.classList.add('d-none'); // esconde o loading

            // carrega os produtos
            for (let item in this.quantidade) {
                axios.get('http://localhost:8080/api/produtos/ativados/' + item)
                .then(response => {
                    const produto = response.data;
                    const cartDiv = document.getElementById('cart');

                    const colDiv = document.createElement('div');
                    colDiv.className = 'col-md-12 mb-4';

                    const cardDiv = document.createElement('div');
                    cardDiv.className = 'card';

                    const carouselDiv = createCarousel(produto.id, produto.fotos);

                    const cardBodyDiv = document.createElement('div');
                    cardBodyDiv.className = 'card-body';

                    const productName = document.createElement('h5');
                    productName.className = 'card-title';
                    productName.textContent = produto.nome;

                    // Create the "X" image for removing the item
                    const removeIcon = document.createElement('img');
                    removeIcon.src = '/img/remove-icon.png';
                    removeIcon.style.width = '20px';
                    removeIcon.style.height = '20px';
                    removeIcon.style.cursor = 'pointer';
                    removeIcon.style.float = 'right'; 
                    removeIcon.addEventListener('click', () => {
                        this.removeItemFromCart(item);
                    });

                    const productDescription = document.createElement('p');
                    productDescription.className = 'card-text';
                    productDescription.textContent = produto.descricao;

                    const productPriceDiv = document.createElement('div');
                    productPriceDiv.className = 'product-price';

                    const productPrice = document.createElement('p');
                    productPrice.className = 'card-text';
                    productPrice.innerHTML = `<strong>R$ ${produto.preco.toFixed(2)}</strong>`;

                    const quantityControls = this.createQuantityControls(produto.id, this.quantidade[item]);

                    productPriceDiv.appendChild(productPrice);
                    productPriceDiv.appendChild(quantityControls);

                    productName.appendChild(removeIcon); // Append the "X" icon to the product name
                    cardBodyDiv.appendChild(productName);
                    cardBodyDiv.appendChild(productDescription);
                    cardBodyDiv.appendChild(productPriceDiv);

                    cardDiv.appendChild(carouselDiv);
                    cardDiv.appendChild(cardBodyDiv);

                    colDiv.appendChild(cardDiv);

                    cartDiv.appendChild(colDiv);

                    qt = 1;
                })
                .catch(error => {
                    console.error(error);
                    alert('Erro ao carregar os produtos');
                });
            }
        });
        // ...
    },
    methods: {
        disconnect() {
            // Limpa os itens da localStorage
            localStorage.removeItem('nome');
            localStorage.removeItem('foto');
            localStorage.removeItem('papel');
            localStorage.removeItem('accessToken');
            localStorage.removeItem('itens_carrinho');

            // Limpa os cookies
            document.cookie = "";

            // refresh
            window.location.href = '/';
        },

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

        createQuantityControls(id, qt) {
            const div = document.createElement('div');
            div.className = 'quantity-controls';
        
            // botao da esquerda
            const decrementButton = document.createElement('button');
            decrementButton.className = 'btn btn-secondary btn-sm decrement-button';
            decrementButton.textContent = '-';
            decrementButton.onclick = function() {
                const quantityInput = this.nextElementSibling;
                let quantity = parseInt(quantityInput.value, 10);
                if (quantity > 1) {
                    quantityInput.value = --quantity;
                }
                this.quantidade[id] = quantity;
            };
        
            // quantidade
            const quantityInput = document.createElement('input');
            quantityInput.type = 'text';
            quantityInput.value = qt;
            quantityInput.className = 'form-control form-control-sm';
            quantityInput.style.width = '40px';
            quantityInput.readOnly = true;
            quantityInput.id = `quantity-${id}`;
        
            // botao da direita
            const incrementButton = document.createElement('button');
            incrementButton.className = 'btn btn-secondary btn-sm increment-button';
            incrementButton.textContent = '+';
            incrementButton.onclick = function() {
                const quantityInput = this.previousElementSibling;
                let quantity = parseInt(quantityInput.value, 10);
                quantityInput.value = ++quantity;
                this.quantidade[id] = quantity;
            };
        
            div.appendChild(decrementButton);
            div.appendChild(quantityInput);
            div.appendChild(incrementButton);
        
            return div;
        },
        
        limparCarrinho() {
            localStorage.removeItem('itens_carrinho');
            window.location.reload();
        },

        removeItemFromCart(item) {
            this.itens_carrinho = this.itens_carrinho.filter(cartItem => cartItem !== item);
            localStorage.setItem('itens_carrinho', this.itens_carrinho);
            if (this.itens_carrinho.length === 0) {
                localStorage.removeItem('itens_carrinho');
            }
            window.location.reload();
        }
    }
});