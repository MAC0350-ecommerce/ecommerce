describe('Página inicial', () => {
  it('Acesso a página', () => {
    cy.visit('http://127.0.0.1:8080')
    cy.get("#msg_boas_vindas").contains('Bem Vindo ao Nosso E-commerce')
  })
})
