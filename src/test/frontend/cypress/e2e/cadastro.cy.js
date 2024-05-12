Cypress.on('uncaught:exception', (err, runnable) => {
    return false; // previnir uncaught exceptions
});



describe('Criar conta', () => {
    it('Test0 - Senhas iguais', () => {
        cy.visit('http://localhost:8080/signup')
        // dados do usuario
        cy.get('#nome').type('test')
        cy.get('#login').type('test')
        cy.get('#senha').type('test123')
        cy.get('#confirmar-senha').type('test123') // senha diferente
        cy.get('.botao-estilo1').click()
        // redireciona para login
        cy.url().should('eq', 'http://localhost:8080/login')
    })
    it('Test1 - Senhas diferentes', () => {
        cy.visit('http://localhost:8080/signup')
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
    // Login senha correta
    it('Test2 - Login valido', () => {
        cy.visit('http://localhost:8080/login')
        // login
        cy.get('#login').type('test')
        cy.get('#senha').type('test')
        cy.get('#login-button').click()
        // homepage logado
        cy.url().should('eq', 'http://localhost:8080/')
        cy.get('#nome')
            .should('exist')
            .should('have.text', 'test')
        // deslogar
        cy.get('.logout-btn')
            .should('exist')
            .should('have.text', 'Desconectar')
        cy.get('.logout-btn').click()
        cy.url().should('eq', 'http://localhost:8080/')
        cy.get('#nome').should('not.exist')
        cy.get('.logout-btn').should('not.exist')
    })
    // Login senhas errada
    it('Test3 - Login invalido', () => {
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

