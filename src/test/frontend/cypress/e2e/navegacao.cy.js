Cypress.on('uncaught:exception', (err, runnable) => {
    return false; // previnir uncaught exceptions
});



describe('Navegando pelo site', () => {
    it('Test0 - Entrando em todas as paginas', () => {
        cy.visit('http://localhost:8080/')
        // about
        cy.get('#about').click()
        cy.url().should('eq', 'http://localhost:8080/about')
        // homepage
        cy.get('#home').click()
        cy.url().should('eq', 'http://localhost:8080/')
        cy.get('#nome').should('not.exist');
        cy.get('.logout-btn').should('not.exist')
        // login
        cy.get('#login-button')
            .should('exist')
            .click()
        cy.url().should('eq', 'http://localhost:8080/login')
        // signup
        cy.get('#signup-button').click()
        cy.url().should('eq', 'http://localhost:8080/signup')
        // voltando
        cy.get('#voltar').click()
        cy.url().should('eq', 'http://localhost:8080/')
    })
})