//useful information:
//managing scheduling policies in done in '/LeagueOptionsPage'
//you can access this page only if you are loged in as
//an Accossiacion Representative or admin otherwise the 
describe('manage schedule Test', () => {
    it('enters the manageSchedulePage and changes the Scheduling policy', () => {
      cy.visit("localhost:8080")
      cy.contains('span', 'Login').click()//click on Login button
      cy.get('input').eq(1).click({force:true}).type('representative')//insert username 
      cy.get('input').eq(2).click({force:true}).type('Password123')//insert password
      cy.get('button').contains('Login').eq(0).click()//click on login button
      cy.get('button').eq(0).click()//open menu
      cy.contains('div', 'Association Representitive').click({force:true})//click on Association Representitive menu
      cy.contains('div', 'Leagues\' Options').click({force:true})//click on Leagues' Options button
      })//it
    })//describe