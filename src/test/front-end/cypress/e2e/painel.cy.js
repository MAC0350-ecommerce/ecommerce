describe('Painel', () => {

  it('Acesso ao painel sem permissão', () => {
    cy.request({
      method: 'GET',
      url: 'http://127.0.0.1:8080/painel',
      failOnStatusCode: false,
    }).then((resp) => {
      expect(resp.status).to.eq(403)
    })
  })
  
  it('Acesso ao painel como administrador', () => {
    cy.visit('http://127.0.0.1:8080/login')
    cy.get("input#login" ).type('admin_teste')
    cy.get("input#senha").type('1234')
    cy.get("button#login-button").click()
    cy.wait(500)
    cy.visit('http://127.0.0.1:8080/painel')

  })

  it('Acesso ao painel como cliente', () => {
    cy.visit('http://127.0.0.1:8080/login')
    cy.get("input#login" ).type('cliente_teste')
    cy.get("input#senha").type('1234')
    cy.get("button#login-button").click()
    cy.wait(500)
    cy.request({
      method: 'GET',
      url: 'http://127.0.0.1:8080/painel',
      failOnStatusCode: false,
    }).then((resp) => {
      expect(resp.status).to.eq(403)
    })

  })

});