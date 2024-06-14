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
        itens_carrinho: []
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
        }
    }
});