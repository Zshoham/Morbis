//useful information
//a real user's data:
//username: this.$root.test_username
//password: this.$root.test_password
//name: real username
//email: real@username.com
describe('Login button Test', () => {
    it('test the Login button', () => {
        cy.visit("localhost:8080")//enetr the site on loaclhost
        cy.contains('span', 'Login').click()//click on Login button
        cy.url().should('include','LoginPage')//check if the redireciotn is correct (to LoginPage)
    })
  })
describe('Bad Login Test', () => {
    it('types bad values in login form', () => {
        cy.visit("localhost:8080")//enter the site in localhost
        cy.contains('span', 'Login').click()//click on Login button
        cy.get('input').eq(1).click({force:true}).type('USERNAME').should('have.value','USERNAME')//insert username
        cy.get('input').eq(2).click({force:true}).type('PASSWORD').should('have.value',"PASSWORD")//insert password
        cy.get('button').contains('Login').eq(0).click()//click on login to send form
        cy.on('window:alert', (str) => {
          expect(str).to.equal(`The combination of username and password doesn't exist`)
        })//on
    })//it
  })//describe

describe('Good Login Test', () => {
  it('types good values in login form', () => {
    cy.visit("localhost:8080")//enter the site in localhost
    cy.contains('span', 'Login').click()//click the Login button
    cy.get('input').eq(1).click({force:true}).type('representative')//insert username
    cy.get('input').eq(2).click({force:true}).type('Password123')//insert password
    cy.get('button').contains('Login').eq(0).click()//click login to send the form
    cy.url().should('include','HomePage')//check if the redirection is correct
    cy.contains('span','All-User Logout').click();//logout
    })//it
  })//describe