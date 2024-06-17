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
        id: null,
        login: null,
        papel: null,
        endereco: null,
        itens_carrinho: []
    },
    mounted() {
        this.displayUserImage('display1');
        this.nome = localStorage.getItem('nome');
        this.papel = localStorage.getItem('papel'); 
        this.id = localStorage.getItem('id');
        this.login = localStorage.getItem('login');

        this.itens_carrinho = localStorage.getItem('itens_carrinho'); 
        this.itens_carrinho = (this.itens_carrinho === null) ? [] : this.itens_carrinho.split(','); 

        // Pega o token
        const accessToken = localStorage.getItem('accessToken');
        const headers = {
            Authorization: 'Bearer ' + accessToken
        };

        waitForElementToExist('#loading').then(loading => {
            document.getElementById('profile-id').textContent = this.id;
            document.getElementById('profile-name').textContent = this.nome;
            document.getElementById('profile-login').textContent = this.login;
            document.getElementById('profile-role').textContent = this.papel;
            this.displayUserImage('profile-image');

            loading.classList.add('d-none'); // esconde o loading

            // Pega os pedidos
            axios.get('http://localhost:8080/api/cadastros/' + this.id + '/pedidos', { headers })
            .then(response => {
                const pedidos = response.data;
                const container = document.getElementById('pedidosContainer');
                
                pedidos.reverse().forEach(pedido => {
                    const pedidoDiv = document.createElement('div');
                    pedidoDiv.className = 'pedido';
                    
                    // pega os valores
                    const frete = pedido.precoFrete;
                    const precoItens = (pedido.valorTotal - frete);

                    // simulacao
                    const now = new Date();
                    now.setHours(now.getHours() + 3); // ajuste de fuso horario
                    const dataCadastro = new Date(pedido.dataCadastro);

                    const timeDifference = Math.floor((now - dataCadastro) / 1000); // em segundos
                    let statusColor, statusText;

                    if (timeDifference < 60) {
                        statusColor = 'darkblue';
                        statusText = 'Aguardando Pagamento';
                    } else if (timeDifference < 120) {
                        statusColor = 'blue';
                        statusText = 'Enviando Pedido';
                    } else if (pedido.pagamento.status === 'ERRO') {
                        statusColor = 'red';
                        statusText = 'Falha no Envio';
                    } else {
                        statusColor = 'green';
                        statusText = 'Pedido Entregue';
                    }

                    pedidoDiv.innerHTML = `
                        <h3>Pedido ID: ${pedido.id}</h3>
                        <p><strong>Endereço de Entrega:</strong> ${pedido.enderecoEntrega}</p>
                        <p><strong>Número de Itens:</strong> ${pedido.itens.length}</p>
                        <p><strong>Data de Cadastro:</strong> ${dataCadastro.toLocaleString()}</p>
                        <p><strong>Valor Itens:</strong> R$ ${precoItens.toFixed(2)}</p>
                        <p><strong>Valor Frete:</strong> R$ ${frete.toFixed(2)}</p>
                        <hr>
                        <p><strong>Total: R$ <ins>${pedido.valorTotal.toFixed(2)}</ins></strong> <span class="time-difference" style="color: ${statusColor}">${statusText}</span></p>
                    `;

                    container.appendChild(pedidoDiv);
                });
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

            // Limpa os cookies
            document.cookie = "";

            // refresh
            window.location.href = '/';
        },

        displayUserImage(display) {
            var userImg = document.getElementById(display);
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