describe('Register button Test', () => {
    it('test the Register button', () => {
        cy.visit("localhost:8081:8080")//ener the site in localhost:8081
        cy.contains('span', 'Register').click()//click on Register button
        cy.url().should('include','RegisterPage')//check if the redirection is correct (to RegisterPage)
    })
  })
describe('Bad Register Test', () => {
    it('types bad values in register form', () => {
        cy.visit("localhost:8081:8080")//enter the site in localhost:8081
        cy.contains('span', 'Register').click()//click the Register button
        cy.get('input').eq(1).click({force:true}).type('USERNAME').should('have.value','USERNAME')//insert username
        cy.get('input').eq(2).click({force:true}).type('SHORT').should('have.value','SHORT')//insert password
        cy.get('input').eq(3).click({force:true}).type('USER NAME').should('have.value','USER NAME')//insert name
        cy.get('input').eq(4).click({force:true}).type('USER@NAME.MAIL.COM').should('have.value','USER@NAME.MAIL.COM')//insert email
        cy.get('button').contains('Register').eq(0).click()//click register
    })
  })

describe('Good Register Test', () => {
  it('types good values in register form', () => {
    cy.visit("localhost:8081:8080")//enter the site in localhost:8081
    cy.contains('span', 'Register').click()//click the Register button
    cy.get('input').eq(1).click({force:true}).type('USERNAME').should('have.value','USERNAME')//insert username
    cy.get('input').eq(2).click({force:true}).type('Password123').should('have.value','Password123')//insert password
    cy.get('input').eq(3).click({force:true}).type('USER NAME').should('have.value','USER NAME')//insert name
    cy.get('input').eq(4).click({force:true}).type('USER@NAME.MAIL.COM').should('have.value','USER@NAME.MAIL.COM')//insert email
    cy.get('button').contains('Register').eq(0).click()//click register
})



})