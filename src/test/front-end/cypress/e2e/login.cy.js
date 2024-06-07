describe('Login', () => {
  
  it('Acesso à página', () => {
    cy.visit('http://127.0.0.1:8080/login')
    cy.get("input#login" ).should('be.visible')
    cy.get("input#senha").should('be.visible')
    cy.get("button#login-button").should('be.visible')
    cy.visit('http://127.0.0.1:8080')
  })

  it('Login como Adminstrador', () => {
    cy.visit('http://127.0.0.1:8080/login')
    cy.get("input#login" ).type('admin_teste')
    cy.get("input#senha").type('1234')
    cy.get("button#login-button").click()
    cy.get("#nome_usuario").should('have.text', "Administrador Teste")
  })

  it('Login como Cliente', () => {
    cy.visit('http://127.0.0.1:8080/login')
    cy.get("input#login" ).type('cliente_teste')
    cy.get("input#senha").type('1234')
    cy.get("button#login-button").click()
    cy.get("#nome_usuario").should('have.text', "Cliente Teste")
  })
})
