describe('LoginPage Test', () => {
    it('test the Login button', () => {
        cy.visit("localhost:8080")
        cy.contains('div', 'Login').click()
        cy.url().should('include','LoginPage')
    })
  })
describe('Bad Login Test', () => {
    it('types bad values in login form', () => {
        cy.visit("localhost:8080")
        cy.contains('div', 'Login').click()
        cy.get('input').eq(1).click({force:true}).type('USERNAME').should('have.value','USERNAME')
        cy.get('input').eq(2).click({force:true}).type('PASSWORD').should('have.value',"PASSWORD")
        cy.get('button').contains('Login').eq(0)
//         cy.server()
// // spy on POST requests to /users endpoint
// cy.route('POST', '/Login').as('loginResponse')
// // trigger network calls by manipulating web app's user interface, then
// cy.get('#results')
//   .should('have.property', 'status', 404)
    })
  })

// describe('Good Login Test', () => {
//     it('Visits the app root url', () => {
//       cy.visit("localhost:8080")
//       cy.contains('h1', 'Morbis')
  
//     })
//   })