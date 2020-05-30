describe('About button Test', () => {
    it('test the About button', () => {
        cy.visit("localhost:8080")//enetr the site on localhost
        cy.contains('span', 'About').click()//click on About button
        cy.url().should('include','about')//check if the redirection is correct (to about)
    })//it
  })//describe