Cypress.on('uncaught:exception', (err, runnable) => {
    return false; // previnir uncaught exceptions
});



describe('Criar conta', () => {
    beforeEach(() => {
        cy.visit('http://localhost:8080/signup')
    })

    it('Test0 - Senhas iguais', () => {
        // dados do usuario
        cy.get('#nome').type('test')
        cy.get('#login').type('test')
        cy.get('#senha').type('test123')
        cy.get('#confirmar-senha').type('test123') // senha diferente
        cy.get('.botao-estilo1').click()

        // alerta da conta criada
        cy.on('window:alert', (text) => {
            expect(text).to.contains('Conta criada com sucesso!');
        });
    })

    it('Test1 - Senhas diferentes', () => {
        // dados do usuario
        cy.get('#nome').type('test')
        cy.get('#login').type('test')
        cy.get('#senha').type('test123')
        cy.get('#confirmar-senha').type('test123456789') // senha diferente
        cy.get('.botao-estilo1').click()
        cy.url().should('eq', 'http://localhost:8080/signup')

        // erro
        cy.get('.error-message')
            .should('exist')
            .should('have.text', 'Criação de conta falhouAs senhas não conferem!')
    })
})



describe('Login', () => {
    beforeEach(() => {
        cy.visit('http://localhost:8080/login')
    })

    it('Test2 - Login usuario valido', () => {
        // login
        cy.get('#login').type('cliente_teste')
        cy.get('#senha').type('1234')
        cy.get('#login-button').click()
        
        // homepage logado
        cy.url().should('eq', 'http://localhost:8080/')
        
        // deslogar
        cy.get('.logout-btn')
            .should('exist')
            .should('have.text', 'Desconectar')
        cy.get('.logout-btn').click()
        cy.url().should('eq', 'http://localhost:8080/')
        cy.get('.logout-btn').should('not.exist')
    })


    it ('Test3 - Login admin', () => {
        // login
        cy.get('#login').type('cliente_teste')
        cy.get('#senha').type('1234')
        cy.get('#login-button').click()

        // homepage logado
        cy.url().should('eq', 'http://localhost:8080/')

        // pagina admin
        cy.get('.adm-btn').should('exist')

        // deslogar
        cy.get('.logout-btn')
            .should('exist')
            .should('have.text', 'Desconectar')
        cy.get('.logout-btn').click()
        cy.url().should('eq', 'http://localhost:8080/')
        cy.get('.logout-btn').should('not.exist')
    })

    it('Test4 - Login invalido', () => {
        cy.visit('http://localhost:8080/login')
        // login
        cy.get('#login').type('123')
        cy.get('#senha').type('1234')
        cy.get('#login-button').click()
        cy.url().should('eq', 'http://localhost:8080/login')
        
        // erro: senha errada
        cy.get('.error-message')
            .should('exist')
            .should('have.text', 'Usuário ou Senha incorretos')
    })
})

