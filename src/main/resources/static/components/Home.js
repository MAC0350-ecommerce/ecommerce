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


function createProductCard(id, photos, name, price) {
    const cardDiv = document.createElement('div');
    cardDiv.className = 'card';

    const carousel = createCarousel(id, photos);
    cardDiv.appendChild(carousel);

    const cardBody = document.createElement('div');
    cardBody.className = 'product-info';

    const cardTitle = document.createElement('h5');
    cardTitle.className = 'card-title';
    cardTitle.textContent = name;
    cardBody.appendChild(cardTitle);

    const cardPrice = document.createElement('p');
    cardPrice.className = 'card-text';
    const formattedPrice = parseFloat(price).toFixed(2);
    cardPrice.innerHTML = `<strong>R$${formattedPrice}</strong>`;
    cardBody.appendChild(cardPrice);
    

    // Creating the "Add to Cart" button
    const addToCartButton = document.createElement('button');
    addToCartButton.textContent = 'Comprar';
    addToCartButton.className = 'btn btn-primary';
    addToCartButton.style.backgroundColor = 'white';
    addToCartButton.style.color = 'black';
    addToCartButton.style.border = '1px solid rgba(0, 0, 0, 0.5)';
    addToCartButton.style.borderRadius = '50px'; // Rounded borders
    addToCartButton.style.transition = 'background-color 0.3s ease'; // Smooth transition
    addToCartButton.addEventListener('mouseenter', function() {
        addToCartButton.style.backgroundColor = 'lightblue';
    });
    addToCartButton.addEventListener('mouseleave', function() {
        addToCartButton.style.backgroundColor = 'white';
    });
    cardBody.appendChild(addToCartButton);


    cardDiv.appendChild(cardBody);

    return cardDiv;
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



new Vue({
    el: '#app',
    data: {
        accessToken: '',
        refreshToken: '',
        nome: '',
        papel: '',
        foto: 'null'
    },
    mounted() {
        // Pega os itens da localStorage
        this.displayUserImage();
        this.accessToken = localStorage.getItem('accessToken');
        this.refreshToken = localStorage.getItem('refreshToken');
        this.nome = localStorage.getItem('nome');
        this.papel = localStorage.getItem('papel');
        this.foto = localStorage.getItem('foto')

        // Pega o token
        const accessToken = localStorage.getItem('accessToken');
        const headers = {
            Authorization: 'Bearer ' + accessToken
        };

        waitForElementToExist('#loading').then(loading => {
            // GET request
            axios.get('http://localhost:8080/api/produtos/', { headers })
                .then(response => {
                    const produtos = response.data;
                    const carouselContainer = document.getElementById('productCarouselContainer');
                    
                    produtos.forEach(produto => {
                        const productCard = createProductCard(produto.id, produto.fotos, produto.nome, produto.preco);
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
            localStorage.removeItem('accessToken');
            localStorage.removeItem('refreshToken');
            localStorage.removeItem('login');
            localStorage.removeItem('nome');
            localStorage.removeItem('foto');
            localStorage.removeItem('papel');

            // Limpa os cookies
            document.cookie = ""

            // Limpa os itens do Vue
            this.accessToken = '';
            this.refreshToken = '';
            this.nome = ''; 
            this.papel = '';
            this.foto = 'null';

            // Seta a foto placeholder
            userImg = document.getElementById('display1');
            userImg.src = '../img/usuario_padrao.png';

            // Refresh na pagina
            location.reload();
        },

        // Exibe a foto do usuário
        displayUserImage() {
            var userImg = document.getElementById('display1');
            this.foto = localStorage.getItem('foto');
            if (this.foto && this.foto !== 'null') {
                userImg.src = 'data:image/png;base64,' + this.foto.replace(/"/g, '');
            }
            else {
                userImg.src = '../img/usuario_padrao.png';
            }
        }
    },
});